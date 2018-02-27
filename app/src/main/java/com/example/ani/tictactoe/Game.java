package com.example.ani.tictactoe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.Contract;

public abstract class Game extends AppCompatActivity {

    int gridSize;
    int moves;
    int[][] state;

    TextView playerText;
    LinearLayout winLayout;

    abstract void updateTurnText();
    abstract void updateResultText(int n);

    private void resetState() {
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                state[i][j] = -1;
            }
        }
    }

    private void jumpToMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Contract(pure = true)
    int getX(int pos) {
        return (pos / gridSize);
    }

    @Contract(pure = true)
    int getY(int pos) {
        return (pos % gridSize);
    }

    int checkForWinner() {
        return checkForWinner(state, moves);
    }

    int checkForWinner(int[][] state, int moves) {
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
        if(moves == (int)Math.pow(gridSize, 2)) {
            return 2;
        }
        return -1;
    }

    void endGame() {
        playerText.setVisibility(View.GONE);

        android.support.v7.widget.GridLayout grid = (android.support.v7.widget.GridLayout) findViewById(R.id.gameBoard);
        for(int i = 0; i < grid.getChildCount(); i++) {
            grid.getChildAt(i).setClickable(false);
        }
        grid.setClickable(false);

        winLayout.setVisibility(View.VISIBLE);
        winLayout.animate()
                .alpha(1f)
                .setDuration(300)
                .setListener(null);
    }

    public void dropIn(View view) {
        if(moves == 0) {
            winLayout.setVisibility(View.GONE);
        }

        ImageView counter = (ImageView) view;
        int pos = Integer.parseInt(counter.getTag().toString());
        int x = getX(pos), y = getY(pos);
        if(state[x][y] != -1) {
            Toast.makeText(this, "You can't replace a piece!", Toast.LENGTH_SHORT).show();
            return;
        }
        counter.setTranslationY(-1000f);
        if((moves % 2) == 0) {
            counter.setImageResource(R.drawable.x);
        }
        else {
            counter.setImageResource(R.drawable.o);
        }
        counter.animate()
                .translationYBy(1000f)
                .alpha(1f)
                .rotation(360f)
                .setDuration(300)
                .setListener(null);

        state[x][y] = (moves % 2);
        moves++;

        int result = checkForWinner();
        if(result != -1) {
            updateResultText(result + 1);
            endGame();
            return;
        }
        updateTurnText();
    }

    public void restart(View view) {
        moves = 0;
        resetState();

        android.support.v7.widget.GridLayout grid = (android.support.v7.widget.GridLayout) findViewById(R.id.gameBoard);
        for(int i = 0; i < grid.getChildCount(); i++) {
            final ImageView child = (ImageView) grid.getChildAt(i);
            child.setClickable(true);
            child.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            child.setRotation(-360f);
                        }
                    });
        }
        grid.setClickable(true);

        winLayout.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        winLayout.setVisibility(View.GONE);
                    }
                });

        playerText.setVisibility(View.VISIBLE);
        updateTurnText();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_3);

        gridSize = getIntent().getIntExtra("gridSize", 3);
        state = new int[gridSize][gridSize];
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
