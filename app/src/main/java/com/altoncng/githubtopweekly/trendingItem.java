package com.altoncng.githubtopweekly;

import java.util.ArrayList;

/**
 * Created by Eye on 8/11/2015.
 */
public class trendingItem {

    private String name;
    private String details;
    private ArrayList<profile> contributors;

    public trendingItem(){}

    public trendingItem(String name, String details, ArrayList<profile> contributor){
        this.name = name;
        this.details = details;
        this.contributors = new ArrayList<profile>();
        for(profile p: contributor)
            contributors.add(new profile(p.getName(), p.getProfileLink(), p.getPictureLink()));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<profile> getContributors() {
        return contributors;
    }

    public void setContributors(ArrayList<profile> contributors) {
        this.contributors = contributors;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
