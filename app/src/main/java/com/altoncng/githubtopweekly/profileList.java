package com.altoncng.githubtopweekly;

import android.app.Fragment;
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
import android.view.ViewGroup;
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

public class profileList extends Fragment {

    private ListView profileListView;
    private profileArrayAdapter profileAdapter;

    ArrayList<profile> profList;

    public static profileList newInstance(ArrayList<profile> profileList) {
        profileList fragment = new profileList();
        Bundle args = new Bundle();
        args.putParcelableArrayList("profileAry", profileList);
        fragment.setArguments(args);
        return fragment;
    }

    public profileList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profList = getArguments().getParcelableArrayList("profileAry");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_list, container, false);
        profileListView = (ListView) view.findViewById(R.id.gitHubTrendsListView);

        fillView();

        profileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id != -1)
                    openLink(profList.get((int) id).profileLink);
            }
        });

        return view;
    }

    public void fillView(){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View labelRow = inflater.inflate(R.layout.profile_rows, null);
        profileListView.addHeaderView(labelRow);
    }

    @Override
    public void onResume(){
        super.onResume();

        new fetcher().execute();
    }

    public class fetcher extends AsyncTask<Void,Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            profileAdapter = new profileArrayAdapter(getActivity().getApplicationContext(), profList);
            profileListView.setAdapter(profileAdapter);
        }
    }

    public void openLink(String link){
        Uri uri = Uri.parse(link); // missing 'http://' will cause crash
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
