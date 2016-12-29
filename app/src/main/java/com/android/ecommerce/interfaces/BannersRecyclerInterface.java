package com.android.ecommerce.interfaces;

import com.android.ecommerce.entities.Banner;
import com.android.ecommerce.entities.BannerProducts;

public interface BannersRecyclerInterface {
    void onBannerSelected(Banner banner);
    void onBannerProductSelected(BannerProducts product);
}
