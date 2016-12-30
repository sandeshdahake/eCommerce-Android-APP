package com.android.ecommerce.interfaces;


import com.android.ecommerce.entities.User;

/**
 * Interface declaring methods for login dialog.
 */
public interface LoginDialogInterface {

    void successfulLoginOrRegistration(User user);

}
