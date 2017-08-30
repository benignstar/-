package ahn.mirim.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.Display;

public class GameOver {
    final int WAIT=1;   // 버튼입력 대기상태
    final int TOUCH=2;  // 버튼 선택
    final int BTN_YES=1;// 버튼 상태
    final int BTN_NO=2;

    private int btnWhich;
    private int status= WAIT;

    private int x1, y1, x2, w, h;
    private Bitmap imgOver;
    private Bitmap imgReplay, imgHome;
    private Bitmap imgTemp[]=new Bitmap[4];
    private Rect rectYes, rectNo;   // 버튼의 좌표

    public GameOver(){
        imgOver=BitmapFactory.decodeResource(GameView.context.getResources(), R.drawable.gameover);
        imgOver=Bitmap.createScaledBitmap(imgOver, GameView.width, GameView.height, false);
        imgTemp[0]=BitmapFactory.decodeResource(GameView.context.getResources(), R.drawable.btn_replay);
        imgTemp[0]=Bitmap.createScaledBitmap(imgTemp[0], GameView.width/7, GameView.width/7, false);
        imgTemp[1]=BitmapFactory.decodeResource(GameView.context.getResources(), R.drawable.btn_home);
        imgTemp[1]=Bitmap.createScaledBitmap(imgTemp[1], GameView.width/7, GameView.width/7, false);
        imgTemp[2]=BitmapFactory.decodeResource(GameView.context.getResources(), R.drawable.btn_replay1);
        imgTemp[2]=Bitmap.createScaledBitmap(imgTemp[2], GameView.width/7, GameView.width/7, false);
        imgTemp[3]=BitmapFactory.decodeResource(GameView.context.getResources(), R.drawable.btn_home1);
        imgTemp[3]=Bitmap.createScaledBitmap(imgTemp[3], GameView.width/7, GameView.width/7, false);

        imgReplay=imgTemp[0];
        imgHome=imgTemp[1];

        y1=GameView.height/10*3+GameView.height/8;
        w=imgHome.getWidth();
        h=imgHome.getHeight();

        x1=GameView.width/5*3;
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

        GameView.mThread.DrawGame(canvas);
        canvas.drawBitmap(imgOver, 0, 0, null);
        canvas.drawBitmap(GameView.totScore.imgScore, GameView.width/24, GameView.height/40*17, null);
        canvas.drawBitmap(imgReplay, x1, y1, null);
        canvas.drawBitmap(imgHome, x2, y1, null);
    }

    public void CheckButton(){
        if(btnWhich==BTN_NO){
            GameView.GameOver();
            GameView.Reset();
            return;
        }

        status=WAIT;
        btnWhich=0;

        GameView.Reset();
    }

    public boolean TouchEvent(int x, int y, boolean action){
        if(btnWhich==0) {
            if (rectYes.contains(x, y)&&action) {
                imgReplay = imgTemp[2];
            }
            else if(rectYes.contains(x, y)&&!action){
                imgReplay = imgTemp[0];
                btnWhich = BTN_YES;
                status=TOUCH;
            }
            else if (rectNo.contains(x, y)&&action) {
                imgHome = imgTemp[3];
            }
            else if(rectNo.contains(x, y)&&!action){
                btnWhich = BTN_NO;
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


