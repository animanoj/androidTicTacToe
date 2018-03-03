package com.example.ani.tictactoe;

import android.widget.TextView;

public class TwoPlayerActivity extends Game {

    void updateTurnText() {
        playerText.setText(getString(R.string.player_move, (moves % 2) + 1));
    }

    void updateResultText(int n) {
        TextView text = (TextView) findViewById(R.id.winnerText);
        switch(n) {
            case 1:
            case 2:
                text.setText(getResources().getString(R.string.player_win, n));
                break;
            case 3:
                text.setText(R.string.draw);
                break;
            default:
                break;
        }
    }

    int legalMove() {
        return 0;
    }
}
