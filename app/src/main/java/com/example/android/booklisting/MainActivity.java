package com.example.android.booklisting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void submitSearch(View view) {
        EditText userSearch = (EditText) findViewById(R.id.userSearch);
        String userText = userSearch.getText().toString();
        Log.e("userText", userText);

        Intent intent = new Intent(MainActivity.this, BookActivity.class);
        intent.putExtra("userText", userText);
        startActivity(intent);
        Log.e("Activity Start", "Starting BookActivity.java");

    }
}
