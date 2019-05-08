package com.example.ashwin.cartroom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashwin on 4/15/2019.
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ShoppingDataBase";
    private static final String TABLE_NAME = "Items_Table";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "Item_name";
    private static final String COLUMN_QUANTITY = "Quantity";
    private static final String COLUMN_WEIGHT = "Weight";
    private static final String COLUMN_PRICE = "Price";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Table

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creating table for first time
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT NOT NULL UNIQUE," + COLUMN_QUANTITY + " INTEGER NOT NULL," + COLUMN_WEIGHT + " REAL," + COLUMN_PRICE + " REAL" + ")";
        Log.d("Database", "Execute " + CREATE_TABLE);

        db.execSQL(CREATE_TABLE);
        Log.d("Database", "Created Table Successfully");
    }

    // Upgrading Database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create Updated Database

        onCreate(db);
    }

    public void Additem(String ItemName, int Quantity, double Weight, String Weight_Measurement, double Price) {

        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, ItemName);
        values.put(COLUMN_QUANTITY, Quantity);

        if(Weight != 0) {

            values.put(COLUMN_WEIGHT, Weight + "" + Weight_Measurement);

        } else{

            values.put(COLUMN_WEIGHT,"-");

        }
        values.put(COLUMN_PRICE, Price);

        // Inserting entry to Table

        db.insert(TABLE_NAME, null, values);
        db.close();// Close table after each entry
    }

    public void Additem(String ItemName, int Quantity, String Weight, double Price) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, ItemName);
        values.put(COLUMN_QUANTITY, Quantity);
        values.put(COLUMN_WEIGHT, Weight);
        //  values.put(COLUMN_WEIGHT_MEASUREMENT, Weight_Measurement);
        values.put(COLUMN_PRICE, Price);

        // Inserting entry to Table

        db.insert(TABLE_NAME, null, values);
        db.close();// Close table after each entry
    }

    public List<Items> getAllitems() { //Used to store contents in table before delteing the table for Updating table.

        List<Items> ItemList = new ArrayList<Items>();

        String querry = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querry, null);

        if (cursor.moveToFirst()) {
            do {
                Items items = new Items();

                items.setId(Integer.parseInt(cursor.getString(0)));

                items.setName(cursor.getString(1));

                items.setQuantity(Integer.parseInt(cursor.getString(2)));

                items.setWeight(cursor.getString(3));

                items.setPrice(Double.parseDouble(cursor.getString(4)));

                ItemList.add(items);
            } while (cursor.moveToNext());


        }
        cursor.close();
        //db.close();
        return ItemList;
    }

    public void UpdateDatabase() {
        List<Items> updatedItems = getAllitems();

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        db.close();


        for (Items it : updatedItems) {

            Additem(it._item_Name, it.getQuantity(), it.getWeight(), it.getPrice());

        }

    }


}
