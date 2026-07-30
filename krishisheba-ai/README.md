# KrishiSheba AI Backend

FastAPI backend serving the custom EfficientNetB3 plant disease classification model.

## Run

```bash
pip install -r requirements.txt
uvicorn app:app --reload


# 🧠 AI Model

The trained **EfficientNetB3** model is distributed separately from the repository because GitHub limits repository files to **100 MB**.

## Download Model

Download **plant_disease_model.h5** from the latest GitHub Release.

## Installation

Place the downloaded model here:

```text
krishisheba-ai/
└── models/
    └── plant_disease_model.h5
```

The backend automatically loads the model from this location when the FastAPI server starts.

> **Note:** The repository includes all source code and configuration files. Only the trained model needs to be downloaded separately.
