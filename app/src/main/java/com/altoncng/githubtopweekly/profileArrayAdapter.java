package com.altoncng.githubtopweekly;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class profileArrayAdapter extends ArrayAdapter<profile> {

    private static class ViewHolder {
        ImageView profileImageView;
        TextView nameTextView;
    }

    ArrayList<profile> profileData;

    public profileArrayAdapter(Context context, ArrayList<profile> profileData) {
        super(context, R.layout.profile_rows, profileData);
        this.profileData = profileData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        profile data = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.profile_rows, parent, false);

            viewHolder.profileImageView = (ImageView) convertView.findViewById(R.id.profileImageView);
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        new DownloadImageTask(viewHolder.profileImageView).execute(data.getPictureLink());
        viewHolder.nameTextView.setText(data.getName());
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public int getCount(){

        if(profileData != null) {
            return profileData.size();
        }else {
            return 0;
        }
    }

    //download profile images
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
