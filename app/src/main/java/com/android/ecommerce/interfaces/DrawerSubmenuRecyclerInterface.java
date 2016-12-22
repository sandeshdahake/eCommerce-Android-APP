package com.android.ecommerce.interfaces;

import android.view.View;

import com.android.ecommerce.entities.drawerMenu.DrawerItemCategory;
import com.android.ecommerce.entities.drawerMenu.DrawerItemSubCategory;


public interface DrawerSubmenuRecyclerInterface {

    void onSubCategorySelected(View v, DrawerItemSubCategory drawerItemCategory);

}
