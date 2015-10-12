package com.mobiles.mkshop.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobiles.mkshop.NavigationDrawerCallbacks;
import com.mobiles.mkshop.NavigationDrawerFragment;
import com.mobiles.mkshop.R;
import com.mobiles.mkshop.application.Client;
import com.mobiles.mkshop.application.MkShop;
import com.mobiles.mkshop.fragments.Attendance;
import com.mobiles.mkshop.fragments.ExpenseManagerFragment;
import com.mobiles.mkshop.fragments.GeopointsFragment;
import com.mobiles.mkshop.fragments.Incentive;
import com.mobiles.mkshop.fragments.LeaderBoardFragment;
import com.mobiles.mkshop.fragments.OffersFragment;
import com.mobiles.mkshop.fragments.PartsRequestFragment;
import com.mobiles.mkshop.fragments.ProfileFragment;
import com.mobiles.mkshop.fragments.RequestRepair;
import com.mobiles.mkshop.fragments.RevenueCompatorFragment;
import com.mobiles.mkshop.fragments.SaleFragment;
import com.mobiles.mkshop.fragments.SalesReport;
import com.mobiles.mkshop.fragments.SendNotificationFragment;
import com.mobiles.mkshop.fragments.ServiceReport;
import com.mobiles.mkshop.fragments.UserListFragment;
import com.mobiles.mkshop.fragments.ViewProductFragment;
import com.mobiles.mkshop.gcm.Controller;
import com.mobiles.mkshop.gcm.RegistrationIntentService;
import com.mobiles.mkshop.pojos.LoginDetails;
import com.mobiles.mkshop.pojos.UserType;

