package com.help.sd.uni_con;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class postAdapter extends ArrayAdapter<ParseObject> {

    Context context; int resource; List<ParseObject> objects;

    public postAdapter(Context context, int resource, List<ParseObject> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource,parent,false);
        }
        TextView tv1 = (TextView) convertView.findViewById(R.id.textView32);
        TextView tv2 = (TextView) convertView.findViewById(R.id.textView33);
        TextView tv3 = (TextView) convertView.findViewById(R.id.textView34);
        TextView tv4 = (TextView) convertView.findViewById(R.id.textView35);
        TextView tv5 = (TextView) convertView.findViewById(R.id.textView36);
        TextView tv6 = (TextView) convertView.findViewById(R.id.textView37);
        ImageView iv = (ImageView) convertView.findViewById(R.id.imageView8);
        tv4.setMaxLines(2);
        ParseObject post = objects.get(position);
        String category = post.getString("category");
        tv1.setText(category);
        tv2.setText("Post ID : "+post.getInt("post_id"));
        tv3.setText(post.getString("title"));
        tv4.setText("Description : "+ post.getString("content"));
        tv5.setText("Created By "+post.getParseObject("author").getString("name"));
        tv6.setText(post.getCreatedAt().toString());
        switch(category){
            case "Announcement" : tv1.setTextColor(Color.BLUE);
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.announcer5));
                break;
            case "Submission Request" :tv1.setTextColor(Color.RED);
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.directory1));
                Calendar date = Calendar.getInstance();
                date.setTime(post.getDate("deadline"));
                tv4.setText("Deadline is "+date.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.ENGLISH)+" "+date.get(Calendar.DAY_OF_MONTH));
                break;
            case "Material Post" : tv1.setTextColor(Color.GREEN);
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.books30));
                break;
            case "Forum Thread" : tv1.setTextColor(Color.YELLOW);
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.messages11));
                break;
        }
        return convertView;
    }
}
