package com.avinash_ksworks.jci.nammabengaluru.adapter;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.id;

public class DatabaseHelper  extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "jarvis";

    private static final String TABLE_PLACES = "jarvis_places";
    private static final String TABLE_FAVOURITES = "jarvis_fav";
    private static final String TABLE_VISITED = "jarvis_vis";


    private static final String place_image = "image";
    private static final String place_id = "id";
    private static final String place_name = "name";
    private static final String place_description = "description";
    private static final String place_bestSeason = "bestSeason";
    private static final String place_contact = "contact";
    private static final String place_entryFee = "entryFee";
    private static final String place_additionalInformation = "place_additionalInformation";
    private static final String place_latitude = "latitude";
    private static final String place_longitude = "longitude";
    private static final String place_category = "category";





    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_table_places="create table "+TABLE_PLACES+" ("+place_id+" integer primary key, "+place_image+" text, "+place_name+" text, "+place_description+" text, "+place_bestSeason+" text, "+place_contact+" text, "+place_entryFee+" text, "+ place_additionalInformation +" text, "+place_latitude+" double, "+place_longitude+" double, "+place_category+" text); ";
        db.execSQL(create_table_places);


        String create_table_fav = "create table "+TABLE_FAVOURITES+"("+place_id+" integer primary key); ";
        db.execSQL(create_table_fav);


        String create_table_vis="create table "+TABLE_VISITED+"("+place_id+" integer primary key);";
        db.execSQL(create_table_vis);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_PLACES);

        db.execSQL("drop table if exists "+TABLE_FAVOURITES);

        db.execSQL("drop table if exists "+TABLE_VISITED);

        onCreate(db);
    }

    public boolean insert_into_places(int id, String image, String name, String description, String bestSeason, String contact, String entryFee, String additionalInformation, double latitude, double longitude, String category){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(place_id, id);
        contentValues.put(place_image, image);
        contentValues.put(place_name, name);
        contentValues.put(place_description, description);
        contentValues.put(place_bestSeason, bestSeason);
        contentValues.put(place_contact, contact);
        contentValues.put(place_entryFee, entryFee);
        contentValues.put(place_additionalInformation, additionalInformation);
        contentValues.put(place_latitude, latitude);
        contentValues.put(place_longitude, longitude);
        contentValues.put(place_category, category);

        db.insert(TABLE_PLACES,null,contentValues);
        return  true;
    }

    public boolean insertIntoFavourites(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(place_id, id);
        db.insert(TABLE_FAVOURITES, null, contentValues);
        return true;
    }


    public boolean insertIntoVisited(int id){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(place_id,id);
        database.insert(TABLE_VISITED,null,contentValues);



        return true;
    }

    public void deleteTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db,0,1);
    }







    public Cursor getAllTemples(){
        String str = "select * from "+TABLE_PLACES+" where "+place_category+" = 'temple';";
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.rawQuery(str,null);
    }

    public Cursor getAllTrekking(){

        String str = "select * from "+TABLE_PLACES+" where "+place_category+" = 'trekking';";
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.rawQuery(str,null);
    }

    public Cursor getAllHeritage(){
        String str = "select * from "+TABLE_PLACES+" where "+place_category+" = 'heritage';";
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.rawQuery(str,null);

    }

    public Cursor getAllOther(){
        String str = "select * from "+TABLE_PLACES+" where "+place_category+" = 'other';";
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.rawQuery(str,null);
    }





    public Cursor getAllFavourites()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_FAVOURITES+";",null);
    }



    public Cursor getAllVisited()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_VISITED+";",null);
    }


    public Cursor getPlaceById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_PLACES+" where "+place_id+" = "+id+" ;", null);
    }


}