import java.lang.reflect.Type;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerCallbacks {

    Controller aController;
    AsyncTask<Void, Void, Void> mRegisterTask;

    SharedPreferences sharedPreferences;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    Bitmap bitmapAvtar = null;
    LoginDetails loginDetailsList;
    long back_pressed;


    @Override
    protected void onResume() {
        super.onResume();


        MkShop.AUTH = sharedPreferences.getString("AUTH", null);
        MkShop.Username = sharedPreferences.getString("USERNAME", null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            mToolbar.setTitle("Mk shop "+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }





        setSupportActionBar(mToolbar);


        sharedPreferences = getSharedPreferences("MKSHOP", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("DETAIL", null);
        Type type = new TypeToken<LoginDetails>() {
        }.getType();
        loginDetailsList = new Gson().fromJson(json, type);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        mNavigationDrawerFragment.setUserData(loginDetailsList.getName(), "", loginDetailsList.getPhoto());
        // populate the navigation drawer


    }


//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment;


        if (!MkShop.Role.equalsIgnoreCase(UserType.ADMIN.name())) {

            switch (position) {
                case 0:
                    //Attendance
                    fragment = getSupportFragmentManager().findFragmentByTag(Attendance.TAG);
                    if (fragment == null) {
                        fragment = new Attendance();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    break;
                case 1: //sale
                    fragment = getSupportFragmentManager().findFragmentByTag(SaleFragment.TAG);
                    if (fragment == null) {
                        fragment = new SaleFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    break;
                case 2:
                    //request part
                    fragment = getSupportFragmentManager().findFragmentByTag(PartsRequestFragment.TAG);
                    if (fragment == null) {
                        fragment = new PartsRequestFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    break;
                case 3:
                    //service
                    fragment = getSupportFragmentManager().findFragmentByTag(RequestRepair.TAG);
                    if (fragment == null) {
                        fragment = new RequestRepair();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    break;

                case 4:
                    //View Product

                    fragment = new ViewProductFragment();

                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    break;
                case 5:
                    //offers
                    fragment = getSupportFragmentManager().findFragmentByTag(OffersFragment.TAG);
                    if (fragment == null) {
                        fragment = new OffersFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    break;

            }
        } else if
                (MkShop.Role.equalsIgnoreCase(UserType.ADMIN.name())) {
            switch (position) {
                case 0:
                    //sales Report
                    fragment = new SalesReport();
//                    if (fragment == null) {
//                        fragment = new SalesReport();
//                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    break;
                case 1: //service Report
                    fragment = new ServiceReport();
//                    if (fragment == null) {
//                        fragment = new ServiceReport();
//                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    break;
                case 2:
                    //request part
                    fragment = getSupportFragmentManager().findFragmentByTag(PartsRequestFragment.TAG);
                    if (fragment == null) {
                        fragment = new PartsRequestFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    break;
                case 3:
                    //User data
                    fragment = getSupportFragmentManager().findFragmentByTag(UserListFragment.TAG);
                    if (fragment == null) {
                        fragment = new UserListFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    break;
                case 4:
                    //LeaderBoard
                    fragment = getSupportFragmentManager().findFragmentByTag(LeaderBoardFragment.TAG);
                    if (fragment == null) {
                        fragment = new LeaderBoardFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    break;


                case 5:
                    fragment = getSupportFragmentManager().findFragmentByTag(RevenueCompatorFragment.TAG);
                    if (fragment == null) {
                        fragment = new RevenueCompatorFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    break;
                case 6:
                    fragment = getSupportFragmentManager().findFragmentByTag(SendNotificationFragment.TAG);
                    if (fragment == null) {
                        fragment = new SendNotificationFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    break;
                case 7:
                    //set incentive
                    fragment = getSupportFragmentManager().findFragmentByTag(Incentive.TAG);
                    if (fragment == null) {
                        fragment = new Incentive();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    break;
                case 8:
                    fragment = getSupportFragmentManager().findFragmentByTag(ExpenseManagerFragment.TAG);
                    if (fragment == null) {
                        fragment = new ExpenseManagerFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    break;
                case 9:
                    //set location
                    fragment = getSupportFragmentManager().findFragmentByTag(GeopointsFragment.TAG);
                    if (fragment == null) {
                        fragment = new GeopointsFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    break;

            }
        }
    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else {

            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                super.onBackPressed();
            } else {
                if (back_pressed + 2000 > System.currentTimeMillis())
                    super.onBackPressed();

                else
                    Toast.makeText(getBaseContext(), "Press once again to exit!",
                            Toast.LENGTH_SHORT).show();
                back_pressed = System.currentTimeMillis();
            }
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        if (menu.size() == 3) {
            if (sharedPreferences.getBoolean("NOTI", false)) {
                menu.getItem(2).setTitle("Disable notification");
            } else {
                menu.getItem(2).setTitle("Enable notification");

            }
            MainActivity.this.invalidateOptionsMenu();

        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.

            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logOut) {

            Client.INSTANCE.logout(MkShop.Username, new Callback<String>() {
                @Override
                public void success(String s, Response response) {


                    sharedPreferences.edit().putString("AUTH", null).apply();
                    sharedPreferences.edit().putString("USERNAME", null).apply();
                    sharedPreferences.edit().putString("DETAIL", null).apply();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();


                }

                @Override
                public void failure(RetrofitError error) {

                    if (error.getKind().equals(RetrofitError.Kind.NETWORK))
                        MkShop.toast(MainActivity.this, "please check your internet connection");
                    else MkShop.toast(MainActivity.this, "something went wrong");

                }
            });


        } else if (id == R.id.profile) {

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(ProfileFragment.TAG);
            if (fragment == null) {
                fragment = ProfileFragment.newInstance(MkShop.Username);
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();


        } else if (id == R.id.notification) {
            Boolean noti = sharedPreferences.getBoolean("NOTI", false);
            if (noti) {
                sharedPreferences.edit().putBoolean("NOTI", !noti).apply();
//
//
//                aController = (Controller) getApplicationContext();
//
//
//                mRegisterTask = new AsyncTask<Void, Void, Void>() {
//
//                    @Override
//                    protected Void doInBackground(Void... params) {
//
//                        // Register on our server
//                        // On server creates a new user
//                        aController.unregister(MainActivity.this, sharedPreferences.getString("reg", ""));
//                        return null;
//                    }
//
//                    @Override
//                    protected void onPostExecute(Void result) {
//                        mRegisterTask = null;
//                        sharedPreferences.edit().putString("reg", null).apply();
//                    }
//
//                };
//                // execute AsyncTask
//                mRegisterTask.execute(null, null, null);


            } else {
                sharedPreferences.edit().putBoolean("NOTI", !noti).apply();

                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);

            }

        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
