package com.help.sd.uni_con.LogIn_SignUp_Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.help.sd.uni_con.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class SignUp extends Fragment {

    private OnFragmentInteractionListener mListener;
    ImageView iv;Bitmap image = null;
    public SignUp() {
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

        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.signup);
        iv = (ImageView) getView().findViewById(R.id.imageView);
        image = BitmapFactory.decodeResource(getResources(),
                R.drawable.no_image);
        iv.setImageBitmap(image);

        iv.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), 1);
            }
        });

        getView().findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignUpButtonPressed();
            }
        });
        getView().findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelButtonPressed();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    private void onSignUpButtonPressed() {
        EditText et_name = (EditText) getView().findViewById(R.id.editText1);
        EditText et_email = (EditText) getView().findViewById(R.id.editText2);
        EditText et_userName = (EditText) getView().findViewById(R.id.editText5);
        EditText et_passwd = (EditText) getView().findViewById(R.id.editText3);
        EditText et_confirm_passwd = (EditText) getView().findViewById(R.id.editText4);
        Spinner sp = (Spinner) getView().findViewById(R.id.spinner);

        final ParseUser user = new ParseUser();
        if (et_name.getText().toString().length()==0||et_email.getText().toString().length()==0||et_passwd.getText().toString().length()==0||et_confirm_passwd.getText().toString().length()==0||et_userName.getText().toString().length()==0)
            Toast.makeText(getActivity(), "Fields Shouldn't Be Empty", Toast.LENGTH_SHORT).show();
        else if (et_passwd.getText().toString().equals( et_confirm_passwd.getText().toString())){
            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setTitle("Signing Up A New Account...");
            pd.show();
            user.setPassword(et_passwd.getText().toString());
            user.setUsername(et_userName.getText().toString());
            user.setEmail(et_email.getText().toString());
            user.put("name", (String) et_name.getText().toString());
            user.put("role", (String) sp.getSelectedItem().toString());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageData= stream.toByteArray();
            if(imageData != null){
                final ParseFile pf = new ParseFile(et_userName.getText().toString()+".jpg",imageData);
                pf.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        user.put("profile_pic",pf);
                        user.signUpInBackground(new SignUpCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(getActivity(), "Sign Up successful", Toast.LENGTH_SHORT).show();
                                    mListener.onSignUpButtonPressed();
                                } else {
                                    Toast.makeText(getActivity(), "Sign Up Failed, Email or User Name Already Exists", Toast.LENGTH_SHORT).show();
                                }
                                pd.dismiss();
                            }
                        }); } }); } }else Toast.makeText(getActivity(), "Password Doesn't Match", Toast.LENGTH_SHORT).show();


    }




    private void onCancelButtonPressed() {
        if (mListener != null) {
            mListener.onCancelButtonPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== 1){
            if(resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                try {
                    image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                iv.setImageBitmap(image);
            }
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
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onSignUpButtonPressed();
        public void onCancelButtonPressed();
    }
}
