package ahn.mirim.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class GameOver {
    final int WAIT=1;   // 버튼입력 대기상태
    final int TOUCH=2;  // 버튼 선택

    final int REPLAY=1;// 버튼 상태
    final int HOME=2;

    private int btnWhich;
    private int status= WAIT;

    private int x1, y1, x2, w, h;
    private Bitmap imgOver;
    private Bitmap imgReplay, imgHome;
    private Bitmap imgTemp[]=new Bitmap[4];
    private Rect rectYes, rectNo;   // 버튼의 좌표

    public GameOver(){
        imgOver=BitmapFactory.decodeResource(GameThread.context.getResources(), R.drawable.gameover);
        imgOver=Bitmap.createScaledBitmap(imgOver, GameThread.width, GameThread.height, false);
        imgTemp[0]=BitmapFactory.decodeResource(GameThread.context.getResources(), R.drawable.btn_replay);
        imgTemp[0]=Bitmap.createScaledBitmap(imgTemp[0], GameThread.width/7, GameThread.width/7, false);
        imgTemp[1]=BitmapFactory.decodeResource(GameThread.context.getResources(), R.drawable.btn_home);
        imgTemp[1]=Bitmap.createScaledBitmap(imgTemp[1], GameThread.width/7, GameThread.width/7, false);
        imgTemp[2]=BitmapFactory.decodeResource(GameThread.context.getResources(), R.drawable.btn_replay1);
        imgTemp[2]=Bitmap.createScaledBitmap(imgTemp[2], GameThread.width/7, GameThread.width/7, false);
        imgTemp[3]=BitmapFactory.decodeResource(GameThread.context.getResources(), R.drawable.btn_home1);
        imgTemp[3]=Bitmap.createScaledBitmap(imgTemp[3], GameThread.width/7, GameThread.width/7, false);

        imgReplay=imgTemp[0];
        imgHome=imgTemp[1];

        y1= GameThread.height/10*3+ GameThread.height/8;
        w=imgHome.getWidth();
        h=imgHome.getHeight();

        x1= GameThread.width/5*3;
        x2=x1+imgReplay.getWidth()/4+imgReplay.getWidth();

        rectYes=new Rect(x1, y1, x1+w, y1+h);
        rectNo=new Rect(x2, y1, x2+w, y1+h);
    }

    public void SetOver(Canvas canvas){
        switch (status){
            case WAIT:
                DisplayAll(canvas);
                break;
            case TOUCH :
                CheckButton();
        }
    }

    public void DisplayAll(Canvas canvas){

        GameThread.DrawGame(canvas);
        canvas.drawBitmap(imgOver, 0, 0, null);
        canvas.drawBitmap(GameThread.totScore.imgScore, GameThread.width/24, GameThread.height/40*17, null);
        canvas.drawBitmap(imgReplay, x1, y1, null);
        canvas.drawBitmap(imgHome, x2, y1, null);
    }

    public void CheckButton(){
        if(btnWhich==HOME){
            GameThread.GameOver();
            GameThread.Reset();
            return;
        }

        status=WAIT;
        btnWhich=0;
        GameThread.Reset();
    }

    public boolean TouchEvent(int x, int y, boolean action){
        if(btnWhich==0) {
            if (rectYes.contains(x, y)&&action) {
                imgReplay = imgTemp[2];
            }
            else if(rectYes.contains(x, y)&&!action){
                imgReplay = imgTemp[0];
                btnWhich = REPLAY;
                status=TOUCH;
            }
            else if (rectNo.contains(x, y)&&action) {
                imgHome = imgTemp[3];
            }
            else if(rectNo.contains(x, y)&&!action){
                btnWhich = HOME;
                imgHome = imgTemp[1];
                status=TOUCH;
            }
            else {
                imgHome=imgTemp[1];
                imgReplay=imgTemp[0];
            }
        }
        return true;
    }


}


