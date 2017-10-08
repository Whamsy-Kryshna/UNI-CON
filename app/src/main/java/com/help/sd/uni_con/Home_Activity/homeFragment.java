package com.help.sd.uni_con.Home_Activity;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.help.sd.uni_con.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class homeFragment extends Fragment {

    public homeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().setTitleColor(Color.parseColor("#d3d3d3"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ListView lv1 = (ListView) getActivity().findViewById(R.id.listView2);
        final ListView lv2 = (ListView) getActivity().findViewById(R.id.listView3);
        final ArrayList<String> ann = new ArrayList<>(),events = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(list.size()>0){
                    for(ParseObject po:list){
                        if(po.getString("type").equals("Ann"))
                            ann.add(po.getString("info"));
                        else events.add(po.getString("info"));
                    }
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,ann);
                    lv1.setAdapter(adapter1);
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,events);
                    lv2.setAdapter(adapter2);
                }
            }
        });
    }
}
