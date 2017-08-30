package ahn.mirim.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        gameView=(GameView)findViewById(R.id.gameView);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GameView.StopGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GameView.ResumeGame();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        GameView.ResumeGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GameView.StopGame();
    }

    @Override
    public void onBackPressed() {
        if (((GlobalVars)getApplicationContext()).getStatus()==GameView.PROCESS){
            GameView.status=GameView.PAUSE;
            ((GlobalVars)getApplicationContext()).setStatus(GameView.PAUSE);
            GameView.imgPause=GameView.imgTemp[2];
        }

        else if (((GlobalVars)getApplicationContext()).getStatus()==GameView.PAUSE){
            GameView.status=GameView.PROCESS;
            ((GlobalVars)getApplicationContext()).setStatus(GameView.PROCESS);
            GameView.addScore=1;
            GameView.imgPause=GameView.imgTemp[0];
        }
    }
}
