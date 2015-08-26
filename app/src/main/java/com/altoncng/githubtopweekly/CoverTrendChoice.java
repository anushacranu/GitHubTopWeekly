package com.altoncng.githubtopweekly;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class CoverTrendChoice extends Activity {

    private Spinner trendsRangeSpinner;
    private Spinner languagesSpinner;
    private Button confirmButton;

    private String chosenTrendsRange = "weekly";

    ArrayList<String> languages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover_trend_choice);

        trendsRangeSpinner = (Spinner) findViewById(R.id.trendsRangeSpinner);
        trendsRangeSpinner.setSelection(1);
        trendsRangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 0)
                    chosenTrendsRange = "daily";
                else if (pos == 1)
                    chosenTrendsRange = "weekly";
                else
                    chosenTrendsRange = "monthly";
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        confirmButton = (Button) findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoverTrendChoice.this, MainActivity.class);
                intent.putExtra("range", chosenTrendsRange);
                intent.putExtra("language", languagesSpinner.getSelectedItem().toString());
                startActivity(intent);
            }
        });

        languages = new ArrayList<String>();
        languagesSpinner = (Spinner) findViewById(R.id.languagesSpinner);
    }

    @Override
    protected void onResume(){
        super.onResume();
        new scrapeLanguages().execute();
    }

    public class scrapeLanguages extends AsyncTask<Void,Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            //originally pull from website, but since it takes a while, get from strings array instead in the xml
            /*try {
                Document doc = Jsoup.connect("https://github.com/trending").get();
                Elements languageBase = doc.getElementsByClass("select-menu-item");

                for(Element el: languageBase){
                    String languageName = el.getElementsByClass("select-menu-item-text").text();
                    languages.add(languageName);
                }

            }catch (IOException ex){
                ;
            }*/
            /*languages.set(0, "All languages");
            languages.set(1, "unknown");
            languages.remove(2);*/

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            setLanguageAdapter();
        }
    }

    protected void setLanguageAdapter(){
        //ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //languagesSpinner.setAdapter(dataAdapter);
        languagesSpinner.setSelection(0);
    }

}
