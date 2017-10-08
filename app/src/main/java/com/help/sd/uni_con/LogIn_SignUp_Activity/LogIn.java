package com.help.sd.uni_con.LogIn_SignUp_Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.help.sd.uni_con.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.Timer;
import java.util.TimerTask;

public class LogIn extends Fragment {

    private OnFragmentInteractionListener mListener;
    private int i =1;
    ImageView iv;
    Timer timer;
    public LogIn() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        //getActivity().getActionBar().setIcon(R.mipmap.ic_launcher);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Log In");
        Button b1 = (Button) getView().findViewById(R.id.button);
        Button b2 = (Button) getView().findViewById(R.id.button2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogInButtonPressed();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewUserButtonPressed();
            }
        });
        iv = (ImageView) getView().findViewById(R.id.imageView);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            iv.setImageResource(getResourceInt(i));
                            if (i == 3) i = 1;
                            else i++;

                    }
                });

            }
        }, 0, 6000);
    }

    private int getResourceInt(int num) {
        switch (num){
            case 1 : return R.drawable.quote2;
            case 2 : return R.drawable.quote1;
            case 3 : return R.drawable.quote3;
        }
        return 0;
    }

    // TODO: Rename method, update argument and hook method into UI event
    private void onLogInButtonPressed() {
        timer.cancel();
        final EditText et_email = (EditText) getView().findViewById(R.id.editText);
        final EditText et_passwd = (EditText) getView().findViewById(R.id.editText2);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Logging In...");
        pd.show();
        ParseUser.logInInBackground(et_email.getText().toString(), et_passwd.getText().toString(), new LogInCallback() {

            @Override
            public void done(ParseUser user, ParseException e) {
                if (et_email.getText().toString().length() == 0)
                    et_email.setError("Enter ID");
                else if (et_passwd.getText().toString().length() == 0)
                    et_passwd.setError("Enter Password");
                else if (user != null) {
                    mListener.onLogInButtonPressed();
                } else {
                    Toast.makeText(getActivity(), "Log In Failed, Invalid Email or Password", Toast.LENGTH_SHORT).show();
                }
                pd.dismiss();
            }
        });
    }

    private void onNewUserButtonPressed() {
        timer.cancel();
        if (mListener != null) {
            mListener.onNewUserButtonPressed();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        timer.cancel();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onNewUserButtonPressed();
        public void onLogInButtonPressed();
    }

}
