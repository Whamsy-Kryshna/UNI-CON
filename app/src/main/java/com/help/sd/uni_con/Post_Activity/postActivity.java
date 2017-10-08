package com.help.sd.uni_con.Post_Activity;

import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.help.sd.uni_con.R;

public class postActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
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
        findViewById(R.id.container).setBackgroundColor(getResources().getColor(R.color.color_light));
        if(getIntent().getIntExtra("id",0)==0){
            getFragmentManager().beginTransaction().add(R.id.container,new create_post_Fragment()).commit();
        } else getFragmentManager().beginTransaction().add(R.id.container,new PostViewFragment()).commit();
    }
}
