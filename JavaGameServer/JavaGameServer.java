package com.tpgml.server;
//JavaObjServer.java ObjectStream 기반 채팅 Server

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.Font;
import java.awt.Color;
public class JavaGameServer extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea textArea;
	private JTextField txtPortNumber;

	private ServerSocket socket; // 서버소켓
	private Socket client_socket; // accept() 에서 생성된 client 소켓
	private Vector UserVec = new Vector(); // 연결된 사용자를 저장할 벡터
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	String answer=null;
	Quiz quiz;
	Timer timer;
	boolean gameSet;
	public Vector nick=new Vector();
	public int count=0;
	int score;
	int readyPlayer;
	public static final int MAX_CLIENT=4;
	ArrayList<String> authList = new ArrayList<String>(); // 문제 출제자 랜덤 선택
	String auth;
	Map<String,Integer> map=new HashMap<>();
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaGameServer frame = new JavaGameServer();
					
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JavaGameServer() {
		quiz=new Quiz();
		setTitle("JAVA CatchMySoul Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 338, 440);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 300, 298);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		JLabel lblNewLabel = new JLabel("Port Number");
		lblNewLabel.setFont(new Font("Baloo", Font.PLAIN, 14));
		lblNewLabel.setBounds(13, 318, 87, 26);
		contentPane.add(lblNewLabel);

		txtPortNumber = new JTextField();
		txtPortNumber.setFont(new Font("Baloo", Font.PLAIN, 12));
		txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortNumber.setText("30000");
		txtPortNumber.setBounds(112, 318, 199, 26);
		contentPane.add(txtPortNumber);
		txtPortNumber.setColumns(10);

		JButton btnServerStart = new JButton("Server Start");
		btnServerStart.setBackground(new Color(255, 192, 203));
		btnServerStart.setForeground(new Color(0, 0, 0));
		btnServerStart.setFont(new Font("Baloo", Font.BOLD, 14));
		btnServerStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
					socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText()));
				} catch (NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				AppendText("Chat Server Running..");
				btnServerStart.setText("Server Running..");
				btnServerStart.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
				txtPortNumber.setEnabled(false); // 더이상 포트번호 수정못 하게 막는다
				AcceptServer accept_server = new AcceptServer();
				accept_server.start();
			}
		});
		btnServerStart.setBounds(12, 356, 300, 35);
		contentPane.add(btnServerStart);
	}

	
	// 새로운 참가자 accept() 하고 user thread를 새로 생성한다.
	class AcceptServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
				try {
					AppendText("Waiting new clients ...");
					client_socket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
					AppendText("새로운 참가자 from " + client_socket);
					// User 당 하나씩 Thread 생성
					UserService new_user = new UserService(client_socket);
					UserVec.add(new_user); // 새로운 참가자 배열에 추가
					
					
					new_user.start(); // 만든 객체의 스레드 실행
					AppendText("현재 참가자 수 " + UserVec.size());
				} catch (IOException e) {
					AppendText("accept() error");
					// System.exit(0);
				}
			}
		}
	}


	public synchronized void AppendText(String str) {
		// textArea.append("사용자로부터 들어온 메세지 : " + str+"\n");
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	public synchronized void AppendObject(ChatMsg msg) {
		// textArea.append("사용자로부터 들어온 object : " + str+"\n");
		textArea.append("code = " + msg.code + "\n");
		textArea.append("id = " + msg.UserName + "\n");
		textArea.append("data = " + msg.data + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	// User 당 생성되는 Thread
	// Read One 에서 대기 -> Write All
	class UserService extends Thread {
		private InputStream is;
		private OutputStream os;
		private DataInputStream dis;
		private DataOutputStream dos;

		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		private Socket client_socket;
		private Vector user_vc;
		public String UserName = "";
		public String UserStatus;
		public int UserCount=0;
		
		public boolean gameSet=false;
		public boolean turn=false;
		
		public UserService(Socket client_socket) {
			// TODO Auto-generated constructor stub
			// 매개변수로 넘어온 자료 저장
			this.client_socket = client_socket;
			this.user_vc = UserVec;
			//this.UserCount=UserCounting();
			
			
			try {
//				is = client_socket.getInputStream();
//				dis = new DataInputStream(is);
//				os = client_socket.getOutputStream();
//				dos = new DataOutputStream(os);

				oos = new ObjectOutputStream(client_socket.getOutputStream());
				oos.flush();
				ois = new ObjectInputStream(client_socket.getInputStream());

				// line1 = dis.readUTF();
				// /login user1 ==> msg[0] msg[1]
//				byte[] b = new byte[BUF_LEN];
//				dis.read(b);		
//				String line1 = new String(b);
//
//				//String[] msg = line1.split(" ");
//				//UserName = msg[1].trim();
//				UserStatus = "O"; // Online 상태
//				Login();
				
				
			} catch (Exception e) {
				AppendText("userService error");
			}
		}

		
		public void Login() {
			AppendText(UserName + " 님이 입장");
			WriteOne(UserName + "님 입장하셨습니다.\n"); // 연결된 사용자에게 정상접속을 알림
			String msg = "[" + UserName + "]님이 입장 하였습니다.\n";
			String msg1="현재 이 방의 인원 수는  "+UserVec.size()+"입니다.\n";
			WriteOthers(msg+msg1); // 아직 user_vc에 새로 입장한 user는 포함되지 않았다.
			readyPlayer++;
			
		
		}

		public void Logout() {
			String msg = "[" + UserName + "]님이 퇴장 하였습니다.\n";
			UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
			WriteAll(msg); // 나를 제외한 다른 User들에게 전송
			AppendText("사용자 " + "[" + UserName + "] 퇴장. 현재 참가자 수 " + UserVec.size());
		
		}

		
	
		
		//접속자들 인원 
		public synchronized void UserCounting() {
			UserCount=UserVec.size();
			WriteAllObject(UserCount);
			//System.out.println(UserCount);
			//WriteAll(Integer.toString(UserVec.size()));
		}
		
		// 모든 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
		public synchronized void WriteAll(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O")
					user.WriteOne(str);
			}
		}
		// 모든 User들에게 Object를 방송. 채팅 message와 image object를 보낼 수 있다
		public synchronized void WriteAllObject(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user.UserStatus == "O")
					user.WriteOneObject(ob);
			}
		}

		// 나를 제외한 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
		public synchronized void WriteOthers(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user != this && user.UserStatus == "O")
					user.WriteOne(str);
			}
		}

		// Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
		public byte[] MakePacket(String msg) {
			byte[] packet = new byte[BUF_LEN];
			byte[] bb = null;
			int i;
			for (i = 0; i < BUF_LEN; i++)
				packet[i] = 0;
			try {
				bb = msg.getBytes("euc-kr");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (i = 0; i < bb.length; i++)
				packet[i] = bb[i];
			return packet;
		}

		// UserService Thread가 담당하는 Client 에게 1:1 전송
		public synchronized void WriteOne(String msg) {
			try {
				// dos.writeUTF(msg);
//				byte[] bb;
//				bb = MakePacket(msg);
//				dos.write(bb, 0, bb.length);
				ChatMsg obcm = new ChatMsg("SERVER", "200", msg);
				oos.writeObject(obcm);
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {
//					dos.close();
//					dis.close();
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout(); // 에러가난 현재 객체를 벡터에서 지운다
			}
		}

		// 귓속말 전송
		public synchronized void WritePrivate(String msg) {
			try {
				ChatMsg obcm = new ChatMsg("귓속말", "200", msg);
				oos.writeObject(obcm);
			} catch (IOException e) {
				AppendText("dos.writeObject() error");
				try {
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout(); // 에러가난 현재 객체를 벡터에서 지운다
			}
		}
		public synchronized void WriteOneObject(Object ob) {
			try {
			    oos.writeObject(ob);
			} 
			catch (IOException e) {
				AppendText("oos.writeObject(ob) error");		
				try {
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;				
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout();
			}
		}
		
		
		// 내부 클래스 - 타이머
		class Timer extends Thread
		{
			long preTime = System.currentTimeMillis();
			public void run() {
				try{
					while(gameSet == true){
						sleep(10);
						long time = System.currentTimeMillis() - preTime;
						WriteAll("Timer" + (toTime(time)));
						if(toTime(time).equals("00 : 00")){
							//showSystemMsg("게임종료"+(toTime(time)));
							readyPlayer = 0;
							gameSet = false;
							break;
						}else if(readyPlayer==0){
							break;
						}
					}
				}catch (Exception e){}
			}
			String toTime(long time){
				int m = (int)(3-(time / 1000.0 / 60.0));
				int s = (int)(60-(time % (1000.0 * 60) / 1000.0));
				//int ms = (int)(100-(time % 1000 / 10.0));
				return String.format("%02d : %02d ", m, s);
			}
		}
		
		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
				try {
					// String msg = dis.readUTF();
//					byte[] b = new byte[BUF_LEN];
//					int ret;
//					ret = dis.read(b);
//					if (ret < 0) {
//						AppendText("dis.read() < 0 error");
//						try {
//							dos.close();
//							dis.close();
//							client_socket.close();
//							Logout();
//							break;
//						} catch (Exception ee) {
//							break;
//						} // catch문 끝
//					}
//					String msg = new String(b, "euc-kr");
//					msg = msg.trim(); // 앞뒤 blank NULL, \n 모두 제거
					Object obcm = null;
					String msg = null;
					ChatMsg cm = null;
					if (socket == null)
						break;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						AppendObject(cm);
					} else
						continue;
					
					if (cm.code.matches("100")) {
						
						UserName = cm.UserName;
						nick.add(UserName);
						 Iterator<String> it = nick.iterator();
						 while(it.hasNext())
							 authList.add(it.next());
						 
						 map.put(UserName, 0);
						 //System.out.println(map);
						 
						UserStatus = "O"; // Online 상태
						Login();
				
						
						if(nick.size()==4) {
							//WriteAllObject(new ChatMsg("User List","101",nick.toString()));
							WriteAllObject(new ChatMsg("User: Score","101",map.toString()));
							WriteAll("4명의 user가 모두 입장하여 준비가 되었습니다.");
						}
					} 
					
					else if (cm.code.matches("200")) {
						msg = String.format("[%s] %s", cm.UserName, cm.data);
						
						AppendText(msg); // server 화면에 출력
						String[] args = msg.split(" "); // 단어들을 분리한다.
						if (args.length == 1) { // Enter key 만 들어온 경우 Wakeup 처리만 한다.
							UserStatus = "O";
						} else if (args[1].matches("/exit")) {
							Logout();
							break;
							
						} else if (args[1].matches("/list")) {
							WriteOne("User list\n");
							WriteOne("Name\tStatus\n");
							WriteOne("-----------------------------\n");
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								WriteOne(user.UserName + "\t" + user.UserStatus + "\n");
							}
							WriteOne("-----------------------------\n");
							
						} else if (args[1].matches("/sleep")) {
							UserStatus = "S";
						} else if (args[1].matches("/wakeup")) {
							UserStatus = "O";
						} else if (args[1].matches("/to")) { // 귓속말
							for (int i = 0; i < user_vc.size(); i++) {
								UserService user = (UserService) user_vc.elementAt(i);
								if (user.UserName.matches(args[2]) && user.UserStatus.matches("O")) {
									String msg2 = "";
									for (int j = 3; j < args.length; j++) {// 실제 message 부분
										msg2 += args[j];
										if (j < args.length - 1)
											msg2 += " ";
									}
									// /to 빼고.. [귓속말] [user1] Hello user2..
									user.WritePrivate(args[0] + " " + msg2 + "\n");
									//user.WriteOne("[귓속말] " + args[0] + " " + msg2 + "\n");
									break;
								}
							}
						}
						
						
						else { // 일반 채팅 메시지
							UserStatus = "O";
							//WriteAll(msg + "\n"); // Write All
							WriteAllObject(cm);
							
							if(args[1].matches(answer)){	
							//정답맞추면 800보냄 맞춘사람, 정답
							
								WriteAllObject(new ChatMsg(UserName,"800",answer));
								
								//break;
							
							}
						}
					}
					else if (cm.code.matches("400")) { // logout message 처리
						
						Logout();
						break;
					} 
					
				 
					else if(cm.code.matches("600")) {	//게임시작
						 cm.gameSet=true;
								 for(int i=3; i>0; i--){
									 try{
									 	WriteAll("[ " + i + "초 후 게임을 시작합니다 .. ]");
									 	Thread.sleep(1000);
									 }catch(InterruptedException ie){}
								 }
								 
								 Random rd = new Random();
								 auth=nick.get(rd.nextInt(nick.size())).toString();
								
								
								 answer=quiz.setQuiz(); // 문제 출제
								
								 WriteAllObject(new ChatMsg(auth,"700",answer)); 
								
							
					}
					
					else if(cm.code.matches("700")) {	//4번 오는지 체크
						
						count++;
						
						
						if(count%4==0 &&count!=0) {
							cm.gameSet=true;
						for(int i=3; i>0; i--){
							 try{
							 	WriteAll("[ " + i + "초 후 게임을 시작합니다 .. ]");
							 	Thread.sleep(1000);
							 }catch(InterruptedException ie){}
						 }
					 
				 
						Random rd = new Random();
				 		auth=nick.get(rd.nextInt(nick.size())).toString();
				
				 		answer=quiz.setQuiz(); // 문제 출제
				
				 		WriteAllObject(new ChatMsg(auth,"700",answer)); 
				 
						}
					}
						
						
					else if(cm.code.matches("900")) {	//3점으로 게임종료
						
						cm.gameSet=false;
						WriteAll("게임종료");
						WriteAllObject(new ChatMsg(cm.UserName,"900","게임 종료"));
						readyPlayer=0;
						
						
					
					}
				
				
					else { // 300, 500, ... 기타 object는 모두 방송한다.
						
						WriteAllObject(cm);
					} 
					
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
//						dos.close();
//						dis.close();
						ois.close();
						oos.close();
						client_socket.close();
						Logout(); // 에러가난 현재 객체를 벡터에서 지운다
						turn=false;
						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝
			} // while
		} // run
	
	}
}


