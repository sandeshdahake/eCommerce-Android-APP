package com.android.ecommerce.interfaces;

import android.view.View;

import com.android.ecommerce.entities.drawerMenu.DrawerItemCategory;
import com.android.ecommerce.entities.drawerMenu.DrawerItemPage;


public interface DrawerRecyclerInterface {

    void onCategorySelected(View v, DrawerItemCategory drawerItemCategory);

    void onPageSelected(View v, DrawerItemPage drawerItemPage);

    void onHeaderSelected();
}
