package com.android.ecommerce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.android.ecommerce.entities.Shop;
import com.android.ecommerce.entities.User;
import com.android.ecommerce.entities.product.ProductMetadata;
import com.android.ecommerce.utils.MsgUtils;
import com.android.ecommerce.utils.Utils;
import com.android.ecommerce.ux.SplashActivity;
import com.google.gson.JsonObject;

import timber.log.Timber;

/**
 * Class providing app specific sharedPreference settings.
 */
public class SettingsMy {
    public static final String PREF_ACTUAL_SHOP = "pref_actual_shop";
    public static final String PREF_ACTIVE_USER = "pref_active_user";
    public static final String PREF_USER_EMAIL = "pref_user_email";

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    public static final String COMPARE_1 = "compare1";
    public static final String COMPARE_2 = "compare2";
    public static final String COMPARE_3 = "compare3";
    public static final String COMPARE_4 = "compare4";

    private static final String TAG = SettingsMy.class.getSimpleName();
    private static Shop actualShop;
    private static User activeUser;
    //private static ProductMetadata productMetadata;
    private static SharedPreferences sharedPref;

    private SettingsMy() {}

    /**
     * Get actually selected shop.
     *
     * @return actual shop or null if not selected.
     */
    public static Shop getActualShop() {
        if (actualShop != null) {
            Timber.d("%s - Returned actual shop", TAG);
            return actualShop;
        } else {
            SharedPreferences prefs = getSettings();
            String json = prefs.getString(PREF_ACTUAL_SHOP, "");
            if (json.isEmpty() || "null".equals(json)) {
                Timber.e("%s - Returned null shop", TAG);
                return null;
            } else {
                actualShop = Utils.getGsonParser().fromJson(json, Shop.class);
                Timber.d("%s - Returned shop from memory: %s", TAG, actualShop.toString());
                return actualShop;
            }
        }
    }

    /**
     * Set actually selected shop.
     *
     * @param actualShop selected shop or null for disable selection.
     */
    public static void setActualShop(Shop actualShop) {
        if (actualShop != null)
            Timber.d("%s - Set selected shop: %s", TAG, actualShop.toString());
        else
            Timber.d("%s - Disable selected shop", TAG);
        SettingsMy.actualShop = actualShop;

        String json = Utils.getGsonParser().toJson(SettingsMy.actualShop);
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(PREF_ACTUAL_SHOP, json);
        editor.apply();
    }

