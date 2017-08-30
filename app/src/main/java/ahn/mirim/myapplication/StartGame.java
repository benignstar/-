package ahn.mirim.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;



public class StartGame extends Activity {
    long pressedTime=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

        findViewById(R.id.start_btn).setOnClickListener(clickListener);

    }

    Button.OnClickListener clickListener=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.start_btn :
                    startActivity(new Intent(StartGame.this, MainActivity.class));
                    ((GlobalVars)getApplicationContext()).setStatus(GameView.PROCESS);
                    finish();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        if ( pressedTime == 0 ) {
            Toast.makeText(StartGame.this, " 한 번 더 누르면 종료됩니다." , Toast.LENGTH_LONG).show();
            pressedTime = System.currentTimeMillis();
        }
        else {
            int seconds = (int) (System.currentTimeMillis() - pressedTime);

            if ( seconds > 2000 ) {
                Toast.makeText(StartGame.this, " 한 번 더 누르면 종료됩니다." , Toast.LENGTH_SHORT).show();
                pressedTime = 0 ;
            }
            else {
                super.onBackPressed();
            }
        }

    }
}
