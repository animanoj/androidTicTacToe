package com.example.ani.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int player;
    int[][] state;
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
            int x1 = getX(winningPositions[i][0]), x2 = getX(winningPositions[i][1]), x3 = getX(winningPositions[i][2]);
            int y1 = getY(winningPositions[i][0]), y2 = getY(winningPositions[i][1]), y3 = getY(winningPositions[i][2]);
            if((state[x1][y1] == state[x2][y2]) &&
                    (state[x2][y2] == state[x3][y3]) &&
                    (state[x3][y3] == state[x1][y1]) &&
                    (state[x1][y1] != -1)) {
                updateResultText(state[x1][y1] + 1);
                endGame();
                return 0;
            }
        }
        return -1;
    }

    private int getX(int pos) {
        int x = 0;
        switch(pos) {
            case 3:
            case 4:
            case 5:
                x = 1;
                break;
            case 6:
            case 7:
            case 8:
                x = 2;
                break;
            default:
                break;
        }
        return x;
    }

    private int getY(int pos) {
        int y = 0;
        switch(pos) {
            case 1:
            case 4:
            case 7:
                y = 1;
                break;
            case 2:
            case 5:
            case 8:
                y = 2;
                break;
            default:
                break;
        }
        return y;
    }

    private void resetState() {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                state[i][j] = -1;
            }
        }
    }

    public void dropIn(View view) {
        if(player == 0) {
            winLayout.setVisibility(View.INVISIBLE);
        }

        ImageView counter = (ImageView) view;
        int pos = Integer.parseInt(counter.getTag().toString());
        int x = getX(pos), y = getY(pos);
        if(state[x][y] != -1) {
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

        state[x][y] = (player % 2);
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
        resetState();

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

        state = new int[3][3];
        resetState();

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
