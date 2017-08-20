package ahn.mirim.myapplication;

/**
 * Created by 안성현 on 2017-08-20.
 */

public class Collision {

    public void CheckCollision(){
        Check1();
    }

    private void Check1(){
        int x, y, x1, y1 , w, h;
        x=GameView.player.x;
        y=GameView.player.y;
        w=GameView.player.w;
        h=GameView.player.h;

        for(int i=0; i<13; i++){
            for(int j=0; j<5; j++){
                if(GameView.fastFood[i][j].isDead) continue;
                x1=GameView.fastFood[i][j].x;
                y1=GameView.fastFood[i][j].y;

                if (Math.abs(x1-x)>w || Math.abs(y1-y)>h) // 충돌 없음
                    continue;

                GameView.player.isDead=true;
            }
        }
    }
}
