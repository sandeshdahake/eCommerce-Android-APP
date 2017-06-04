
package com.android.ecommerce.entities.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Price_ {

    @SerializedName("amount")
    @Expose
    private Integer amount;
/*
    @SerializedName("currency")
    @Expose
    private Currency_ currency;
*/

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

/*
    public Currency_ getCurrency() {
        return currency;
    }

    public void setCurrency(Currency_ currency) {
        this.currency = currency;
    }
*/

}
