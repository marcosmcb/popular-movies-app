package com.example.marcoscavalcante.popularmovies;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
