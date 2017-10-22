package ahn.mirim.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Random;


class GameThread extends Thread {
    private static boolean paused=false;
    private SurfaceHolder holder;
    static public Context context;

    static int width, height;

    static Bitmap ground, cloud;
    private static int gy, gm;
    private static int cy;
    private static int cm;

    public static Bitmap imgPause;
    public static Bitmap imgTemp[]=new Bitmap[4];
    private static int px;
    private static int py;
    private int pw;
    private int ph;
    static public Rect rectPause;

    static Player player;
    static Collision collision;
    static GameOver gameOver;
    static Pause pause;
    static Gage gage;
    static ArrayList<Food> foods;

    static int speedDiv=151;
    static int speed;
    static int stageNum=-1;

    static int Tot = 0;          // 득점 합계
    static int ScoreCnt=0;
    static int addScore=1;
    static Score totScore;
    static int pig=0;
    static int counter=0;

    static Food[][] food;

    final static int PROCESS=1;
    final static int GAMEOVER=2;
    final static int PAUSE=3;

    static int  state=PROCESS;

    private final static int FRAME_PERIOD = 1000 / 60;


    public GameThread(SurfaceHolder holder, Context context, GameState gameState){
        this.holder=holder;
        this.context=context;

        InitGame();
        resumeGame();
        if(gameState != null){
            player.x=gameState.x;
            player.y=gameState.y;
            foods=gameState.foods;
            addScore=gameState.addScore;
            Tot=gameState.score;
            speedDiv=gameState.speedDiv;
            speed=gameState.speed;
            state=gameState.state;
        }
    }

    static public void pauseGame(){
        paused=true;
    }

    public void resumeGame(){
        paused=false;
    }

