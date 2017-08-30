package ahn.mirim.myapplication;

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

        for(int i=GameView.foods.size()-1; i>=0; i--){
            if(GameView.foods.get(i).isDead) continue;


            x1=GameView.foods.get(i).x;
            y1=GameView.foods.get(i).y;

            if (Math.abs(x1-x)>w || Math.abs(y1-y)>h) // 충돌 없음
                continue;
            if(GameView.foods.get(i).num>13) {
                GameView.Tot+=10;
                GameView.foods.get(i).isDead=true;
            }
            else {
                GameView.status = GameView.GAMEOVER;
                GameView.player.isDead = true;
            }
        }
    }
}