    /**
     * Get actually selected shop.
     * If actually selected shop is null, then return to {@link SplashActivity}
     *
     * @param activity corresponding activity.
     * @return actually selected shop or empty shop (when the app will be closed).
     */
    @NonNull
    public static Shop getActualNonNullShop(Activity activity) {
        Shop shop = getActualShop();
        if (shop == null) {
            if (activity != null) {
                MsgUtils.showToast(activity, MsgUtils.TOAST_TYPE_INTERNAL_ERROR, null, MsgUtils.ToastLength.LONG);
                Intent intent = new Intent(activity.getApplicationContext(), SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
            } else {
                Timber.e("Null shop detected also with null activity parameter.");
            }
            // Return an empty shop to prevent a null pointer exception before intent is processed.
            return new Shop();
        } else {
            return shop;
        }
    }

    /**
     * Get active user info.
     *
     * @return user or null if nobody logged in.
     */
    public static User getActiveUser() {
        if (activeUser != null) {
            Timber.d("%s - Returned active user", TAG);
            return activeUser;
        } else {
            SharedPreferences prefs = getSettings();
            String json = prefs.getString(PREF_ACTIVE_USER, "");
            if (json.isEmpty() || "null".equals(json)) {
                Timber.d("%s - Returned null", TAG);
                return null;
            } else {
                activeUser = Utils.getGsonParser().fromJson(json, User.class);
                Timber.d("%s - Returned active user from memory: %s", TAG, activeUser.toString());
                return activeUser;
            }
        }
    }

    /**
     * Set active user.
     *
     * @param user active user or null for disable user.
     */
    public static void setActiveUser(User user) {
        if (user != null)
            Timber.d("%s - Set active user with name: %s", TAG, user.toString());
        else
            Timber.d("%s - Deleting active user", TAG);
        SettingsMy.activeUser = user;

        String json = Utils.getGsonParser().toJson(SettingsMy.activeUser);
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(PREF_ACTIVE_USER, json);
        editor.apply();
    }

    /**
     * Get user email. Used for login purpose.
     *
     * @return email of last logged user.
     */
    public static String getUserEmailHint() {
        SharedPreferences prefs = getSettings();
        String userEmail = prefs.getString(PREF_USER_EMAIL, "");
        Timber.d("%s - Obtained user email: %s", TAG, userEmail);
        return userEmail;
    }

    /**
     * Set user email to preferences.
     * Used for login purpose.
     *
     * @param userEmail email of last logged user.
     */
    public static void setUserEmailHint(String userEmail) {
        Timber.d("%s - Set user email: %s", TAG, userEmail);
        putParam(PREF_USER_EMAIL, userEmail);
    }

    /**
     * Get indicator, that GCM token was sent to third party server.
     *
     * @return true if successfully received by third party server. False otherwise.
     */
    public static Boolean getTokenSentToServer() {
        SharedPreferences prefs = getSettings();
        boolean tokenSent = prefs.getBoolean(SENT_TOKEN_TO_SERVER, false);
        Timber.d("%s - Obtained token sent to server: %s", TAG, tokenSent);
        return tokenSent;
    }

    /**
     * Set GCM token sent to third party server indicator.
     *
     * @param tokenSent true if successfully received by server.
     */
    public static void setTokenSentToServer(boolean tokenSent) {
        putParam(SENT_TOKEN_TO_SERVER, tokenSent);
    }

    /**
     * Obtain preferences instance.
     *
     * @return base instance of app SharedPreferences.
     */
    public static SharedPreferences getSettings() {
        if (sharedPref == null) {
            sharedPref = MyApplication.getInstance().getSharedPreferences(MyApplication.PACKAGE_NAME, Context.MODE_PRIVATE);
        }
        return sharedPref;
    }

    private static boolean putParam(String key, String value) {
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(key, value);
        return editor.commit();
    }

    private static boolean putParam(String key, boolean value) {
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * Get actually selected sub category.
     *
     * @return actual subCategory or null if not selected.
     */
    public static ProductMetadata getSubCategory(String id) {
        String subCategoryID= "subCategoryID"+id;

        SharedPreferences prefs = getSettings();
            String json = prefs.getString(subCategoryID, "");
            if (json.isEmpty() || "null".equals(json)) {
                Timber.e("%s - Returned null shop", TAG);
                return null;
            } else {
                return Utils.getGsonParser().fromJson(json, ProductMetadata.class);
            }
        }


    /**
     * Set actually selected productMetadata.
     *
     * @param productMetadata selected SubCategory or null for disable selection.
     */
    public static void setSubCategory(String id, ProductMetadata productMetadata) {
        String subCategoryID= "subCategoryID"+id;
        if (productMetadata != null)
            Timber.d("%s - save selected subCategory: %s", TAG, productMetadata.toString());
        else
            Timber.d("%s - disable selected subCategory", TAG);
        String json = Utils.getGsonParser().toJson(productMetadata);
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(subCategoryID, json);
        editor.apply();
    }
    public static JsonObject getComareProduct(String id) {

        SharedPreferences prefs = getSettings();
        String json = prefs.getString(id, "");
        if (json.isEmpty() || "null".equals(json)) {
            Timber.e("%s - Returned null shop", TAG);
            return null;
        } else {
            return Utils.getGsonParser().fromJson(json, JsonObject.class);
        }
    }


    public static void setComareProduct(String id, JsonObject obj) {
        if (obj != null)
            Timber.d("%s - save selected compare product: %s", TAG, obj.toString());
        else
            Timber.d("%s - disable selected compare product", TAG);
        String json = Utils.getGsonParser().toJson(obj);
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(id, json);
        editor.apply();
    }
}
