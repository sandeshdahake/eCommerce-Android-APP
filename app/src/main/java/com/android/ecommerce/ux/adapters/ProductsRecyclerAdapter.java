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
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.ecommerce.MyApplication;
import com.android.ecommerce.R;
import com.android.ecommerce.entities.productList.Product;
import com.android.ecommerce.interfaces.CategoryRecyclerInterface;
import com.android.ecommerce.ux.views.ResizableImageView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


/**
 * Adapter handling list of product items.
 */
public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.ViewHolder> {
    DecimalFormat df = new DecimalFormat("#.00");
    private final Context context;
    private final CategoryRecyclerInterface categoryRecyclerInterface;
    private List<Product> products = new ArrayList<>();
    private LayoutInflater layoutInflater;

    private boolean loadHighRes = true;

    /**
     * Creates an adapter that handles a list of product items.
     *
     * @param context                   activity context.
     * @param categoryRecyclerInterface listener indicating events that occurred.
     */
    public ProductsRecyclerAdapter(Context context, CategoryRecyclerInterface categoryRecyclerInterface) {
        this.context = context;
        this.categoryRecyclerInterface = categoryRecyclerInterface;

        defineImagesQuality(false);
    }

    private Product getItem(int position) {
        return products.get(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return products.size();
    }

    public void addProducts(List<Product> productList) {
        products.addAll(productList);
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProductsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.list_item_products, parent, false);
        return new ViewHolder(view, categoryRecyclerInterface);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = getItem(position);
        holder.bindContent(product);
        // - replace the contents of the view with that element
        holder.productNameTV.setText(holder.product.getName());

           // String url = "http:\\/\\/comparedunia.net.in\\/assets\\/images\\/products\\" + product.getImage();
       // String url = "http://comparedunia.net.in//assets//images//products/printer/full/305a3ef18dfef6c39b38745a22687d868f08a0d7.jpg";
/*
            Picasso.with(context).load(product.getImage())
                    .fit().centerInside()
                    .placeholder(R.drawable.progress_animation)
                    .into(holder.productImage);
*/

        Picasso.with(context).load(product.getImage())
                .fit().centerInside()
                .placeholder(R.drawable.progress_animation)
                .into(holder.productImage);


        // Determine if product is on sale
            holder.productPriceTV.setVisibility(View.VISIBLE);
            holder.rating.setVisibility(View.VISIBLE);
            holder.productPriceTV.setText("Rs. " + holder.product.getPrice());
            holder.productPriceTV.setPaintFlags(holder.productPriceTV.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.productPriceTV.setTextColor(ContextCompat.getColor(context, R.color.textPrimary));
            holder.rating.setRating(holder.product.getRating());

    }

    public void clear() {
        products.clear();
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
        public ResizableImageView productImage;
        public TextView productNameTV;
        public TextView productPriceTV;
        public RatingBar rating;
        private Product product;

        public ViewHolder(View v, final CategoryRecyclerInterface categoryRecyclerInterface) {
            super(v);
            productNameTV = (TextView) v.findViewById(R.id.product_item_name);
            productPriceTV = (TextView) v.findViewById(R.id.product_item_price);
            rating = (RatingBar) v.findViewById(R.id.ratingBar);
            productImage = (ResizableImageView) v.findViewById(R.id.product_item_image);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoryRecyclerInterface.onProductSelected(v, product);
                }
            });
        }

        public void bindContent(Product product) {
            this.product = product;
        }
    }
}
