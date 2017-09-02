package ahn.mirim.myapplication;

public class Collision {

    public void CheckCollision(){
        Check1();
    }

    private void Check1(){
        int x, y, x1, y1 , w, h;
        x= GameThread.player.x;
        y= GameThread.player.y;
        w= GameThread.player.w;
        h= GameThread.player.h;

        for(int i = GameThread.foods.size()-1; i>=0; i--){
            if(GameThread.foods.get(i).isDead) continue;

            x1= GameThread.foods.get(i).x;
            y1= GameThread.foods.get(i).y;

            if (Math.abs(x1-x)>w*2 || Math.abs(y1-y)>h) // 충돌 없음
                continue;
            if(GameThread.foods.get(i).num>13) {
                GameThread.Tot+=10;
                GameThread.foods.get(i).isDead=true;
            }
            else {
                GameThread.pig+=100;
                GameThread.foods.get(i).isDead=true;
                if(GameThread.pig>=2000) {
                    ((GlobalVars) GameThread.context.getApplicationContext()).setStatus(GameThread.GAMEOVER);
                    GameThread.player.isDead = true;
                }
            }
        }
    }
}
