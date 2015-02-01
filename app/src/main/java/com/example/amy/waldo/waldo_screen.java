package com.example.amy.waldo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class waldo_screen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waldo_screen);
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
        System.out.println("found button pressed");
    }
    public void setting_click(View view){
        System.out.println("setting pressed");
    }
    long lastPress;
    public void exit_click(View view){
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastPress > 5000) {
            Toast. makeText(getBaseContext(), "Press again to leave", Toast.LENGTH_LONG).show();
            lastPress = currentTime;
        }else {
            //finish();
            Intent myIntent = new Intent(this, MainActivity.class);
           // myIntent.putExtra("USER_ID", id);
            this.startActivity(myIntent);
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        System.out.println("NOPE, CHUCK TESTA!");
    }

}
