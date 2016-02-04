package com.comp3160.tru000005.comp3160class;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

/**
 * Created by alial on 2016-01-27.
 */
public class AccessCalendar {
    static int _ID        = 0;
    static int IS_PRIMARY = 7;

    public static int displayAllCalendars(Context context) {

        Uri calendarUri = CalendarContract.Calendars.CONTENT_URI;
        String[] projection = new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.Calendars.OWNER_ACCOUNT,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                CalendarContract.Calendars.NAME,
                CalendarContract.Calendars.CALENDAR_COLOR,
                CalendarContract.Calendars.IS_PRIMARY,
                CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL
                // Haytham: check ACCESS_LEVEL codes @ http://developer.android.com/reference/android/provider/CalendarContract.CalendarColumns.html#CALENDAR_ACCESS_LEVEL
        };

        Cursor Calcursor = context.getContentResolver().query(calendarUri, projection, null, null, null);
        int PrimarycalID = displaylistofCalendars(Calcursor);
        return PrimarycalID;

    }

    public static int addEventtoCalendar (
            Context context, long DTSTART, long DTEND, String EVENT_TIMEZONE, String TITLE, String DESCRIPTION,
            String EVENT_LOCATION, int HAS_ALARM, int CALENDAR_ID){

        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();

        // Insert Event

        values.put(CalendarContract.Events.DTSTART, DTSTART);
        values.put(CalendarContract.Events.DTEND, DTEND);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, EVENT_TIMEZONE);
        values.put(CalendarContract.Events.TITLE, TITLE);
        values.put(CalendarContract.Events.DESCRIPTION, DESCRIPTION);
        values.put(CalendarContract.Events.EVENT_LOCATION, EVENT_LOCATION);
        values.put(CalendarContract.Events.HAS_ALARM, HAS_ALARM);
        values.put(CalendarContract.Events.CALENDAR_ID, CALENDAR_ID);
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        // The returned Uri contains the content-retriever URI for
        // the newly-inserted event, including its id
        int id = Integer.parseInt(uri.getLastPathSegment());
        Log.d("TAG", "Created Calendar Event - " + id);

        return id;
    }


    public static int addAlarmtoEvent(Context context, int EVENT_ID, int MINUTES){

        ContentResolver cr = context.getContentResolver();
        ContentValues reminders = new ContentValues();
        reminders.put(CalendarContract.Reminders.EVENT_ID, EVENT_ID);
        reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        reminders.put(CalendarContract.Reminders.MINUTES, MINUTES);
        Uri urialert = cr.insert(CalendarContract.Reminders.CONTENT_URI, reminders);

        int arertid = Integer.parseInt(urialert.getLastPathSegment());
        Log.d("TAG", "Reminder have been saved successfully - " + arertid);

        return arertid;

    }

    private static int displaylistofCalendars(Cursor cursor) {
        int PrimarycalID = 0;
        cursor.moveToFirst();
        if (cursor != null) {
            do {
                if (cursor.getInt(IS_PRIMARY) == 1){
                    PrimarycalID = cursor.getInt(_ID);
                };
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    Log.d("CALENDAR", " -> " + cursor.getColumnName(i) + " = " + cursor.getString(i));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return PrimarycalID;
    }


}

