/**
* Luke Pringle
* S1624789
* MPD 2019-2020
**/

package mpdproject.gcu.me.org.mpdcwlukepringle;

import java.io.Serializable;
import java.util.Date;

public class CurrentIncidents implements Serializable
{
    private String title;
    private String description;
    private String link;
    private String pubDate;
    private String lat;
    private String lon;

    private String works;
    private String trafficManagement;
    private String diversionInfo;

    public Date setStartDate;
    public Date setEndDate;
    public String startDate;
    public String endDate;

    public CurrentIncidents(String title, String description, String link, String pubDate, String lat, String lon )
    {
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
        this.lat = lat;
        this.lon = lon;
    }

    public void setTitle (String theTitle)
    {
        title = theTitle;
    }

    public String getTitle()
    {
        return title;
    }

    public void setDescription (String theDescription)
    {
        description = theDescription;
    }

    public String getDescription()
    {
        return "Incident Information: " + description;
    }

    public void setLink (String theLink)
    {
        link = theLink;
    }

    public String getLink()
    {
        return "For more information: " + link;
    }

    public void setDate (String theDate)
    {
        pubDate = theDate;
    }

    public String getDate()
    {
        return "Published Date: " + pubDate;
    }

    public void setLat(String lat)
    {
        this.lat = lat;
    }

    public String getLat()
    {
       return lat;
    }

    public void setLon(String lon)
    {
        this.lon = lon;
    }

    public String getLon()
    {
        return lat;
    }

    public String getLatLon()
    {
        return "Lat is: " + lat + "" + "" + "" + "" + "Lon is: " + lon;
    }

    public void setWorks(String theWorks)
    {
        works = theWorks;
    }

    public String getWorks()
    {
        return works;
    }

    public void setTrafficManagement(String theTrafficManagement)
    {
        trafficManagement = theTrafficManagement;
    }

    public String getTrafficManagement()
    {
        return trafficManagement;
    }

    public void setDiversionInfo(String theDiversionInfo)
    {
        diversionInfo = theDiversionInfo;
    }

    public String getDiversionInfo()
    {
        return diversionInfo;
    }

    public void setStartDate(Date setDate)
    {
        setStartDate = setDate;
    }

    public Date getStartDate()
    {
        return setStartDate;
    }

    public void setEndDate(Date setDate)
    {
        setEndDate = setDate;
    }

    public Date getEndDate()
    {
        return setEndDate;
    }

    public int calculateDays(Date date1, Date date2)
    {
        if (date1 != null && date2 != null)
        {
            int days = ((int)((date2.getTime()/(24*60*60*1000))
                    -(int)(date1.getTime()/(24*60*60*1000))));
            return days;
        }
        else
        {
            return  0;
        }
    }

    public int getDurationInDays()
    {
        int days = calculateDays(setStartDate, setEndDate);
        return days;
    }

    public String getWorksAndTrafficDescription()
    {
        return "Current Works: " + works + "\n" + "\n" +
                "Traffic Management: " + trafficManagement + "\n";
    }

    public String getRoadworksDescription()
    {
        return "Current Works: " + works + "\n" + "\n" +
            "Traffic Management: " + trafficManagement + "\n" + "\n" +
                "Diversion Info: " + diversionInfo;
    }

    public String getStartEndDate()
    {
        return ("Start date: " + startDate + " " + " " + " " + "End date: " + endDate
                + "\n " + "\n" + "Estimated time of works: " + Integer.toString(getDurationInDays()) + " days");
    }

    public String returnCurrentIncidentsDescription()
    {
        return title;

    }
}
