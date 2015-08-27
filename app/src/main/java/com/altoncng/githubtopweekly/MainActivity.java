package com.altoncng.githubtopweekly;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.TableLayout;
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
    View fragmentFrameProfileLayout;

    String range;
    String language;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentFrameLayout = (ViewGroup) findViewById(R.id.frame1);

        Intent intent = getIntent();
        range = intent.getStringExtra("range");
        language = intent.getStringExtra("language");

        TrendsFragment trendsFrag = new TrendsFragment();

        Bundle bundle = new Bundle();
        bundle.putString("range", range);
        bundle.putString("language", language);
        trendsFrag.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
            //Log.w("onProjectSelected", "githubapp onCreate not null, " + TrendsFragment.class.getName());
            fragmentTransaction.replace(fragmentFrameLayout.getId(), trendsFrag, TrendsFragment.class.getName());
        }else{
            //Log.w("onProjectSelected", "githubapp onCreate else, " + TrendsFragment.class.getName() + " " + profileList.class.getName());
            fragmentFrameProfileLayout = (ViewGroup) findViewById(R.id.frame2);
            profileList profileFragment = new profileList();

            fragmentFrameLayout.setLayoutParams(new TableLayout.LayoutParams(
                    0, TableLayout.LayoutParams.WRAP_CONTENT, 1));
            fragmentFrameProfileLayout.setLayoutParams(new TableLayout.LayoutParams(
                    0, TableLayout.LayoutParams.WRAP_CONTENT, 1));

            fragmentTransaction.replace(fragmentFrameLayout.getId(), trendsFrag, TrendsFragment.class.getName());
            fragmentTransaction.replace(fragmentFrameProfileLayout.getId(), profileFragment, profileList.class.getName());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onProjectSelected(ArrayList<profile> trendList) {

        profileList profileFragment = (profileList) getFragmentManager()
                .findFragmentByTag(profileList.class.getName());

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.w("onProjectSelected", "githubapp onProjectSelected not null");
            profileFragment.getProjectData(trendList);
        }else {
            Log.w("onProjectSelected", "githubapp onProjectSelected is null");
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            profileFragment = profileList.newInstance(trendList);
            fragmentTransaction.replace(R.id.frame1, profileFragment);
            fragmentTransaction.addToBackStack(TrendsFragment.class.getName());
            fragmentTransaction.commit();
        }
    }
}
