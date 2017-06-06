package com.avinash_ksworks.jci.nammabengaluru;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;



@SuppressLint("AppCompatCustomView")
public class myTextView extends TextView {

    public myTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public myTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public myTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "placenames.otf");
        setTypeface(tf);
    }

}