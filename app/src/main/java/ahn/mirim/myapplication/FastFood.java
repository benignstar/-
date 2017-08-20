package ahn.mirim.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FastFood {
    public int x, y;
    public int w, h;
    public boolean isDead;
    public Bitmap imgFast;

    private int kind, num;
    public int delay;


    public void MakeFast(int kind, int num){
        this.kind=kind;
        this.num=num;

        if(GameView.stage.GetSelection(kind, num)==-1){
            isDead=true;
            return;
        }

        imgFast= BitmapFactory.decodeResource(GameView.context.getResources(), R.drawable.bubble_1);
        imgFast= Bitmap.createScaledBitmap(imgFast, GameView.width/7, GameView.width/7, true);

        w=imgFast.getWidth()/2;
        h=imgFast.getHeight()/2;

        x=GameView.width/7+GameView.width/14+GameView.width/7*num;
        y=-h*2;
        isDead=false;
        delay=GameView.stage.GetDelay(kind, num);
    }

    public void Move(){
        if(isDead) return;
        if(--delay >=0) return;

        y+=GameView.height/150;
        if(y>GameView.height+h*2) isDead=true;
    }
}
