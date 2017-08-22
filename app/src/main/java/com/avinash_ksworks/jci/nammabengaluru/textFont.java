package com.avinash_ksworks.jci.nammabengaluru;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by avinashk on 26/06/17.
 */

@SuppressLint("AppCompatCustomView")
public class textFont extends TextView {

    public textFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public textFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public textFont(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "reading_font.otf");
        setTypeface(tf);
    }

}