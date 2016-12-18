package com.android.ecommerce.interfaces;

public interface UpdateCartItemListener {

    void updateProductInCart(long productCartId, long newVariantId, int newQuantity);

}

