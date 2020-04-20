/**
* Luke Pringle
* S1624789
* MPD 2019-2020
**/

package mpdproject.gcu.me.org.mpdcwlukepringle;

import android.os.Bundle;
import android.widget.TextView;


public class RSSDetailedInformation extends RSSData
{
    CurrentIncidents theItem;
    TextView descriptionText;
    TextView linkText;
    TextView pubDateText;
    TextView startEndDateText;
    TextView latLonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_list_view);

        theItem = (CurrentIncidents) getIntent().getSerializableExtra("CurrentIncidents");

        getSupportActionBar().setTitle(theItem.getTitle());

        descriptionText = (TextView)findViewById(R.id.descriptionText);
        linkText = (TextView)findViewById(R.id.linkText);
        pubDateText = (TextView)findViewById(R.id.pubDateText);
        latLonText = (TextView)findViewById(R.id.latLonText);

        startEndDateText = (TextView)findViewById(R.id.startEndDateText);


        if (theItem.getWorks() != null && theItem.getTrafficManagement() != null)
        {
            descriptionText.setText(theItem.getWorksAndTrafficDescription());

            if (theItem.getDiversionInfo() != null)
            {
                descriptionText.setText(theItem.getRoadworksDescription());
            }

            startEndDateText.setText(theItem.getStartEndDate());

        }
        else
        {
            descriptionText.setText(theItem.getDescription());
        }
        linkText.setText(theItem.getLink());
        pubDateText.setText(theItem.getDate());
        latLonText.setText(theItem.getLatLon());




    }
}
