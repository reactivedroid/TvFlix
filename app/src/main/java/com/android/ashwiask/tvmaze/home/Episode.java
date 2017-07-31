package com.android.ashwiask.tvmaze.home;

/**
 * @author Ashwini Kumar.
 */

public class Episode
{
    private Show show;
    private long id;
    private String url;
    private String name;
    private int season;
    private int number;
    private String airdate;
    private String airtime;
    private int runtime;
    private String summary;

    public Show getShow()
    {
        return show;
    }

    public long getId()
    {
        return id;
    }

    public String getUrl()
    {
        return url;
    }

    public String getName()
    {
        return name;
    }

    public int getSeason()
    {
        return season;
    }

    public int getNumber()
    {
        return number;
    }

    public String getAirdate()
    {
        return airdate;
    }

    public String getAirtime()
    {
        return airtime;
    }

    public int getRuntime()
    {
        return runtime;
    }

    public String getSummary()
    {
        return summary;
    }
}
