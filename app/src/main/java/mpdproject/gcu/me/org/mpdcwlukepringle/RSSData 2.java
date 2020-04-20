/**
* Luke Pringle
* S1624789
* MPD 2019-2020
**/

package mpdproject.gcu.me.org.mpdcwlukepringle;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RSSData extends AppCompatActivity
{
    private String url1="https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String url2="https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String url3="https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private TextView urlInput;
    private String result = "";
    private ListView listView;

    ProgressBar progressBar;
    Toolbar toolbar;
    DatePickerDialog.OnDateSetListener dateSetListener;

    List<CurrentIncidents> curIncItems;
    List<CurrentRoadworks> currentRoadworksList;
    ListViewAdapter adapter;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_data);
        curIncItems = new ArrayList<>();
        listView = (ListView)findViewById(R.id.list);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        if (MainActivity.getIsCI() == true)
        {
            getSupportActionBar().setTitle("Current Incidents");
            startProgress(url1);
        }
        else if (MainActivity.getIsPR() == true)
        {
            getSupportActionBar().setTitle("Planned Roadworks");
            startProgress(url3);
        }        else if (MainActivity.getIsRW() == true)
        {
            getSupportActionBar().setTitle("Current Roadworks");
            startProgress(url2);
        }

        dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void  onDateSet(DatePicker picker, int year, int month, int day)
            {
                month = month + 1;

                String dateString = Integer.toString(day)
                        + Integer.toString(month) + Integer.toString(year);

                SimpleDateFormat df = new SimpleDateFormat("ddMyyyy");
                SimpleDateFormat df2 = new SimpleDateFormat("dd MMMM yyyy");

                try {
                    Date mySearchDate = df.parse(dateString);
                    String theSearchDate = df2.format(mySearchDate);

                    List<CurrentIncidents> newList = new ArrayList<CurrentIncidents>();
                    for (CurrentIncidents item: curIncItems)
                    {
                        if (item.startDate.equals(theSearchDate))
                        {
                            newList.add(item);
                        }
                    }

                    adapter = new ListViewAdapter(getApplicationContext(),R.layout.list_view, newList);
                    listView.setAdapter(adapter);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                CurrentIncidents item = (CurrentIncidents)adapterView.getItemAtPosition(position);

                Intent intent = new Intent(getApplicationContext(), RSSDetailedInformation.class);
                intent.putExtra("CurrentIncidents", item);

                startActivity(intent);

            }
        });

    } // End of onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.viewer_menu, menu);

        final SearchView searchView = (SearchView)MenuItemCompat.getActionView(menu.findItem(R.id.searchIcon));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.calendarIcon)
        {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dDialog = new DatePickerDialog(
                    this,
                    R.style.Theme_AppCompat_Light_Dialog_MinWidth,
                    dateSetListener,
                    year,month,day);

            dDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

            dDialog.show();
        }
        else if (id == R.id.searchIcon)
        {

        }
        return true;
    }

    public void startProgress(String theUrl)
    {
        // Run network access on a separate thread;
        Task newTask = new Task(theUrl);
        newTask.execute();
        //new Thread(new Task(theUrl)).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    class Task extends AsyncTask <Void, Void, List<CurrentIncidents>>
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }

        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected List<CurrentIncidents> doInBackground(Void... aVoid)
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            CurrentIncidents curIncidentsItem = null;

            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                    Log.e("MyTag",inputLine);

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            }

            if (result != null)
            {
                try
                {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);

                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(new StringReader(result));

                    //parser.setInput(new StringReader(result));

                    int event = parser.getEventType();


                    while (event != XmlPullParser.END_DOCUMENT )
                    {
                        //String tag_name = parser.getName();
                        switch (event)
                        {
                            case XmlPullParser.START_DOCUMENT:
                                break;
                            case XmlPullParser.START_TAG:
                                if (parser.getName().equalsIgnoreCase("item"))
                                {
                                    curIncidentsItem = new CurrentIncidents("", "", "", "", "", "");
                                }
                                else if (curIncidentsItem != null)
                                {
                                    if (parser.getName().equalsIgnoreCase("title"))
                                    {
                                        curIncidentsItem.setTitle(parser.nextText().trim());
                                    }
                                    else if (parser.getName().equalsIgnoreCase("description"))
                                    {
                                        curIncidentsItem.setDescription(parser.nextText().trim());

                                        if (MainActivity.getIsPR() == true)
                                        {
                                            SimpleDateFormat df =  new SimpleDateFormat("dd MMMM yyyy");

                                            String[] splitWorks = curIncidentsItem.getDescription().split(":");
                                            String replaceWorks = splitWorks[6].replaceAll("Traffic Management", "").trim();
                                            curIncidentsItem.setWorks(replaceWorks);

                                            String replaceTrafficInfo = splitWorks[7].replaceAll(
                                                    "Diversion Information", " ");
                                            curIncidentsItem.setTrafficManagement(replaceTrafficInfo);

                                            String [] splitStartDate = splitWorks[2].split(", ");
                                            String myStartDate = splitStartDate[1].replaceAll("- 00", "").trim();
                                            Date theStartDate = df.parse(myStartDate);
                                            curIncidentsItem.setStartDate = theStartDate;

                                            String[] splitEndDate = splitWorks[4].split(", ");
                                            String myEndDate = splitEndDate[1].replaceAll("- 00", "").trim();
                                            Date theEndDate = df.parse(myEndDate);
                                            curIncidentsItem.setEndDate = theEndDate;

                                           curIncidentsItem.startDate = df.format(theStartDate);
                                            curIncidentsItem.endDate = df.format(theStartDate);


                                            if (curIncidentsItem.getDescription().contains("Diversion Information"))
                                            {
                                                String splitDiversionInfo = curIncidentsItem.getDescription().
                                                        substring(curIncidentsItem.getDescription().
                                                                indexOf("Diversion Information:"));
                                                splitDiversionInfo = splitDiversionInfo.replaceAll("Diversion Information:", "");
                                                curIncidentsItem.setDiversionInfo(splitDiversionInfo);
                                            }
                                        }

                                        if (MainActivity.getIsRW() == true) {
                                            SimpleDateFormat df =  new SimpleDateFormat("dd MMMM yyyy");
                                            int i;
                                            String[] splitWorks = curIncidentsItem.getDescription().split(":");

//                                            String replaceWorks = splitWorks[6].replaceAll("Traffic Management", "").trim();
//                                            curIncidentsItem.setWorks(replaceWorks);
//
//                                            String replaceTrafficInfo = splitWorks[7].replaceAll(
//                                                    "Diversion Information", " ");
//                                            curIncidentsItem.setTrafficManagement(replaceTrafficInfo);

                                            String [] splitStartDate = splitWorks[2].split(", ");
                                            String myStartDate = splitStartDate[1].replaceAll("- 00", "").trim();
                                            Date theStartDate = df.parse(myStartDate);
                                            curIncidentsItem.setStartDate = theStartDate;

                                            String[] splitEndDate = splitWorks[4].split(", ");
                                            String myEndDate = splitEndDate[1].replaceAll("- 00", "").trim();
                                            Date theEndDate = df.parse(myEndDate);
                                            curIncidentsItem.setEndDate = theEndDate;

                                            curIncidentsItem.startDate = df.format(theStartDate);
                                            curIncidentsItem.endDate = df.format(theStartDate);


                                            if (curIncidentsItem.getDescription().contains("Diversion Information"))
                                            {
                                                String splitDiversionInfo = curIncidentsItem.getDescription().
                                                        substring(curIncidentsItem.getDescription().
                                                                indexOf("Diversion Information:"));
                                                splitDiversionInfo = splitDiversionInfo.replaceAll("Diversion Information:", "");
                                                curIncidentsItem.setDiversionInfo(splitDiversionInfo);
                                            }


                                        }
                                        else
                                        {

                                        }
                                    }
                                    else if (parser.getName().equalsIgnoreCase("link"))
                                    {
                                        curIncidentsItem.setLink(parser.nextText().trim());
                                    }
                                    else if (parser.getName().equalsIgnoreCase("pubDate"))
                                    {
                                        curIncidentsItem.setDate(parser.nextText().trim());
                                    }
                                    else if (parser.getName().equalsIgnoreCase("point"))
                                    {
                                        String latLonString = parser.nextText().trim();
                                        String lonString  = latLonString.substring(latLonString.indexOf("-"));
                                        String latString = latLonString.replaceAll(lonString, "");

                                        curIncidentsItem.setLat(latString);
                                        curIncidentsItem.setLon(lonString);
                                    }
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                if (parser.getName().equalsIgnoreCase("item") && curIncidentsItem != null)
                                {
                                    curIncItems.add(curIncidentsItem);
                                }
                                break;

                        }

                        event = parser.next();
                    }

                }
                catch (XmlPullParserException e)
                {
                    e.printStackTrace();
                }

                catch (IOException e)
                {
                    e.printStackTrace();
                }

                catch (ParseException e)
                {
                    e.printStackTrace();
                }

            }

            return curIncItems;

        }


        @Override
        protected void onPostExecute (List<CurrentIncidents> curInc)
        {
            adapter = new ListViewAdapter(getApplicationContext(), R.layout.list_view, curInc);
            listView.setAdapter(adapter);
        }

    }

}