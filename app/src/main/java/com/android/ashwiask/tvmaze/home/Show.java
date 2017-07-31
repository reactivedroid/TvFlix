package com.android.ashwiask.tvmaze.home;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * @author Ashwini Kumar.
 */

public class Show
{
    private long id;
    private String url;
    private String name;
    private String type;
    private String language;
    private List<String> genres;
    private String status;
    private int runtime;
    private String premiered;
    private String officialSite;
    @SerializedName("network")
    private AirChannel airChannel;
    private Map<String, String> image;
    @SerializedName("externals")
    private ExternalInfo externalInfo;
    private String summary;
    private Map<String, Double> rating;

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

    public String getType()
    {
        return type;
    }

    public String getLanguage()
    {
        return language;
    }

    public List<String> getGenres()
    {
        return genres;
    }

    public String getStatus()
    {
        return status;
    }

    public int getRuntime()
    {
        return runtime;
    }

    public String getPremiered()
    {
        return premiered;
    }

    public String getOfficialSite()
    {
        return officialSite;
    }

    public AirChannel getAirChannel()
    {
        return airChannel;
    }

    public Map<String, String> getImage()
    {
        return image;
    }

    public ExternalInfo getExternalInfo()
    {
        return externalInfo;
    }

    public String getSummary()
    {
        return summary;
    }

    public Map<String, Double> getRating()
    {
        return rating;
    }
}
