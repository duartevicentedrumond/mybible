package com.example.duart.mybible;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ViewGift extends AppCompatActivity {

    private String giftId;
    private SQLiteOpenHelper dataBase;
    private SQLiteDatabase sqLiteDatabase;
    private TextView textViewId;
    private TextView textViewItemName;
    private ImageView imageViewItemName;
    private TextView textViewValue;
    private ImageView imageViewValue;
    private TextView textViewPerson;
    private TextView textViewDescription;
    private TextView textViewDate;
    private TextView textViewCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_gift);

        textViewId = (TextView) findViewById(R.id.text_view_id);
        textViewItemName = (TextView) findViewById(R.id.text_view_item_name);
        imageViewItemName = (ImageView) findViewById(R.id.image_view_item_name);
        textViewValue = (TextView) findViewById(R.id.text_view_value);
        imageViewValue = (ImageView) findViewById(R.id.image_view_value);
        textViewPerson = (TextView) findViewById(R.id.text_view_person);
        textViewDescription = (TextView) findViewById(R.id.text_view_description);
        textViewDate = (TextView) findViewById(R.id.text_view_date);
        textViewCategory = (TextView) findViewById(R.id.text_view_category);
        dataBase = new mybibleDataBase(this);

        giftId = getIntentAndTransform();
        printGift(giftId);

    }

    public String getIntentAndTransform(){
        //this gets the selected string from SeeAllWallet
        Intent intent = getIntent();
        String giftSelected = intent.getExtras().getString("giftSelected");

        //this gets the id from the string
        giftSelected = giftSelected.split("#")[1];
        giftId = giftSelected.split(" ")[0];

        return giftId;
    }

    public void printGift(String id){
        textViewId.setText("#" + id);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor giftData = sqLiteDatabase.rawQuery("SELECT item.name, wallet.value, person.name, gift.description, gift.date, gift.category FROM gift LEFT JOIN item ON gift.id_item=item.id LEFT JOIN wallet ON gift.id_wallet=wallet.id LEFT JOIN person ON person.id=gift.id_person WHERE gift.id=" + id + ";", null);
        giftData.moveToFirst();

        if (giftData.getString(0)==null){
            imageViewItemName.setVisibility(View.GONE);
            textViewItemName.setVisibility(View.GONE);
        }else{
            textViewItemName.setText(giftData.getString(0));
        }

        if (giftData.getString(1)==null){
            imageViewValue.setVisibility(View.GONE);
            textViewValue.setVisibility(View.GONE);
        }else{
            textViewValue.setText(giftData.getString(1));
        }

        textViewPerson.setText(giftData.getString(2));
        textViewDescription.setText(giftData.getString(3));
        textViewDate.setText(giftData.getString(4));
        textViewCategory.setText(giftData.getString(5));
    }

}
