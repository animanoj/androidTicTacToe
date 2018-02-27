package com.example.ani.tictactoe;

import android.widget.ImageView;
import android.widget.TextView;

public class OnePlayerActivity extends Game {

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

        int nextScore = 0;
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                if(tempState[i][j] == -1) {
                    tempState[i][j] = player;
                    nextScore += Math.max(recursiveMove(tempState, (player + 1) % 2, moves + 1), 0);
                    tempState[i][j] = -1;
                }
            }
        }
        return nextScore;
    }

    private void AIMove() {
        int move = -1;
        int score = Integer.MIN_VALUE;
        boolean end = false;
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                if(state[i][j] == -1) {
                    //check if game can end in one move
                    for(int k = 1; k >= 0; k--) {
                        state[i][j] = k;
                        int endVal = checkForWinner(state, moves + 1);
                        state[i][j] = -1;
                        if(endVal == k) {
                            move = (i * gridSize) + j;
                            end = true;
                            break;
                        }
                    }
                    if(end)
                        break;

                    // check for best move
                    state[i][j] = 1;
                    int currScore = recursiveMove(state, 0, moves + 1);
                    state[i][j] = -1;
                    if(currScore > score) {
                        move = (i * gridSize) + j;
                        score = currScore;
                    }
                }
            }
            if(end)
                break;
        }

        int viewID = getResources().getIdentifier("point" + move, "id", getPackageName());
        ImageView view = (ImageView) findViewById(viewID);
        dropIn(view);
    }

    void updateTurnText() {
        if(moves % 2 == 0)
            playerText.setText(R.string.player_text);
        else {
            playerText.setText(R.string.ai_text);
            AIMove();
        }
    }

    void updateResultText(int n) {
        TextView text = (TextView) findViewById(R.id.winnerText);
        switch(n) {
            case 1:
                text.setText(R.string.win);
                break;
            case 2:
                text.setText(R.string.lose);
                break;
            case 3:
                text.setText(R.string.draw);
                break;
            default:
                break;
        }
    }
}
