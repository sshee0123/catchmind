package com.tpgml.server;

public class Quiz {
	String[] answer=new String[20];
	
	public Quiz(){
		answer[1] = "카레";
        answer[2] = "잠실";
        answer[3] = "김경호";
        answer[4] = "스타벅스";
        answer[5] = "퇴학";
     	answer[6] = "원빈";
     	answer[7] = "아이패드";
        answer[8] = "노래방";
        answer[9] = "크리스마스";
        answer[10] = "종강";
        answer[11]="닭발";
        answer[12]="물먹는하마";
        answer[13]="오리발";
        answer[14]="바위섬";
        answer[15]="가로수";
      
	}

   public String setQuiz() {
        int stage = (int) (Math.random() * 15) + 1;
        return answer[stage];
    }

}
