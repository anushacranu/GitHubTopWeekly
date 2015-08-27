package com.altoncng.githubtopweekly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Eye on 8/26/2015.
 */
public class trendsArrayAdapter extends ArrayAdapter<trendingItem> {

    private static class ViewHolder {
        TextView trendName;
        TextView trendDetails;
    }

    ArrayList<trendingItem> trendsData;

    public trendsArrayAdapter(Context context, ArrayList<trendingItem> trendsData) {
        super(context, R.layout.rows, trendsData);
        this.trendsData = trendsData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        trendingItem data = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rows, parent, false);

            viewHolder.trendName = (TextView) convertView.findViewById(R.id.trendName);
            viewHolder.trendDetails = (TextView) convertView.findViewById(R.id.trendDetails);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.trendName.setText(data.getName());
        viewHolder.trendDetails.setText(data.getDetails());
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public int getCount(){

        if(trendsData != null) {
            return trendsData.size();
        }else {
            return 0;
        }
    }
}