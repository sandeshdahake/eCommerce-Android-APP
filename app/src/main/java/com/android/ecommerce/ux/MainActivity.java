/*******************************************************************************
 * Copyright (C) 2016 Business Factory, s.r.o.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.android.ecommerce.ux;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.ecommerce.BuildConfig;
import com.android.ecommerce.CONST;
import com.android.ecommerce.MyApplication;
import com.android.ecommerce.R;
import com.android.ecommerce.entities.Banner;
import com.android.ecommerce.entities.BannerProducts;
import com.android.ecommerce.entities.drawerMenu.DrawerItemCategory;
import com.android.ecommerce.entities.drawerMenu.DrawerItemPage;
import com.android.ecommerce.entities.drawerMenu.DrawerItemSubCategory;
import com.android.ecommerce.utils.MsgUtils;
import com.android.ecommerce.utils.Utils;
import com.android.ecommerce.ux.fragments.AccountFragment;
import com.android.ecommerce.ux.fragments.BannersFragment;
import com.android.ecommerce.ux.fragments.CategoryFragment;
import com.android.ecommerce.ux.fragments.DrawerFragment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Application is based on one core activity, which handles fragment operations.
 */
public class MainActivity extends AppCompatActivity implements DrawerFragment.FragmentDrawerListener {

    public static final String MSG_MAIN_ACTIVITY_INSTANCE_IS_NULL = "MainActivity instance is null.";
    private static MainActivity mInstance = null;

    /**
     * Reference tied drawer menu, represented as fragment.
     */
    public DrawerFragment drawerFragment;
    /**
     * Indicate that app will be closed on next back press
     */
    private boolean isAppReadyToFinish = false;



    // Fields used in searchView.
    private SimpleCursorAdapter searchSuggestionsAdapter;
    private ArrayList<String> searchSuggestionsList;


    /**
     * Update actionBar title.
     * Create action only if called from fragment attached to MainActivity.
     */
    public static void setActionBarTitle(String title) {
        MainActivity instance = MainActivity.getInstance();
        if (instance != null) {
            // TODO want different toolbar text font?
//            SpannableString s = new SpannableString(title);
//            s.setSpan(new TypefaceSpan("sans-serif-light"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            instance.setTitle(s);
            instance.setTitle(title);
        } else {
            Timber.e(MSG_MAIN_ACTIVITY_INSTANCE_IS_NULL);
        }
    }

    /**
     * Method checks if MainActivity instance exist. If so, then drawer menu header will be invalidated.
     */
    public static void invalidateDrawerMenuHeader() {
        MainActivity instance = MainActivity.getInstance();
        if (instance != null && instance.drawerFragment != null) {
            instance.drawerFragment.invalidateHeader();
        } else {
            Timber.e(MSG_MAIN_ACTIVITY_INSTANCE_IS_NULL);
        }
    }

    /**
     * Return MainActivity instance. Null if activity doesn't exist.
     *
     * @return activity instance.
     */
    private static synchronized MainActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;

        Timber.d("%s onCreate", MainActivity.class.getSimpleName());

         setContentView(R.layout.activity_main);