    public void InitGame(){
        Display display=((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point=new Point();
        display.getSize(point);

        width=point.x;
        height=point.y;

        ground= BitmapFactory.decodeResource(context.getResources(), R.drawable.ground);
        ground=Bitmap.createScaledBitmap(ground, width, height*5, true);

        speed=height/speedDiv;

        gy=height*5;
        gm=-speed;
        cy=height;
        cm=-speed;

        imgTemp[0]= BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_pause);
        imgTemp[0]=Bitmap.createScaledBitmap(imgTemp[0], width/9, width/9, false);
        imgTemp[1]= BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_pause1);
        imgTemp[1]=Bitmap.createScaledBitmap(imgTemp[1], width/9, width/9, false);
        imgTemp[2]=BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_play);
        imgTemp[2]=Bitmap.createScaledBitmap(imgTemp[2], width/9, width/9, false);
        imgTemp[3]=BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_play1);
        imgTemp[3]=Bitmap.createScaledBitmap(imgTemp[3], width/9, width/9, false);

        py=height/50;
        pw=imgTemp[0].getWidth();
        ph=imgTemp[0].getHeight();
        px=width/15*12;

        imgPause=imgTemp[0];
        rectPause=new Rect(px, py, px+pw, py+ph);

        player=new Player();
        collision=new Collision();
        totScore = new Score(width/20, height/50, 0);
        gameOver=new GameOver();
        pause=new Pause();
        gage=new Gage();
        foods=new ArrayList<Food>();
    }

    public void MakeFood(){
        Random rnd=new Random();
        int r;

        if(foods.size() > 15 || rnd.nextInt(40) < 38) return;
        if(rnd.nextInt(100)<80) r=rnd.nextInt(27);
        else r=rnd.nextInt(4)+26;
        foods.add(new Food(rnd.nextInt(50)/10, r));
    }

    public void run(){
        Canvas canvas=null;
        long sleepTime = 0;
        long begin;
        long end;
        long diff;

        while(!paused){
            canvas=holder.lockCanvas();
            begin = System.currentTimeMillis();
            if(canvas!=null) {
                switch (state) {
                    case PROCESS:
                        CheckCollision();
                        SpeedUP();
                        MakeFood();
                        MoveCharacter();
                        DrawGame(canvas);
                        break;
                    case GAMEOVER:
                        addScore = 0;
                        gameOver.SetOver(canvas);
                        break;
                    case PAUSE:
                        addScore = 0;
                        pause.SetPause(canvas);
                        canvas.drawBitmap(imgPause, px, py, null);
                        break;
                }
            }
            if (canvas != null)
                holder.unlockCanvasAndPost(canvas);
            end = System.currentTimeMillis();
            diff = end - begin;
            sleepTime = FRAME_PERIOD - diff;
            if(sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {}
        }
    } // run

    public static void GameOver(){
        pauseGame();
        context.startActivity(new Intent(context, GameActivity.class));
        ((Activity)context).finish();
    }

    static public void Reset(){
        gy=height*5;
        gm=-height/150;
        cy=height;
        cm=-height/150;   // background

        speedDiv=150;
        speed=height/speedDiv;  // speed

        player.Reset();
        ((GlobalVars)context.getApplicationContext()).setStatus(PROCESS);
        pig=0;
        Tot=0;
        addScore=1;

        foods.clear();
    }

    public GameState getGameState() {
        GameState gameState = new GameState();
        gameState.x = player.x;
        gameState.y = player.y;
        gameState.addScore=addScore;
        gameState.foods=foods;
        gameState.score=Tot;
        gameState.speedDiv=speedDiv;
        gameState.speed=speed;
        if(state!=PROCESS)
            gameState.state = state;

        return gameState;
    }

    public static void SpeedUP(){
        if(counter%1000==0 && speedDiv>100) {
            speedDiv-=10;
            speed = height / speedDiv;
            gm = -speed;
            cm = -speed;
        }
    }

    static public void DrawGame(Canvas canvas) {
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

        canvas.drawBitmap(gage.imgGage, gage.x, gage.y, null);
        if(!player.isDead)
            canvas.drawBitmap(player.imgPlayer, player.x-player.w, player.y-player.h, null);

        if( state!=PAUSE)
            canvas.drawBitmap(imgPause, px, py, null);

    }

    public void MakeScore(){
        ScoreCnt++;
        if(ScoreCnt%30 == 0){
            Tot+=addScore;
        }
    }

    public void CheckCollision(){
        collision.CheckCollision();
    }

    public void MoveCharacter(){
        if(GameThread.pig>=2000) {
            pig=2000;
            ((GlobalVars) GameThread.context.getApplicationContext()).setStatus(GameThread.GAMEOVER);
            player.isDead = true;
        }

        counter++;
        if(counter%1000==0) {
            if(pig>=100) pig-=100;
        }
        cy+=cm;
        if(cy<0) cy=height/2;
        gage.update();
        gy+=gm;
        if(gy<height) gy=height*5;

        for(int i=foods.size()-1; i>=0; i--){
            foods.get(i).Move();
            if(foods.get(i).isDead) {
                foods.remove(i);
            }
        }

        MakeScore();

        if(!player.isDead)
            player.Move();
    }

    public boolean handleTouchEvent(MotionEvent event) {
        int x, y;
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            if (event.getPointerCount() > 1) {
                x = (int) event.getX(event.getPointerCount() - 1);
                y = (int) event.getY(event.getPointerCount() - 1);
            } else {
                x = (int) event.getX();
                y = (int) event.getY();
            }

            if (state == GAMEOVER)
                return gameOver.TouchEvent(x, y, true);

            else if (state == PAUSE)
                return pause.TouchEvent(x, y, true);

            else if (state == PROCESS) {
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
        if(event.getAction()==MotionEvent.ACTION_UP){
        synchronized (holder){
            x = (int) event.getX();
            y = (int) event.getY();

            if (state == GAMEOVER)
                return gameOver.TouchEvent(x, y, false);

            else if(state==PAUSE)
                return pause.TouchEvent(x, y, false);

            else if(state==PROCESS) {
                if(rectPause.contains(x, y)) {
                    imgPause = imgTemp[0];
                    state = PAUSE;
                    ((GlobalVars) context.getApplicationContext()).setStatus(PAUSE);
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