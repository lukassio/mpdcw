/**
* Luke Pringle
* S1624789
* MPD 2019-2020
**/

package mpdproject.gcu.me.org.mpdcwlukepringle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public Button cIbutton;
    public Button pRbutton;
    public Button RwButton;


    public static Boolean isCI;
    public static Boolean isPR;



    public static Boolean isRW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cIbutton = (Button) findViewById(R.id.cIButton);
        cIbutton.setOnClickListener(this);
        pRbutton = (Button) findViewById(R.id.pRButton);
        pRbutton.setOnClickListener(this);
        RwButton = (Button) findViewById(R.id.RwButton);
        RwButton.setOnClickListener(this);
    }

    public void onClick(View theView)
    {
        Intent theIntent;
        if (theView == cIbutton)
        {
            isCI = true;
            isPR = false;
            isRW = false;
            theIntent = new Intent(getApplicationContext() ,RSSData.class);
            startActivity(theIntent);

        }
        else if (theView == pRbutton)
        {
            isCI = false;
            isPR = true;
            isRW = false;
            theIntent = new Intent(getApplicationContext() ,RSSData.class);
            startActivity(theIntent);
        }else if (theView  == RwButton) {
            isCI = false;
            isPR = false;
            isRW = true;
            theIntent = new Intent(getApplicationContext() ,RSSData.class);
            startActivity(theIntent);
        }

    }

    public static Boolean getIsCI()
    {

        return isCI;
    }


    public static Boolean getIsPR()
    {

        return isPR;
    }

    public static Boolean getIsRW() {
        return isRW;
    }

}