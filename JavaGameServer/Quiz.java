package com.tpgml.server;

public class Quiz {
	String[] answer=new String[20];
	
	public Quiz(){
		answer[1] = "ī��";
        answer[2] = "���";
        answer[3] = "���ȣ";
        answer[4] = "��Ÿ����";
        answer[5] = "����";
     	answer[6] = "����";
     	answer[7] = "�����е�";
        answer[8] = "�뷡��";
        answer[9] = "ũ��������";
        answer[10] = "����";
        answer[11]="�߹�";
        answer[12]="���Դ��ϸ�";
        answer[13]="������";
        answer[14]="������";
        answer[15]="���μ�";
      
	}

   public String setQuiz() {
        int stage = (int) (Math.random() * 15) + 1;
        return answer[stage];
    }

}
