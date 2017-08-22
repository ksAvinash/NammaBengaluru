package com.avinash_ksworks.jci.nammabengaluru;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.avinash_ksworks.jci.nammabengaluru.adapter.DatabaseHelper;
import com.avinash_ksworks.jci.nammabengaluru.adapter.MyLocation;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class PlaceDisplayFragment extends Fragment {


    public PlaceDisplayFragment() {
    }



    @SuppressLint("ValidFragment")
    public PlaceDisplayFragment(int id) {
        this.id = id;
    }

    View view;
    Cursor cursor;
    int id;
    private Double latitude, longitude;
    DatabaseHelper myDBHelper;
    Context context;
    Button gmapButton;
    String placename;
    String contactNumber;
    TextView description_textView, season_textView, additional_information, entryFee, place_textView;
    SimpleDraweeView draweeView;
    ImageView gallery_icon, contact_icon, google_distance_icon;
    LinearLayout place_view;
    ProgressDialog pd;
    Location myLocation;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_place_display, container, false);
        initializeViews();


        myDBHelper = new DatabaseHelper(context);
        cursor = myDBHelper.getPlaceById(id);

        while (cursor.moveToNext()){
            placename = cursor.getString(2);
            place_textView.setText(placename);
            description_textView.setText(cursor.getString(3));
            season_textView.setText(cursor.getString(4));
            contactNumber = cursor.getString(5);
            entryFee.setText(cursor.getString(6));
            additional_information.setText(cursor.getString(7));

            latitude = cursor.getDouble(8);
            longitude = cursor.getDouble(9);


            Uri uri = Uri.parse(cursor.getString(1));
            draweeView.getHierarchy().setProgressBarImage(new CircleProgressBarDrawable(2));
            draweeView.setImageURI(uri);

        }

        gmapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(
                        new Intent(
                                android.content.Intent.ACTION_VIEW,
                                Uri.parse("geo:" + latitude + "," + longitude + "?q=(" + place_textView.getText() + ")@" + latitude + "," + longitude)));

            }
        });


        contact_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkConnected()){
                    Toast.makeText(context, "Contact details after the ad",Toast.LENGTH_SHORT).show();
                    //show the ad
//                    AdRequest adRequest = new AdRequest.Builder().build();
//                    final InterstitialAd interstitial = new InterstitialAd(context);
//                    interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
//                    interstitial.loadAd(adRequest);
//                    interstitial.setAdListener(new AdListener() {
//                        public void onAdLoaded() {
//
//                            if (interstitial.isLoaded()) {
//                                interstitial.show();
//                            }
//                        }
//                    });

//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contactNumber, null));
                            startActivity(intent);
//                        }
//                    }, 3000);


                }else {
                    Toast.makeText(context, "No Internet Connection!",Toast.LENGTH_SHORT).show();
                }
            }
        });


        google_distance_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check for maps permissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                            //check for internet connection
                            if(isNetworkConnected()){
                                getLocation();
                            }else {
                                Toast.makeText(context, "No Internet Connection!",Toast.LENGTH_SHORT).show();
                            }
                    }
                    else {
                        Toast.makeText(context, "Enable Location permissions!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getApplication().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }

            }
        });

        gallery_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(view, "Will be implemented in the next Update!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        return view;
    }


    public void initializeViews(){
        context = getActivity().getApplicationContext();
        place_textView = (TextView) view.findViewById(R.id.place_textView);
        description_textView = (TextView) view.findViewById(R.id.description_textView);
        season_textView = (TextView) view.findViewById(R.id.season_textView);
        additional_information = (TextView) view.findViewById(R.id.additionalInformation);
        entryFee = (TextView) view.findViewById(R.id.entryFee_textView);
        gmapButton = (Button) view.findViewById(R.id.gmapButton);
        draweeView = (SimpleDraweeView) view.findViewById(R.id.layout_image);
        gallery_icon = (ImageView) view.findViewById(R.id.imagesGallery);
        contact_icon = (ImageView) view.findViewById(R.id.contactNumber);
        google_distance_icon = (ImageView) view.findViewById(R.id.GoogleMapsAPI);
        place_view = (LinearLayout) view.findViewById(R.id.place_view);
        pd = new ProgressDialog(getActivity());
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private class GoogleMapsDistanceAPI extends AsyncTask<String, String, String> {
        HttpURLConnection connection;
        BufferedReader reader;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Contact Google Maps\n Please Wait..");
            pd.setCancelable(false);
            pd.show();
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
        protected void onPostExecute(String str) {
            super.onPostExecute(str);

            Log.d("RESULT",str);
            if(pd.isShowing())
                pd.dismiss();

            try {
                JSONObject result = new JSONObject(str);
                if(result.getString("status").equals("OK")){
                    JSONArray routes = result.getJSONArray("routes");

                    //Note there can be multiple routes, alter implementation based on Distance & time
                    JSONObject routes_object = routes.getJSONObject(0);
                    JSONArray legs = routes_object.getJSONArray("legs");
                    //Read about legs
                    JSONObject legs_object = legs.getJSONObject(0);

                    JSONObject distance = legs_object.getJSONObject("distance");
                    JSONObject duration = legs_object.getJSONObject("duration");
                    str = "DISTANCE  "+distance.getString("text") + "\nTIME TO TRAVEL  " + duration.getString("text");
                    changeUI(str);

                }else {
                    Toast.makeText(context, "FAIL", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    private void changeUI(String str) {
        CardView distance_card = (CardView)view.findViewById(R.id.distance_card);
        TextView result_distance_textview = (TextView) view.findViewById(R.id.result_distance_textview);
        Button resultMapButton = (Button) view.findViewById(R.id.resultMapButton);

        place_view.setVisibility(View.GONE);
        distance_card.setVisibility(View.VISIBLE);

        result_distance_textview.setText(str);

        resultMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr="+myLocation.getLatitude()+","+myLocation.getLongitude()+"&daddr="+latitude+","+longitude+""));
                startActivity(intent);
            }
        });
    }

    private void getLocation() {
        pd.setMessage("Determining your location..");
        pd.setCancelable(false);
        pd.show();

        MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
            @Override
            public void gotLocation(Location source){
                myLocation = source;
                if(source!=null){

                    Location destination = new Location("nk");
                    destination.setLatitude(latitude);
                    destination.setLongitude(longitude);
                    Log.d("SOURCE",source.getLatitude()+", "+source.getLongitude());
                    Log.d("DESTINATION",destination.getLatitude()+", "+destination.getLongitude());

                    new GoogleMapsDistanceAPI().execute("https://maps.googleapis.com/maps/api/directions/json?origin="+source.getLatitude()+","+source.getLongitude()+"&destination="+destination.getLatitude()+","+destination.getLongitude()+"&key=AIzaSyCbYFmxSntxhbcIcpYR9R6hCfeOX1LWU2M");

                }



            }
        };

        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(context, locationResult);
    }


}


