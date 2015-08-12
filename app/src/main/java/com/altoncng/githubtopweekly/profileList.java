package com.altoncng.githubtopweekly;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class profileList extends AppCompatActivity {

    private ListView profileListView;
    private profileArrayAdapter profileAdapter;

    ArrayList<profile> profList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);

        //get parcelable arraylist data
        profList = getIntent().getParcelableArrayListExtra("profileAry");

        profileListView = (ListView) findViewById(R.id.gitHubTrendsListView);

        fillView();

        //onclick, open profile page
        profileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(id != -1)
                    openLink(profList.get((int)id).profileLink);
            }
        });
        new fetcher().execute();
    }

    public void fillView(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View labelRow = inflater.inflate(R.layout.profile_rows, null);
        profileListView.addHeaderView(labelRow);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public class fetcher extends AsyncTask<Void,Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            profileAdapter = new profileArrayAdapter(profileList.this, profList);
            profileListView.setAdapter(profileAdapter);
        }
    }

    public void openLink(String link){
        Uri uri = Uri.parse(link); // missing 'http://' will cause crash
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
