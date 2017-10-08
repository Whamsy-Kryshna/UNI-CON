package com.help.sd.uni_con.LogIn_SignUp_Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.RelativeLayout;

import com.help.sd.uni_con.Home_Activity.Home;
import com.help.sd.uni_con.R;
import com.parse.ParseUser;

import java.util.Timer;
import java.util.TimerTask;


public class LogIn_SignUp extends AppCompatActivity implements
        LogIn.OnFragmentInteractionListener,
        SignUp.OnFragmentInteractionListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in__sign_up);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.root);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setIcon(R.mipmap.ic_launcher);
                actionBar.setSubtitle("Welcome to UNI-CON");
                //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF7A9585")));
                actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_image));
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setNavigationBarColor(getResources().getColor(R.color.color_dark));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.color_dark));
                }
                rl.setBackgroundColor(getResources().getColor(R.color.color_light));
                ParseUser currentUser = ParseUser.getCurrentUser();
                if(currentUser!=null){
                    startHomeActivity();
                } else getFragmentManager().beginTransaction().add(R.id.root,new LogIn()).commit();

    }

    @Override
    public void onNewUserButtonPressed() {
        getFragmentManager().beginTransaction().replace(R.id.root,new SignUp()).commit();
    }

    @Override
    public void onLogInButtonPressed() {
        startHomeActivity();
    }

    @Override
    public void onSignUpButtonPressed() {
        startHomeActivity();
    }

    @Override
    public void onCancelButtonPressed() {
        getFragmentManager().beginTransaction().replace(R.id.root,new LogIn()).commit();
    }

    private void startHomeActivity(){
        Intent intent = new Intent(LogIn_SignUp.this,Home.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
