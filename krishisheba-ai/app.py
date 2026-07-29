from fastapi import FastAPI, UploadFile, File
print("=== NEW APP.PY LOADED ===")
import tensorflow as tf
import numpy as np
import json
import shutil
import os
from tensorflow.keras.preprocessing import image
import uuid

# ----------------------------
# App init
# ----------------------------
app = FastAPI()

UPLOAD_DIR = "temp"
os.makedirs(UPLOAD_DIR, exist_ok=True)

# ----------------------------
# Load Model (once at startup)
# ----------------------------
model = tf.keras.models.load_model("models/plant_disease_model.h5")

# ----------------------------
# Load Labels
# ----------------------------
with open("models/labels.json", "r") as f:
    class_names = json.load(f)
    
with open("models/disease_info.json", "r") as f:
    disease_info = json.load(f)

# ----------------------------
# Preprocessing
# ----------------------------
def preprocess_image(img_path):
    img = image.load_img(img_path, target_size=(224, 224))
    img_array = image.img_to_array(img)
    img_array = np.expand_dims(img_array, axis=0)
    return img_array


def normalize_label_key(label):
    return label.lower().replace(" ", "").replace("-", "_").replace("__", "_")


def format_label(label):
    return label.replace("___", " - ").replace("__", " ").replace("_", " ")


normalized_disease_info = {
    normalize_label_key(key): value for key, value in disease_info.items()
}

# ----------------------------
# Prediction
# ----------------------------
def predict_disease(img_path):
    img_array = preprocess_image(img_path)

    predictions = model.predict(img_array, verbose=0)

    predicted_index = str(int(np.argmax(predictions[0])))

    label = class_names[predicted_index]

    confidence = float(np.max(predictions[0]))

    return label, confidence


def get_recommendation(label):
    recommendation = disease_info.get(label)
    if recommendation is not None:
        return recommendation

    return normalized_disease_info.get(
        normalize_label_key(label),
        {
            "cause": f"Detected class: {format_label(label)}",
            "treatment": ["Consult an agriculture expert for field-specific treatment."],
            "prevention": ["Monitor the crop regularly and isolate infected leaves early."]
        }
    )

# ----------------------------
# API Endpoint
# ----------------------------
@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    file_ext = os.path.splitext(file.filename)[1] or ".jpg"
    unique_name = f"{uuid.uuid4()}{file_ext}"
    file_path = os.path.join(UPLOAD_DIR, unique_name)

    try:
        with open(file_path, "wb") as buffer:
            shutil.copyfileobj(file.file, buffer)

        label, confidence = predict_disease(file_path)
        recommendation = get_recommendation(label)

        return {
            "label": label,
            "display_label": format_label(label),
            "confidence": round(confidence, 4),
            "recommendation": recommendation
        }
    finally:
        if os.path.exists(file_path):
            os.remove(file_path)

# ----------------------------
# Health Check
# ----------------------------
@app.get("/")
def home():
    return {
        "message": "KrishiSheba Plant Disease API is running 🌱"
    }
