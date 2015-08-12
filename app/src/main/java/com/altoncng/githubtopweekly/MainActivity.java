package com.altoncng.githubtopweekly;

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

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<String> listAdapter;
    private ListView listView;

    ArrayList<String> trendItemName;
    ArrayList<trendingItem> trendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById( R.id.gitHubTrendsListView );

        //OAuth2 token authentication
        /*GitHubClient client = new GitHubClient();
        client.setOAuth2Token("SlAV32hkKG");*/

        //github api not actually useful here, as it doesn't have any trends data available.
    }

    @Override
    protected void onResume(){
        super.onResume();

        new fetcher().execute();
    }

    public class fetcher extends AsyncTask<Void,Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {//connect to github site to scrape trending weekly page
                Document doc = Jsoup.connect("https://github.com/trending?since=weekly").get();
                Elements trendingElements = doc.getElementsByClass("repo-list-item");//repo-list-item is the trending list

                trendList = new ArrayList<trendingItem>();
                trendItemName = new ArrayList<String>();

                for(Element el: trendingElements){
                    String projectName = el.getElementsByClass("repo-list-name").text();
                    String projectDetails = el.getElementsByClass("repo-list-meta").text();

                    Elements topContributors = el.select("img[src]");

                    ArrayList<profile> profileList = new ArrayList<profile>();
                    for(Element pro: topContributors){
                        profile tempProfile = new profile(pro.attr("title"), "https://github.com/" + pro.attr("title"), pro.attr("src"));
                        profileList.add(tempProfile);
                    }

                    trendingItem tempTrendingItem = new trendingItem(projectName, projectDetails, profileList);
                    trendItemName.add(projectName);
                    trendList.add(tempTrendingItem);//list of trending items and relevant data
                }

            }catch (IOException ex){
                Toast.makeText(getApplicationContext(), "IOException, connection failed", Toast.LENGTH_LONG).show();
            }return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            listAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.rows, trendItemName);
            listView.setAdapter(listAdapter);

            //click a list item to fire intent and bring up profileList activity
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, profileList.class);
                    intent.putParcelableArrayListExtra("profileAry", trendList.get(position).getContributors());
                    startActivity(intent);
                }
            });
        }
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
