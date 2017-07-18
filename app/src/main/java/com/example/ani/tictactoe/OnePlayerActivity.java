package com.example.ani.tictactoe;

import android.content.Intent;
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

import org.jetbrains.annotations.Contract;

public class OnePlayerActivity extends AppCompatActivity {

    int moves;
    int[][] state;
    int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

    TextView playerText;
    LinearLayout winLayout;

    private void updateTurnText() {
        if(moves % 2 == 0)
            playerText.setText("Player's Turn");
        else {
            playerText.setText("AI's Turn");
            AIMove();
        }
    }

    private void updateResultText(int n) {
        TextView text = (TextView) findViewById(R.id.winnerText);
        switch(n) {
            case 1:
                text.setText("You Won!");
                break;
            case 2:
                text.setText("You Lost!");
                break;
            case 3:
                text.setText("Draw!");
                break;
            default:
                break;
        }
    }

    private int checkForWinner(int[][] state, int moves) {
        for (int[] combo : winningPositions) {
            int x1 = getX(combo[0]), x2 = getX(combo[1]), x3 = getX(combo[2]);
            int y1 = getY(combo[0]), y2 = getY(combo[1]), y3 = getY(combo[2]);
            if ((state[x1][y1] == state[x2][y2]) &&
                    (state[x2][y2] == state[x3][y3]) &&
                    (state[x3][y3] == state[x1][y1]) &&
                    (state[x1][y1] != -1)) {
                return state[x1][y1];
            }
        }
        if(moves == 9) {
            return 2;
        }
        return -1;
    }

    @Contract(pure = true)
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

    @Contract(pure = true)
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

    private void jumpToMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private int recursiveMove(int[][] tempState, int player, int moves) {
        int winner = checkForWinner(tempState, moves);
        if(winner != -1) {
            switch(winner) {
                case 0:
                    return -1;
                case 1:
                    return 1;
                case 2:
                    return 0;
            }
        }

        int nextScore = -10;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(tempState[i][j] == -1) {
                    tempState[i][j] = player;
                    int currScore = recursiveMove(tempState, (player + 1) % 2, moves + 1);
                    tempState[i][j] = -1;
                    if(nextScore == -10 || (player == 1 && currScore > nextScore) || (player == 0 && currScore < nextScore))
                        nextScore = currScore;
                }
            }
        }
        return nextScore;
    }

    private int[][] stateClone() {
        int[][] tempState = new int[3][3];
        for(int i = 0; i < 3; i++) {
            System.arraycopy(state[i], 0, tempState[i], 0, 3);
        }
        return tempState;
    }

    private void AIMove() {
        int move = -1;
        int score = -2;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(state[i][j] == -1) {
                    int[][] tempState = stateClone();
                    tempState[i][j] = 1;
                    int currScore = recursiveMove(tempState, 0, moves + 1);
                    if(currScore > score) {
                        move = (i * 3) + j;
                        score = currScore;
                    }
                }
            }
        }

        int viewID = getResources().getIdentifier("point" + move, "id", getPackageName());
        ImageView view = (ImageView) findViewById(viewID);
        dropIn(view);
    }

    public void dropIn(View view) {
        if(moves == 0) {
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
        if((moves % 2) == 0) {
            counter.setImageResource(R.drawable.x);
        }
        else {
            counter.setImageResource(R.drawable.o);
        }
        counter.animate().translationYBy(1000f).alpha(1f).rotation(360f).setDuration(300);

        state[x][y] = (moves % 2);
        moves++;

        int result = checkForWinner(state, moves);
        if(result != -1) {
            updateResultText(result + 1);
            endGame();
            return;
        }
        updateTurnText();
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
        moves = 0;
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
        setContentView(R.layout.activity_game);

        state = new int[3][3];
        resetState();

        playerText = (TextView) findViewById(R.id.playerView);
        winLayout = (LinearLayout) findViewById(R.id.winnerLayout);

        moves = 0;
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
            case R.id.menu_button:
                jumpToMain();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        jumpToMain();
    }
}
