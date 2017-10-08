package com.help.sd.uni_con.Course_Activity;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.help.sd.uni_con.LogIn_SignUp_Activity.LogIn_SignUp;
import com.help.sd.uni_con.Profile_Activity.profileActivity;
import com.help.sd.uni_con.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class participants_Fragment extends Fragment {
    int courseCode;
    public participants_Fragment(int courseCode) {
        this.courseCode=courseCode;
    }

    public participants_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_participants, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        final ArrayList<String> participants = new ArrayList<>();
        final ArrayList<ParseUser> users = new ArrayList<>();
        final ListView lv = (ListView) getView().findViewById(R.id.listView8);
        final ParseQuery<ParseObject> q = ParseQuery.getQuery("Courses");
        q.whereEqualTo("courseCode", courseCode);
        q.include("prof");
        q.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                final ParseObject course = list.get(0);
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Reg_Info");
                query.whereEqualTo("reg_status",true);
                query.whereEqualTo("course",course);
                query.include("course");
                query.include("user");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        for (ParseObject po : list) {
                            ParseUser user = po.getParseUser("user");
                            participants.add(user.getString("name"));
                            users.add(user);
                        }
                        participants.add(course.getParseUser("prof").getString("name"));
                        users.add(course.getParseUser("prof"));
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, participants);
                        lv.setAdapter(adapter);
                        pd.dismiss();
                    }
                });
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),profileActivity.class);
                intent.putExtra("edit",false);
                intent.putExtra("user",users.get(position).getUsername());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            ParseUser.logOut();
            getActivity().finish();
            Intent intent = new Intent(getActivity(),LogIn_SignUp.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
