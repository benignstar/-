package ahn.mirim.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Random;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    static GameThread mThread;
    SurfaceHolder mHolder;
    static Context context;

    static int width, height; // View Size

    static Bitmap ground, cloud;
    private static int gy;
    private static int gm;
    private static int cy;
    private static int cm;
    private int counter=0;

    public static Bitmap imgPause;
    public static Bitmap imgTemp[]=new Bitmap[4];
    private int px, py, pw, ph;
    private Rect rectPause;

    static Player player;
    static Collision collision;
    static ArrayList<Food> foods;
    static GameOver gameOver;
    static Pause pause;

    static int speedDiv=150;
    static int speed;
    static int Tot = 0;          // 득점 합계
    static int ScoreCnt=0;
    static int addScore=1;
    static Score totScore;

    final static int READY=0;
    final static int PROCESS=1;
    final static int GAMEOVER=2;
    final static int PAUSE=3;

    static int status=PROCESS;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SurfaceHolder holder=getHolder();
        holder.addCallback(this);

        mHolder=holder;
        GameView.context=context;
        mThread=new GameThread(holder, context);


        InitGame();
        MakeGame();
        setFocusable(true);


    }

    static public void Reset(){
        gy=height*5;
        gm=-height/150*2;
        cy=height;
        cm=-height/150*2;

        speedDiv=150;
        speed=height/speedDiv;
        foods.clear();
        player.Reset();
        GameView.status=GameView.PROCESS;

        Tot=0;
        addScore=1;
    }
    public void MakeGame() {
        ground= BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        ground=Bitmap.createScaledBitmap(ground, width, height*5, true);


        speed=GameView.height/speedDiv;

        gy=height*5;
        gm=-speed*2;
        cy=height;
        cm=-speed*2;

        imgTemp[0]= BitmapFactory.decodeResource(GameView.context.getResources(), R.drawable.btn_pause);
        imgTemp[0]=Bitmap.createScaledBitmap(imgTemp[0], GameView.width/9, GameView.width/9, false);
        imgTemp[1]= BitmapFactory.decodeResource(GameView.context.getResources(), R.drawable.btn_pause1);
        imgTemp[1]=Bitmap.createScaledBitmap(imgTemp[1], GameView.width/9, GameView.width/9, false);
        imgTemp[2]=BitmapFactory.decodeResource(GameView.context.getResources(), R.drawable.btn_play);
        imgTemp[2]=Bitmap.createScaledBitmap(imgTemp[2], GameView.width/9, GameView.width/9, false);
        imgTemp[3]=BitmapFactory.decodeResource(GameView.context.getResources(), R.drawable.btn_play1);
        imgTemp[3]=Bitmap.createScaledBitmap(imgTemp[3], GameView.width/9, GameView.width/9, false);

        py=GameView.height/50;
        pw=imgTemp[0].getWidth();
        ph=imgTemp[0].getHeight();
        px=GameView.width/15*12;

        imgPause=imgTemp[0];
        rectPause=new Rect(px, py, px+pw, py+ph);

    }

    public void InitGame(){
        Display display=((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point=new Point();
        display.getSize(point);

        width=point.x;
        height=point.y;

        player=new Player();
        collision=new Collision();

        totScore = new Score(width/20, height/50, 0);

        foods=new ArrayList<>();
        gameOver=new GameOver();
        pause=new Pause();

    }
    public static void GameOver(){
        StopGame();
        context.startActivity(new Intent(context, StartGame.class));
        ((Activity)context).finish();
    }
    public void MakeFood(){
        Random rnd=new Random();
        int r;

        if(foods.size() > 15 || rnd.nextInt(40) < 38) return;
        if(rnd.nextInt(100)<80) r=rnd.nextInt(14);
        else r=rnd.nextInt(4)+14;
        foods.add(new Food(rnd.nextInt(50)/10, r));

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try{
            mThread.start();
        }catch (Exception e) {
            RestartGame();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        StopGame();
    }

    public static void StopGame() {
        mThread.StopThread();
    }

    public static void PauseGame(){
        mThread.PauseResume(true);
    }

    public static void ResumeGame(){
        mThread.PauseResume(false);
    }

    public void RestartGame(){
        mThread.StopThread();
        mThread=null;
        mThread=new GameThread(mHolder, context);
        mThread.start();
    }


    class GameThread extends Thread {
        boolean canRun=true;
        boolean isWait=false;

        public GameThread(SurfaceHolder holder, Context context){

        }

        public void run(){
            Canvas canvas=null;
            while(canRun){
                canvas=mHolder.lockCanvas();
                try {
                    synchronized (this){
                        if(isWait)
                            try {
                                wait();
                            } catch (Exception e){}
                    }
                    synchronized (mHolder){
                        switch (status) {
                            case PROCESS :
                             //   imgPause=imgTemp[0];
                                if(Tot%100==0&&Tot!=0) {
                                    speedDiv--;
                                    speed=height/speedDiv;
                                    gm=-speed*2;
                                    cm=-speed*2;
                                }
                                MakeFood();
                                CheckCollision();
                                MoveCharacter();
                                DrawGame(canvas);
                                break;
                            case GAMEOVER:
                                addScore=0;
                                gameOver.SetOver(canvas);
                                ((GlobalVars)context.getApplicationContext()).setStatus(GAMEOVER);
                                break;
                            case PAUSE:
                                addScore=0;
                                pause.SetPause(canvas);
                                canvas.drawBitmap(imgPause, px, py, null);
                                break;
                        }
                    }
                }finally {
                    if (canvas != null)
                        mHolder.unlockCanvasAndPost(canvas);
                }
            }

        } // run


        public void DrawGame(Canvas canvas) {
            Rect groundSrc=new Rect();
            Rect cloudSrc=new Rect();
            Rect dst=new Rect();
            dst.set(0,0, width, height);


            groundSrc.set(0, gy-height, width, gy);
            cloudSrc.set(0, cy, width, cy+height);
            canvas.drawBitmap(ground, groundSrc, dst, null);
          //  canvas.drawBitmap(cloud, cloudSrc, dst, null);



            for(int i=foods.size()-1; i>=0; i--){
                canvas.drawBitmap(foods.get(i).imgFood, foods.get(i).x-foods.get(i).w, foods.get(i).y-foods.get(i).h, null);
            }


            totScore.MakeScore(Tot);
            canvas.drawBitmap(totScore.imgScore, totScore.x, totScore.y, null);

            if(!player.isDead)
                canvas.drawBitmap(player.imgPlayer, player.x-player.w, player.y-player.h, null);

            if(status!=PAUSE)
                canvas.drawBitmap(imgPause, px, py, null);
        }

        public void MakeScroe(){
            ScoreCnt++;
            if(ScoreCnt%20 == 0){
                Tot+=addScore;
            }
        }

        public void CheckCollision(){
            collision.CheckCollision();
        }

        public void MoveCharacter(){
            counter++;

            cy+=cm;
            if(cy<0) cy=height/2;

            if(counter%2==0){
                gy+=gm;
                if(gy<height) gy=height*5;
            }

            for(int i=foods.size()-1; i>=0; i--){
                foods.get(i).Move();
                if(foods.get(i).isDead) {
                    foods.remove(i);

                }
            }
            MakeScroe();

            if(!player.isDead)
                player.Move();
        }


        public void StopThread() {
            canRun=false;
            synchronized (this){
                this.notify();
            }
        }

        public void PauseResume(boolean wait){
           // isWait=wait;
            canRun=true;
            synchronized (this){
                this.notify();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            if (event.getPointerCount() > 1) {
                synchronized (mHolder) {
                    int x = (int) event.getX(event.getPointerCount());
                    int y = (int) event.getY(event.getPointerCount());

                    if (status == GAMEOVER) {
                        return gameOver.TouchEvent(x, y, true);
                    }

                    else if (status == PAUSE)
                        return pause.TouchEvent(x, y, true);

                    else if (status == PROCESS) {
                        if (!player.isDead) {
                            if (rectPause.contains(x, y)) {
                                imgPause=imgTemp[1];
                            } else {
                                player.dir = 0;
                                if (x > 0 && x < width / 2 + 1)
                                    player.dir = 1;
                                if (x > width / 2 && x <= width)
                                    player.dir = 2;
                            }
                        }
                    }
                }


            } else {
                synchronized (mHolder) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    if (status == GAMEOVER) {
                        return gameOver.TouchEvent(x, y, true);
                    } else if (status == PAUSE)
                        return pause.TouchEvent(x, y, true);

                    if (!player.isDead) {
                        if(rectPause.contains(x, y)){
                            imgPause=imgTemp[1];
                        }
                        else {
                            player.dir = 0;
                            if (x > 0 && x < width / 2 + 1)
                                player.dir = 1;
                            if (x > width / 2 && x <= width)
                                player.dir = 2;
                        }
                    }
                }
            }
        }
        if(event.getAction()==MotionEvent.ACTION_UP){
            synchronized (mHolder){
                int x = (int) event.getX();
                int y = (int) event.getY();

                if (status == GAMEOVER) {
                    return gameOver.TouchEvent(x, y, false);
                }

                else if(status==PAUSE)
                    return pause.TouchEvent(x, y, false);

                else if(status==PROCESS) {
                    if(rectPause.contains(x, y)) {
                        imgPause = imgTemp[0];
                        GameView.status = GameView.PAUSE;
                        ((GlobalVars) context.getApplicationContext()).setStatus(GameView.PAUSE);
                        imgPause = imgTemp[2];
                    }
                    else {
                        player.dir = 0;
                        imgPause=imgTemp[0];
                    }
                }

            }
        }

        return true;
    }


}
