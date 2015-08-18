package com.altoncng.githubtopweekly;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*
Project "Github API":
Using the Github Rest API (https://developer.github.com/v3/), develop an Android
application displaying a list of the trending Github repositories for the last week.

When one of the repository is selected in the list, display its top contributors
with names, pictures and profile links

Keywords: Rest API, RecyclerView, Material Design
Minimum supported API level: 14
*/

public class MainActivity extends Activity implements TrendsFragment.OnMyItemClickListener{

    private ArrayAdapter<String> listAdapter;
    private ListView listView;

    ArrayList<String> trendItemName;
    ArrayList<trendingItem> trendList;

    View fragmentFrameLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentFrameLayout = (ViewGroup) findViewById(R.id.frame1);
        if (fragmentFrameLayout != null) {

            // Add image selector fragment to the activity's container layout
            TrendsFragment trendsFrag = new TrendsFragment();
            FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.replace(fragmentFrameLayout.getId(), trendsFrag,
                    TrendsFragment.class.getName());

            // Commit the transaction
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onProjectSelected(ArrayList<profile> trendList) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        profileList fragment = profileList.newInstance(trendList);
        fragmentTransaction.replace(R.id.frame1, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
