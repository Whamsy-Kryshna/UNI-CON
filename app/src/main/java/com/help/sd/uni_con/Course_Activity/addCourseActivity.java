package com.help.sd.uni_con.Course_Activity;

import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.help.sd.uni_con.R;

public class addCourseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.root);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);
        //actionBar.setSubtitle("Welcome to UNI-CON");
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF7A9585")));
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_image));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.color_dark));
            getWindow().setStatusBarColor(getResources().getColor(R.color.color_dark));
        }
        rl.setBackgroundColor(getResources().getColor(R.color.color_light));
        if (getIntent().getBooleanExtra("student",false))
            getFragmentManager().beginTransaction().add(R.id.root, new add_course_student_Fragment()).commit();
         else
            getFragmentManager().beginTransaction().add(R.id.root, new add_course_prof_Fragment()).commit();
    }
}
