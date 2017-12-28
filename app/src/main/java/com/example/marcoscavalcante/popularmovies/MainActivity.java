package com.example.marcoscavalcante.popularmovies;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcoscavalcante.popularmovies.utils.NetworkUtils;
import com.example.marcoscavalcante.popularmovies.utils.PropertyUtils;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkUtils network = new NetworkUtils( getApplicationContext() );

        TextView tv_test = (TextView) findViewById(R.id.debug_tv);

        try {
            tv_test.setText( network.getKeyValue() );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        Context context;
        String message;

        switch( menuItemThatWasSelected )
        {
            case R.id.action_sort_most_popular:
                context = MainActivity.this;
                message = "most popular clicked";
                Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
                break;


            case R.id.action_sort_top_rated:
                context = MainActivity.this;
                message = "top popular clicked";
                Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
                break;

            default:
                context = MainActivity.this;
                message = "couldnt find selection!";
                Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
