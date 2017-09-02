package ahn.mirim.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Gage {
    int x, y;
    Bitmap imgGage;
    Bitmap imgTemp[]=new Bitmap[21];

    public Gage(){
        for(int i=0; i<21; i++){
            imgTemp[i]= BitmapFactory.decodeResource(GameThread.context.getResources(), R.drawable.gage00+i);
            imgTemp[i]=Bitmap.createScaledBitmap(imgTemp[i], GameThread.width/92*10, GameThread.height/46*10, false);
        }
        imgGage=imgTemp[0];
        x= GameThread.width/15*12+imgGage.getWidth()/2;
        y= GameThread.height/50*10;
    }

    public void update(){
        if(GameThread.pig>=2100) return;
        int i=GameThread.pig/100;
        imgGage=imgTemp[i];
    }
}
