package com.help.sd.uni_con.Profile_Activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.help.sd.uni_con.Home_Activity.Home;
import com.help.sd.uni_con.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class change_passwordActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
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
        final EditText et_old = (EditText) findViewById(R.id.editText6);
        final EditText et_new = (EditText) findViewById(R.id.editText7);
        final EditText et_confirm = (EditText) findViewById(R.id.editText8);
        Button b = (Button) findViewById(R.id.button5);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_old.getText().toString().length()==0||et_new.getText().toString().length()==0||et_confirm.getText().toString().length()==0){
                    Toast.makeText(change_passwordActivity.this, "Fields Shouldn't Be Empty", Toast.LENGTH_SHORT).show();
                } else {
                      ParseUser.logInInBackground(ParseUser.getCurrentUser().getUsername(), et_old.getText().toString(), new LogInCallback() {
                          @Override
                          public void done(ParseUser parseUser, ParseException e) {
                              if(e==null) {
                                  parseUser.setPassword(et_new.getText().toString());
                                  parseUser.saveInBackground(new SaveCallback() {
                                      @Override
                                      public void done(ParseException e) {
                                          Toast.makeText(change_passwordActivity.this, "Password Update Successful", Toast.LENGTH_LONG).show();
                                          Intent intent = new Intent(change_passwordActivity.this, Home.class);
                                          startActivity(intent);
                                          finish();
                                      }
                                  });
                              }
                              else if(et_new.getText().toString().equals(et_confirm.getText().toString())){
                                  Toast.makeText(change_passwordActivity.this,"Incorrect Old Password",Toast.LENGTH_LONG).show();
                              } else Toast.makeText(change_passwordActivity.this,"Passwords Doesn't Match",Toast.LENGTH_LONG).show();
                          }
                      });

                }
            }
        });
    }


}
