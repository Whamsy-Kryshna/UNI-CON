package com.help.sd.uni_con.Post_Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.help.sd.uni_con.R;
import com.parse.FindCallback;
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
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class create_post_Fragment extends Fragment {

    Bitmap image =null;ImageView iv;byte[] material = null;String file_name = "file";
    int y=0, m, d, h, min;TextView tv;
    public create_post_Fragment() {
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
        return inflater.inflate(R.layout.fragment_create_post, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Calendar c =Calendar.getInstance();
        final EditText et1 = (EditText) getView().findViewById(R.id.editText13);
        final EditText et2 = (EditText) getView().findViewById(R.id.editText14);
        final Spinner s = (Spinner) getView().findViewById(R.id.spinner2);
        iv = (ImageView) getView().findViewById(R.id.imageView7);
        Button b1 = (Button) getView().findViewById(R.id.button13);
        final Button b2 = (Button) getView().findViewById(R.id.button14);
        Button b3 = (Button) getView().findViewById(R.id.button12);
        tv = (TextView) getView().findViewById(R.id.textView52);
        b2.setVisibility(View.INVISIBLE);

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et1.getText().toString().length() != 0 && et2.getText().toString().length() != 0) {
                    final ParseObject post = new ParseObject("Posts");
                    final ProgressDialog pd = new ProgressDialog(getActivity());
                    pd.setMessage("Creating A Post...");
                    if(s.getSelectedItemPosition()==1){
                        if(y!=0){
                            pd.show();
                            Calendar temp = Calendar.getInstance();
                            temp.set(y,m,d,h,min);
                            Date date = temp.getTime();
                            post.put("deadline",date);
                            post.put("category",s.getSelectedItem().toString());
                        } else Toast.makeText(getActivity(),"Set A Deadline",Toast.LENGTH_SHORT).show();
                    } else if(s.getSelectedItemPosition()==2){
                        if(material!=null){
                            pd.show();
                            final ParseFile file = new ParseFile(file_name+".txt",material);
                            file.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    post.put("material",file);
                                    post.put("category","Material Post");
                                }
                            });
                        } else Toast.makeText(getActivity(),"Add A Material To Post",Toast.LENGTH_SHORT).show();
                    }else if(s.getSelectedItemPosition()==0){
                        post.put("category",s.getSelectedItem().toString());
                    } else { post.put("category", s.getSelectedItem().toString());
                    }
                    post.put("author", ParseUser.getCurrentUser());
                    post.put("courseCode",getActivity().getIntent().getIntExtra("courseCode",0));
                    if(image!= null){
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] imageData= stream.toByteArray();
                        ParseFile file1 = new ParseFile("image.jpg",imageData);
                        post.put("image",file1);
                    }
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
                    query.orderByDescending("post_id");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            if (list.size() != 0) {
                                post.put("post_id", ((int) list.get(0).getInt("post_id")) + 1);
                            } else post.put("post_id", 1);
                            post.put("content", et2.getText().toString());
                            post.put("title", et1.getText().toString());
                            post.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    pd.dismiss();
                                    getActivity().finish();
                                }
                            });
                        }
                    });
                } else
                    Toast.makeText(getActivity(), "Fields Shouldn't Be Left Empty", Toast.LENGTH_SHORT).show();
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select A Image To Upload"), 1);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s.getSelectedItemPosition()==1){
                    DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                             y = year; m=monthOfYear; d=dayOfMonth;
                            TimePickerDialog dialog1 = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    h = hourOfDay; min = minute;
                                    tv.setText("Selected Deadline is "+m+"-"+d+"-"+y+" "+h+":"+min);
                                }
                            },Calendar.HOUR_OF_DAY,Calendar.MINUTE,true);
                            dialog1.setMessage("Pick Deadline Time");
                            dialog1.show();
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    dialog.setMessage("Pick Deadline Date");
                    dialog.show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    startActivityForResult(intent, 10);
                }
            }
        });
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:b2.setVisibility(View.INVISIBLE);
                           break;
                    case 1:b2.setVisibility(View.VISIBLE);
                           b2.setText("Add Submission Deadline");
                           break;
                    case 2:b2.setVisibility(View.VISIBLE);
                           b2.setText("Add Material File (only PDF)");
                           break;
                    case 3:b2.setVisibility(View.INVISIBLE);
                           break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
        if(requestCode == 10){
            if(resultCode == Activity.RESULT_OK){
                Uri fileUri = data.getData();
                Log.d("test",fileUri.getPath());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                File file = null;
                try {
                    file = new File(fileUri.getPath());
                    FileInputStream fis = new FileInputStream(file);
                    byte[] buffer = new byte[8192];
                    int n = fis.read(buffer);
                    while (n!=-1){
                        baos.write(buffer,0,n);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(file!=null)
                    file_name = file.getName();

                material = baos.toByteArray();
                tv.setText("File Fetch Successful");
                tv.setTextColor(Color.BLUE);
            }
        }
    }
}
