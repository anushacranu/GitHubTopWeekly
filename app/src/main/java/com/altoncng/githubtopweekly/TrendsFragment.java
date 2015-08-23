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

    private ArrayAdapter<String> listAdapter;
    private ListView listView;

    ArrayList<String> trendItemName;
    ArrayList<trendingItem> trendList;

    private OnMyItemClickListener listener;

    String range;

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
            try {//connect to github site to scrape trending weekly page
                Document doc = Jsoup.connect("https://github.com/trending?since=" + range).get();
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
                Toast.makeText(getActivity(), "IOException, connection failed", Toast.LENGTH_LONG).show();
            }return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            Log.w("profileList", "githubapp trendsFragment onPostExecute " + getActivity());
            if(getActivity() == null || getActivity().isFinishing()) {//wait a bit to let activity attach?
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }if(getActivity() != null && !getActivity().isFinishing())
                listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.rows, trendItemName);
            listView.setAdapter(listAdapter);

            //click a list item to fire intent and bring up profileList activity
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openProfile(trendList.get(position).getContributors());
                }
            });
        }
    }

    public void openProfile(ArrayList<profile> trendList){
        listener.onProjectSelected(trendList);
    }

}
