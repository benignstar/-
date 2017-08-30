package ahn.mirim.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Food {
    public int x, y;
    public int w, h;
    public boolean isDead;
    public Bitmap imgFood;
    public int num;


    public Food(int col, int num){

        this.num=num;

        imgFood= BitmapFactory.decodeResource(GameView.context.getResources(), R.drawable.food00+num);
        imgFood= Bitmap.createScaledBitmap(imgFood, GameView.width/9, GameView.width/9, true);

        w=imgFood.getWidth()/2;
        h=imgFood.getHeight()/2;

        x=GameView.width/7+GameView.width/14+GameView.width/7*col;
        y=-h*2;

        for(int i=GameView.foods.size()-1; i>=0; i--) {
            if (Math.abs(GameView.foods.get(i).x - x) < w * 2 && Math.abs(GameView.foods.get(i).y - y) < h * 2) {
                isDead = true;
                return;
            }
        }
        isDead=false;

    }


    public void Move(){
        if(isDead) return;

        y+=GameView.speed;
        if(y>GameView.height+h*2) isDead=true;
    }
}
