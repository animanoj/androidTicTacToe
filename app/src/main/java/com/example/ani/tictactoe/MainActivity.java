package com.example.ani.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int player;
    int[] state = {-1, -1, -1, -1, -1, -1, -1, -1, -1};
    int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

    public void dropIn(View view) {
        if(player == 0) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.winnerLayout);
            layout.setVisibility(View.INVISIBLE);
        }

        ImageView counter = (ImageView) view;

        if(state[Integer.parseInt(counter.getTag().toString())] != -1) {
            Toast.makeText(this, "You can't replace a piece!", Toast.LENGTH_SHORT).show();
            return;
        }

        counter.setImageResource(0);
        counter.setTranslationY(-1000f);

        if((player % 2) == 0) {
            counter.setImageResource(R.drawable.x);
        }
        else {
            counter.setImageResource(R.drawable.o);
        }

        counter.animate().translationYBy(1000f).alpha(1f).rotation(360f).setDuration(300);

        state[Integer.parseInt(counter.getTag().toString())] = (player % 2);
        player++;
        for(int i = 0; i < winningPositions.length; i++) {
            if((state[winningPositions[i][0]] == state[winningPositions[i][1]]) &&
                    (state[winningPositions[i][1]] == state[winningPositions[i][2]]) &&
                    (state[winningPositions[i][0]] == state[winningPositions[i][2]]) &&
                    (state[winningPositions[i][0]] != -1)) {
                TextView text = (TextView) findViewById(R.id.winnerText);
                text.setText("Player " + (state[winningPositions[i][0]] + 1) + " won!");
                endGame();
            }
        }

        if(player == 9) {
            TextView text = (TextView) findViewById(R.id.winnerText);
            text.setText("Draw!");
            endGame();
        }

    }

    public void endGame() {
        android.support.v7.widget.GridLayout grid = (android.support.v7.widget.GridLayout) findViewById(R.id.gameBoard);

        for(int i = 0; i < grid.getChildCount(); i++) {
            grid.getChildAt(i).setClickable(false);
        }

        grid.setClickable(false);

        LinearLayout layout = (LinearLayout) findViewById(R.id.winnerLayout);
        layout.setVisibility(View.VISIBLE);
        layout.animate().alpha(1f).setDuration(300);

        Button restart = (Button) findViewById(R.id.restart);
        restart.animate().alpha(0f).setDuration(300);

    }

    public void restart(View view) {
        player = 0;

        for(int i = 0; i < state.length; i++)
            state[i] = -1;

        android.support.v7.widget.GridLayout grid = (android.support.v7.widget.GridLayout) findViewById(R.id.gameBoard);

        for(int i = 0; i < grid.getChildCount(); i++) {
            ImageView child = (ImageView) grid.getChildAt(i);
            child.setClickable(true);
            child.animate().alpha(0f).setDuration(300);
            child.setRotation(-360f);
        }

        grid.setClickable(true);

        LinearLayout layout = (LinearLayout) findViewById(R.id.winnerLayout);
        layout.animate().alpha(0f).setDuration(300);

        Button restart = (Button) findViewById(R.id.restart);
        restart.animate().alpha(1f).setDuration(300);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        player = 0;
    }
}