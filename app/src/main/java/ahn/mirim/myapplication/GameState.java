package ahn.mirim.myapplication;

import java.util.ArrayList;

public class GameState {
    public int x;
    public int y;
    public int state = GameThread.PAUSE;
    public ArrayList<Food> foods;
    public int speed;
    public int speedDiv;
    public int addScore;
    public int score;

}
