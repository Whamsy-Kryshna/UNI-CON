package com.help.sd.uni_con.Course_Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.help.sd.uni_con.LogIn_SignUp_Activity.LogIn_SignUp;
import com.help.sd.uni_con.Post_Activity.postActivity;
import com.help.sd.uni_con.R;
import com.help.sd.uni_con.postAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class posts_Fragment extends Fragment {
boolean studentFlag=false;int courseCode;List<ParseObject> posts;
    ListView lv;
    public posts_Fragment(int courseCode) {
        this.courseCode = courseCode;
    }

    public posts_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(ParseUser.getCurrentUser().getString("role").equals("Student"))
            studentFlag = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        posts = new ArrayList<>();
        lv = (ListView) getView().findViewById(R.id.listView8);
        listUpdate();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), postActivity.class);
                intent.putExtra("id", posts.get(position).getInt("post_id"));
                intent.putExtra("courseCode",courseCode);
                startActivity(intent);
            }
        });
    }

    private void listUpdate() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.whereEqualTo("courseCode",courseCode);
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
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(!studentFlag)
        inflater.inflate(R.menu.global,menu);
        else inflater.inflate(R.menu.home,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_new) {
            Intent intent = new Intent(getActivity(),postActivity.class);
            intent.putExtra("id",0);
            intent.putExtra("courseCode",courseCode);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_logout) {
            ParseUser.logOut();
            getActivity().finish();
            Intent intent = new Intent(getActivity(),LogIn_SignUp.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        listUpdate();
    }
}
