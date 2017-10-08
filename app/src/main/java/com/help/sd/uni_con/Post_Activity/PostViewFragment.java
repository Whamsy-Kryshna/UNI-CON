package com.help.sd.uni_con.Post_Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.help.sd.uni_con.R;
import com.help.sd.uni_con.commentsAdapter;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class PostViewFragment extends Fragment {

    ParseObject post;ListView lv;TextView tv5;

    public PostViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Loading Post ...");
        pd.setCancelable(false);
        pd.show();
        final RelativeLayout rl = (RelativeLayout) getActivity().findViewById(R.id.root);
        final TextView tv1 = (TextView) getActivity().findViewById(R.id.textView56);
        final TextView tv2 = (TextView) getActivity().findViewById(R.id.textView58);
        final TextView tv3 = (TextView) getActivity().findViewById(R.id.textView60);
        final TextView tv4 = (TextView) getActivity().findViewById(R.id.textView61);
        tv5 = (TextView) getActivity().findViewById(R.id.textView62);
        final TextView tv6 = (TextView) getActivity().findViewById(R.id.textView59);
        ImageView iv2 = (ImageView) getActivity().findViewById(R.id.imageView10);
        final EditText et = (EditText) getActivity().findViewById(R.id.editText15);
        et.setFocusable(false);
        lv = (ListView) getActivity().findViewById(R.id.listView9);
        iv2.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.send4));
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.whereEqualTo("post_id", getActivity().getIntent().getIntExtra("id", 1));
        query.include("author");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                post = list.get(0);
                updateList();
                tv1.setText(post.getString("title"));
                if (post.getParseFile("image") != null) {
                    tv4.setText("View Image Resource : " + post.getParseFile("image").getName());
                }
                switch (post.getString("category")) {
                    case "Announcement":
                        tv3.setTextColor(Color.BLUE);
                        tv3.setText("Announcement");
                        break;
                    case "Submission Request":
                        tv3.setTextColor(Color.RED);
                        tv3.setText("Submission Request");
                        Calendar date = Calendar.getInstance();
                        date.setTime(post.getDate("deadline"));
                        if (post.getParseFile("material") == null)
                            tv5.setText("Click Here To Upload !! (only PDF)" + "\nDeadline is " + date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " + date.get(Calendar.DAY_OF_MONTH));
                        else
                            tv5.setText("File Uploaded : " + post.getParseFile("material").getName());
                        break;
                    case "Material Post":
                        tv3.setTextColor(Color.GREEN);
                        tv3.setText("Material Post");
                        tv5.setText("Click Here To Download File : " + post.getParseFile("material").getName());
                        break;
                    case "Forum Thread":
                        tv3.setTextColor(Color.YELLOW);
                        tv3.setText("Forum Thread");
                        break;
                }
                tv6.setText("Posted By : " + post.getParseUser("author").getString("name"));
                if (post.getInt("courseCode") != 0) {
                    ParseQuery<ParseObject> q = ParseQuery.getQuery("Courses");
                    q.whereEqualTo("courseCode", post.getInt("courseCode"));
                    q.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            tv2.setText(post.getString("content") + "\n" + "Posted Under Course : " + list.get(0).getInt("courseCode") + "  " + list.get(0).getString("courseName"));
                            pd.dismiss();
                        }
                    });
                } else {
                    tv2.setText(post.getString("content"));
                    pd.dismiss();
                }
            }
        });
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tv5.getText().equals(getString(R.string.empty))) {
                    switch (post.getString("category")) {
                        case "Submission Request":
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            startActivityForResult(intent, 10);
                            break;
                        case "Material Post":
                            final ParseFile pf = post.getParseFile("material");
                            pf.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, ParseException pe) {
                                    FileOutputStream fos = null;
                                    try {
                                        fos = new FileOutputStream(new File(getActivity().getFilesDir().getAbsolutePath(),pf.getName()));
                                        //fos = getActivity().openFileOutput(pf.getName(), Context.MODE_PRIVATE);
                                        Log.d("test",getActivity().getFilesDir().getAbsolutePath());
                                        fos.write(bytes);
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(Uri.parse(getActivity().getFilesDir().getAbsolutePath()+"/"+pf.getName()),"application/pdf");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }finally {
                                        try {
                                            fos.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                            break;
                    }
                }
            }
        });
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et2  = new EditText(getActivity());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Enter Comment")
                        .setView(et2)
                        .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setTitle("Saving Comment ..");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                if (!et2.getText().equals("")) {
                                    ParseObject parseObject = new ParseObject("Comments");
                                    parseObject.put("post", post);
                                    parseObject.put("comment", et2.getText().toString());
                                    parseObject.put("author", ParseUser.getCurrentUser().getString("name"));
                                    parseObject.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            updateList();
                                            progressDialog.dismiss();
                                        }
                                    });
                                }
                            }
                        }).setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();

            }
        });
    }

    private void updateList() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comments");
        query.whereEqualTo("post", post);
        query.include("post");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (list.size() > 0) {
                    commentsAdapter adapter = new commentsAdapter(getActivity(), R.layout.row_layout_comment, list);
                    lv.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 10){
            if(resultCode == Activity.RESULT_OK){
                final ProgressDialog pd = new ProgressDialog(getActivity());
                pd.setTitle("uploading file..");
                pd.show();
                Uri fileUri = data.getData();
                Log.d("test", fileUri.getPath());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                File file = null;
                try {
                    file = new File(fileUri.getPath());
                    FileInputStream fis = new FileInputStream(file);
                    byte[] buffer = new byte[1024];
                    int n = fis.read(buffer);
                    while (n!=-1){
                        baos.write(buffer,0,n);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String file_name = null;
                final byte[] material;
                if(file!=null)
                    file_name = file.getName();
                material = baos.toByteArray();
                final String finalFile_name = file_name;
                final ParseFile pf = new ParseFile(file_name+".pdf",material);
                pf.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        post.put("material",pf);
                        post.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(finalFile_name !=null)
                                    tv5.setText(finalFile_name +" uploaded successfully.");
                                pd.dismiss();
                            }
                        });
                    }
                });
            }
        }
    }
}
