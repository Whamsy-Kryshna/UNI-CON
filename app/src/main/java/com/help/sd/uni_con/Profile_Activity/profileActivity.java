package com.help.sd.uni_con.Profile_Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.help.sd.uni_con.LogIn_SignUp_Activity.LogIn_SignUp;
import com.help.sd.uni_con.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class profileActivity extends ActionBarActivity {
    ImageView iv;Bitmap image = null;
    ParseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
        setTitle("Profile");
        String username = getIntent().getStringExtra("user");
        iv = (ImageView) findViewById(R.id.imageView4);
        final TextView tv1 = (TextView) findViewById(R.id.textView1);
        final TextView tv2 = (TextView) findViewById(R.id.textView2);
        final TextView tv3 = (TextView) findViewById(R.id.textView3);
        final TextView tv4 = (TextView) findViewById(R.id.textView4);
        final EditText et1 = (EditText) findViewById(R.id.editTextView1);
        final EditText et2 = (EditText) findViewById(R.id.editTextView2);
        final EditText et3 = (EditText) findViewById(R.id.editTextView3);
        final Spinner sp = (Spinner) findViewById(R.id.spinner);
        final Button b2 = (Button) findViewById(R.id.button4);
        final Button b1 = (Button)findViewById(R.id.button3);
        final ProgressDialog pd = new ProgressDialog(profileActivity.this);
        pd.setTitle("Loading Profile ...");
        pd.show();
        final ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username",username);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                user = list.get(0);
                ParseFile bum = (ParseFile) user.get("profile_pic");
                byte[] file = null;
                try {
                    file = bum.getData();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                image = BitmapFactory.decodeByteArray(file, 0, file.length);
                iv.setImageBitmap(image);
                if (getIntent().getBooleanExtra("edit", false) == false||!(getIntent().getStringExtra("user").equals(ParseUser.getCurrentUser().getUsername()))) {
                    et1.setVisibility(View.INVISIBLE);
                    et2.setVisibility(View.INVISIBLE);
                    et3.setVisibility(View.INVISIBLE);
                    sp.setVisibility(View.INVISIBLE);
                    b2.setVisibility(View.INVISIBLE);
                    tv1.setText(user.get("name").toString());
                    tv2.setText(user.getEmail());
                    tv3.setText(user.getUsername());
                    tv4.setText(user.get("role").toString());
                    if(!(getIntent().getStringExtra("user").equals(ParseUser.getCurrentUser().getUsername()))){
                        b1.setText("Go Back");
                    }
                } else {
                    et1.setText(user.get("name").toString());
                    et2.setText(user.getEmail());
                    et3.setText(user.getUsername());
                    if(user.get("role").toString().equals("Student"))
                        sp.setSelection(0);
                    else sp.setSelection(1);
                }
                pd.dismiss();
            }
        });

        iv.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getBooleanExtra("edit", false) == true&&(getIntent().getStringExtra("user").equals(ParseUser.getCurrentUser().getUsername()))) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), 1);
                }
            }
        });

        (b1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!(getIntent().getStringExtra("user").equals(ParseUser.getCurrentUser().getUsername()))){
                    finish();
                } else {
                    Intent intent = new Intent(profileActivity.this, change_passwordActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername(et3.getText().toString());
                user.setEmail(et2.getText().toString());
                user.put("role",sp.getSelectedItem().toString());
                user.put("name", et1.getText().toString());
                final ProgressDialog pd = new ProgressDialog(profileActivity.this);
                pd.setMessage("Updating Profile ..");
                pd.show();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imageData= stream.toByteArray();
                final ParseFile pf = new ParseFile(et3.getText().toString()+".jpg",imageData);
                pf.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        user.put("profile_pic",pf);
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                pd.dismiss();
                                ParseUser.logOut();
                                Intent intent = new Intent(profileActivity.this,LogIn_SignUp.class);
                                startActivity(intent);
                                Toast.makeText(profileActivity.this,"Log In Again",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        if(!(getIntent().getStringExtra("user").equals(ParseUser.getCurrentUser().getUsername()))){
            MenuItem edit = menu.findItem(R.id.action_edit);
            edit.setVisible(false);
        }
        if (getIntent().getBooleanExtra("edit", false) == true){
            MenuItem edit = menu.findItem(R.id.action_edit);
            edit.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            Intent intent = new Intent(profileActivity.this,profileActivity.class);
            intent.putExtra("edit",true);
            intent.putExtra("user",user.getUsername());
            finish();
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== 1){
            if(resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                iv.setImageBitmap(image);
            }
        }
    }
}
