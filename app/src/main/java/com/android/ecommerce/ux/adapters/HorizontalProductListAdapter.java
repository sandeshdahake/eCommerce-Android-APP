package com.android.ecommerce.ux.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.ecommerce.R;
import com.android.ecommerce.entities.Banner;
import com.android.ecommerce.entities.BannerProducts;
import com.android.ecommerce.interfaces.BannersRecyclerInterface;
import com.android.ecommerce.listeners.OnSingleClickListener;
import com.android.ecommerce.ux.views.ResizableImageView;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

/**
 * Created by sandeshdahake@gmail.com on 28-08-2016.
 */
public class HorizontalProductListAdapter extends RecyclerView.Adapter<HorizontalProductListAdapter.ViewHolder> {
    private final BannersRecyclerInterface bannersRecyclerInterface;
    private final Context context;
    private List<BannerProducts> bannersProductList = new ArrayList<>();
    private LayoutInflater layoutInflater;

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price;
        public ImageView thumbnail, overflow;
        public RatingBar rating;
        private BannerProducts product;

        public ViewHolder(View itemView, final BannersRecyclerInterface bannersRecyclerInterface) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
            rating = (RatingBar) itemView.findViewById(R.id.ratingBar);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            overflow = (ImageView) itemView.findViewById(R.id.overflow);

            itemView.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(final View view) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bannersRecyclerInterface.onBannerProductSelected(product);
                        }
                    }, 200);
                }
            });
        }

        public void bindContent(BannerProducts product) {
            this.product = product;
        }
    }

    /**
     * Creates an adapter that handles a list of banner items.
     *
     * @param context                  activity context.
     * @param bannersRecyclerInterface listener indicating events that occurred.
     */
    public HorizontalProductListAdapter(Context context, BannersRecyclerInterface bannersRecyclerInterface ) {
        this.context = context;
        this.bannersRecyclerInterface = bannersRecyclerInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.horiznontal_product_list_item, parent, false);
        return new ViewHolder(view, bannersRecyclerInterface);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BannerProducts product = getBannerItem(position);
        holder.bindContent(product);
        Picasso.with(context).load(product.getImage())
                .placeholder(R.drawable.placeholder_loading)
                .fit().centerInside()
                .into(holder.thumbnail);
        holder.name.setText(product.getName());
/*
        if(Integer.valueOf(product.getPrice()) == null){
           holder.price.setText("Rs"+ product.getPrice());

        }else{
*/
           // holder.price.setText("Rs"+ NumberFormat.getInstance().format(Integer.valueOf(product.getPrice())));
            holder.price.setText("Rs"+ product.getPrice());

     //   }
        holder.rating.setRating(product.getRating());

    }

    private BannerProducts getBannerItem(int position) {
        return bannersProductList.get(position);
    }

    @Override
    public int getItemCount() {
        return bannersProductList.size();
    }

    public void addBannerProducts(List<BannerProducts> bannerProductList) {
        if (bannerProductList != null && !bannerProductList.isEmpty()) {
            bannersProductList.addAll(bannerProductList);
            notifyDataSetChanged();
        } else {
            Timber.e("Adding empty banner productlist.");
        }
    }

    /**
     * Clear all data.
     */
    public void clear() {
        bannersProductList.clear();
    }

    /**
     * Check if some banners exist.
     *
     * @return true if content is empty.
     */
    public boolean isEmpty() {
        return bannersProductList == null || bannersProductList.isEmpty();
    }
}