package com.comp3160.tru000005.comp3160class;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alial on 2016-01-27.
 */
public class SendJSON extends AsyncTask<String, String, String> {
    String strFileContents = null;
    Context ctx;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        OutputStream os = null;
        InputStream is = null;
        HttpURLConnection conn = null;

        String TableID = strings[0];

        String URL_LINK = "http://courseattendance.com/accessdb/accessdb_w_Reserved02.php";
        try {
            //constants


            URL url = new URL(URL_LINK);
            JSONObject jsonObject = new JSONObject();
            Date cDate = new Date();
            String DATE = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
            jsonObject.put("TITLE", strings[0]);
            jsonObject.put("DESC", strings[1]);
            jsonObject.put("DATE", DATE);
            jsonObject.put("ACTION", strings[2]);

            String message = jsonObject.toString();
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /*milliseconds*/);
            conn.setConnectTimeout(1000 /* milliseconds */);  // Temp. Fix for Issue 2. Reduce connection timeout
            conn.setUseCaches(false); // Temp. Fix for Issue 2. Clear Cache - Exception in Android L #79 - https://github.com/square/okio/issues/79
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(message.getBytes().length);
            //make some HTTP header nicety
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            //open
            conn.connect();
            //setup send
            os = new BufferedOutputStream(conn.getOutputStream());
            os.write(message.getBytes());
            //clean up
            os.flush();
            //do somehting with response
            is = conn.getInputStream();
            //String contentAsString = readIt(is,len);
            BufferedInputStream in = new BufferedInputStream(conn.getInputStream() );
            byte[] contents = new byte[1024];

            int bytesRead=0;

            while( (bytesRead = in.read(contents)) != -1){
                strFileContents = new String(contents, 0, bytesRead);
            }
            Log.d("DEBUG", "Input Stream = " + strFileContents);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            //clean up
            try {
                if ((os != null) && (is != null)) {
                    os.close();
                    is.close();
                }

                if (os == null) {

                    Log.d("DEBUG", " os NULL POINTERS");
                }

                if (is == null){
                    Log.d("DEBUG","is NULL POINTERS");

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {                       // Temp. Fix for Issue 2. Surround with Try/Catch to through an exception when URL-connection can't be closed
                conn.disconnect();
                if (strFileContents!=null){
                    //publishProgress("Data Sent to Server");
                    Log.d("DEBUG", "Data Sent to Server");
                }
            } catch (Exception e) {
                e.printStackTrace();
                //publishProgress("Server connection problem. Please Try again!");
                Log.d("DEBUG", "HttpURLConnection didn't work as expected");
            }


            return "";
        }

    } // End of doInBackground

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected void onProgressUpdate(String... values) {

        super.onProgressUpdate(values);
        String msg = values[0];
    }

}
