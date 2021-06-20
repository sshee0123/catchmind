package com.tpgml.server;

// ChatMsg.java ä�� �޽��� ObjectStream ��.
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.ImageIcon;
import java.util.*;

class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	public String code; // 100:�α���, 400:�α׾ƿ�, 200:ä�ø޽���, 300:Image, 500: Mouse Event
	public String UserName;
	public String data;
	public ImageIcon img;
	public MouseEvent mouse_e;
	public int pen_size; // pen size
	public int pen;
	public Color pen_color;
	public int firstX,firstY,lastX,lastY;
	public int UserCount;
	public String answer=null;
	public boolean turn;
	public boolean gameSet=false;
	public Vector nick;
	public int count;
	
	public ChatMsg(String UserName, String code, String msg) {
		this.code = code;
		this.UserName = UserName;
		this.data = msg;
	}
	public String getName(){
		return UserName;
	}
	
}