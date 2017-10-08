package com.help.sd.uni_con.Home_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.help.sd.uni_con.Home_Activity.coursesFragment;
import com.help.sd.uni_con.Profile_Activity.profileActivity;
import com.help.sd.uni_con.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;


public class add_student_to_course_Fragment extends Fragment {
    ParseUser user; ParseObject course;

    public add_student_to_course_Fragment() {
        // Required empty public constructor
    }

    public add_student_to_course_Fragment(ParseUser parseUser, ParseObject parseObject) {
        this.user = parseUser;
        this.course = parseObject;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_student_to_course, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView tv3 = (TextView) getView().findViewById(R.id.textView29);
        TextView tv1 = (TextView) getView().findViewById(R.id.textView30);
        TextView tv2 = (TextView) getView().findViewById(R.id.textView31);
        tv1.setText(user.getString("name"));
        tv2.setText(user.getUsername());
        tv3.setText("Registration Pending for "+course.getString("courseName"));
        ((Button) getView().findViewById(R.id.button9)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), profileActivity.class);
                intent.putExtra("edit", false);
                intent.putExtra("user",user.getUsername());
                startActivity(intent);
            }
        });
        ((Button) getView().findViewById(R.id.button10)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Reg_Info");
                query.whereEqualTo("user",user);
                query.whereEqualTo("course",course);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        ParseObject po = list.get(0);
                        po.put("reg_status",true);
                        po.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                getFragmentManager().beginTransaction().replace(R.id.container,new coursesFragment()).commit();
                            }
                        });
                    }
                });
            }
        });
    }
}
