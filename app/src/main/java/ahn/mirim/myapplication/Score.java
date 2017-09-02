package ahn.mirim.myapplication;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Score {
    public int x, y, sw, sh;
    public Bitmap imgScore;

    private Bitmap fonts[]=new Bitmap[10];
    private int loop=0;

    public Score(int x, int y, int _score){
        this.x=x;
        this.y=y;


        for(int i=0; i<10; i++){
            fonts[i]= BitmapFactory.decodeResource(GameThread.context.getResources(), R.drawable.f0+i);
            fonts[i]=Bitmap.createScaledBitmap(fonts[i], GameThread.width/15, GameThread.width/15*13/10, false);
        }

        MakeScore(_score);
        Move();
    }

    public void MakeScore(int _score) {
        String score=""+_score;
        int L=score.length();

        imgScore=Bitmap.createBitmap(fonts[0].getWidth()*L, fonts[0].getHeight(), fonts[0].getConfig());

        Canvas canvas=new Canvas();
        canvas.setBitmap(imgScore);

        int w=fonts[0].getWidth();
        for(int i=0; i<L; i++){
            int n=(int)score.charAt(i)-48;
            canvas.drawBitmap(fonts[n], w*i, 0, null);
        }
        sw=imgScore.getWidth()/2;
        sh=imgScore.getHeight()/2;
    }

    public boolean Move() {
        y-=4;
        if(loop>20) return false;
        loop++;
        return true;
    }
}
