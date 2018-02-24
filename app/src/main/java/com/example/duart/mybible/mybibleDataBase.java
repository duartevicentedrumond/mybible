package com.example.duart.mybible;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by duart on 18/02/2018.
 */

public class mybibleDataBase extends SQLiteOpenHelper {

    //constructor for database
    public mybibleDataBase( Context appContext ) {
        super(appContext, "mybible.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableWalletQuery;
        createTableWalletQuery = "CREATE TABLE wallet ( id INTEGER PRIMARY KEY, date DATETIME DEFAULT CURRENT_TIMESTAMP, description TEXT, value TEXT, source_destination TEXT, repay TEXT, repayment TEXT, type TEXT, status INTEGER)";
        sqLiteDatabase.execSQL(createTableWalletQuery);

        String createTableItemQuery;
        createTableItemQuery = "CREATE TABLE item ( id INTEGER PRIMARY KEY, name TEXT, category TEXT, status INTEGER)";
        sqLiteDatabase.execSQL(createTableItemQuery);

        String createTableSubdivisionQuery;
        createTableSubdivisionQuery = "CREATE TABLE subdivision ( id INTEGER PRIMARY KEY, subdivision TEXT, status INTEGER)";
        sqLiteDatabase.execSQL(createTableSubdivisionQuery);

        String createTableBoxQuery;
        createTableBoxQuery = "CREATE TABLE box ( id INTEGER PRIMARY KEY, box TEXT, id_subdivision INTEGER, status INTEGER)";
        sqLiteDatabase.execSQL(createTableBoxQuery);

        String createTableLocationQuery;
        createTableLocationQuery = "CREATE TABLE location ( id_item INTEGER, id_subdivision INTEGER, id_box INTEGER, status INTEGER)";
        sqLiteDatabase.execSQL(createTableLocationQuery);

        String createTableLinkClothesQuery;
        createTableLinkClothesQuery = "CREATE TABLE link_clothes ( id_item_1 INTEGER, id_item_2 INTEGER, status INTEGER )";
        sqLiteDatabase.execSQL(createTableLinkClothesQuery);

        String createTableConsumablesQuery;
        createTableConsumablesQuery = "CREATE TABLE consumables ( id_item INTEGER, date DATETIME DEFAULT CURRENT_TIMESTAMP, change_stock INTEGER, status INTEGER )";
        sqLiteDatabase.execSQL(createTableConsumablesQuery);

        String createTableNoConsumablesQuery;
        createTableNoConsumablesQuery = "CREATE TABLE noconsumables ( id_item INTEGER, date DATETIME DEFAULT CURRENT_TIMESTAMP, state INTEGER, status INTEGER )";
        sqLiteDatabase.execSQL(createTableNoConsumablesQuery);

        String createTableCostQuery;
        createTableCostQuery = "CREATE TABLE cost ( id_item INTEGER, date DATETIME DEFAULT CURRENT_TIMESTAMP, cost TEXT, status INTEGER )";
        sqLiteDatabase.execSQL(createTableCostQuery);

        String createTableBorrowQuery;
        createTableBorrowQuery = "CREATE TABLE borrow ( id_item INTEGER, date DATETIME DEFAULT CURRENT_TIMESTAMP, state INTEGER, person TEXT, status INTEGER )";
        sqLiteDatabase.execSQL(createTableBorrowQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
