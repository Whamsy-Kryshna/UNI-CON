package com.help.sd.uni_con.Home_Activity;

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
import android.widget.ListView;

import com.help.sd.uni_con.LogIn_SignUp_Activity.LogIn_SignUp;
import com.help.sd.uni_con.R;
import com.help.sd.uni_con.Profile_Activity.change_passwordActivity;
import com.help.sd.uni_con.Profile_Activity.profileActivity;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class profileFragment extends Fragment {

    public profileFragment() {
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getActivity().setTitle("Profile Settings");
        List<String> options = new ArrayList<>();
        options.add("View My Profile");
        options.add("Edit My Profile");
        options.add("Change Password");
        options.add("Settings");
        options.add("Delete My Account");

        ListView lv = (ListView) getView().findViewById(R.id.listView4);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,options);
        adapter.setNotifyOnChange(true);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch(position){
                    case 0 :
                        intent = new Intent(getActivity(),profileActivity.class);
                        intent.putExtra("edit",false);
                        intent.putExtra("user",ParseUser.getCurrentUser().get("username").toString());
                        startActivity(intent);
                        break;
                    case 1 :
                        intent = new Intent(getActivity(),profileActivity.class);
                        intent.putExtra("edit", true);
                        intent.putExtra("user",ParseUser.getCurrentUser().get("username").toString());
                        startActivity(intent);
                        break;
                    case 2 :
                        intent = new Intent(getActivity(),change_passwordActivity.class);
                        startActivity(intent);
                        break;
                    case 3 : getActivity().getFragmentManager().beginTransaction().replace(R.id.container,new settingsFragment()).commit();
                        break;
                    case 4 :
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Are You Sure ??");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    ParseUser.getCurrentUser().delete();
                                    ParseUser.logOut();
                                    Intent in = new Intent(getActivity(),LogIn_SignUp.class);
                                    startActivity(in);
                                    getActivity().finish();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog ad = builder.create();
                        ad.show();
                        break;
                }
            }
        });
    }
}
