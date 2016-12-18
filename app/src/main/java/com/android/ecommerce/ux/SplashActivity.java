/*******************************************************************************
 * Copyright (C) 2016 Business Factory, s.r.o.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.android.ecommerce.ux;

import android.animation.Animator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.android.ecommerce.CONST;
import com.android.ecommerce.MyApplication;
import com.android.ecommerce.api.EndPoints;
import com.android.ecommerce.utils.MsgUtils;
import com.android.ecommerce.utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.appevents.AppEventsLogger;

import java.util.List;
import java.util.Locale;

import com.android.ecommerce.R;
import timber.log.Timber;

/**
 * Initial activity. Handle install referrers, notifications and shop selection;
 * <p>
 *
 */
public class SplashActivity extends AppCompatActivity {
    public static final String REFERRER = "referrer";
    private static final String TAG = SplashActivity.class.getSimpleName();

    private Activity activity;
    private ProgressDialog progressDialog;

    /**
     * Indicates if layout has been already created.
     */
    private boolean layoutCreated = false;

    /**
     * Spinner offering all available shops.
     */
    private Spinner shopSelectionSpinner;

    /**
     * Button allowing selection of shop during fresh start.
     */
    private Button continueToShopBtn;

    /**
     * Indicates that window has been already detached.
     */
    private boolean windowDetached = false;

    // Possible layouts
    private View layoutIntroScreen;
    private View layoutContent;
    private View layoutContentNoConnection;
    private View layoutContentSelectShop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag(TAG);
        activity = this;

        // init loading dialog
        progressDialog = Utils.generateProgressDialog(this, false);
        init();
    }

    /**
     * Prepares activity view and handles incoming intent(Notification, utm data).
     */

    private void init() {
        progressDialog.hide();
        initSplashLayout();
        // Skip intro screen.
        layoutContent.setVisibility(View.VISIBLE);
        layoutIntroScreen.setVisibility(View.GONE);

        // Check if data connected.
        if (!MyApplication.getInstance().isDataConnected()) {

            // Show retry button.
            layoutContentNoConnection.setVisibility(View.VISIBLE);
            layoutContentSelectShop.setVisibility(View.GONE);
        } else {
            //to do for notification managment
            // If opened by notification. Try load shop defined by notification data. If error, just start shop with last used shop.
            if (this.getIntent() != null && this.getIntent().getExtras() != null && this.getIntent().getExtras().getString(EndPoints.NOTIFICATION_LINK) != null) {
                Timber.d("Run by notification.");
                String type = this.getIntent().getExtras().getString(EndPoints.NOTIFICATION_LINK, "");
                final String title = this.getIntent().getExtras().getString(EndPoints.NOTIFICATION_TITLE, "");
                try {
                    String[] linkParams = type.split(":");
                    if (linkParams.length != 3) {
                        Timber.e("Bad notification format. NotifyType: %s", type);
                        throw new Exception("Bad notification format. NotifyType:" + type);
                    } else {

                    }
                } catch (Exception e) {
                    Timber.e(e, "Skip Splash activity after notification error.");
                    startMainActivity(null);
                }
            } else {
                // Nothing special. try continue to MainActivity.
                Timber.d("Nothing special.");
                startMainActivity(null);
            }
        }
    }



    /**
     * SetContentView to activity and prepare layout views.
     */
    private void initSplashLayout() {
        if (!layoutCreated) {
            setContentView(R.layout.activity_splash);

            layoutContent = findViewById(R.id.splash_content);
            layoutIntroScreen = findViewById(R.id.splash_intro_screen);
            layoutContentNoConnection = findViewById(R.id.splash_content_no_connection);
            layoutContentSelectShop = findViewById(R.id.splash_content_select_shop);

            shopSelectionSpinner = (Spinner) findViewById(R.id.splash_shop_selection_spinner);
            Button reRunButton = (Button) findViewById(R.id.splash_re_run_btn);
            if (reRunButton != null) {
                reRunButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.show();
                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                init();
                            }
                        }, 600);
                    }
                });
            } else {
                Timber.e(new RuntimeException(), "ReRunButton didn't found");
            }
            layoutCreated = true;
        } else {
            Timber.d("%s screen is already created.", this.getClass().getSimpleName());
        }
    }

    /**
     * Check if shop is selected. If so then start . If no then show form with selection.
     *
     * @param bundle notification specific data.
     */
    private void startMainActivity(Bundle bundle) {
            Timber.d("Missing active shop. Show shop selection.");
           // initSplashLayout();
            layoutContentNoConnection.setVisibility(View.GONE);
            layoutContent.setVisibility(View.VISIBLE);
            animateContentVisible();
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            if (bundle != null) {
                Timber.d("Pass bundle to main activity");
                mainIntent.putExtras(bundle);
            }
            startActivity(mainIntent);
            finish();

    }



    /**
     * Hide intro screen and display content layout with animation.
     */
    private void animateContentVisible() {
        if (layoutIntroScreen != null && layoutContent != null && layoutIntroScreen.getVisibility() == View.VISIBLE) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (windowDetached) {
                                if (layoutContent != null) layoutContent.setVisibility(View.VISIBLE);
                            } else {
//                            // If lollipop use reveal animation. On older phones use fade animation.
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                                    Timber.d("Circular animation.");
                                    // get the center for the animation circle
                                    final int cx = (layoutContent.getLeft() + layoutContent.getRight()) / 2;
                                    final int cy = (layoutContent.getTop() + layoutContent.getBottom()) / 2;

                                    // get the final radius for the animation circle
                                    int dx = Math.max(cx, layoutContent.getWidth() - cx);
                                    int dy = Math.max(cy, layoutContent.getHeight() - cy);
                                    float finalRadius = (float) Math.hypot(dx, dy);

                                    Animator animator = ViewAnimationUtils.createCircularReveal(layoutContent, cx, cy, 0, finalRadius);
                                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                                    animator.setDuration(1250);
                                    layoutContent.setVisibility(View.VISIBLE);
                                    animator.start();
                                } else {
                                    Timber.d("Alpha animation.");
                                    layoutContent.setAlpha(0f);
                                    layoutContent.setVisibility(View.VISIBLE);
                                    layoutContent.animate()
                                            .alpha(1f)
                                            .setDuration(1000)
                                            .setListener(null);
                                }
                            }
                        }
                    }, 330);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
    }

    @Override
    protected void onStop() {
        if (progressDialog != null) progressDialog.cancel();
        if (layoutIntroScreen != null) layoutIntroScreen.setVisibility(View.GONE);
        if (layoutContent != null) layoutContent.setVisibility(View.VISIBLE);
        MyApplication.getInstance().cancelPendingRequests(CONST.SPLASH_REQUESTS_TAG);
        super.onStop();
    }

    @Override
    public void onAttachedToWindow() {
        windowDetached = false;
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        windowDetached = true;
        super.onDetachedFromWindow();
    }
}
