package com.example.ani.tictactoe;

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
