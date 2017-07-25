package com.avinash_ksworks.jci.nammabengaluru;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avinash_ksworks.jci.nammabengaluru.adapter.DatabaseHelper;
import com.avinash_ksworks.jci.nammabengaluru.adapter.generic_adapter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;



public class TempleFragment extends Fragment {




    public TempleFragment() {}


    View view;
    DatabaseHelper myDBHelper;
    Context context;
    TextView t;
    ListView list;
    SimpleDraweeView draweeView;
    private List<generic_adapter> TEMPLE_ADAPTER_LIST = new ArrayList<>();

    Cursor cursor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_temple, container, false);
        context = getActivity().getApplicationContext();


        Fresco.initialize(getActivity());

        list = (ListView) view.findViewById(R.id.templeList);

        TEMPLE_ADAPTER_LIST.clear();


        myDBHelper = new DatabaseHelper(context);
        cursor = myDBHelper.getAllTemples();

        while (cursor.moveToNext()){

            TEMPLE_ADAPTER_LIST.add(new generic_adapter(

                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getDouble(8),
                    cursor.getDouble(9)

                    ));
        }


        displayList();

        return view;
    }



    public void displayList(){

        ArrayAdapter<generic_adapter> adapter = new myTempleListAdapterClass();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                cursor.moveToPosition(position);
                int img_id = Integer.parseInt(cursor.getString(0));

                Fragment fragment = new PlaceDisplayFragment(img_id);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });
    }




    public class myTempleListAdapterClass extends ArrayAdapter<generic_adapter> {

        myTempleListAdapterClass() {
            super(context, R.layout.list_item, TEMPLE_ADAPTER_LIST);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                itemView = inflater.inflate(R.layout.list_item, parent, false);

            }
            generic_adapter current = TEMPLE_ADAPTER_LIST.get(position);

            //Code to download image from url and paste.
            Uri uri = Uri.parse(current.getImage());

            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.item_Image);
            draweeView.getHierarchy().setProgressBarImage(new CircleProgressBarDrawable(1));
            draweeView.setImageURI(uri);


            TextView t_name = (TextView) itemView.findViewById(R.id.item_Title);
            t_name.setText(current.getName());


            return itemView;
        }
    }

}
