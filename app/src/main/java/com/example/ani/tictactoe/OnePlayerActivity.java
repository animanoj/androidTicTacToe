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
