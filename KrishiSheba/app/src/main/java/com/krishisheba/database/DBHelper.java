package com.krishisheba.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.krishisheba.models.Crop;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "KrishiSheba.db";
    private static final int DB_VERSION = 2;

    private static final String TABLE_USERS = "users";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_USERS + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT,"
                + "email TEXT UNIQUE,"
                + "password TEXT"
                + ")";
        db.execSQL(createTable);

        String createCropTable =
                "CREATE TABLE crops (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT," +
                        "season TEXT," +
                        "soil TEXT," +
                        "description TEXT" +
                        ")";
        db.execSQL(createCropTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // INSERT USER
    public boolean insertUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    // LOGIN CHECK
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE email=? AND password=?",
                new String[]{email, password}
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public void insertCrop(
            String name,
            String season,
            String soil,
            String description) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("season", season);
        values.put("soil", soil);
        values.put("description", description);

        db.insert("crops", null, values);
    }

    public ArrayList<Crop> getAllCrops() {

        ArrayList<Crop> cropList =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM crops",
                        null);

        if(cursor.moveToFirst()) {

            do {

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("name"));

                String season =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("season"));

                String soil =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("soil"));

                String description =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("description"));

                cropList.add(
                        new Crop(
                                name,
                                season,
                                soil,
                                description
                        ));

            } while(cursor.moveToNext());

        }

        cursor.close();

        return cropList;
    }

    public Crop getCropByName(String cropName) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM crops WHERE LOWER(name)=LOWER(?) LIMIT 1",
                new String[]{cropName}
        );

        Crop crop = null;

        if (cursor.moveToFirst()) {

            String name =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("name"));

            String season =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("season"));

            String soil =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("soil"));

            String description =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("description"));

            crop = new Crop(
                    name,
                    season,
                    soil,
                    description
            );
        }

        cursor.close();

        return crop;
    }

    public void seedCropData() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT COUNT(*) FROM crops",
                        null);

        cursor.moveToFirst();

        int count = cursor.getInt(0);

        cursor.close();

        if(count == 0){

            insertCrop(
                    "Rice",
                    "Kharif",
                    "Clay Soil",
                    "Major staple crop in Bangladesh."
            );

            insertCrop(
                    "Maize",
                    "Summer",
                    "Loam Soil",
                    "Used for food and livestock feed."
            );

            insertCrop(
                    "Potato",
                    "Winter",
                    "Sandy Loam",
                    "Popular winter vegetable crop."
            );

            insertCrop(
                    "Wheat",
                    "Winter",
                    "Loam Soil",
                    "Important cereal crop."
            );
        }
    }

}
