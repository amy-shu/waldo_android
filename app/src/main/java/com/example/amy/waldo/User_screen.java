package com.example.amy.waldo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class User_screen extends ActionBarActivity {

    public View circle;
    int red = 0;
    boolean up = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);
        new AlertDialog.Builder(this)
                .setTitle("Waldo")
                .setMessage("You are now the new Waldo")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        Intent intent = getIntent();
        String id = intent.getStringExtra("USER_ID");
        Toast.makeText(getApplicationContext(), id,
                Toast.LENGTH_SHORT).show();
        circle = (View)findViewById(R.id.textView);
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

    public void color_tick() {
        if (up) {
            if (red < 245) {
                System.out.println("tick");
                System.out.println(red);
                ((GradientDrawable) circle.getBackground()).setColor(Color.argb(red, 255, 32, 64));
                red += 10;
            } else {
                up = false;
            }
        } else {
            if (red > 10) {
                System.out.println("tock");
                System.out.println(red);
                ((GradientDrawable) circle.getBackground()).setColor(Color.argb(red, 255, 32, 64));
                red -= 10;
            } else {
                up = true;
            }
        }
    }
    public void waldo_change(View view) {
        Intent myIntent = new Intent(this, waldo_screen.class);
        this.startActivity(myIntent);
    }

    public void color_change(View view) {
        Toast.makeText(getApplicationContext(), "color changing",
                Toast.LENGTH_SHORT).show();
        //((GradientDrawable) circle.getBackground()).setColor(Color.argb(255, red, 0, 0));
        int delay = 1000; // delay for 1 sec.
        int period = 1000; // repeat every 10 sec.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                       color_tick();
                    }
                });
            }
        }, delay, period);


    }
}
