package com.avinash_ksworks.jci.nammabengaluru;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.avinash_ksworks.jci.nammabengaluru.adapter.DatabaseHelper;
import com.facebook.drawee.view.SimpleDraweeView;



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
    TextView description_textView, season_textView, additionalInformation, entryFee, place_textView;
    SimpleDraweeView draweeView;
    LottieAnimationView favourite_icon, gallery_icon, visited_icon;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
            entryFee.setText(cursor.getString(6));
            additionalInformation.setText(cursor.getString(7));

            latitude = cursor.getDouble(8);
            longitude = cursor.getDouble(9);

            //image downloading
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

        favourite_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favourite_icon.playAnimation();
                myDBHelper.insertIntoFavourites(id);
                Snackbar.make(view, placename+" Added to Favourites list", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }
        });

        visited_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visited_icon.playAnimation();
                myDBHelper.insertIntoVisited(id);
                Snackbar.make(view,"Nice, you have visited "+placename, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        gallery_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery_icon.playAnimation();

            }
        });



        return view;
    }



    public void initializeViews(){
        context = getActivity().getApplicationContext();
        place_textView = (TextView) view.findViewById(R.id.place_textView);
        description_textView = (TextView) view.findViewById(R.id.description_textView);
        season_textView = (TextView) view.findViewById(R.id.season_textView);
        additionalInformation = (TextView) view.findViewById(R.id.additionalInformation);
        entryFee = (TextView) view.findViewById(R.id.entryFee_textView);
        gmapButton = (Button) view.findViewById(R.id.gmapButton);
        draweeView = (SimpleDraweeView) view.findViewById(R.id.layout_image);

        favourite_icon = (LottieAnimationView) view.findViewById(R.id.favourite_animation);
        gallery_icon = (LottieAnimationView) view.findViewById(R.id.gallery_animation);
        visited_icon = (LottieAnimationView) view.findViewById(R.id.visited_animation);


        favourite_icon.setAnimation("add_to_favourites_animation.json");
        gallery_icon.setAnimation("emoji_shock.json");

        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(Color.YELLOW, PorterDuff.Mode.LIGHTEN);
        visited_icon.addColorFilter(colorFilter);
        visited_icon.setAnimation("visited_animation.json");
    }


}
