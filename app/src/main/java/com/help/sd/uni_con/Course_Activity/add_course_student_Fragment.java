package com.help.sd.uni_con.Course_Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.help.sd.uni_con.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class add_course_student_Fragment extends Fragment {

    public add_course_student_Fragment() {
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
        return inflater.inflate(R.layout.fragment_add_course_student, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ArrayList<String> display_list = new ArrayList<>();
        final ArrayList<String> course_names = new ArrayList<>();
        final ArrayList<ParseObject> course_list = new ArrayList<>();
        ImageView search = (ImageView) getView().findViewById(R.id.imageView5);
        final EditText search_text = (EditText) getView().findViewById(R.id.editText9);
        final ListView lv = (ListView) getView().findViewById(R.id.listView7);
        final TextView tv = (TextView) getView().findViewById(R.id.textView26);
        search.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(search_text.getText()!=null||search_text.getText().toString()!=""){
                    ParseQuery<ParseObject> query_name = ParseQuery.getQuery("Courses");
                    query_name.whereMatches("courseName", search_text.getText().toString(), "i");
                    query_name.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            if(list.size()>0) {
                                for (ParseObject po : list) {
                                    display_list.add(po.get("courseCode")+" "+po.get("courseName").toString());
                                    course_list.add(po);
                                    course_names.add(po.get("courseName").toString());
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,display_list);
                                adapter.setNotifyOnChange(true);
                                lv.setAdapter(adapter);
                            } else tv.setText("No Matching Results");
                        }
                    });
                } else search_text.setError("Enter Something");
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do You Want To Register for "+course_names.get(position))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ParseObject object = new ParseObject("Reg_Info");
                                object.put("user", ParseUser.getCurrentUser());
                                object.put("course", course_list.get(position));
                                object.put("reg_status", false);
                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        Intent intent = new Intent(getActivity(), coursesActivity.class);
                                        intent.putExtra("courseCode", course_list.get(position).getInt("courseCode"));
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                });
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
