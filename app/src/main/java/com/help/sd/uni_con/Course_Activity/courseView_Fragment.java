package com.help.sd.uni_con.Course_Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.sd.uni_con.Home_Activity.Home;
import com.help.sd.uni_con.LogIn_SignUp_Activity.LogIn_SignUp;
import com.help.sd.uni_con.R;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class courseView_Fragment extends Fragment {
    int courseCode;boolean studentFlag;ParseObject course,class_info;
    public courseView_Fragment(int courseCode) {
        this.courseCode = courseCode;
    }

    public courseView_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ImageView iv = (ImageView) getView().findViewById(R.id.imageView6);
        final TextView tv1 = (TextView) getView().findViewById(R.id.textView39);
        final TextView tv2 = (TextView) getView().findViewById(R.id.textView41);
        final TextView tv3 = (TextView) getView().findViewById(R.id.textView43);
        final TextView tv4 = (TextView) getView().findViewById(R.id.textView45);
        final TextView tv5 = (TextView) getView().findViewById(R.id.textView47);
        final TextView tv6 = (TextView) getView().findViewById(R.id.textView49);
        Button b = (Button) getView().findViewById(R.id.button11);

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        if(ParseUser.getCurrentUser().getString("role").equals("Student")){b.setText("Unregister For This Course");studentFlag=true;}
        else {b.setText("Delete This Course");studentFlag=false;}
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Courses");
        query.whereEqualTo("courseCode",courseCode);
        query.include("prof");
        query.include("class_info");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                ParseObject po = list.get(0);
                getActivity().setTitle(po.getString("courseName"));
                course = po;
                ParseUser prof = po.getParseUser("prof");
                ParseFile bum = (ParseFile) prof.get("profile_pic");
                byte[] file = null;
                try {
                    file = bum.getData();
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
                Bitmap image = BitmapFactory.decodeByteArray(file, 0, file.length);
                iv.setImageBitmap(image);
                tv1.setText(prof.getString("name"));
                tv2.setText(prof.getString("email"));
                tv3.setText(courseCode + " " + po.getString("courseName"));
                tv4.setText(po.getString("description"));
                class_info = po.getParseObject("class_info");
                StringBuilder sb = new StringBuilder();
                if (class_info.getBoolean("mon")) sb.append(" Monday");
                if (class_info.getBoolean("tue")) sb.append(" Tuesday");
                if (class_info.getBoolean("wed")) sb.append(" Wednesday");
                if (class_info.getBoolean("thr")) sb.append(" Thursday");
                if (class_info.getBoolean("fri")) sb.append(" Friday");
                if (class_info.getBoolean("sat")) sb.append(" Saturday");
                tv5.setText("Time : " + class_info.getString("time_from") + " to " + class_info.getString("time_to") + " \n on every" + sb.toString());
                tv6.setText(class_info.getString("address"));
                pd.dismiss();
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(studentFlag){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are You Sure ?").setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ProgressDialog pd1 = new ProgressDialog(getActivity());
                            pd1.setMessage("Leaving The Course...");
                            pd1.show();
                            ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Reg_Info");
                            query1.whereEqualTo("course", course);
                            query1.whereEqualTo("user", ParseUser.getCurrentUser());
                            query1.include("course");
                            query1.include("user");
                            query1.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> list, ParseException e) {
                                    ParseObject po1 = list.get(0);
                                    po1.deleteInBackground(new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            Intent intent = new Intent(getActivity(), Home.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                            pd1.dismiss();
                                        }
                                    });
                                }
                            });
                        }
                    });
                    AlertDialog ad = builder.create();
                    ad.show();
                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage("Are You Sure ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final ProgressDialog pd2 = new ProgressDialog(getActivity());
                                    pd2.setMessage("Deleting The Course...");
                                    pd2.show();
                                    course.deleteInBackground(new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            ParseQuery<ParseObject> q1 = ParseQuery.getQuery("Reg_Info");
                                            q1.whereEqualTo("course", course);
                                            q1.include("course");
                                            q1.findInBackground(new FindCallback<ParseObject>() {
                                                @Override
                                                public void done(final List<ParseObject> list, ParseException e) {
                                                    for (final ParseObject o : list) {
                                                        o.deleteInBackground();
                                                    }
                                                    Intent intent = new Intent(getActivity(), Home.class);
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                    pd2.dismiss();
                                                }
                                            });
                                        }
                                    });
                                    class_info.deleteInBackground();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder1.create().show();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            ParseUser.logOut();
            getActivity().finish();
            Intent intent = new Intent(getActivity(),LogIn_SignUp.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
