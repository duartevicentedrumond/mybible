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
        createTableWalletQuery = "CREATE TABLE wallet ( id INTEGER PRIMARY KEY, date DATETIME DEFAULT CURRENT_TIMESTAMP, description TEXT, value TEXT, id_person INTEGER, id_gift INTEGER, repay TEXT, type TEXT, status INTEGER)";
        sqLiteDatabase.execSQL(createTableWalletQuery);

        String createTablePersonQuery;
        createTablePersonQuery = "CREATE TABLE person ( id INTEGER PRIMARY KEY, name TEXT, complete_name TEXT, status INTEGER)";
        sqLiteDatabase.execSQL(createTablePersonQuery);

        String createTableBirthdayQuery;
        createTableBirthdayQuery = "CREATE TABLE birthday ( id_person INTEGER, date DATE, status INTEGER)";
        sqLiteDatabase.execSQL(createTableBirthdayQuery);

        String createTableEmailQuery;
        createTableEmailQuery = "CREATE TABLE email ( id_person INTEGER, email TEXT, category TEXT, status INTEGER)";
        sqLiteDatabase.execSQL(createTableEmailQuery);

        String createTablePhoneQuery;
        createTablePhoneQuery = "CREATE TABLE phone ( id_person INTEGER, number TEXT, category TEXT, status INTEGER)";
        sqLiteDatabase.execSQL(createTablePhoneQuery);

        String createTableGiftQuery;
        createTableGiftQuery = "CREATE TABLE gift ( id INTEGER PRIMARY KEY, id_item INTEGER, id_wallet INTEGER, id_person INTEGER, date DATE, category TEXT, status INTEGER)";
        sqLiteDatabase.execSQL(createTableGiftQuery);

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
        createTableLinkClothesQuery = "CREATE TABLE link ( id_item_1 INTEGER, id_item_2 INTEGER, status INTEGER )";
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
