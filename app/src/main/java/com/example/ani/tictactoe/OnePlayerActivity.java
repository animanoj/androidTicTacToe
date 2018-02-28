package com.example.ani.tictactoe;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnePlayerActivity extends Game {

    private String boardToString(int[][] state, int player) {
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                result
                        .append(String.valueOf((i * gridSize) + j))
                        .append(".")
                        .append(String.valueOf(state[i][j]))
                        .append(".");
            }
        }
        return result.append(String.valueOf(player)).toString();
    }

    private int recursiveMove(int[][] tempState, List<Point> availPoints, Map<String, Integer> map, int player, int moves) {
        String key = boardToString(state, player);
        if(map.containsKey(key))
            return map.get(key);
        int winner = checkForWinner(tempState, moves);
        if(winner != -1) {
            int score;
            if(winner == 2)
                score = 0;
            else if(winner == 1)
                score = 10;
            else
                score = -10;
            map.put(key, score);
            return score;
        }

        int nextScore = player == 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        for(int i = 0; i < availPoints.size(); i++) {
            int nextPlayer = (player + 1) % 2;

            Point curr = availPoints.remove(i);
            int x = curr.getX(), y = curr.getY();
            tempState[x][y] = player;
            int currScore = recursiveMove(tempState, availPoints, map, nextPlayer, moves + 1);
            tempState[x][y] = -1;
            availPoints.add(i, curr);

            if(player == 0)
                nextScore = Math.min(nextScore, currScore);
            else
                nextScore = Math.max(nextScore, currScore);
        }
        map.put(key, nextScore);
        return nextScore;
    }

    private void AIMove() {
        List<Point> availPoints = new ArrayList<>();
        int move = -1;

        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                if(state[i][j] == -1) {
                    availPoints.add(new Point(i, j, gridSize));
                }
            }
        }

        int score = Integer.MIN_VALUE;
        Map<String, Integer> map = new HashMap<>();
        for(int i = 0; i < availPoints.size(); i++) {
            Point curr = availPoints.get(i);
            int x = curr.getX(), y = curr.getY();

            availPoints.remove(i);
            state[x][y] = 1;
            int currScore = recursiveMove(state, availPoints, map, 0, moves + 1);
            state[x][y] = -1;
            availPoints.add(i, curr);

            System.err.println(x + " " + y + " " + currScore);
            if(currScore > score) {
                move = curr.getVal();
                score = currScore;
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
