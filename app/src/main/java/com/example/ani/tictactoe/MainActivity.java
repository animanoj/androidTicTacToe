package com.example.ani.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    TextView playerText;
    LinearLayout winLayout;

    private void updateTurnText() {
        if(player % 2 == 0) {
            playerText.setText("Player 1's Turn");
        } else {
            playerText.setText("Player 2's Turn");
        }
    }

    private void updateResultText(int n) {
        TextView text = (TextView) findViewById(R.id.winnerText);
        switch(n) {
            case 1:
            case 2:
                text.setText("Player " + n + " won!");
                break;
            case -1:
                text.setText("Draw!");
                break;
            default:
                break;
        }
    }

    private int checkForWinner() {
        for(int i = 0; i < winningPositions.length; i++) {
            if((state[winningPositions[i][0]] == state[winningPositions[i][1]]) &&
                    (state[winningPositions[i][1]] == state[winningPositions[i][2]]) &&
                    (state[winningPositions[i][0]] == state[winningPositions[i][2]]) &&
                    (state[winningPositions[i][0]] != -1)) {
                updateResultText(state[winningPositions[i][0]] + 1);
                endGame();
                return 0;
            }
        }
        return -1;
    }

    public void dropIn(View view) {
        if(player == 0) {
            winLayout.setVisibility(View.INVISIBLE);
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
        if(checkForWinner() == 0) {
            return;
        }

        player++;
        if(player == 9) {
            updateResultText(-1);
            endGame();
        } else {
            updateTurnText();
        }

    }

    public void endGame() {
        playerText.setVisibility(View.INVISIBLE);

        android.support.v7.widget.GridLayout grid = (android.support.v7.widget.GridLayout) findViewById(R.id.gameBoard);
        for(int i = 0; i < grid.getChildCount(); i++) {
            grid.getChildAt(i).setClickable(false);
        }
        grid.setClickable(false);

        winLayout.setVisibility(View.VISIBLE);
        winLayout.animate().alpha(1f).setDuration(300);
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

        winLayout.animate().alpha(0f).setDuration(300);

        playerText.setVisibility(View.VISIBLE);
        updateTurnText();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerText = (TextView) findViewById(R.id.playerView);
        winLayout = (LinearLayout) findViewById(R.id.winnerLayout);

        player = 0;
        updateTurnText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.restart:
                restart(null);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
