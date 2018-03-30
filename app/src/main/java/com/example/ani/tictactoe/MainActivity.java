package com.example.ani.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    private Spinner gridSpinner;

    private void putGridSize(Intent intent) {
        intent.putExtra("gridSize", Character.getNumericValue(gridSpinner.getSelectedItem().toString().charAt(0)));
    }

    public void twoPlayer(View view) {
        Intent intent = new Intent(getApplicationContext(), TwoPlayerActivity.class);
        putGridSize(intent);
        startActivity(intent);
    }

    public void onePlayer(View view) {
        Intent intent = new Intent(getApplicationContext(), OnePlayerActivity.class);
        putGridSize(intent);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridSpinner = (Spinner) findViewById(R.id.gridSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gridSize, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gridSpinner.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}
