package com.android.ecommerce.ux.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.ecommerce.MyApplication;
import com.android.ecommerce.R;
import com.android.ecommerce.entities.product.WebStoreProductDetail;
import com.android.ecommerce.entities.productList.Product;
import com.android.ecommerce.interfaces.CategoryRecyclerInterface;
import com.android.ecommerce.interfaces.WebStoreRecyclerInterface;
import com.android.ecommerce.ux.views.ResizableImageView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


/**
 * Adapter handling list of product items.
 */
public class WebStoreRecyclerAdapter extends RecyclerView.Adapter<WebStoreRecyclerAdapter.ViewHolder> {
    private final Context context;
    private final WebStoreRecyclerInterface webStoreRecyclerInterface;
    private List<WebStoreProductDetail> shops = new ArrayList<>();
    private LayoutInflater layoutInflater;

    private boolean loadHighRes = true;


    public WebStoreRecyclerAdapter(Context context, WebStoreRecyclerInterface webStoreRecyclerInterface) {
        this.context = context;
        this.webStoreRecyclerInterface = webStoreRecyclerInterface;
        defineImagesQuality(true);
    }

    private WebStoreProductDetail getItem(int position) {
        return shops.get(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return shops.size();
    }

    public void addShop(List<WebStoreProductDetail> shopList) {
        shops.addAll(shopList);
        notifyDataSetChanged();
    }


    // Create new views (invoked by the layout manager)
    @Override
    public WebStoreRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.list_item_webstore, parent, false);
        return new ViewHolder(view, webStoreRecyclerInterface);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WebStoreProductDetail shop = getItem(position);
        holder.bindContent(shop);
        // - replace the contents of the view with that element
        holder.storePriceTV.setText(Integer.toString(holder.shop.getPrice().getAmount()));


            Picasso.with(context).load(shop.getProductUrl().get0())
                    .fit().centerInside()
                    .placeholder(R.drawable.placeholder_loading)
                    .into(holder.storeImage);

    }

    public void clear() {
        shops.clear();
    }

    /**
     * Define required image quality. On really big screens is higher quality always requested.
     *
     * @param highResolution true for better quality.
     */
    public void defineImagesQuality(boolean highResolution) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int densityDpi = dm.densityDpi;
        // On big screens and wifi connection load high res images always
        if (((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE)
                && MyApplication.getInstance().isWiFiConnection()) {
            loadHighRes = true;
        } else if (highResolution) {
            // Load high resolution images only on better screens.
            loadHighRes = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_NORMAL)
                    && densityDpi >= DisplayMetrics.DENSITY_XHIGH;
        } else {
            // otherwise
            loadHighRes = false;
        }

        Timber.d("Image high quality selected: %s", loadHighRes);
        notifyDataSetChanged();
    }


    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ResizableImageView storeImage;
        public TextView storePriceTV;
        public Button buyNowBtn;
        private WebStoreProductDetail shop;

        public ViewHolder(View v, final WebStoreRecyclerInterface recyclerInterface) {
            super(v);
            buyNowBtn = (Button) v.findViewById(R.id.store_item_button);
            storePriceTV = (TextView) v.findViewById(R.id.store_item_price);
            storeImage = (ResizableImageView) v.findViewById(R.id.store_item_image);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerInterface.onWebStoreSelected(v, shop.getProductUrl().get0());
                }
            });
        }

        public void bindContent(WebStoreProductDetail shop) {
            this.shop = shop;
        }
    }
}
