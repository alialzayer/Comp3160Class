package com.comp3160.tru000005.comp3160class;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by alial on 2016-01-27.
 */
public class CalendarActivity extends Activity{

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        context=this;
    }

    public void onClick_AddEvent1(View view){

            int PrimarycalID = AccessCalendar.displayAllCalendars(context);
            Log.d("MAIN_LOG", " -> PrimarycalID = " + PrimarycalID);
    }

    public void addEvent(View view) {
        // Construct event details
        long DTSTART = 0;
        long DTEND = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2016, 0, 28, 10, 30);
        DTSTART = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2016, 0, 28, 11, 20);
        DTEND = endTime.getTimeInMillis();
        TimeZone timeZone = TimeZone.getDefault();
        String EVENT_TIMEZONE = timeZone.getID();
        String TITLE = "COMP 3160";
        String DESCRIPTION = "How to use Content Providers to access Calendars in Android";
        String EVENT_LOCATION = "OM 1360";
        int HAS_ALARM = 1;
        int CALENDAR_ID = 1;
        int MINUTES = 5;
        int EVENT_ID = AccessCalendar.addEventtoCalendar(context, DTSTART, DTEND, EVENT_TIMEZONE, TITLE, DESCRIPTION, EVENT_LOCATION, HAS_ALARM, CALENDAR_ID);
        int AlertID = AccessCalendar.addAlarmtoEvent(context,EVENT_ID, MINUTES);
    }
}
