package com.example.amy.waldo;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.Timer;
import java.util.TimerTask;



public class waldo_screen extends ActionBarActivity {

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waldo_screen);
        Intent intent = getIntent();
        id = intent.getStringExtra("USER_ID");
        updateInfo update = new updateInfo();
        update.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_waldo_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void found_me(View view){
        postFound task = new postFound();
        task.execute("true");
        Intent myIntent = new Intent(this, User_screen.class);
        myIntent.putExtra("USER_ID", id);
        this.startActivity(myIntent);
        finish();
    }

    public class postFound extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return changeData(params[0]);
        }
    }

    public class quit extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return changeData(params[0]);
        }
    }

    public String changeData(String bool) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://2e664d7b.ngrok.com/change_waldo/");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("leave_game", bool));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            String responseString = new BasicResponseHandler().handleResponse(response);
            return responseString;
        } catch (ClientProtocolException e) {
            Log.e("UGH", "uhoh bad post");
        } catch (IOException e) {
            Log.e("UGH", "uhoh bad post");
        }
        return null;
    }


    public void setting_click(View view){
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                String input = userInput.getText().toString();
                                updateDescription u = new updateDescription();
                                u.execute(input);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public class updateInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            final int delay = 1000; // delay for 1 sec.
            final int period = 1000; // repeat every 10 sec.
            final Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask()
            {
                public void run()
                {
                    String success = postData();
                    if (!success.equals("Success.")) {
                        Log.e("ERROR", "Failed post");
                    }
                }
            }, delay, period);

            return null;
        }

    }

    public class updateDescription extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(final String... params) {
            String success = postDescription(params[0]);
            if (!success.equals("Success.")) {
                Log.e("ERROR", "BAD POST");
            }
            return null;
        }

    }

    public String postData() {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://2e664d7b.ngrok.com/waldo_status/");

        try {
            System.out.println(id);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("uid", id));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);

            return new BasicResponseHandler().handleResponse(response);
        } catch (ClientProtocolException e) {
            Log.e("UGH", "uhoh bad post");
        } catch (IOException e) {
            Log.e("UGH", "uhoh bad post");
        }
        return null;
    }

    public String postDescription(String d) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://2e664d7b.ngrok.com/description/");

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("description", d));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);

            return new BasicResponseHandler().handleResponse(response);
        } catch (ClientProtocolException e) {
            Log.e("UGH", "uhoh bad post");
        } catch (IOException e) {
            Log.e("UGH", "uhoh bad post");
        }
        return null;
    }

    long lastPress = 0;
    public void exit_click(View view){
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastPress > 5000) {
            Toast. makeText(getBaseContext(), "Press again to leave", Toast.LENGTH_LONG).show();
            lastPress = currentTime;
        } else {
            Intent myIntent = new Intent(this, MainActivity.class);
            this.startActivity(myIntent);

            quit task = new quit();
            task.execute("false");

            finish();
        }
    }

    @Override
    public void onBackPressed() {
        System.out.println("NOPE, CHUCK TESTA!");
    }

}
