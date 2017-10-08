package com.help.sd.uni_con.Home_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.help.sd.uni_con.Post_Activity.postActivity;
import com.help.sd.uni_con.R;
import com.help.sd.uni_con.postAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class forumFragment extends Fragment {
    List<ParseObject> posts;
    public forumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forum, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        posts = new ArrayList<>();
        final ListView lv = (ListView) getView().findViewById(R.id.listView8);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.whereEqualTo("category","Forum Thread");
        query.orderByDescending("createdAt");
        query.include("author");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                postAdapter adapter = new postAdapter(getActivity(), R.layout.row_layout_post, list);
                adapter.setNotifyOnChange(true);
                posts = list;
                lv.setAdapter(adapter);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),postActivity.class);
                intent.putExtra("id",posts.get(position).getInt("post_id"));
                intent.putExtra("courseCode",0);
                startActivity(intent);
            }
        });
    }
}

