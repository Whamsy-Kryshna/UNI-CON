package com.help.sd.uni_con.Home_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.help.sd.uni_con.R;
import com.help.sd.uni_con.Course_Activity.addCourseActivity;
import com.help.sd.uni_con.Course_Activity.coursesActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class coursesFragment extends Fragment {

    ParseUser user;boolean studentFlag;
    public coursesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        user = ParseUser.getCurrentUser();
        if(user.get("role").toString().equals("Student"))
            studentFlag = true;
        else studentFlag = false;
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_courses, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ArrayList<String> pending=new ArrayList<>();final ArrayList<String> myCourses = new ArrayList<>();
        final ArrayList<Integer> myCourses1 = new ArrayList<>();
        final ArrayList<ParseObject> pending1 = new ArrayList<>();
        final ArrayList<ParseUser> pending2 = new ArrayList<>();
        final TextView emptyMessage1 = (TextView) getView().findViewById(R.id.textView16);
        final TextView emptyMessage2 = (TextView) getView().findViewById(R.id.textView17);
        TextView requests = (TextView) getView().findViewById(R.id.textView7);
        Button b = (Button) getView().findViewById(R.id.button6);
        final ListView lv1 = (ListView) getView().findViewById(R.id.listView5);
        final ListView lv2 = (ListView) getView().findViewById(R.id.listView6);
        final ProgressBar pb1 = (ProgressBar) getView().findViewById(R.id.progressBar4);
        final ProgressBar pb2 = (ProgressBar) getView().findViewById(R.id.progressBar2);
        pb1.setVisibility(View.VISIBLE);
        pb2.setVisibility(View.VISIBLE);
        ParseQuery<ParseObject> query = null;
        if(studentFlag) {
            query = ParseQuery.getQuery("Reg_Info");
            query.whereEqualTo("user", user);
            query.whereEqualTo("reg_status", true);
            query.include("course");
        } else {
            query = ParseQuery.getQuery("Courses");
            query.whereEqualTo("prof",user);
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (list.size() != 0) {
                    for (ParseObject o : list) {
                        ParseObject course = null;
                        if(studentFlag) {
                            course = o.getParseObject("course");
                        } else course = o;
                        myCourses.add(course.get("courseCode")+" "+course.get("courseName").toString());
                        myCourses1.add(course.getInt("courseCode"));

                    }
                    if(getActivity()!=null){
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, myCourses);
                    lv1.setAdapter(adapter);}
                } else emptyMessage1.setText("Nothing to Display");
                pb1.setVisibility(View.INVISIBLE);
            }
        });
        if(!studentFlag){
            requests.setText("Requests Pending");
            b.setText("Add A Course");
        }
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Reg_Info");;
        if(studentFlag) {
            query2.whereEqualTo("reg_status", false);
            query2.whereEqualTo("user", user);
            query2.include("course");
            query2.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (list.size() != 0) {
                        for (ParseObject o : list) {
                            ParseObject course = o.getParseObject("course");
                            pending.add(course.getInt("courseCode") + " " + course.getString("courseName").toString());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, pending);
                        lv2.setAdapter(adapter);
                    } else {
                        emptyMessage2.setText("Nothing to Display");
                    } pb2.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            query2.whereEqualTo("reg_status", false);
            query2.include("course");
            query2.include("user");
            query2.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (list.size() != 0) {
                        Log.d("test", "x");
                        for (ParseObject o : list) {
                            ParseObject course = o.getParseObject("course");
                            if (myCourses1.contains(course.getInt("courseCode"))) {
                                ParseUser student = o.getParseUser("user");
                                pending.add(student.getString("name") + " wants to register for " + course.getString("courseName"));
                                pending1.add(course);
                                pending2.add(student);
                            }
                        }
                        if (pending.size() != 0) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, pending);
                            lv2.setAdapter(adapter);
                        } else emptyMessage2.setText("Nothing to Display");
                    } else emptyMessage2.setText("Nothing to Display");
                    pb2.setVisibility(View.INVISIBLE);
                }
            });
        }

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), coursesActivity.class);
                intent.putExtra("courseCode", myCourses1.get(position));
                startActivity(intent);
            }
        });
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(studentFlag){
                    Toast.makeText(getActivity(),"Registration Acceptance Pending With Professor",Toast.LENGTH_LONG).show();
                } else {
                    getFragmentManager().beginTransaction().replace(R.id.container,new add_student_to_course_Fragment(pending2.get(position),pending1.get(position))).commit();
                }
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), addCourseActivity.class);
                intent.putExtra("student", studentFlag);
                startActivity(intent);
            }
        });
    }
}
