package com.altoncng.githubtopweekly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

public class CoverTrendChoice extends Activity {

    private Spinner trendsRangeSpinner;
    private Button confirmButton;

    private String chosenTrendsRange = "weekly";

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
                else if(pos == 1)
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
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
    }
}
