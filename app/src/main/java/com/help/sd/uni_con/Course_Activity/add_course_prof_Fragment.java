package com.help.sd.uni_con.Course_Activity;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.help.sd.uni_con.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.List;


public class add_course_prof_Fragment extends Fragment {
    int time1_h,time1_m,time2_h,time2_m;
    public add_course_prof_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Calendar calendar = Calendar.getInstance();
        final EditText et1 = (EditText) getView().findViewById(R.id.editText10);
        final EditText et2 = (EditText) getView().findViewById(R.id.editText11);
        final EditText et3 = (EditText) getView().findViewById(R.id.editText12);
        final TextView t1 = (TextView) getView().findViewById(R.id.button);
        final TextView t2 = (TextView) getView().findViewById(R.id.button1);
        final CheckBox cb1 = (CheckBox) getView().findViewById(R.id.checkBox);
        final CheckBox cb2 = (CheckBox) getView().findViewById(R.id.checkBox2);
        final CheckBox cb3 = (CheckBox) getView().findViewById(R.id.checkBox3);
        final CheckBox cb4 = (CheckBox) getView().findViewById(R.id.checkBox4);
        final CheckBox cb5 = (CheckBox) getView().findViewById(R.id.checkBox5);
        final CheckBox cb6 = (CheckBox) getView().findViewById(R.id.checkBox6);
        final TextView tv = (TextView) getView().findViewById(R.id.textView25);
        Button b1 = (Button) getView().findViewById(R.id.button7);
        Button b2 = (Button) getView().findViewById(R.id.button8);

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time1_h =hourOfDay;
                        time1_m = minute;
                        t1.setText(hourOfDay+":"+minute);
                    }
                },calendar.HOUR_OF_DAY,calendar.MINUTE,true);
                dialog.setTitle("Pick Time");
                dialog.show();
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time2_h =hourOfDay;
                        time2_m = minute;
                        t2.setText(hourOfDay+":"+minute);
                    }
                },calendar.HOUR_OF_DAY,calendar.MINUTE,true);
                dialog.setTitle("Pick Time");

                dialog.show();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Courses");
                query.orderByDescending("courseCode");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        if(list.size()!=0){
                        tv.setText(((int)list.get(0).getInt("courseCode"))+1+"");}
                        else tv.setText("1001");
                    }
                });
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et1.getText().toString().length()!=0&&et2.getText().toString().length()!=0&&et3.getText().toString().length()!=0&&tv.getText().toString().length()!=0){
                    if(cb1.isChecked()||cb2.isChecked()||cb3.isChecked()||cb4.isChecked()||cb5.isChecked()||cb6.isChecked()){
                        if(!(t1.getText().toString().equals(getResources().getString(R.string.click_here))||t2.getText().toString().equals(getResources().getString(R.string.click_here)))){
                        if(compareTime()) {
                            final ParseObject class_info = new ParseObject("Class_Info");
                            class_info.put("mon", cb1.isChecked());
                            class_info.put("tue", cb2.isChecked());
                            class_info.put("wed", cb3.isChecked());
                            class_info.put("thr", cb4.isChecked());
                            class_info.put("fri", cb5.isChecked());
                            class_info.put("sat", cb6.isChecked());
                            class_info.put("courseCode", (Integer) Integer.parseInt(tv.getText().toString()));
                            class_info.put("time_from", t1.getText().toString());
                            class_info.put("time_to", t2.getText().toString());
                            class_info.put("address", et3.getText().toString());
                            class_info.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    ParseObject course = new ParseObject("Courses");
                                    course.put("class_info", class_info);
                                    course.put("courseName", et1.getText().toString());
                                    course.put("courseCode", (Integer) Integer.parseInt(tv.getText().toString()));
                                    course.put("description",et2.getText().toString());
                                    course.put("prof", ParseUser.getCurrentUser());
                                    course.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            Intent intent = new Intent(getActivity(), coursesActivity.class);
                                            intent.putExtra("courseCode", (Integer) Integer.parseInt(tv.getText().toString()));
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    });
                                }
                            });
                        } else Toast.makeText(getActivity(),"Invalid Timimgs",Toast.LENGTH_SHORT).show();
                        } else Toast.makeText(getActivity(),"Invalid Class Time",Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(getActivity(),"No Class Day Is Selected",Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getActivity(),"Fields Mustn't Be Empty",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean compareTime() {
        if(time1_h<=time2_h) {
            if (time1_h < time2_h)
                return true;
            else if (time1_m < time2_m)
                return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_course_prof, container, false);
    }

}
