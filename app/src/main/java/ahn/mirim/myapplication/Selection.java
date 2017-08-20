package ahn.mirim.myapplication;

/**
 * Created by 안성현 on 2017-08-20.
 */

public class Selection {
    private int select[][]=new int[13][5];
    private int count;

    public Selection(String str){
        String tmp[]=str.split("\n");   // 매개변수로 넘어온 문자열을 행 단위로 잘라 배열에 저장한다
        String s;
        count=0;
        char ch;

        for(int i=1; i<tmp.length; i++) {// 배열의 첫 요소는 selection이므로 1번째 배열부터 처리한다
            s=tmp[i];
            for(int j=0; j<5; j++){
                ch=s.charAt(j);
                switch (ch){
                    case '-':
                        select[i-1][j]=-1; // '-'이면 아무것도 없음
                        break;
                    default:
                        count++;         // 장애물의 수
                        if(ch<='9')
                            select[i-1][j]=ch-48;  // 0~9
                        else
                            select[i-1][j]=ch-87;  // 'a'~'z'
                } // switch
            } // j
        } // i
    }

    public int GetSelection(int kind, int num){
        return select[kind][num];
    }

    public int GetCount(){ // 전체 적군 수 구하기
        return count;
    }
}
