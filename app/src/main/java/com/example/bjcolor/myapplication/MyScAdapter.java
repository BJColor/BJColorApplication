package com.example.bjcolor.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by BJColor on 2017/9/21.
 */

public class MyScAdapter extends BaseAdapter {

    private final ArrayList<String> strings;
    private final Context context1;
    private final LayoutInflater from;
    private ViewHolder viewHolder;

    public MyScAdapter(Context context,ArrayList<String> arrayList) {
        strings = arrayList;
        context1 = context;
        from = LayoutInflater.from(context1);
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public Object getItem(int position) {
        return strings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = from.inflate(R.layout.itemsc, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv);

            convertView.setTag(viewHolder);
        }else{
             viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(strings.get(position));
        return convertView;
    }

    private class ViewHolder {
        TextView textView;
    }
}
