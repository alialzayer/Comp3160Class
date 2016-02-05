package com.comp3160.tru000005.comp3160class;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {


    EditText titel,desc,action;
    static Date cDate = new Date();
    static String DATE = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
    Context context;
    DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        titel = (EditText)findViewById(R.id.editText);
        desc = (EditText)findViewById(R.id.editText2);
        action = (EditText)findViewById(R.id.editText4);
        TextView texDate = (TextView) findViewById(R.id.textView6);
        texDate.setText(DATE);
        openDB();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }


    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }
    private void closeDB() {
        myDb.close();
    }



    private void displayText(String message) {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(message);
    }



    public void onClick_AddRecord(View v) {
        displayText("Clicked add record!");

        String sTitel, sDec, sAction;
        sTitel=titel.getText().toString();
        sDec= desc.getText().toString();
        sAction=action.getText().toString();

        long newId = myDb.insertRow(sTitel, sDec, sAction, DATE);

        //String TableID = "Instructor";
        //TableID = tableid.getText().toString();

       // if (TableID.equals("") || TableID.matches("")) TableID = "Instructor";
        SendJSON sendObject = new SendJSON();
        sendObject.execute(sTitel,sDec,sAction);
        // Query for the record we just added.
        // Use the ID:
        Cursor cursor = myDb.getRow(newId);
        displayRecordSet(cursor);
    }

    public void onClick_ClearAll(View v) {
        displayText("Clicked clear all!");
        myDb.deleteAll();
    }

    public void onClick_DisplayRecords(View v) {
        displayText("Clicked display record!");

        Cursor cursor = myDb.getAllRows();
        displayRecordSet(cursor);
    }

    // Display an entire recordset to the screen.
    private void displayRecordSet(Cursor cursor) {
        String message = "";
        // populate the message from the cursor

        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String title = cursor.getString(DBAdapter.COL_TITLE);
                String desc = cursor.getString(DBAdapter.COL_DESC);
                String action = cursor.getString(DBAdapter.COL_ACTION);
                String date = cursor.getString(DBAdapter.COL_DATE);

                // Append data to the message:
                message += "id: " + id
                        +", Title: " + title
                        +", Desc: " + desc
                        +", Action: " + action
                        +", Date: " + date
                        +"\n";
            } while(cursor.moveToNext());
        }

        // Close the cursor to avoid a resource leak.
        cursor.close();

        displayText(message);
    }

    public void onClick_ToCalendar(View view){

        startActivity(new Intent(MainActivity.this, CalendarActivity.class));
    }

    public void onClick_ToTTS(View view){

        startActivity(new Intent(MainActivity.this,TTSActivity.class));
    }

    public void onClick_ToGPS(View view){

        startActivity(new Intent(MainActivity.this,Geolocation.class));
    }

}
