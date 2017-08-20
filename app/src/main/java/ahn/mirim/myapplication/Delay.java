package ahn.mirim.myapplication;

import android.view.Display;

/**
 * Created by 안성현 on 2017-08-20.
 */

public class Delay {
    private int delay[][]=new int[13][5];

    public Delay(String str){
        String tmp[]=str.split("\n");
        String s;

        for(int i=1; i<tmp.length; i++){
            for(int j=0; j<5; j++){
                s=tmp[i].substring(j*4, (j+1)*4).trim(); // 각 행의 문자열을 네 문자씩 잘라 좌우의 공백을 제거
                if(s.equals("---"))
                    delay[i-1][j]=-1;
                else
                    delay[i-1][j]=Integer.parseInt(s);
            } // j
        } // i
    }

    public int GetDelay(int kind, int num){
        return delay[kind][num];
    }
}
