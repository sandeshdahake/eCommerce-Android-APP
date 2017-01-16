package com.android.ecommerce.ux.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.ecommerce.CONST;
import com.android.ecommerce.MyApplication;
import com.android.ecommerce.R;
import com.android.ecommerce.SettingsMy;
import com.android.ecommerce.api.EndPoints;
import com.android.ecommerce.api.GsonRequest;
import com.android.ecommerce.api.JsonRequest;
import com.android.ecommerce.entities.User;
import com.android.ecommerce.entities.product.MetadataItem;
import com.android.ecommerce.entities.product.ProductMetadata;
import com.android.ecommerce.entities.product.Property;
import com.android.ecommerce.interfaces.RequestListener;
import com.android.ecommerce.listeners.OnSingleClickListener;
import com.android.ecommerce.utils.MsgUtils;
import com.android.ecommerce.utils.Utils;
import com.android.ecommerce.ux.MainActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import timber.log.Timber;

/**
 * Fragment handles shopping cart.
 */
public class CompareFragment extends Fragment {
    private JsonObject obj1;
    private JsonObject obj2;
    private JsonObject obj3;
    private JsonObject obj4;
    private TableLayout tableLayoutProgrammatically ;
    private ProductMetadata metadata;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s - onCreateView", this.getClass().getSimpleName());
        MainActivity.setActionBarTitle("Product Compare");

        obj1 = SettingsMy.getComareProduct(SettingsMy.COMPARE_1);
        obj2 = SettingsMy.getComareProduct(SettingsMy.COMPARE_2);
        obj3 = SettingsMy.getComareProduct(SettingsMy.COMPARE_3);
        obj4 = SettingsMy.getComareProduct(SettingsMy.COMPARE_4);
       // SettingsMy.setComareProduct(SettingsMy.COMPARE_1,null);
        if(obj1 != null){
            metadata = SettingsMy.getSubCategory(obj1.get("SubcategoryId").toString());
        }

        // SET THIS AS CONTENT VIEW SO THAT WE CAN ABLE TO ADD MORE ELEMENT
        ScrollView contentView = new ScrollView(getContext());
        contentView.setBackgroundColor(Color.WHITE);

        // THIS IS OUR MAIN LAYOUT
        LinearLayout mainLayout = new LinearLayout(getContext());
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        FloatingActionButton btnFab  = new FloatingActionButton(this.getContext());
        btnFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                obj1 = null;
                obj2 = null;
                obj3 = null;
                obj4 = null;

