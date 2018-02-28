package com.example.ani.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public void twoPlayer(View view) {
        Intent intent = new Intent(getApplicationContext(), TwoPlayerActivity.class);
        intent.putExtra("gridSize", 3);
        startActivity(intent);
    }

    public void onePlayer(View view) {
        Intent intent = new Intent(getApplicationContext(), OnePlayerActivity.class);
        intent.putExtra("gridSize", 3);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}
