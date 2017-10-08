package com.help.sd.uni_con;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by SaideepReddy on 11/14/2015.
 */
public class commentsAdapter extends ArrayAdapter<ParseObject> {
    Context context; int resource; List<ParseObject> objects;

    public commentsAdapter(Context context, int resource, List<ParseObject> objects) {
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
        TextView tv1 = (TextView) convertView.findViewById(R.id.textView63);
        TextView tv2 = (TextView) convertView.findViewById(R.id.textView64);
        TextView tv3 = (TextView) convertView.findViewById(R.id.textView65);
        ParseObject po = objects.get(position);
        tv1.setText(po.getString("author")+" : ");
        tv2.setText(po.getString("comment"));
        Calendar date = Calendar.getInstance();
        date.setTime(po.getCreatedAt());
        tv3.setText(date.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH)+date.get(Calendar.DAY_OF_MONTH)+" "+
        date.get(Calendar.HOUR_OF_DAY)+":"+date.get(Calendar.MINUTE));
        switch(position%4){
            case 0:tv1.setTextColor(Color.parseColor("#FFE0981A"));
                break;
            case 1:tv1.setTextColor(Color.parseColor("#FF1EE2D5"));
                break;
            case 2:tv1.setTextColor(Color.parseColor("#FF9729E0"));
                break;
            case 3:tv1.setTextColor(Color.parseColor("#FFE024D0"));
                break;
        }
        return convertView;
    }
}
