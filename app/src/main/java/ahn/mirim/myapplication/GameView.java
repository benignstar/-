package ahn.mirim.myapplication;

import android.content.Context;
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


public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    static GameThread mThread;
    SurfaceHolder mHolder;
    static Context context;

    static int width, height; // View Size

    static Bitmap ground, cloud;
    private int gy, gm, cy, cm;
    private int counter=0;

    static Player player;
    static Stage stage;
    static FastFood fastFood[][]=new FastFood[13][5];
    static FastFood fastFood2[][]=new FastFood[13][5];
    static Collision collision;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SurfaceHolder holder=getHolder();
        holder.addCallback(this);

        mHolder=holder;
        GameView.context=context;
        mThread=new GameThread(holder, context);

        InitGame();
        MakeGame();
        MakeStage();
        setFocusable(true);


    }

    public void MakeGame() {
        ground= BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        ground=Bitmap.createScaledBitmap(ground, width, height, true);
        cloud=BitmapFactory.decodeResource(getResources(), R.drawable.cloud);
        cloud=Bitmap.createScaledBitmap(cloud, width, height, true);

        gy=height/2;
        gm=-height/150;
        cy=height/2;
        cm=-height/150;
    }

    public void InitGame(){
        Display display=((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point=new Point();
        display.getSize(point);

        width=point.x;
        height=point.y;

        player=new Player();
        stage=new Stage();
        collision=new Collision();

        for(int i=0; i<13; i++){
            for(int j=0; j<5; j++)
                fastFood[i][j]=new FastFood();
        }

    }

    public void MakeStage(){
        stage.ReadStage(0);
        for(int i=0; i<13; i++){
            for(int j=0; j<5; j++)
                fastFood[i][j].MakeFast(i, j);
        }
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

    public void StopGame() {
        mThread.StopThread();
    }

    public void PauseGame(){
        mThread.PauseResume(true);
    }

    public void ResumeGame(){
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
                    synchronized (mHolder){
                        CheckCollision();
                        MoveCharacter();
                        DrawGame(canvas);
                    }
                }finally {
                    if (canvas != null)
                        mHolder.unlockCanvasAndPost(canvas);
                }
            }

            synchronized (this){
                if(isWait)
                    try {
                        wait();
                    } catch (Exception e){}
            }

        } // run

        public void DrawGame(Canvas canvas) {
            Rect groundSrc=new Rect();
            Rect cloudSrc=new Rect();
            Rect dst=new Rect();
            dst.set(0,0, width, height);


            groundSrc.set(0, gy, width, gy+height/2);
            cloudSrc.set(0, cy, width, cy+height/2);
            canvas.drawBitmap(ground, groundSrc, dst, null);
            canvas.drawBitmap(cloud, cloudSrc, dst, null);

            for(int i=12; i>=0; i--){
                for(int j=0; j<5; j++) {
                    if (fastFood[i][j].isDead) continue;
                    canvas.drawBitmap(fastFood[i][j].imgFast, fastFood[i][j].x-fastFood[i][j].w, fastFood[i][j].y-fastFood[i][j].h, null);
                }
            }

            if(!player.isDead)
                canvas.drawBitmap(player.imgPlayer, player.x-player.w, player.y-player.h, null);
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
                if(gy<0) gy=height/2;
            }

            for(int i=12; i>=0; i--){
                for(int j=0; j<5; j++) {
                //    if(fastFood[0][4].isDead) MakeStage();
                    if(fastFood[0][4].delay<0) MakeStage();
                    fastFood[i][j].Move();
                }
            }
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
            isWait=wait;
            synchronized (this){
                this.notify();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getPointerCount()>1){
            synchronized (mHolder) {
                int x = (int) event.getX(1);

                if(!player.isDead){
                    player.dir=0;
                    if(x>0 && x<width/2+1)
                        player.dir=1;
                    if(x>width/2 && x<=width)
                        player.dir=2;
                }
            }

        } else {
            synchronized (mHolder){
                int x = (int) event.getX();

                if(!player.isDead){
                    player.dir=0;
                    if(x>0 && x<width/2+1)
                        player.dir=1;
                    if(x>width/2 && x<=width)
                        player.dir=2;
                }
            }

        }
        if(event.getAction()==MotionEvent.ACTION_UP){
            synchronized (mHolder){
                player.dir=0;
            }
        }

        return true;
    }
}
