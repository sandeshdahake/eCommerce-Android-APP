package com.android.ecommerce.ux.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ecommerce.R;
import com.android.ecommerce.entities.drawerMenu.DrawerItemCategory;
import com.android.ecommerce.entities.drawerMenu.DrawerItemSubCategory;
import com.android.ecommerce.interfaces.DrawerSubmenuRecyclerInterface;

import java.util.ArrayList;
import java.util.List;


/**
 * Adapter handling list of drawer sub-items.
 */
public class DrawerSubmenuRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final DrawerSubmenuRecyclerInterface drawerSubmenuRecyclerInterface;
    private LayoutInflater layoutInflater;
    private List<DrawerItemSubCategory> drawerItemCategoryList = new ArrayList<>();

    /**
     * Creates an adapter that handles a list of drawer sub-items.
     *
     * @param drawerSubmenuRecyclerInterface listener indicating events that occurred.
     */
    public DrawerSubmenuRecyclerAdapter(DrawerSubmenuRecyclerInterface drawerSubmenuRecyclerInterface) {
        this.drawerSubmenuRecyclerInterface = drawerSubmenuRecyclerInterface;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.list_item_drawer_category, parent, false);
        return new ViewHolderItemCategory(view, drawerSubmenuRecyclerInterface);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderItemCategory viewHolderItemCategory = (ViewHolderItemCategory) holder;

        DrawerItemSubCategory drawerItemCategory = getDrawerItem(position);
        viewHolderItemCategory.bindContent(drawerItemCategory);
        viewHolderItemCategory.itemText.setText(drawerItemCategory.getLabel());
        viewHolderItemCategory.subMenuIndicator.setVisibility(View.INVISIBLE);
    }


    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return drawerItemCategoryList.size();
    }


    private DrawerItemSubCategory getDrawerItem(int position) {
        return drawerItemCategoryList.get(position);
    }

    public void changeDrawerItems(List<DrawerItemSubCategory> children) {
        drawerItemCategoryList.clear();
        drawerItemCategoryList.addAll(children);
        notifyDataSetChanged();
    }


    // Provide a reference to the views for each data item
    public static class ViewHolderItemCategory extends RecyclerView.ViewHolder {
        public TextView itemText;
        public ImageView subMenuIndicator;
        public LinearLayout layout;
        private DrawerItemSubCategory drawerItemCategory;

        public ViewHolderItemCategory(View itemView, final DrawerSubmenuRecyclerInterface drawerSubmenuRecyclerInterface) {
            super(itemView);
            itemText = (TextView) itemView.findViewById(R.id.drawer_list_item_text);
            subMenuIndicator = (ImageView) itemView.findViewById(R.id.drawer_list_item_indicator);
            layout = (LinearLayout) itemView.findViewById(R.id.drawer_list_item_layout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerSubmenuRecyclerInterface.onSubCategorySelected(v, drawerItemCategory);
                }
            });
        }

        public void bindContent(DrawerItemSubCategory drawerItemCategory) {
            this.drawerItemCategory = drawerItemCategory;
        }
    }
}
