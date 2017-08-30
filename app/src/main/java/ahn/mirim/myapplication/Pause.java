package ahn.mirim.myapplication;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Pause {

    final int WAIT=1;   // 버튼입력 대기상태
    final int TOUCH=2;  // 버튼 선택
    final int BTN_YES=1;// 버튼 상태

    private int btnWhich;
    private int status= WAIT;

    public static int x1, y1, w, h;
    private Bitmap imgPasue;
    private Bitmap imgPlay;
    private Bitmap imgTemp[]=new Bitmap[2];
    private Rect rectPlay;   // 버튼의 좌표

    public Pause(){
        imgTemp[0]=BitmapFactory.decodeResource(GameView.context.getResources(), R.drawable.btn_play);
        imgTemp[0]=Bitmap.createScaledBitmap(imgTemp[0], GameView.width/9, GameView.width/9, false);
        imgTemp[1]=BitmapFactory.decodeResource(GameView.context.getResources(), R.drawable.btn_play1);
        imgTemp[1]=Bitmap.createScaledBitmap(imgTemp[1], GameView.width/9, GameView.width/9, false);

        imgPasue=BitmapFactory.decodeResource(GameView.context.getResources(), R.drawable.pasue);
        imgPasue=Bitmap.createScaledBitmap(imgPasue, GameView.width, GameView.height, false);
        imgPlay=imgTemp[0];


        y1=GameView.height/50;
        w=imgPlay.getWidth();
        h=imgPlay.getHeight();

        x1=GameView.width/15*12;


        rectPlay=new Rect(x1, y1, x1+w, y1+h);
    }

    public void SetPause(Canvas canvas){
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
        canvas.drawBitmap(imgPasue, 0, 0, null);
    }

    public void CheckButton(){
        status=WAIT;
        btnWhich=0;

        GameView.addScore=1;
        GameView.status=GameView.PROCESS;
        ((GlobalVars)GameView.context.getApplicationContext()).setStatus(GameView.PROCESS);
    }

    public boolean TouchEvent(int x, int y, boolean action){
        if(btnWhich==0) {
            if (rectPlay.contains(x, y)&&action)
                GameView.imgPause = imgTemp[1];
            else if (rectPlay.contains(x, y)&&!action) {
                btnWhich = BTN_YES;
                GameView.imgPause = imgTemp[0];
                status=TOUCH;
                GameView.imgPause = GameView.imgTemp[0];
            }
            else GameView.imgPause = GameView.imgTemp[2];
        }


        return true;
    }

}
