package com.avinash_ksworks.jci.nammabengaluru;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.avinash_ksworks.jci.nammabengaluru.adapter.DatabaseHelper;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.sa90.materialarcmenu.ArcMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.fragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    SliderLayout mDemoSlider;
    static int localVersion, serverVersion;
    ProgressDialog pd;
    View view;
    DatabaseHelper myDBHelper;
    ArcMenu arcMenu;
    FloatingActionButton fab_temple, fab_trekking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeViews();

        setImageSlider();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        if(isNetworkConnected()){

            SharedPreferences preferences = getSharedPreferences("only_once", Context.MODE_PRIVATE);
            if(preferences.getInt("first", 0) == 0) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("first", 1);
                editor.commit();
                Toast.makeText(getApplicationContext(), "Please Wait!",Toast.LENGTH_SHORT).show();
                new FetchVersion().execute("http://nammabengaluru.000webhostapp.com/general/base_version.json");
            }

            Toast.makeText(this, "An ad appears in 5 seconds",Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

//                AdRequest adRequest = new AdRequest.Builder().build();
//                final InterstitialAd interstitial = new InterstitialAd(getApplicationContext());
//                interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
//                interstitial.loadAd(adRequest);
//                interstitial.setAdListener(new AdListener() {
//                    public void onAdLoaded() {
//
//                        if (interstitial.isLoaded()) {
//                            interstitial.show();
//                        }
//                    }
//                });
                }
            }, 3000);

        }





    }

    public void initializeViews(){
        view = findViewById(android.R.id.content);
        pd = new ProgressDialog(this);
        arcMenu = (ArcMenu) findViewById(R.id.arcMenu);

        fab_temple = (FloatingActionButton)findViewById(R.id.fab_temple);
        fab_trekking = (FloatingActionButton)findViewById(R.id.fab_trekking);
    }

    public void fab_button_click(View view){
        arcMenu.toggleMenu();

        Fragment fragment;
        FragmentTransaction ft;
        switch (view.getId()){

            case R.id.fab_lakes:
                fragment = new LakesFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.fab_temple:
                fragment = new TempleFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.fab_trekking:
                fragment = new TrekkingFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.fab_kids:
                fragment = new KidsFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.fab_parks:
                fragment = new ParkFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;
        }
    }

    public void setImageSlider(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                mDemoSlider = (SliderLayout) findViewById(R.id.mainActivitySlider);
                final HashMap<String, Integer> file_maps = new HashMap<>();
                //Positively do not change any images
                file_maps.put("Tippu Fort", R.drawable.tippufort);
                file_maps.put("Cubbon Park", R.drawable.cubbonpark);
                file_maps.put("Ulsoor Lake", R.drawable.ulsoor);
                file_maps.put("Vidhana Soudha", R.drawable.vidhana);
                file_maps.put("Visvesvaraya Museum", R.drawable.vishwesh);

                for (final String name : file_maps.keySet()) {
                    TextSliderView textSliderView = new TextSliderView(getApplicationContext());
                    // initialize a SliderLayout
                    textSliderView
                            .description(name)
                            .image(file_maps.get(name))
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {

                                    String[] str = name.split(" ");
                                    myDBHelper = new DatabaseHelper(getApplicationContext());
                                    Cursor cursor = myDBHelper.getPlaceByString(str[0]);

                                    Fragment fragment = new SearchResultsFragment(cursor);
                                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.content_main, fragment);
                                    ft.addToBackStack(null);
                                    ft.commit();

                                }
                            })
                            .setScaleType(BaseSliderView.ScaleType.Fit);

                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle()
                            .putString("extra", name);

                    mDemoSlider.addSlider(textSliderView);
                }

                mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                mDemoSlider.setDuration(7000);


            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        final SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener(){

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        myDBHelper = new DatabaseHelper(getApplicationContext());
                        Cursor cursor = myDBHelper.getPlaceByString(query);

                        Fragment fragment = new SearchResultsFragment(cursor);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_main, fragment);
                        ft.commit();

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        myDBHelper = new DatabaseHelper(getApplicationContext());
                        Cursor cursor = myDBHelper.getPlaceByString(newText);

                        Fragment fragment = new SearchResultsFragment(cursor);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_main, fragment);
                        ft.commit();

                        return false;
                    }
                }
        );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){

            case R.id.action_share:
                            String str = "https://play.google.com/store/apps/details?id=" + getPackageName();
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT,
                                    "All places you can roam in Bengaluru\n\nDownload:\n" + str);
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);
                return true;

            case R.id.action_aboutDev:
                            Intent intent = new Intent(this, AboutDev.class);
                            startActivity(intent);
                return true;

            case R.id.action_refresh:
                            if (isNetworkConnected()) {
                                SharedPreferences preferences = getSharedPreferences("base_version", Context.MODE_PRIVATE);
                                localVersion = preferences.getInt("version", 0);
                                Toast.makeText(getApplicationContext(), "Please Wait!",Toast.LENGTH_SHORT).show();
                                new FetchVersion().execute("http://nammabengaluru.000webhostapp.com/general/base_version.json");
                            }
                            else
                                Snackbar.make(view, "No Internet Connection! ", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                return  true;


        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment;
        FragmentTransaction ft;

        switch (id){

            case R.id.nav_temples:
                    fragment = new TempleFragment();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_main, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                break;


            case R.id.nav_otherPlaces:
                    fragment = new OtherFragment();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_main, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                break;
            case R.id.nav_trekking:
                    fragment = new TrekkingFragment();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_main, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                break;

            case R.id.nav_parks:
                fragment = new ParkFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.nav_lakes:
                fragment = new LakesFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;


            case R.id.nav_kids:
                fragment = new KidsFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;


            case  R.id.nav_home:
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                break;

            case R.id.feedback:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "justmailtoavi@gmail.com, raghavisatish24@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Namma Bengaluru Feedback");
                startActivity(intent);
                break;

            case R.id.nav_maps:
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;


            case R.id.new_place:
                Snackbar.make(view,"Just mail the place name, We'll add it asap!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public class FetchVersion extends AsyncTask<String, String, String> {
        HttpURLConnection connection;
        BufferedReader reader;

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                return builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if(s!=null){
                try {
                    JSONObject parent = new JSONObject(s);
                    JSONObject base_version = parent.getJSONObject("base_version");
                    serverVersion = base_version.getInt("version");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (localVersion != serverVersion) {
                    pd.setMessage("Fetching new places please wait..");
                    pd.setCancelable(false);
                    pd.show();
                    new baseFile().execute("http://nammabengaluru.000webhostapp.com/general/base.json");
                }else {
                    Snackbar.make(view,"All places are upto date", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }

        }
    }

    public class baseFile extends AsyncTask<String, String, String> {

        HttpURLConnection connection;
        BufferedReader reader;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                return builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);


            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {

                        JSONObject parent = new JSONObject(s);
                        JSONArray list = parent.getJSONArray("list");

                        if (list != null) {


                            if (pd.isShowing())
                                pd.dismiss();


                            myDBHelper = new DatabaseHelper(getApplicationContext());
                            myDBHelper.deleteTables();


                            for (int i = 0; i < list.length(); i++) {

                                JSONObject child = list.getJSONObject(i);
                                String image = child.getString("image");
                                int id = child.getInt("id");
                                String name = child.getString("name");
                                String description = child.getString("description");
                                String bestSeason = child.getString("bestSeason");
                                String contact = child.getString("contact");
                                String entryFee = child.getString("entryFee");
                                String additionalInformation = child.getString("additionalInformation");
                                Double latitute  = child.getDouble("latitude");
                                Double longitude  = child.getDouble("longitude");
                                String category = child.getString("category");
                                myDBHelper.insert_into_places(id, image, name, description,bestSeason, contact, entryFee, additionalInformation, latitute, longitude, category);
                            }


                            SharedPreferences preferences = getSharedPreferences("base_version", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("version", serverVersion);
                            editor.commit();


                        } else {
                            SharedPreferences preferences = getSharedPreferences("base_version", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("version", localVersion);
                            editor.commit();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            Snackbar.make(view,"Update Successful", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }

    }


}
