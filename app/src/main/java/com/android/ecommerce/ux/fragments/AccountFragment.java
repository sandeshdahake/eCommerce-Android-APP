package com.android.ecommerce.ux.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ecommerce.CONST;
import com.android.ecommerce.MyApplication;
import com.android.ecommerce.R;
import com.android.ecommerce.SettingsMy;
import com.android.ecommerce.api.EndPoints;
import com.android.ecommerce.api.GsonRequest;
import com.android.ecommerce.entities.User;
import com.android.ecommerce.interfaces.LoginDialogInterface;
import com.android.ecommerce.listeners.OnSingleClickListener;
import com.android.ecommerce.utils.MsgUtils;
import com.android.ecommerce.utils.Utils;
import com.android.ecommerce.ux.MainActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import timber.log.Timber;

import static com.android.ecommerce.R.string.Shipping;


/**
 * Fragment provides the account screen with options such as logging, editing and more.
 */
public class AccountFragment extends Fragment {

    private ProgressDialog pDialog;

    /**
     * Indicates if user data should be loaded from server or from memory.
     */
    private boolean mAlreadyLoaded = false;

    // User information
    private LinearLayout userInfoLayout;
    private TextView tvUserName;
    private TextView tvAddress;
    private TextView tvPhone;
    private TextView tvEmail;

    // Actions
    private Button loginLogoutBtn;
    private Button updateUserBtn;
    private Button myOrdersBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s - OnCreateView", this.getClass().getSimpleName());
        MainActivity.setActionBarTitle(getString(R.string.Profile));

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        pDialog = Utils.generateProgressDialog(getActivity(), false);

        userInfoLayout = (LinearLayout) view.findViewById(R.id.account_user_info);
        tvUserName = (TextView) view.findViewById(R.id.account_name);
        tvAddress = (TextView) view.findViewById(R.id.account_address);
        tvEmail = (TextView) view.findViewById(R.id.account_email);
        tvPhone = (TextView) view.findViewById(R.id.account_phone);

        updateUserBtn = (Button) view.findViewById(R.id.account_update);
        updateUserBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (getActivity() instanceof MainActivity)
                    ((MainActivity) getActivity()).onAccountEditSelected();
            }
        });
        myOrdersBtn = (Button) view.findViewById(R.id.account_my_orders);
        myOrdersBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (getActivity() instanceof MainActivity)
                    ((MainActivity) getActivity()).onOrdersHistory();
            }
        });


        Button settingsBtn = (Button) view.findViewById(R.id.account_settings);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity != null && activity instanceof MainActivity)
                    ((MainActivity) getActivity()).startSettingsFragment();
            }
        });
        Button dispensingPlaces = (Button) view.findViewById(R.id.account_dispensing_places);

        loginLogoutBtn = (Button) view.findViewById(R.id.account_login_logout_btn);
        loginLogoutBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (SettingsMy.getActiveUser() != null) {
                    LoginDialogFragment.logoutUser();
                    refreshScreen(null);
                } else {
                    LoginDialogFragment loginDialogFragment = LoginDialogFragment.newInstance(new LoginDialogInterface() {
                        @Override
                        public void successfulLoginOrRegistration(User user) {
                            refreshScreen(user);
                            MainActivity.updateCartCountNotification();
                        }
                    });
                    loginDialogFragment.show(getFragmentManager(), LoginDialogFragment.class.getSimpleName());
                }
            }
        });


        User user = SettingsMy.getActiveUser();
        if (user != null) {
            Timber.d("user: %s", user.toString());
            // Sync user data if fragment created (not reuse from backstack)
            if (savedInstanceState == null && !mAlreadyLoaded) {
                mAlreadyLoaded = true;
                syncUserData(user);
            } else {
                refreshScreen(user);
            }
        } else {
            refreshScreen(null);
        }
        return view;
    }

    private void syncUserData(@NonNull User user) {
    }

    private void refreshScreen(User user) {
        if (user == null) {
            loginLogoutBtn.setText(getString(R.string.Log_in));
            userInfoLayout.setVisibility(View.GONE);
            updateUserBtn.setVisibility(View.GONE);
            myOrdersBtn.setVisibility(View.GONE);
        } else {
            loginLogoutBtn.setText(getString(R.string.Log_out));
            userInfoLayout.setVisibility(View.VISIBLE);
            updateUserBtn.setVisibility(View.VISIBLE);
            myOrdersBtn.setVisibility(View.VISIBLE);

            tvUserName.setText(user.getName());

            String address = user.getStreet();
            address = appendCommaText(address, user.getHouseNumber(), false);
            address = appendCommaText(address, user.getCity(), true);
            address = appendCommaText(address, user.getZip(), true);

            tvAddress.setText(address);
            tvEmail.setText(user.getEmail());
            tvPhone.setText(user.getPhone());
        }
    }

    /**
     * The method combines two strings. As the string separator is used space or comma.
     *
     * @param result   first part of final string.
     * @param append   second part of final string.
     * @param addComma true if comma with space should be used as separator. Otherwise is used space.
     * @return concatenated string.
     */
    private String appendCommaText(String result, String append, boolean addComma) {
        if (result != null && !result.isEmpty()) {
            if (append != null && !append.isEmpty()) {
                if (addComma)
                    result += getString(R.string.format_comma_prefix, append);
                else
                    result += getString(R.string.format_space_prefix, append);
            }
            return result;
        } else {
            return append;
        }
    }

    @Override
    public void onStop() {
        MyApplication.getInstance().getRequestQueue().cancelAll(CONST.ACCOUNT_REQUESTS_TAG);
        super.onStop();
    }
}
