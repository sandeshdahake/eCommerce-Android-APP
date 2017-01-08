package com.android.ecommerce.interfaces;

import android.view.View;

import com.android.ecommerce.entities.productList.Product;


public interface CategoryRecyclerInterface {

    void onProductSelected(View view, Product product);

}
