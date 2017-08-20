package ahn.mirim.myapplication;

import java.io.IOException;
import java.io.InputStream;

public class Stage {
    private Selection selection;
    private Delay delay;

    public void ReadStage(int num){
        InputStream fi=GameView.context.getResources().openRawResource(R.raw.ex);

        try{
            byte[] data=new byte[fi.available()]; // 파일의 크기를 배열의 크기로
            fi.read(data);
            fi.close();

            String s=new String(data, "EUC-KR");
            MakeStage(s);
        }catch (IOException e){}
    }

    public void MakeStage(String str) {
        int n=str.indexOf("delay");
        selection=new Selection(str.substring(0, n));

        delay=new Delay(str.substring(n));

    }

    public int GetSelection(int kind, int num){
        return selection.GetSelection(kind, num);
    }

    public int GetDelay(int kind, int num){
        return delay.GetDelay(kind, num);
    }
}
