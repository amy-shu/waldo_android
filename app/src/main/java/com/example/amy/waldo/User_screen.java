package com.example.amy.waldo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
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


public class User_screen extends ActionBarActivity {

    View circle;
    TextView description;
    String id;
    Double dis = 0.0;
    String des = "";
    boolean isWaldo = false;

    boolean up = true;
    int red = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);

        Intent intent = getIntent();
        id = intent.getStringExtra("USER_ID");
        Toast.makeText(getApplicationContext(), id,
                Toast.LENGTH_SHORT).show();
        circle = (View)findViewById(R.id.textView);
        description = (TextView)findViewById(R.id.textView4);
        updateInfo update = new updateInfo(this);
        update.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_screen, menu);
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

    private void colorCircle () {
        Double ratio = dis / 10.0;
        Double num = ratio * 230;
        int color_num = (int) Math.round(num);
        color_num += 10;
        ((GradientDrawable) circle.getBackground()).setColor(Color.argb(color_num, 255, 32, 64));
    }

    public class updateInfo extends AsyncTask<Void, Void, Void> {

        private Context mContext;

        public updateInfo (Context context) {
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            final int delay = 1000; // delay for 1 sec.
            final int period = 1000; // repeat every 10 sec.
            final Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask()
            {
                public void run()
                {
                    String jsonStr = postData();
                    JSONObject jsonObj;
                    Log.d("BOO", jsonStr);
                    try {
                        jsonObj = new JSONObject(jsonStr);
                        des = jsonObj.getString("description");
                        String d = jsonObj.getString("distance");
                        dis = Double.parseDouble(d);
                        String w = jsonObj.getString("isWaldo");
                        isWaldo = Boolean.valueOf(w);
                    } catch (JSONException e) {
                        jsonObj = new JSONObject();
                        e.printStackTrace();
                    }

                    if (isWaldo) {
                        timer.cancel();
                        timer.purge();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setMessage("You are now Waldo!")
                                        .setTitle("YAYYY")
                                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                Intent myIntent = new Intent(mContext, waldo_screen.class);
                                                mContext.startActivity(myIntent);
                                            }
                                        }).create().show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                colorCircle();
                                description.setText(des);
                            }
                        });
                    }
                }
            }, delay, period);

            return null;
        }

    }

    public String postData() {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://2e664d7b.ngrok.com/user_status/");

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
    long lastPress;
    public void exit_click(){
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastPress > 5000) {
            Toast. makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_LONG).show();
            lastPress = currentTime;
        }else {
            finish();
            System.exit(0);
        }
    }




}
