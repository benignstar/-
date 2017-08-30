package ahn.mirim.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Player {
    public int x, y;
    public int w, h;
    public boolean isDead;
    public Bitmap imgPlayer;
    public int dir;

    private int counter=0;
    private Bitmap imgTemp[]=new Bitmap[4];
    private int imgNum=0;
    private int move[]={0, -GameView.width/50, GameView.width/50};

    public Player() {
        for (int i = 0; i <= 3; i++) {
            imgTemp[i] = BitmapFactory.decodeResource(GameView.context.getResources(), R.drawable.player00 + i);
            imgTemp[i] = Bitmap.createScaledBitmap(imgTemp[i], GameView.width/9, GameView.width/9*37/20, true);
        }
        w=imgTemp[0].getWidth()/2;
        h=imgTemp[0].getHeight()/2;

        Reset();
    }

    public void Reset() {
        x=GameView.width/2;
        y=GameView.height/10*9;
        isDead=false;
        imgPlayer=imgTemp[1];
        dir=0;
    }

    public void Move(){

        counter++;
        if(counter%20==0)
            imgNum++;
        if(imgNum>=4) imgNum=0;
        imgPlayer = imgTemp[imgNum];

        x+=move[dir];

        if(x<w+GameView.width/7){
            x=w+GameView.width/7;
            dir=0;
        } else if(x>GameView.width-w-GameView.width/7){
            x=GameView.width-w-GameView.width/7;
            dir=0;
        }
    }
}
