package com.android.ecommerce.api;

import com.android.ecommerce.CONST;

public class EndPoints {
    /**
     * Base server url.
     */
    //http://www.comparedunia.net.in/ProductAPI/subcategory_metadata?id=12
    private static final String API_URL                 = "http://comparedunia.net.in";    // staging
    public static final String NAVIGATION_DRAWER        = API_URL.concat("/ProductAPI/menu");
    public static final String BANNERS                  = API_URL.concat("/ProductAPI/homepage_slider");
    public static final String BANNERS_FEATURED         = API_URL.concat("/ProductAPI/featured_products");
    public static final String BANNERS_POPULAR          = API_URL.concat("/ProductAPI/popular_products");
    public static final String PRODUCTS                 = API_URL.concat("/SubcategoryController/products/");
    public static final String PRODUCT                  = API_URL.concat("/ProductAPI/product?id=");
    public static final String PRODUCTS_METADATA        = API_URL.concat("/ProductAPI/subcategory_metadata?id=");
    public static final String PRODUCTS_SINGLE_RELATED  = API_URL.concat("/ProductAPI/similar_products?id=");


    public static final String PAGES_SINGLE             = API_URL.concat("%d/pages/%d");
    public static final String PAGES_TERMS_AND_COND     = API_URL.concat("%d/pages/terms");
    public static final String USER_REGISTER            = API_URL.concat("%d/users/register");
    public static final String USER_LOGIN_EMAIL         = API_URL.concat("%d/login/email");
    public static final String USER_LOGIN_FACEBOOK      = API_URL.concat("%d/login/facebook");
    public static final String USER_RESET_PASSWORD      = API_URL.concat("%d/users/reset-password");


    // Notifications parameters
    public static final String NOTIFICATION_LINK        = "link";
    public static final String NOTIFICATION_MESSAGE     = "message";
    public static final String NOTIFICATION_TITLE       = "title";
    public static final String NOTIFICATION_IMAGE_URL   = "image_url";
    public static final String NOTIFICATION_SHOP_ID     = "shop_id";
    public static final String NOTIFICATION_UTM         = "utm";

    private EndPoints() {}
}
