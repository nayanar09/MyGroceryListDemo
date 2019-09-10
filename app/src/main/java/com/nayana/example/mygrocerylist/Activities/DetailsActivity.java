package com.nayana.example.mygrocerylist.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.nayana.example.mygrocerylist.R;

public class DetailsActivity extends AppCompatActivity {

    private TextView itemNameDet;
    private TextView quantityDet;
    private TextView dateAddedDet;
    private Button editButtonDet;
    private Button deleteButtonDet;
    private int groceryIdDet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        itemNameDet = (TextView) findViewById(R.id.itemNameDet);
        quantityDet = (TextView) findViewById(R.id.quantityDet);
        dateAddedDet = (TextView) findViewById(R.id.dateAddedDet);
        editButtonDet = (Button) findViewById(R.id.editButtonDet);
        deleteButtonDet = (Button) findViewById(R.id.deleteButtonDet);

        Bundle bundle = getIntent().getExtras();

        if ( bundle != null ) {
            itemNameDet.setText(bundle.getString("name"));
            quantityDet.setText(bundle.getString("quantity"));
            dateAddedDet.setText((bundle.getString("date")));

            groceryIdDet = bundle.getInt("id");
        }
    }
}
