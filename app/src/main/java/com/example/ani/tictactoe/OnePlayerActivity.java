package com.example.ani.tictactoe;

import android.os.AsyncTask;
import android.util.LongSparseArray;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

interface TaskCompleted {
    void onTaskComplete(Integer result);
}

class Point {
    private int x, y, val;

    Point(int a, int b, int gridSize) {
        x = a;
        y = b;
        val = (x * gridSize) + y;
    }

    int getX() {
        return x;
    }
    int getY() {
        return y;
    }
    int getVal() {
        return val;
    }
}

class State {
    int gridSize, moves;
    int[][] state;
    LongSparseArray<Integer> map;

    State(int g, int m, int[][] s, LongSparseArray<Integer> map) {
        gridSize = g;
        moves = m;
        this.map = map;
        state = new int[gridSize][gridSize];
        for(int i = 0; i < gridSize; i++) {
            System.arraycopy(s[i], 0, state[i], 0, gridSize);
        }
    }
}

class AIMove extends AsyncTask<State, Void, Integer> {
    private boolean zobristSetup = false;
    private int[][][] zobristVal;
    private int moves, gridSize;
    private int[][] state;
    private TaskCompleted activity;

    AIMove(TaskCompleted a) {
        activity = a;
    }

    private void initZobristVal(int gridSize) {
        zobristVal = new int[gridSize][gridSize][2];
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                zobristVal[i][j][0] = (int)(Math.random() * Integer.MAX_VALUE);
                zobristVal[i][j][1] = (int)(Math.random() * Integer.MAX_VALUE);
            }
        }
    }

    private Long boardToHash(int[][] state, int player, int depth) {
        long h = 0;
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                int curr = state[i][j];
                if(curr != -1)
                    h ^= zobristVal[i][j][curr];
            }
        }
        return ((h^depth)^player);
    }

    private int checkForWinner(int[][] state, int moves) {
        int curr;
        boolean win;

        //Rows
        for(int i = 0; i < gridSize; i++) {
            curr = state[i][0];
            if(curr == -1)
                continue;
            win = true;
            for(int j = 1; j < gridSize; j++) {
                if(state[i][j] != curr) {
                    win = false;
                    break;
                }
            }
            if(win)
                return curr;
        }

        //Columns
        for(int j = 0; j < gridSize; j++) {
            curr = state[0][j];
            if(curr == -1)
                continue;
            win = true;
            for(int i = 1; i < gridSize; i++) {
                if(state[i][j] != curr) {
                    win = false;
                    break;
                }
            }
            if(win)
                return curr;
        }

        //Forward Diagonal
        curr = state[0][0];
        if(curr != -1) {
            win = true;
            for (int i = 1; i < gridSize; i++) {
                if (state[i][i] != curr) {
                    win = false;
                    break;
                }
            }
            if (win)
                return curr;
        }

        //Backward Diagonal
        curr = state[0][gridSize - 1];
        if(curr != -1) {
            win = true;
            for (int i = 1; i < gridSize; i++) {
                if (state[i][gridSize - (i + 1)] != curr) {
                    win = false;
                    break;
                }
            }
            if (win)
                return curr;
        }

        if(moves == (int)Math.pow(gridSize, 2))
            return 2;

        return -1;
    }

    private int recursiveMove(int[][] tempState, int player, int moves, List<Point> availPoints, LongSparseArray<Integer> map, int alpha, int beta) {
        int depth = moves - this.moves;
        Long key = boardToHash(state, player, depth);
        int rVal = map.get(key, -1);
        if(rVal != -1)
            return rVal;

        int winner = checkForWinner(tempState, moves);
        if(winner != -1 || depth > 8) {
            int score = 0, gS = (int)Math.pow(gridSize, 2);
            switch(winner) {
                case 0:
                    score = -gS + depth;
                    break;
                case 1:
                    score = gS - depth;
                    break;
                default:
                    break;
            }
            map.put(key, score);
            return score;
        }

        int currScore = player == 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        for(int i = 0; i < availPoints.size(); i++) {
            int nextPlayer = (player + 1) % 2;

            Point next = availPoints.remove(i);
            int x = next.getX(), y = next.getY();
            tempState[x][y] = player;
            int nextScore = recursiveMove(tempState, nextPlayer, moves + 1, availPoints, map, alpha, beta);
            tempState[x][y] = -1;
            availPoints.add(i, next);

            if(player == 0) {
                currScore = Math.min(currScore, nextScore);
                beta = Math.min(beta, currScore);
            }
            else if(player == 1) {
                currScore = Math.max(currScore, nextScore);
                alpha = Math.max(alpha, currScore);
            }
            if(beta <= alpha)
                break;
        }

        map.put(key, currScore);
        return currScore;
    }

    @Override
    protected Integer doInBackground(State... states) {
        State currState = states[0];
        gridSize = currState.gridSize;
        moves = currState.moves;
        state = currState.state;
        LongSparseArray<Integer> map = currState.map;

        if(!zobristSetup) {
            initZobristVal(gridSize);
            zobristSetup = true;
        }

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
        for (int i = 0; i < availPoints.size(); i++) {
            Point next = availPoints.get(i);
            int x = next.getX(), y = next.getY();

            availPoints.remove(i);
            state[x][y] = 1;
            int nextScore = recursiveMove(state, 0, moves + 1, availPoints, map, Integer.MIN_VALUE, Integer.MAX_VALUE);
            state[x][y] = -1;
            availPoints.add(i, next);

            System.err.println(x + " " + y + " " + nextScore);
            if (nextScore > score) {
                move = next.getVal();
                score = nextScore;
            }
        }
        return move;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        activity.onTaskComplete(integer);
    }
}

public class OnePlayerActivity extends Game implements TaskCompleted {
    LongSparseArray<Integer> map = new LongSparseArray<>();

    void updateTurnText() {
        if(moves % 2 == 0)
            playerText.setText(R.string.player_text);
        else {
            playerText.setText(R.string.ai_text);
            new AIMove(OnePlayerActivity.this).execute(new State(gridSize, moves, state, map));
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

    int legalMove() {
        return (moves % 2);
    }

    public void onTaskComplete(Integer result) {
        System.out.println("Move: " + result);
        int viewID = getResources().getIdentifier("point" + result, "id", getPackageName());
        ImageView view = (ImageView) findViewById(viewID);
        dropIn(view);
    }
}