        // Prepare toolbar and navigation drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
        } else {
            Timber.e(new RuntimeException(), "GetSupportActionBar returned null.");
        }
        drawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.main_navigation_drawer_fragment);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.main_drawer_layout), toolbar, this);

        // Initialize list for search suggestions
        searchSuggestionsList = new ArrayList<>();

         addInitialFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Prepare search view
        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            prepareSearchView(searchItem);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Prepare toolbar search view. Invoke search suggestions and handle search queries.
     *
     * @param searchItem corresponding menu item.
     */
    private void prepareSearchView(@NonNull final MenuItem searchItem) {
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSubmitButtonEnabled(true);
        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                Timber.d("Search query text changed to: %s", newText);
                showSearchSuggestions(newText, searchView);
                return false;
            }

            public boolean onQueryTextSubmit(String query) {
                // Submit search query and hide search action view.
              //  onSearchSubmitted(query);
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();
                return true;
            }
        };

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                // Submit search suggestion query and hide search action view.
                MatrixCursor c = (MatrixCursor) searchSuggestionsAdapter.getItem(position);
                //onSearchSubmitted(c.getString(1));
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();
                return true;
            }
        });
        searchView.setOnQueryTextListener(queryTextListener);
    }

    /**
     * Show user search whisperer with generated suggestions.
     *
     * @param query      actual search query
     * @param searchView corresponding search action view.
     */
    private void showSearchSuggestions(String query, SearchView searchView) {
        if (searchSuggestionsAdapter != null && searchSuggestionsList != null) {
            Timber.d("Populate search adapter - mySuggestions.size(): %d", searchSuggestionsList.size());
            final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "categories"});
            for (int i = 0; i < searchSuggestionsList.size(); i++) {
                if (searchSuggestionsList.get(i) != null && searchSuggestionsList.get(i).toLowerCase().startsWith(query.toLowerCase()))
                    c.addRow(new Object[]{i, searchSuggestionsList.get(i)});
            }
            searchView.setSuggestionsAdapter(searchSuggestionsAdapter);
            searchSuggestionsAdapter.changeCursor(c);
        } else {
            Timber.e("Search adapter is null or search data suggestions missing");
        }
    }

    @Override
    public void prepareSearchSuggestions(List<DrawerItemCategory> navigation) {
        final String[] from = new String[]{"categories"};
        final int[] to = new int[]{android.R.id.text1};

        searchSuggestionsAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
                null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        if (navigation != null && !navigation.isEmpty()) {
            for (int i = 0; i < navigation.size(); i++) {
                if (!searchSuggestionsList.contains(navigation.get(i).getName())) {
                    searchSuggestionsList.add(navigation.get(i).getName());
                }

                if (navigation.get(i).hasChildren()) {
                    for (int j = 0; j < navigation.get(i).getSubcategories().size(); j++) {
                        if (!searchSuggestionsList.contains(navigation.get(i).getSubcategories().get(j).getName())) {
                            searchSuggestionsList.add(navigation.get(i).getSubcategories().get(j).getName());
                        }
                    }
                }
            }
            searchSuggestionsAdapter.notifyDataSetChanged();
        } else {
            Timber.e("Search suggestions loading failed.");
            searchSuggestionsAdapter = null;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_wish_list) {
           // onWishlistSelected();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Add first fragment to the activity. This fragment will be attached to the bottom of the fragments stack.
     * When fragment stack is cleared {@link #clearBackStack}, this fragment will be shown.
     */
    private void addInitialFragment() {
        Fragment fragment = new BannersFragment();
        FragmentManager frgManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = frgManager.beginTransaction();
        fragmentTransaction.add(R.id.main_content_frame, fragment).commit();
        frgManager.executePendingTransactions();
    }

    /**
     * Method creates fragment transaction and replace current fragment with new one.
     *
     * @param newFragment    new fragment used for replacement.
     * @param transactionTag text identifying fragment transaction.
     */
    private void replaceFragment(Fragment newFragment, String transactionTag) {
        if (newFragment != null) {
            FragmentManager frgManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = frgManager.beginTransaction();
            fragmentTransaction.addToBackStack(transactionTag);
            fragmentTransaction.replace(R.id.main_content_frame, newFragment).commit();
            frgManager.executePendingTransactions();
        } else {
            Timber.e(new RuntimeException(), "Replace fragments with null newFragment parameter.");
        }
    }

    /**
     * Method clear fragment backStack (back history). On bottom of stack will remain Fragment added }.
     */
    private void clearBackStack() {
        Timber.d("Clearing backStack");
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            if (BuildConfig.DEBUG) {
                for (int i = 0; i < manager.getBackStackEntryCount(); i++) {
                    Timber.d("BackStack content_%d= id: %d, name: %s", i, manager.getBackStackEntryAt(i).getId(), manager.getBackStackEntryAt(i).getName());
                }
            }
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        Timber.d("backStack cleared.");
//        TODO maybe implement own fragment backStack handling to prevent banner fragment recreation during clearing.
//        http://stackoverflow.com/questions/12529499/problems-with-android-fragment-back-stack
    }


    @Override
    public void onDrawerBannersSelected() {
        clearBackStack();
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.main_content_frame);
        if (f == null || !(f instanceof BannersFragment)) {
            Fragment fragment = new BannersFragment();
            replaceFragment(fragment, BannersFragment.class.getSimpleName());
        } else {
            Timber.d("Banners already displayed.");
        }
    }

    @Override
    public void onAccountSelected() {
        AccountFragment fragment = new AccountFragment();
        replaceFragment(fragment, AccountFragment.class.getSimpleName());
    }

    @Override
    public void onBannerProductSelected(BannerProducts product) {

    }

    @Override
    public void onDrawerItemPageSelected(DrawerItemPage drawerItemPage) {

    }

    @Override
    public void onDrawerItemCategorySelected(DrawerItemCategory drawerItemCategory) {

    }

    @Override
    public void onDrawerItemSubCategorySelected(DrawerItemSubCategory drawerItemCategory) {
        clearBackStack();
        Fragment fragment = CategoryFragment.newInstance(drawerItemCategory);
        replaceFragment(fragment, CategoryFragment.class.getSimpleName());

    }


    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.getInstance().cancelPendingRequests(CONST.MAIN_ACTIVITY_REQUESTS_TAG);
    }

    public void onBannerSelected(Banner banner) {
    }

    public void onAccountEditSelected() {
    }

    public void onOrdersHistory() {
    }

    public void startSettingsFragment() {
    }

    public void registerGcmOnServer() {
    }

    public static void updateCartCountNotification() {
    }

    public void onProductSelected(String id) {

    }
}
