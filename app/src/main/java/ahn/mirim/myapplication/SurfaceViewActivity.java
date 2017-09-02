package ahn.mirim.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class SurfaceViewActivity extends AppCompatActivity {
    GameSurfaceView gameSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.surface);
        gameSurfaceView =(GameSurfaceView)findViewById(R.id.gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameSurfaceView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameSurfaceView.setVisibility(View.VISIBLE);

    }


    @Override
    public void onBackPressed() {
        if (((GlobalVars)getApplicationContext()).getStatus()== GameThread.PROCESS){
            ((GlobalVars)getApplicationContext()).setStatus(GameThread.PAUSE);
            GameThread.imgPause= GameThread.imgTemp[2];
        }

        else if (((GlobalVars)getApplicationContext()).getStatus()== GameThread.PAUSE){
            ((GlobalVars)getApplicationContext()).setStatus(GameThread.PROCESS);
            GameThread.addScore=1;
            GameThread.imgPause= GameThread.imgTemp[0];
        }
    }
}
