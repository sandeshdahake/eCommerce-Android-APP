package com.android.ecommerce.ux.fragments;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.ecommerce.R;
import com.android.ecommerce.entities.Metadata;
import com.android.ecommerce.entities.product.MetadataItem;
import com.android.ecommerce.entities.product.ProductMetadata;
import com.android.ecommerce.entities.product.Property;
import com.android.ecommerce.entities.product.WebStoreProductDetail;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.List;


/**
 * Created by sandeshdahake@gmail.com on 19-09-2016.
 */
public class ProductDetailSpecs extends Fragment {

    public static ProductDetailSpecs newInstance(JsonObject properties, ProductMetadata metadata) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("metadata", (Serializable) metadata);
        bundle.putString("properties", properties.toString());
        ProductDetailSpecs fragment = new ProductDetailSpecs();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        JsonObject properties = new Gson().fromJson(bundle.getString("properties"), JsonObject.class);
        ProductMetadata metadata = (ProductMetadata) bundle.getSerializable("metadata");


            // SET THIS AS CONTENT VIEW SO THAT WE CAN ABLE TO ADD MORE ELEMENT
            ScrollView contentView = new ScrollView(getContext());
            contentView.setBackgroundColor(Color.WHITE);

            // THIS IS OUR MAIN LAYOUT
            LinearLayout mainLayout = new LinearLayout(getContext());
            mainLayout.setOrientation(LinearLayout.VERTICAL);

            // ADD MAINLAYOUT TO SCROLLVIEW (contentView)

            contentView.addView(mainLayout);

            // SET CONTENT VIEW




            TableLayout tableLayoutProgrammatically = this.tableLayout(metadata, properties);

            // JUST LABEL TEXTVIEW

            TextView secondLabelTextView = new TextView(getContext());

            secondLabelTextView.setTextSize(18);


            // ADD TABLEROW AND LABEL HERE


            mainLayout.addView(secondLabelTextView);
            mainLayout.addView(tableLayoutProgrammatically);


        //return inflater.inflate(R.layout.fragment_specs, container, false);
        return contentView;
    }

    TableLayout tableLayout(ProductMetadata metadata, JsonObject properties){

        // OUR TABLELAYOUT

        TableLayout table = new TableLayout(getContext());

        // SET SHRINKABLE OUR SECOND COLUMN

        table.setColumnShrinkable(1, true);

        // CREATE PARAMS

        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        List<MetadataItem> listData = metadata.getItemList();
        for(MetadataItem item : listData){
            List<Property> productSpec =  item.getProperties();
            TableRow tableRowGroup = new TableRow(getContext());
            TextView tableRowGroupText = new TextView(getContext());
            tableRowGroupText.setBackgroundColor(Color.rgb(211,211,211));
            tableRowGroupText.setLayoutParams(params);
            tableRowGroupText.setText(item.getGrouplabel());
            tableRowGroupText.setPadding(20, 20, 20, 20);
            View view = new View(getContext());
            table.setColumnStretchable(1, true);
            view.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1));
            TextView blanktxt = new TextView(getContext());
            blanktxt.setBackgroundColor(Color.rgb(211,211,211));
            blanktxt.setLayoutParams(params);
            blanktxt.setPadding(20, 20, 20, 20);
            tableRowGroup.addView(tableRowGroupText);
            tableRowGroup.addView(blanktxt);
            table.addView(tableRowGroup);
           // table.addView(view);
            int propCount = productSpec.size();
            int nullPropCount = 0 ;
            for(Property specLabel:productSpec){
                if(properties.get(specLabel.getName())== null || properties.get(specLabel.getName()).isJsonNull()) {
                    nullPropCount ++;
                    continue;
                }


                TableRow tableRowSpec = new TableRow(getContext());
                TextView tableRowLabelText = new TextView(getContext());
                tableRowLabelText.setBackgroundColor(Color.WHITE);
                tableRowLabelText.setLayoutParams(params);
                tableRowLabelText.setText(specLabel.getLabel());
                tableRowLabelText.setPadding(20, 20, 20, 20);

                TextView tableRowValueText = new TextView(getContext());
                tableRowValueText.setBackgroundColor(Color.WHITE);
                tableRowValueText.setLayoutParams(params);
                tableRowValueText.setText(properties.get(specLabel.getName()).toString());
                tableRowValueText.setPadding(20, 20, 20, 20);

                tableRowSpec.addView(tableRowLabelText);
                tableRowSpec.addView(tableRowValueText);

                View view1 = new View(getContext());
                view1.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,2));
                table.addView(tableRowSpec);
                table.addView(view1);

            }
            if(nullPropCount== propCount){
                table.removeView(tableRowGroup);
            }
        }
/*
        // OUT ELEMENT
        TextView a1 = new TextView(getContext());
        TextView b1 = new TextView(getContext());
        // SET BACKGROUND COLOR TO OUT ELEMENT SO THAT IT IS VISIBLE
        a1.setBackgroundColor(Color.WHITE);
        b1.setBackgroundColor(Color.WHITE);
        // SET PARAMS
        a1.setLayoutParams(params);
        b1.setLayoutParams(params);
        // SET TEXT
        a1.setText("A");
        b1.setText("THIS IS THE LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOONG TEXT");
        // SET PADDING
        a1.setPadding(20, 20, 20, 20);
        b1.setPadding(20, 20, 20, 20);
        // ADD ELEMENT TO OUR TABLEROW
        tableRow1.addView(a1);
        tableRow1.addView(b1);



        // THIS VIEW IS JUST A BOUDARY
        View view = new View(getContext());
        view.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,2));

        // ADD OUR TABLEROW TO TABLE
        table.addView(tableRow1);
        table.addView(view);
        table.addView(tableRow2);*/
        return table;
    }
}