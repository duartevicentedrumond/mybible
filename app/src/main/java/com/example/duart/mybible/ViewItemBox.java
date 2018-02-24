package com.example.duart.mybible;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinTask;

public class ViewItemBox extends AppCompatActivity {

    private String itemId;
    private TextView textViewItemName;
    private SQLiteOpenHelper dataBase;
    private SQLiteDatabase sqLiteDatabase;
    private ArrayList<String> arrayListItemName = new ArrayList<>();
    private ArrayList<String> arrayListItemSubdivisionId = new ArrayList<>();
    private ArrayList<String> arrayListItemBoxId = new ArrayList<>();
    private ArrayList<String> arrayListDate = new ArrayList<>();
    private ArrayList<String> arrayListCost = new ArrayList<>();
    private ArrayList<String> arrayListDateFinal = new ArrayList<>();
    private TextView textViewItemLocation;
    private TextView textViewCostHistory;
    private TextView textViewConsumables;
    private TextView textViewConsumablesHistory;
    private TextView textViewClothes;
    private TextView textViewClothesState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item_box);

        textViewItemName = (TextView) findViewById(R.id.text_view_item_name);
        textViewItemLocation = (TextView) findViewById(R.id.text_view_item_location);
        textViewCostHistory = (TextView) findViewById(R.id.text_view_cost_history);
        textViewConsumables = (TextView) findViewById(R.id.text_view_consumables);
        textViewConsumablesHistory = (TextView) findViewById(R.id.text_view_consumables_history);
        textViewClothes = (TextView) findViewById(R.id.text_view_clothes);
        textViewClothesState = (TextView) findViewById(R.id.text_view_clothes_state);
        dataBase = new mybibleDataBase(this);

        getIntentAndTransform();
        printItemInformation(itemId);
        printCostHistory(itemId);
        printConsumableSection(itemId);
        printClothesSection(itemId);
    }

    public String getIntentAndTransform(){
        //this gets the selected string from SeeAllWallet
            Intent intent = getIntent();
            String itemSelected = intent.getExtras().getString("itemSelected");

        //this gets the id from the string
            itemSelected = itemSelected.split("#")[1];
            itemId = itemSelected.split(" ")[0];

        return itemId;
    }

    public void printItemInformation(String itemId){

        //gets item's id
            sqLiteDatabase = dataBase.getReadableDatabase();
            Cursor itemNameData = sqLiteDatabase.rawQuery("SELECT * FROM item WHERE id=" + itemId + ";", null);
            while (itemNameData.moveToNext()){
                arrayListItemName.add(itemNameData.getString(1));
            }

        //gets item's location
            sqLiteDatabase = dataBase.getReadableDatabase();
            Cursor itemLocationData = sqLiteDatabase.rawQuery("SELECT id_subdivision, id_box FROM location WHERE id_item=" + itemId + ";", null);
            while (itemLocationData.moveToNext()){
                arrayListItemSubdivisionId.add(itemLocationData.getString(0));
                arrayListItemBoxId.add(itemLocationData.getString(1));
            }
            sqLiteDatabase = dataBase.getReadableDatabase();
            Cursor itemSubdivisionName = sqLiteDatabase.rawQuery("SELECT subdivision FROM subdivision WHERE id=" + arrayListItemSubdivisionId.get(0) + ";", null);
            itemSubdivisionName.moveToFirst();

            sqLiteDatabase = dataBase.getReadableDatabase();
            Cursor itemBoxName = sqLiteDatabase.rawQuery("SELECT box FROM box WHERE id='" + arrayListItemBoxId.get(0) + "';", null);

        if(itemBoxName.getCount()!=0){
            itemBoxName.moveToFirst();

            textViewItemName.setText("#" + itemId + " " + arrayListItemName.get(0));
            textViewItemLocation.setText(itemSubdivisionName.getString(0) + " > " + itemBoxName.getString(0));
        }else{
            textViewItemName.setText("#" + itemId + " " + arrayListItemName.get(0));
            textViewItemLocation.setText(itemSubdivisionName.getString(0));
        }
    }

    public void printCostHistory(String itemId){
        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor itemCostHistory = sqLiteDatabase.rawQuery("SELECT date, cost FROM cost WHERE id_item=" + itemId + ";", null);
        while (itemCostHistory.moveToNext()){
            arrayListDate.add(itemCostHistory.getString(0));
            arrayListCost.add(itemCostHistory.getString(1));
        }

        arrayListDateFinal = getDate(arrayListDate);

        for (int i = 0; i < arrayListDateFinal.size(); i++ ){
            textViewCostHistory.append( arrayListDateFinal.get(i) + "     " + arrayListCost.get(i) + "€\n" );
        }

    }

    public void printConsumableSection(String itemId){
        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor itemConsumableHistory = sqLiteDatabase.rawQuery("SELECT * FROM consumables WHERE id_item=" + itemId + ";", null);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor itemConsumableClothes = sqLiteDatabase.rawQuery("SELECT * FROM clothes WHERE id_item=" + itemId + ";", null);

        //checks if item is a consumable or no consumable item
        if( itemConsumableHistory.getCount() == 0 && itemConsumableClothes.getCount() == 0 ){
            //if the item is no consumable...

            textViewConsumables.setText("estado");
            sqLiteDatabase = dataBase.getReadableDatabase();
            Cursor itemState = sqLiteDatabase.rawQuery("SELECT state FROM noconsumables WHERE id_item=" + itemId + " ORDER BY state DESC LIMIT 1;", null);
            itemState.moveToFirst();

            if( itemState.getString(0).equals("1") ){
                textViewConsumablesHistory.setText( "pronto a usar\n" );
            }else if( itemState.getString(0).equals("2") ){
                textViewConsumablesHistory.setText( "não está pronto a usar\n" );
            }else if( itemState.getString(0).equals("3") ){
                textViewConsumablesHistory.setText( "emprestado\n" );
            }else if( itemState.getString(0).equals("4") ){
                textViewConsumablesHistory.setText( "morreu\n" );
            }

        }else if ( itemConsumableHistory.getCount() != 0 && itemConsumableClothes.getCount() == 0 ){
            //if the item is consumable...

            textViewConsumables.setText("stock");
            sqLiteDatabase = dataBase.getReadableDatabase();
            Cursor itemStock = sqLiteDatabase.rawQuery("SELECT SUM(change_stock) FROM consumables WHERE id_item=" + itemId + ";", null);
            itemStock.moveToFirst();
            textViewConsumablesHistory.setText( itemStock.getString(0) + "\n" );

        }
    }

    public void printClothesSection(String itemId){

        //checks if item's id belongs to table clothes
            sqLiteDatabase = dataBase.getReadableDatabase();
            Cursor itemclothes = sqLiteDatabase.rawQuery("SELECT state FROM clothes WHERE id_item=" + itemId + " ORDER BY state DESC LIMIT 1;", null);
            itemclothes.moveToFirst();

            if ( itemclothes.getCount() !=0 ){
                textViewClothes.setText( "roupa" );
                if ( itemclothes.getString(0).equals("1") ){
                    textViewClothesState.setText( "pronto a usar\n" );
                }else if ( itemclothes.getString(0).equals("2") ){
                    textViewClothesState.setText( "a lavar\n" );
                }else if ( itemclothes.getString(0).equals("3") ){
                    textViewClothesState.setText( "emprestado\n" );
                }else if ( itemclothes.getString(0).equals("4") ){
                    textViewClothesState.setText( "morreu\n" );
                }
            }
    }

    public ArrayList<String> getDate(ArrayList<String> array_input){

        ArrayList<String> array_output = new ArrayList();

        for ( int i = 0; i < array_input.size() ; i++ ) {
            String date = array_input.get(i).substring(8, array_input.get(i).length() - 9)
                    + "."
                    + array_input.get(i).substring(5, array_input.get(i).length() - 12)
                    + "."
                    + array_input.get(i).substring(2, array_input.get(i).length() - 15);
            array_output.add(date);
        }

        return array_output;
    }
}
