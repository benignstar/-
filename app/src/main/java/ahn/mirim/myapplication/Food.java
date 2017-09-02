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

        imgFood= BitmapFactory.decodeResource(GameThread.context.getResources(), R.drawable.food00+num);
        imgFood= Bitmap.createScaledBitmap(imgFood, GameThread.width/9, GameThread.width/9, true);

        w=imgFood.getWidth()/2;
        h=imgFood.getHeight()/2;

        x= GameThread.width/7+ GameThread.width/14+ GameThread.width/7*col;
        y=-h*2;

        for(int i = GameThread.foods.size()-1; i>=0; i--) {
            if (Math.abs(GameThread.foods.get(i).x - x) < w * 2 && Math.abs(GameThread.foods.get(i).y - y) < h * 2) {
                isDead = true;
                return;
            }
        }
        isDead=false;

    }


    public void Move(){
        if(isDead) return;

        y+= GameThread.speed;
        if(y> GameThread.height+h*2) isDead=true;
    }
}
