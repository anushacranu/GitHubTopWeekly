package com.altoncng.githubtopweekly;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class TrendsFragment extends Fragment{

    private trendsArrayAdapter listAdapter;
    private ListView listView;

    ArrayList<String> trendItemName;
    ArrayList<trendingItem> trendList;

    private OnMyItemClickListener listener;

    String range;
    String language;

    public static TrendsFragment newInstance() {
        TrendsFragment fragment = new TrendsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public TrendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // handle intent extras
        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null)
        {
            range = extras.getString("range");
            language = "l=" + extras.getString("language") + "&";
            if(language.equals("l=All languages&"))
                language = "";
            language = language.replaceAll(" ", "-");

            Toast.makeText(getActivity(),"Tap for list of contributors or tap and hold for project page",Toast.LENGTH_LONG).show();
            Log.w("githubapp", "githubapp trendsfragment onCreate " + language);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_1, container, false);
        listView = (ListView) view.findViewById(R.id.gitHubTrendsListView);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new fetcher().execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof TrendsFragment.OnMyItemClickListener) {
            listener = (TrendsFragment.OnMyItemClickListener) activity;
        }else{
            throw new ClassCastException(activity.toString() + " must implement TrendsFragment.OnItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnMyItemClickListener
    {
        public void onProjectSelected(ArrayList<profile> trendList);
    }

    public class fetcher extends AsyncTask<Void,Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            int count = 0;
            int maxTries = 3;
            while(true) {
                try {//connect to github site to scrape trending weekly page
                    Log.w("githubapp", "githubapp trendsFragment fetcher " + language);
                    Document doc = Jsoup.connect("https://github.com/trending?" + language + "since=" + range).get();
                    Elements trendingElements = doc.getElementsByClass("repo-list-item");//repo-list-item is the trending list

                    trendList = new ArrayList<trendingItem>();
                    trendItemName = new ArrayList<String>();

                    for(Element el: trendingElements){
                        //getElementsByClass("classname") gets a list of elements with that class within Element el
                        //.text() gets all children's text, while select gets the elements with the selection (a[href] here
                        //a being the element and href being the attribute within a, denoted by []. .attr() gets the value of that attribute
                        String projectName = el.getElementsByClass("repo-list-name").text();
                        String projectLink = el.getElementsByClass("repo-list-name").select("a[href]").attr("href");
                        String projectDetails = el.getElementsByClass("repo-list-meta").text();
                        projectDetails = projectDetails.replace("Built by", "");

                        Elements topContributors = el.select("img[src]");

                        ArrayList<profile> profileList = new ArrayList<profile>();
                        for(Element pro: topContributors){
                            profile tempProfile = new profile(pro.attr("title"), "https://github.com/" + pro.attr("title"), pro.attr("src"));
                            profileList.add(tempProfile);
                        }

                        trendingItem tempTrendingItem = new trendingItem(projectName, projectDetails, projectLink, profileList);
                        trendItemName.add(projectName);
                        trendList.add(tempTrendingItem);//list of trending items and relevant data
                    }break;

                }catch (IOException ex){
                    if (++count == maxTries)
                        ;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            //Log.w("profileList", "githubapp trendsFragment onPostExecute " + getActivity());
            if(getActivity() == null || getActivity().isFinishing()) {//wait a bit to let activity attach?
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }if(getActivity() != null && !getActivity().isFinishing())
                listAdapter = new trendsArrayAdapter(getActivity().getApplicationContext(), trendList);
            listView.setAdapter(listAdapter);

            //click a list item to fire intent and bring up profileList activity
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openProfile(trendList.get(position).getContributors());
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {
                    Log.v("long clicked", "pos: " + pos);
                    openLink(trendList.get(pos).getLink());
                    return true;
                }
            });
        }
    }

    public void openProfile(ArrayList<profile> trendList){
        listener.onProjectSelected(trendList);
    }

    public void openLink(String link){
        Uri uri = Uri.parse("https://github.com" + link); // missing 'http://' will cause crash
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}
