package com.avinash_ksworks.jci.nammabengaluru;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
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

import com.avinash_ksworks.jci.nammabengaluru.adapter.generic_adapter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

public class SearchResultsFragment extends Fragment {

    View view;
    Cursor PlaceCursor;
    Context context;
    SimpleDraweeView draweeView;
    ListView list;
    private List<generic_adapter> search_adapterList = new ArrayList<>();

    public SearchResultsFragment(){}

    @SuppressLint("ValidFragment")
    public SearchResultsFragment(Cursor placeCursor) {
        PlaceCursor = placeCursor;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_results, container, false);

        context = getContext();
        Fresco.initialize(context);
        list = (ListView) view.findViewById(R.id.searchList);


        while (PlaceCursor.moveToNext()){

            search_adapterList.add(new generic_adapter(
                    Integer.parseInt(PlaceCursor.getString(0)),
                    PlaceCursor.getString(1),
                    PlaceCursor.getString(2),
                    PlaceCursor.getString(3),
                    PlaceCursor.getString(4),
                    PlaceCursor.getString(5),
                    PlaceCursor.getString(6),
                    PlaceCursor.getString(7),
                    PlaceCursor.getDouble(8),
                    PlaceCursor.getDouble(9)
            ));

        }

        displayList();

        return view;
    }


    private void displayList() {
        ArrayAdapter<generic_adapter> adapter = new mySearchListAdapterClass();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PlaceCursor.moveToPosition(position);
                int img_id = Integer.parseInt(PlaceCursor.getString(0));

                Fragment fragment = new PlaceDisplayFragment(img_id);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });


    }


    private class mySearchListAdapterClass extends ArrayAdapter<generic_adapter> {

        mySearchListAdapterClass() {
            super(context, R.layout.list_item, search_adapterList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                itemView = inflater.inflate(R.layout.list_item, parent, false);

            }
            generic_adapter current = search_adapterList.get(position);

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
