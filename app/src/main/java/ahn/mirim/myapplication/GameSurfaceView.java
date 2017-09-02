package ahn.mirim.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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


public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    static GameThread gameThread;
    private GameState gameState;

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        gameThread=new GameThread(getHolder(), getContext(), null);

        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(gameThread.getState().toString().equals("TERMINATED")){
            gameThread=new GameThread(getHolder(), getContext(), gameState);
            gameThread.start();
        } else {
            gameThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        gameState=gameThread.getGameState();
        gameThread.pauseGame();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(gameThread != null && gameThread.getState() != Thread.State.TERMINATED){
            return gameThread.handleTouchEvent(event);
        }
        else return false;
    }
}