                SettingsMy.setComareProduct(SettingsMy.COMPARE_1,null);
                SettingsMy.setComareProduct(SettingsMy.COMPARE_2,null);
                SettingsMy.setComareProduct(SettingsMy.COMPARE_3,null);
                SettingsMy.setComareProduct(SettingsMy.COMPARE_4,null);
                tableLayoutProgrammatically.removeAllViewsInLayout();
            }
        });
        mainLayout.addView(btnFab);
        // ADD MAINLAYOUT TO SCROLLVIEW (contentView)

        contentView.addView(mainLayout);

        // SET CONTENT VIEW

        tableLayoutProgrammatically = this.tableLayout(metadata);

        // JUST LABEL TEXTVIEW
        TextView secondLabelTextView = new TextView(getContext());
        secondLabelTextView.setTextSize(18);
        // ADD TABLEROW AND LABEL HERE

        mainLayout.addView(secondLabelTextView);
        mainLayout.addView(tableLayoutProgrammatically);

        return contentView;
    }

    TableLayout tableLayout(ProductMetadata metadata){
        // OUR TABLELAYOUT

        TableLayout table = new TableLayout(getContext());
        if(metadata  == null){
          return table;
        }

        // SET SHRINKABLE OUR SECOND COLUMN

        table.setColumnShrinkable(1, true);

        // CREATE PARAMS

        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        List<MetadataItem> listData = metadata.getItemList();
        for(MetadataItem item : listData){
            List<Property> productSpec =  item.getProperties();
/*
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
*/
            // table.addView(view);
            for(Property specLabel:productSpec){

                TableRow tableRowLabel = new TableRow(getContext());
                TextView tableRowLabelText = new TextView(getContext());
                tableRowLabelText.setBackgroundColor(Color.rgb(211,211,211));
                tableRowLabelText.setLayoutParams(params);
                tableRowLabelText.setText(specLabel.getLabel());
                tableRowLabelText.setPadding(20, 20, 20, 20);
                tableRowLabel.addView(tableRowLabelText);
                TextView blanktxt = new TextView(getContext());
                blanktxt.setBackgroundColor(Color.rgb(211,211,211));
                blanktxt.setLayoutParams(params);
                blanktxt.setPadding(20, 20, 20, 20);
                tableRowLabel.addView(blanktxt);


                TableRow tableRowProduct1 = new TableRow(getContext());
                TableRow tableRowProduct2= new TableRow(getContext());
                TableRow tableRowProduct3= new TableRow(getContext());
                TableRow tableRowProduct4= new TableRow(getContext());
                Boolean isNotNull = false;

                if(obj1 != null){
                    TextView tableRowProductNameText1 = new TextView(getContext());
                    tableRowProductNameText1.setBackgroundColor(Color.WHITE);
                    tableRowProductNameText1.setLayoutParams(params);
                    tableRowProductNameText1.setText(obj1.get("Name").toString());
                    tableRowProductNameText1.setPadding(20, 20, 20, 20);
                    tableRowProduct1.addView(tableRowProductNameText1);

                    TextView tableRowProductValue1 = new TextView(getContext());
                    tableRowProductValue1.setBackgroundColor(Color.WHITE);
                    tableRowProductValue1.setLayoutParams(params);
                    tableRowProductValue1.setText(obj1.get(specLabel.getName())== null?"":obj1.get(specLabel.getName()).toString());
                    tableRowProductValue1.setPadding(20, 20, 20, 20);
                    tableRowProduct1.addView(tableRowProductValue1);
                    if(obj1.get(specLabel.getName())!= null){
                        isNotNull = true;
                    }
                }
                if(obj2 != null){
                    TextView tableRowProductNameText2 = new TextView(getContext());
                    tableRowProductNameText2.setBackgroundColor(Color.WHITE);
                    tableRowProductNameText2.setLayoutParams(params);
                    tableRowProductNameText2.setText(obj2.get("Name").toString());
                    tableRowProductNameText2.setPadding(20, 20, 20, 20);
                    tableRowProduct2.addView(tableRowProductNameText2);

                    TextView tableRowProductValue2 = new TextView(getContext());
                    tableRowProductValue2.setBackgroundColor(Color.WHITE);
                    tableRowProductValue2.setLayoutParams(params);
                    tableRowProductValue2.setText(obj2.get(specLabel.getName())== null?"":obj2.get(specLabel.getName()).toString());
                    tableRowProductValue2.setPadding(20, 20, 20, 20);
                    tableRowProduct2.addView(tableRowProductValue2);
                    if(obj2.get(specLabel.getName())!= null){
                        isNotNull = true;
                    }

                }
                if(obj3 != null){
                    TextView tableRowProductNameText3 = new TextView(getContext());
                    tableRowProductNameText3.setBackgroundColor(Color.WHITE);
                    tableRowProductNameText3.setLayoutParams(params);
                    tableRowProductNameText3.setText(obj2.get("Name").toString());
                    tableRowProductNameText3.setPadding(20, 20, 20, 20);
                    tableRowProduct3.addView(tableRowProductNameText3);

                    TextView tableRowProductValue3 = new TextView(getContext());
                    tableRowProductValue3.setBackgroundColor(Color.WHITE);
                    tableRowProductValue3.setLayoutParams(params);
                    tableRowProductValue3.setText(obj3.get(specLabel.getName())== null?"":obj3.get(specLabel.getName()).toString());
                    tableRowProductValue3.setPadding(20, 20, 20, 20);
                    tableRowProduct3.addView(tableRowProductValue3);
                    if(obj3.get(specLabel.getName())!= null){
                        isNotNull = true;
                    }

                }
                if(obj4 != null){
                    TextView tableRowProductNameText4 = new TextView(getContext());
                    tableRowProductNameText4.setBackgroundColor(Color.WHITE);
                    tableRowProductNameText4.setLayoutParams(params);
                    tableRowProductNameText4.setText(obj4.get("Name").toString());
                    tableRowProductNameText4.setPadding(20, 20, 20, 20);
                    tableRowProduct4.addView(tableRowProductNameText4);

                    TextView tableRowProductValue4 = new TextView(getContext());
                    tableRowProductValue4.setBackgroundColor(Color.WHITE);
                    tableRowProductValue4.setLayoutParams(params);
                    tableRowProductValue4.setText(obj4.get(specLabel.getName())== null?"":obj4.get(specLabel.getName()).toString());
                    tableRowProductValue4.setPadding(20, 20, 20, 20);
                    tableRowProduct4.addView(tableRowProductValue4);
                    if(obj4.get(specLabel.getName())!= null){
                        isNotNull = true;
                    }

                }

                if(isNotNull){
                    table.addView(tableRowLabel);
                    table.addView(tableRowProduct1);
                    table.addView(tableRowProduct2);
                    table.addView(tableRowProduct3);
                    table.addView(tableRowProduct4);

                }
            }
        }

        return table;
    }


    @Override
    public void onStop() {
        super.onStop();
    }
}
