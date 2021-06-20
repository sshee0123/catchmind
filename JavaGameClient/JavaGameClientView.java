package com.tpgml.server;

// JavaObjClientView.java ObjecStram 占쏙옙占� Client
//占쏙옙占쏙옙占쏙옙占쏙옙 채占쏙옙 창
import java.awt.*;


import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.JToggleButton;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Canvas;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.tpgml.server.JavaGameServer.UserService;

import javax.swing.JComboBox;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.border.EtchedBorder;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class JavaGameClientView extends JFrame {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtInput;
	private String UserName;
	private JButton btnSend;
	private static final int BUF_LEN = 128; 
	private Socket socket; 
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	private JButton btnGameStart;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	private JLabel lblUserName;
	// private JTextArea textArea;
	private JTextPane textArea;
	private JLabel myanswer;
	private Frame frame;
	private FileDialog fd;
	private JButton imgBtn;
	private JButton SaveBtn;
	JPanel panel;
	private Graphics gc;
	private int pen_size = 2; // minimum 2
	private int pen=2;
	private Color pen_color;
	
	private Image panelImage = null; 
	private Graphics gc2 = null;
	private int firstX,firstY,lastX,lastY;
	private int UserCount=1;
	String nickName;
	Vector nickVec = new Vector(); // 연결된 사용자를 저장할 벡터
	String[] nicks;
	boolean gameSet;
	boolean auth;
	JLabel myTimer;
	JLabel NickName1,NickName2,NickName3,NickName4;
	JLabel myPoint;
	JLabel User1,User2,User3,User4;
	int s1,s2,s3,s4,p1;
	String arr=null;
	boolean turn=false;
	JButton OptionBtn;
	Clip clip;
	/**
	 * Create the frame.
	 * @throws BadLocationException 
	 */
	 

	public JavaGameClientView(String username, String ip_addr, String port_no)  {
		setTitle("Catch My Soul");
		
		File mfile=new File("src/Catchmind.wav");
		 try {
	            
	            AudioInputStream stream = AudioSystem.getAudioInputStream(mfile);
	            clip = AudioSystem.getClip();
	            clip.open(stream);
	            clip.start();
	            
	            
	        } catch(Exception e) {
	            
	            e.printStackTrace();
	        }
		
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1307, 646);
		contentPane = new JPanel();
		contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		contentPane.setMinimumSize(new Dimension(0, 0));
		contentPane.setMaximumSize(new Dimension(100, 100));
		contentPane.setForeground(new Color(0, 0, 0));
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 77, 352, 404);
		contentPane.add(scrollPane);

		textArea = new JTextPane();
		textArea.setEditable(true);
		textArea.setFont(new Font("굴림", Font.PLAIN, 14));
		scrollPane.setViewportView(textArea);

		txtInput = new JTextField();
		txtInput.setBounds(12, 489, 271, 40);
		contentPane.add(txtInput);
		txtInput.setColumns(10);

		btnSend = new JButton("SEND");
		btnSend.setBackground(new Color(255, 192, 203));
		btnSend.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		btnSend.setFont(new Font("Baloo", Font.PLAIN, 14));
		btnSend.setBounds(295, 489, 69, 40);
		contentPane.add(btnSend);

		lblUserName = new JLabel();
		lblUserName.setBackground(new Color(255, 192, 203));
		lblUserName.setFont(new Font("Baloo", Font.BOLD, 22));
		lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName.setBounds(186, 556, 113, 44);
		contentPane.add(lblUserName);
		setVisible(true);
		setVisible(true);

		AppendText("User " + username + " connecting " + ip_addr + " " + port_no);
		UserName = username;
		lblUserName.setText(username);

		/*imgBtn = new JButton("+");
		imgBtn.setFont(new Font("굴림", Font.PLAIN, 16));
		imgBtn.setBounds(12, 489, 50, 40);
		contentPane.add(imgBtn);*/

		JButton exitBtn = new JButton();
		exitBtn.setIcon(new ImageIcon("src/door.png"));
		exitBtn.setBorderPainted(false);
		exitBtn.setFocusPainted(false);
		exitBtn.setContentAreaFilled(false);
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg msg = new ChatMsg(UserName, "400", "Bye");
				SendObject(msg);
				System.exit(0);
			}
		});
		exitBtn.setBounds(1180, 520, 65, 55);
		contentPane.add(exitBtn);

		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);
		panel.setBounds(376, 77, 668, 404);
		contentPane.add(panel);
		gc = panel.getGraphics();

		
		panelImage = createImage(panel.getWidth(), panel.getHeight());
		gc2 = panelImage.getGraphics();
		gc2.setColor(panel.getBackground());
		gc2.fillRect(0,0, panel.getWidth(),  panel.getHeight());
		gc2.setColor(Color.BLACK);
		gc2.drawRect(0,0, panel.getWidth()-1,  panel.getHeight()-1);
		
		JButton eraserButton = new JButton();
		eraserButton.setFont(new Font("굴림", Font.PLAIN, 12));
		eraserButton.setIcon(new ImageIcon("src/eraser.png"));
		eraserButton.setBorderPainted(false);
		eraserButton.setFocusPainted(false);
		eraserButton.setContentAreaFilled(false);
		eraserButton.setBounds(876, 489, 69, 40);
		contentPane.add(eraserButton);
		eraserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JButton b=(JButton)e.getSource();
			
					pen=5;
			}
		});
		
		JButton LineBtn = new JButton();
		LineBtn.setIcon(new ImageIcon("src/pen.png"));
		LineBtn.setBorderPainted(false);
		LineBtn.setFocusPainted(false);
		LineBtn.setContentAreaFilled(false);
		LineBtn.setBounds(957, 485, 69, 44);
		contentPane.add(LineBtn);
		LineBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JButton b=(JButton)e.getSource();
				
					pen=2;
			}
			
		});
		
		JButton RecBtn = new JButton("□");
		RecBtn.setBackground(new Color(255, 192, 203));
		RecBtn.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		RecBtn.setBounds(534, 547, 50, 25);
		contentPane.add(RecBtn);
		RecBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JButton b=(JButton)e.getSource();
				if(b.getText().equals("占쏙옙"))
					pen=3;
			}
			
		});
		
		JButton CircleBtn = new JButton("○");
		CircleBtn.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		CircleBtn.setBackground(new Color(255, 192, 203));
		CircleBtn.setBounds(626, 547, 50, 25);
		contentPane.add(CircleBtn);
		CircleBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JButton b=(JButton)e.getSource();
				if(b.getText().equals("○"))
					pen=4;
			}
			
		});
		
		JButton dotBtn = new JButton("●");
		dotBtn.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		dotBtn.setBackground(new Color(255, 192, 203));
		dotBtn.setBounds(438, 547, 50, 25);
		contentPane.add(dotBtn);
		dotBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JButton b=(JButton)e.getSource();
				if(b.getText().equals("●"))
					pen=1;
			}
			
		});
		
		JButton BlackBtn = new JButton();
		BlackBtn.setBackground(Color.WHITE);
		BlackBtn.setIcon(new ImageIcon("src/black.png"));
		BlackBtn.setBorderPainted(false);
		BlackBtn.setContentAreaFilled(false);
		BlackBtn.setFocusPainted(false);
		BlackBtn.setBounds(376, 492, 100, 44);
		contentPane.add(BlackBtn);
		BlackBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JButton b=(JButton)e.getSource();
					pen_color=Color.BLACK;
			}
			
		});
		
		
		JButton GreenBtn = new JButton();
		GreenBtn.setBackground(Color.WHITE);
		GreenBtn.setIcon(new ImageIcon("src/green.png"));
		GreenBtn.setBorderPainted(false);
		GreenBtn.setFocusPainted(false);
		GreenBtn.setContentAreaFilled(false);
		GreenBtn.setBounds(776, 492, 100, 44);
		contentPane.add(GreenBtn);
		GreenBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JButton b=(JButton)e.getSource();
					pen_color=Color.GREEN;
			}
			
		});
		
		JButton YellowBtn = new JButton();
		YellowBtn.setBackground(Color.WHITE);
		YellowBtn.setIcon(new ImageIcon("src/yellow.png"));
		YellowBtn.setBorderPainted(false);
		YellowBtn.setFocusPainted(false);
		YellowBtn.setContentAreaFilled(false);
		YellowBtn.setBounds(676, 492, 100, 44);
		contentPane.add(YellowBtn);
		YellowBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JButton b=(JButton)e.getSource();
					pen_color=Color.YELLOW;
			}
			
		});
		
		JButton RedBtn = new JButton();
		RedBtn.setBackground(Color.WHITE);
		RedBtn.setIcon(new ImageIcon("src/red.png"));
		RedBtn.setBounds(576, 492, 100, 44);
		RedBtn.setBorderPainted(false);
		RedBtn.setFocusPainted(false);
		RedBtn.setContentAreaFilled(false);
		contentPane.add(RedBtn);
		RedBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JButton b=(JButton)e.getSource();
					pen_color=Color.RED;
			}
			
		});
		
		JButton BlueBtn = new JButton();
		BlueBtn.setBackground(Color.WHITE);
		BlueBtn.setIcon(new ImageIcon("src/blue.png"));
		BlueBtn.setBounds(476, 492, 100, 44);
		BlueBtn.setBorderPainted(false);
		BlueBtn.setFocusPainted(false);
		BlueBtn.setContentAreaFilled(false);
		contentPane.add(BlueBtn);
		BlueBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JButton b=(JButton)e.getSource();
					pen_color=Color.BLUE;
			}
			
		});
		
		JButton AllClearBtn = new JButton("ALL CLEAR");
		AllClearBtn.setBackground(new Color(255, 192, 203));
		AllClearBtn.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		AllClearBtn.setFont(new Font("Baloo", Font.PLAIN, 14));
		AllClearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton b=(JButton)e.getSource();
					pen=6;
					repaint();
				
			}
		});
		AllClearBtn.setBounds(720, 547, 90, 25);
		contentPane.add(AllClearBtn);
		
		OptionBtn = new JButton();
		OptionBtn.setIcon(new ImageIcon("src/on.png"));
		
		OptionBtn.setBorderPainted(false);
		OptionBtn.setFocusPainted(false);
		OptionBtn.setContentAreaFilled(false);
		OptionBtn.setBounds(1099, 520, 53, 52);
		OptionBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(e.getSource()==OptionBtn) {
					OptionBtn.setIcon(new ImageIcon("src/off.png"));
					clip.stop();
					
				}
			}
		});
		contentPane.add(OptionBtn);
		
		SaveBtn = new JButton("CAPTURE");
		SaveBtn.setBackground(new Color(255, 192, 203));
		SaveBtn.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		SaveBtn.setFont(new Font("Baloo", Font.PLAIN, 14));
		SaveBtn.setBounds(845, 547, 80, 25);
		SaveBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(e.getSource()==SaveBtn) {
					frame=new Frame("화면 저장");
					FileDialog fd1=new FileDialog(frame,"화면 저장",FileDialog.SAVE);
					fd1.setVisible(true);
					
					String dir=fd1.getDirectory();
					String file=fd1.getFile();
					String saveFileExtension="png";
					if(dir==null||file==null)
						return;
				
					try {
						Robot robot=new Robot();
						Rectangle ractangle=new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
						BufferedImage image=robot.createScreenCapture(ractangle);
						image.setRGB(0, 0, 100);
						File file2=new File(dir+file+"."+saveFileExtension);
						ImageIO.write(image,saveFileExtension,file2);
						
					}catch(Exception e2) {
						e2.printStackTrace();
					}
					}}}
					);
				
		contentPane.add(SaveBtn);
		
		JSlider slider = new JSlider();
		slider.setBorder(null);
		slider.setBackground(new Color(255, 255, 255));
		slider.setValue(20);
		slider.setBounds(438, 582, 238, 24);
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				pen_size=slider.getValue();	
			}
			
		});
		contentPane.add(slider);
		
		JLabel penSize = new JLabel("WIDTH");
		penSize.setFont(new Font("Baloo", Font.PLAIN, 14));
		penSize.setBounds(376, 582, 56, 22);
		contentPane.add(penSize);
		
		JLabel TimePoint = new JLabel("MY POINT");
		TimePoint.setFont(new Font("Baloo", Font.PLAIN, 22));
		TimePoint.setBounds(742, 10, 107, 40);
		contentPane.add(TimePoint);
		
		
		/*myTimer = new JLabel("00 : 00");
		myTimer.setHorizontalTextPosition(SwingConstants.CENTER);
		myTimer.setHorizontalAlignment(SwingConstants.CENTER);
		myTimer.setForeground(Color.BLACK);
		myTimer.setFont(new Font("굴림", Font.BOLD, 20));
		myTimer.setBounds(876, 10, 150, 48);
		contentPane.add(myTimer);*/
		
		myPoint = new JLabel("0");
		myPoint.setFont(new Font("굴림", Font.BOLD, 20));
		myPoint.setBounds(900, 10, 92, 40);
		contentPane.add(myPoint);
		
		JLabel lblNewLabel = new JLabel("Catch My Soul");
		lblNewLabel.setFont(new Font("Baloo", Font.BOLD, 35));
		lblNewLabel.setBounds(12, 10, 303, 52);
		contentPane.add(lblNewLabel);
		
		JLabel UserList = new JLabel("USER LIST");
		UserList.setFont(new Font("Baloo", Font.PLAIN, 24));
		UserList.setBounds(1112, 18, 153, 38);
		contentPane.add(UserList);
		
		NickName1 = new JLabel("USER1");
		NickName1.setFont(new Font("Baloo", Font.BOLD, 14));
		NickName1.setBorder(null);
		NickName1.setHorizontalAlignment(SwingConstants.CENTER);
		NickName1.setForeground(Color.BLACK);
		NickName1.setBackground(Color.WHITE);
		NickName1.setBounds(1056, 111, 82, 25);
		
		contentPane.add(NickName1);
		
		User1 = new JLabel();
		User1.setBorder(new LineBorder(new Color(0, 0, 0)));
		User1.setBounds(1150, 77, 132, 97);
		contentPane.add(User1);
		
		NickName2 = new JLabel("USER2");
		NickName2.setFont(new Font("Baloo", Font.BOLD, 14));
		NickName2.setBorder(null);
		NickName2.setForeground(Color.BLACK);
		NickName2.setHorizontalAlignment(SwingConstants.CENTER);
		NickName2.setBackground(Color.WHITE);
		NickName2.setBounds(1056, 218, 82, 28);
		contentPane.add(NickName2);
		
		NickName3 = new JLabel("USER3");
		NickName3.setFont(new Font("Baloo", Font.BOLD, 14));
		NickName3.setBorder(null);
		NickName3.setHorizontalAlignment(SwingConstants.CENTER);
		NickName3.setForeground(Color.BLACK);
		NickName3.setBackground(Color.WHITE);
		NickName3.setBounds(1056, 324, 82, 28);
		contentPane.add(NickName3);
		
		NickName4 = new JLabel("USER4");
		NickName4.setFont(new Font("Baloo", Font.BOLD, 14));
		NickName4.setBorder(null);
		NickName4.setHorizontalAlignment(SwingConstants.CENTER);
		NickName4.setForeground(Color.BLACK);
		NickName4.setBackground(Color.WHITE);
		NickName4.setBounds(1056, 434, 82, 28);
		contentPane.add(NickName4);
		
		User2 =new JLabel();
		User2.setBorder(new LineBorder(new Color(0, 0, 0)));
		User2.setBounds(1150, 184, 132, 97);
		contentPane.add(User2);
		
		User3 = new JLabel();
		User3.setBorder(new LineBorder(new Color(0, 0, 0)));
		User3.setBounds(1150, 291, 132, 97);
		contentPane.add(User3);
		
		User4 = new JLabel();
		User4.setBorder(new LineBorder(new Color(0, 0, 0)));
		User4.setBounds(1150, 398, 132, 97);
		contentPane.add(User4);
		
		
		myanswer = new JLabel("");
		myanswer.setFont(new Font("굴림", Font.BOLD, 14));
		myanswer.setForeground(Color.BLACK);
		myanswer.setBorder(null);
		myanswer.setBackground(Color.ORANGE);
		myanswer.setHorizontalAlignment(SwingConstants.CENTER);
		myanswer.setBounds(476, 10, 172, 40);
		contentPane.add(myanswer);
		
		JLabel answer = new JLabel("ANSWER");
		answer.setFont(new Font("Baloo", Font.PLAIN, 22));
		answer.setBounds(376, 10, 88, 38);
		contentPane.add(answer);
		
		JLabel lblOther = new JLabel("OTHER");
		lblOther.setFont(new Font("Baloo", Font.PLAIN, 14));
		lblOther.setBounds(376, 545, 50, 27);
		contentPane.add(lblOther);
		
		/*JLabel lblTimer = new JLabel("TIMER");
		lblTimer.setFont(new Font("Baloo", Font.PLAIN, 22));
		lblTimer.setBounds(756, 10, 100, 38);
		contentPane.add(lblTimer);*/
		
		btnGameStart = new JButton("GAME START");
		btnGameStart.setFont(new Font("Baloo", Font.PLAIN, 14));
		btnGameStart.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		btnGameStart.setBackground(new Color(255, 192, 203));
		btnGameStart.setBounds(957, 547, 90, 25);
		contentPane.add(btnGameStart);
		
		JLabel lblNewLabel_1 =new JLabel(new ImageIcon("src/canvas.png"));
		lblNewLabel_1.setBounds(376, 53, 668, 38);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("My NickName : ");
		lblNewLabel_2.setFont(new Font("Baloo", Font.BOLD, 22));
		lblNewLabel_2.setBounds(12, 556, 178, 40);
		contentPane.add(lblNewLabel_2);
		

		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));
//			is = socket.getInputStream();
//			dis = new DataInputStream(is);
//			os = socket.getOutputStream();
//			dos = new DataOutputStream(os);

			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

		
			ChatMsg obcm = new ChatMsg(UserName, "100", "Hello");
			
			SendObject(obcm);
			
		
			ListenNetwork net = new ListenNetwork();
			net.start();
			TextSendAction action = new TextSendAction();
			btnSend.addActionListener(action);
			txtInput.addActionListener(action);
			txtInput.requestFocus();
			ImageSendAction action2 = new ImageSendAction();
			//imgBtn.addActionListener(action2);
			MyMouseEvent mouse = new MyMouseEvent();
			panel.addMouseMotionListener(mouse);
			panel.addMouseListener(mouse);
			MyMouseWheelEvent wheel = new MyMouseWheelEvent();
			panel.addMouseWheelListener(wheel);
			
			GameStartAction action3=new GameStartAction();
			btnGameStart.addActionListener(action3);
			
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppendText("connect error");
		}
	}
	public void getNickName() {
		
	}
	

	public void paint(Graphics g) {
		super.paint(g);
		
		gc.drawImage(panelImage, 0, 0, this);
	}
	
	ImageIcon answer1icon=new ImageIcon("src/answer1.jpg");
	ImageIcon answer2icon=new ImageIcon("src/answer2.jpg");
	ImageIcon answer3icon=new ImageIcon("src/answer3.jpg");
	ImageIcon answer4icon=new ImageIcon("src/answer4.jpeg");
	ImageIcon answer5icon=new ImageIcon("src/answer5.jpeg");
	ImageIcon answer6icon=new ImageIcon("src/answer6.jpg");
	ImageIcon answer7icon=new ImageIcon("src/answer7.jpg");
	ImageIcon answer8icon=new ImageIcon("src/answer8.jpg");
	ImageIcon answer9icon=new ImageIcon("src/answer9.jpg");
	ImageIcon answer10icon=new ImageIcon("src/answer10.png");
	ImageIcon answer11icon=new ImageIcon("src/answer11.jpeg");
	ImageIcon answer12icon=new ImageIcon("src/answer12.jpeg");
	ImageIcon answer13icon=new ImageIcon("src/answer13.png");
	ImageIcon answer14icon=new ImageIcon("src/answer14.jpeg");
	ImageIcon answer15icon=new ImageIcon("src/answer15.png");
	
	class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				try {
					Object obcm = null;
					String msg = null;
					ChatMsg cm;
					Vector nick=null;
					
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						msg = String.format("[%s]\n%s", cm.UserName, cm.data);
						
					} else
						continue;
					
				
					switch (cm.code) {
					
					case "101":
						
						//String arr=null;
						Pattern p=Pattern.compile("\\{(.*?)\\}");
						Matcher m=p.matcher(cm.data);
						while(m.find()) {
							arr=m.group(1);
						}
						
						String array[]=arr.split(",");
						String arr1[]=array[0].split("=");
						String arr2[]=array[1].split("=");
						String arr3[]=array[2].split("=");
						String arr4[]=array[3].split("=");
						
						NickName1.setText(arr1[0]);
						NickName2.setText(arr2[0]);
						NickName3.setText(arr3[0]);
						NickName4.setText(arr4[0]);
						
						ImageIcon ii;
						ii = new ImageIcon("src/pp1.jpg");
						ii.getImage().flush();
						User1.setIcon(ii);
						ii = new ImageIcon("src/pp2.jpg");
						ii.getImage().flush();
						User2.setIcon(ii);
						ii = new ImageIcon("src/pp3.jpg");
						ii.getImage().flush();
						User3.setIcon(ii);
						ii = new ImageIcon("src/pp4.jpg");
						ii.getImage().flush();
						User4.setIcon(ii);
						
						//Music introMusic = new Music("src/CandyLand.mp3", true);
						//introMusic.start();
						
				
					case "200": // chat message
						if (cm.UserName.equals(UserName))
							AppendTextR(msg); 
						else
							AppendText(msg);
						break;
						
					case "300": // Image 첨占쏙옙
						if (cm.UserName.equals(UserName))
							AppendTextR("[" + cm.UserName + "]");
						else
							AppendText("[" + cm.UserName + "]");
						AppendImage(cm.img);
						break;
						
					case "500": // Mouse Event
						DoMouseEvent(cm);
						break;
						
					case "600": 	//게임시작
						cm.gameSet=true;
						gc2.setColor(Color.WHITE);
						gc2.fillRect(1, 1, panel.getWidth(), panel.getHeight()-2);
						repaint();
						AppendText(msg);	
						break;
					
					case "700":	//차례
						if (cm.UserName.equals(UserName))		//출제자
						{
							AppendTextR("당신의 차례입니다. 그림을 그리세요.");
							AppendTextR(msg);
							myanswer.setText(cm.data);
							if(cm.data.matches("카레")) {
								AppendImage2(answer1icon);
							}
							else if(cm.data.matches("잠실")) {
								AppendImage2(answer2icon);
							}
							else if(cm.data.matches("김경호")) {
								AppendImage2(answer3icon);
							}
							else if(cm.data.matches("스타벅스")) {
								AppendImage2(answer4icon);
							}
							else if(cm.data.matches("퇴학")) {
								AppendImage2(answer5icon);
							}
							else if(cm.data.matches("원빈")) {
								AppendImage2(answer6icon);
							}
							else if(cm.data.matches("아이패드")) {
								AppendImage2(answer7icon);
							}
							else if(cm.data.matches("노래방")) {
								AppendImage2(answer8icon);
							}
							else if(cm.data.matches("크리스마스")) {
								AppendImage2(answer9icon);
							}
							else if(cm.data.matches("종강")) {
								AppendImage2(answer10icon);
							}
							else if(cm.data.matches("닭발")) {
								AppendImage2(answer11icon);
							}
							else if(cm.data.matches("물먹는하마")) {
								AppendImage2(answer12icon);
							}
							else if(cm.data.matches("오리발")) {
								AppendImage2(answer13icon);
							}
							else if(cm.data.matches("바위섬")) {
								AppendImage2(answer14icon);
							}
							else if(cm.data.matches("가로수")) {
								AppendImage2(answer15icon);
							}
						
							
						}
						else {
							AppendText("출제자는 "+cm.UserName+"입니다.");
							myanswer.setText("???");
						}
						break;
						
						
						
					case "800":	//문제맞춤.
							
							AppendText(cm.UserName+"님이 문제를 맞췄습니다.");
							AppendText("정답은 "+cm.data+"입니다.");
							
							
							if(cm.UserName.equals(UserName)) {
								p1=Integer.parseInt(myPoint.getText());
								p1++;
								String po1=String.valueOf(p1);
								myPoint.setText(po1);
								
							}
					
							myanswer.setText(cm.data);
							try {
								if(cm.data.matches("카레")) {
									AppendImage2(answer1icon);
								}
								else if(cm.data.matches("잠실")) {
									AppendImage2(answer2icon);
								}
								else if(cm.data.matches("김경호")) {
									AppendImage2(answer3icon);
								}
								else if(cm.data.matches("스타벅스")) {
									AppendImage2(answer4icon);
								}
								else if(cm.data.matches("퇴학")) {
									AppendImage2(answer5icon);
								}
								else if(cm.data.matches("원빈")) {
									AppendImage2(answer6icon);
								}
								else if(cm.data.matches("아이패드")) {
									AppendImage2(answer7icon);
								}
								else if(cm.data.matches("노래방")) {
									AppendImage2(answer8icon);
								}
								else if(cm.data.matches("크리스마스")) {
									AppendImage2(answer9icon);
								}
								else if(cm.data.matches("종강")) {
									AppendImage2(answer10icon);
								}
								else if(cm.data.matches("닭발")) {
									AppendImage2(answer11icon);
								}
								else if(cm.data.matches("물먹는하마")) {
									AppendImage2(answer12icon);
								}
								else if(cm.data.matches("오리발")) {
									AppendImage2(answer13icon);
								}
								else if(cm.data.matches("바위섬")) {
									AppendImage2(answer14icon);
								}
								else if(cm.data.matches("가로수")) {
									AppendImage2(answer15icon);
								}
								Thread.sleep(3000);
								
								
						 }catch(InterruptedException ie){}
							if(p1==3) {
								SendObject(new ChatMsg(UserName,"900","3점"));
								
							}else
								SendObject(new ChatMsg(UserName,"700","다음 문제"));
					
							gc2.setColor(Color.WHITE);
							gc2.fillRect(1, 1, panel.getWidth(), panel.getHeight()-2);
							repaint();
							
						break;
					
					case "900":		//게임종료
						AppendText(cm.UserName+"님이 먼저 3점을 얻어 승리하였습니다.");
						ImageIcon gameovericon=new ImageIcon("src/bobgameover.png");
						AppendImage2(gameovericon);
						
						ImageIcon ii2;
						if(cm.UserName.matches(NickName1.getText())) {
							NickName1.setForeground(Color.ORANGE);
						}
						else if(cm.UserName.matches(NickName2.getText())) {
							
							NickName2.setForeground(Color.ORANGE);
						}
						else if(cm.UserName.matches(NickName3.getText())) {
							
							NickName3.setForeground(Color.ORANGE);
						}
						else if(cm.UserName.matches(NickName4.getText())) {
							
							NickName4.setForeground(Color.ORANGE);
						}
						
						break;
					}
						
					
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
//						dos.close();
//						dis.close();
						ois.close();
						oos.close();
						socket.close();

						break;
					} catch (Exception ee) {
						break;
					} // catch占쏙옙 占쏙옙
				} // 占쌕깍옙 catch占쏙옙占쏙옙

			}
		}
	}

	
	// Mouse Event 占쏙옙占쏙옙 처占쏙옙
	public void DoMouseEvent(ChatMsg cm) {
		Color c;
		c=cm.pen_color;
		gc2.setColor(c);
		Graphics2D g2=(Graphics2D) gc2;
		
		if(cm.pen==1) {
			gc2.fillOval(cm.mouse_e.getX() - pen_size/2, cm.mouse_e.getY() - cm.pen_size/2, cm.pen_size, cm.pen_size);
		}
		else if(cm.pen==2) {
			g2.setStroke(new BasicStroke(cm.pen_size));		
			g2.drawLine(cm.firstX,cm.firstY,cm.lastX,cm.lastY);
		}
		else if(cm.pen==3) {
			gc2.fillRect(cm.mouse_e.getX(), cm.mouse_e.getY(), cm.pen_size,cm.pen_size);
		}
		else if(cm.pen==4) {
			
			gc2.drawOval(cm.mouse_e.getX() - pen_size/2, cm.mouse_e.getY() - cm.pen_size/2, cm.pen_size, cm.pen_size);
		}
		else if(cm.pen==5) {
			gc2.setColor(Color.WHITE);
			g2.setStroke(new BasicStroke(cm.pen_size));		
			g2.drawLine(cm.firstX,cm.firstY,cm.lastX,cm.lastY);
		}
		else if(cm.pen==6) {
			gc2.setColor(Color.WHITE);
			gc2.fillRect(1, 1, panel.getWidth(), panel.getHeight()-2);
			//gc2.clearRect(0, 0, panel.getWidth(),panel.getHeight());
			repaint();
		}
		gc.drawImage(panelImage, 0, 0, panel);
	}

	public void SendMouseEvent(MouseEvent e) {
		ChatMsg cm = new ChatMsg(UserName, "500", "MOUSE");
		cm.mouse_e = e;
		cm.pen_size = pen_size;
		cm.pen=pen;
		cm.pen_color=pen_color;
		cm.firstX=firstX;
		cm.firstY=firstY;
		cm.lastX=lastX;
		cm.lastY=lastY;
		
		SendObject(cm);
	}

	
	
	class MyMouseWheelEvent implements MouseWheelListener {
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			// TODO Auto-generated method stub
			if (e.getWheelRotation() < 0) { 
				if (pen_size < 20)
					pen_size++;
			} else {
				if (pen_size > 2)
					pen_size--;
			}
			
		}
	}
	// Mouse Event Handler
	class MyMouseEvent implements MouseListener, MouseMotionListener {
		Graphics2D g2=(Graphics2D) gc2;
		@Override
		public void mouseDragged(MouseEvent e) {
			
			Color c=pen_color;
			gc2.setColor(c);
			
			if(pen==1) {
				gc2.fillOval(e.getX()-pen_size/2, e.getY()-pen_size/2, pen_size, pen_size);
			}
			else if(pen==2) {
				lastX=e.getX();
				lastY=e.getY();
				g2.setStroke(new BasicStroke(pen_size));
				g2.drawLine(firstX,firstY, lastX, lastY);
			}
			else if(pen==3) {
				gc2.fillRect(e.getX(),e.getY(),pen_size,pen_size);
			}
			else if(pen==4) {
				gc2.drawOval(e.getX()-pen_size/2,e.getY()-pen_size/2,pen_size,pen_size);
			}
			else if(pen==5) {
				gc2.setColor(Color.WHITE);
				lastX=e.getX();
				lastY=e.getY();
				g2.setStroke(new BasicStroke(pen_size));
				g2.drawLine(firstX,firstY, lastX, lastY);
			}
			else if(pen==6) {
				gc2.setColor(Color.WHITE);
				gc2.fillRect(1, 1, panel.getWidth(), panel.getHeight()-2);
				//gc2.clearRect(0, 0, panel.getWidth(),panel.getHeight());
				repaint();
			}
			
			SendMouseEvent(e);
			firstX=lastX;
			firstY=lastY;
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			
			Color c=pen_color;
			gc2.setColor(c);
			
			if(pen==1) {
				gc2.fillOval(e.getX()-pen_size/2, e.getY()-pen_size/2, pen_size, pen_size);
				}
				else if(pen==2) {
					g2.setStroke(new BasicStroke(pen_size));
					g2.drawLine(e.getX(), e.getY(), e.getX(), e.getY());
					
				}
				else if(pen==3) {
					gc2.fillRect(e.getX(),e.getY(),pen_size,pen_size);
				}
				else if(pen==4) {
					gc2.drawOval(e.getX()-pen_size/2,e.getY()-pen_size/2,pen_size,pen_size);
				}
				else if(pen==5) {
					gc2.setColor(Color.WHITE);
					lastX=e.getX();
					lastY=e.getY();
					g2.setStroke(new BasicStroke(pen_size));
					g2.drawLine(firstX, firstY, lastX, lastY);
					firstX=lastX;
					firstY=lastY;
				}
				else if(pen==6) {
					gc2.setColor(Color.WHITE);
					gc2.fillRect(1, 1, panel.getWidth(), panel.getHeight()-2);
					//gc2.clearRect(0, 0, panel.getWidth(),panel.getHeight());
					
					repaint();
				}
			gc.drawImage(panelImage, 0, 0, panel);
			SendMouseEvent(e);
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
			if(pen==2 || pen==5) {
				firstX=e.getX();
				firstY=e.getY();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
		}
	}

	// keyboard enter key 치면 서버로 전송
		class TextSendAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Send button을 누르거나 메시지 입력하고 Enter key 치면
				if (e.getSource() == btnSend || e.getSource() == txtInput) {
					String msg = null;
					// msg = String.format("[%s] %s\n", UserName, txtInput.getText());
					msg = txtInput.getText();
					SendMessage(msg);
					txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
					txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
					if (msg.contains("/exit")) // 종료 처리
						System.exit(0);
				}
			}
		}
	
		class GameStartAction implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnGameStart) {
					ChatMsg obcm = new ChatMsg(UserName, "600", "게임 시작");
					obcm.gameSet=true;
					SendObject(obcm);
					
					//gc2.drawImage(img,0,0,panel);
				
				}
			}
		}

		class ImageSendAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 액션 이벤트가 sendBtn일때 또는 textField 에세 Enter key 치면
				if (e.getSource() == imgBtn) {
					frame = new Frame("이미지첨부");
					fd = new FileDialog(frame, "이미지 선택", FileDialog.LOAD);
					// frame.setVisible(true);
					// fd.setDirectory(".\\");
					fd.setVisible(true);
					// System.out.println(fd.getDirectory() + fd.getFile());
					if (fd.getDirectory().length() > 0 && fd.getFile().length() > 0) {
						ChatMsg obcm = new ChatMsg(UserName, "300", "IMG");
						ImageIcon img = new ImageIcon(fd.getDirectory() + fd.getFile());
						obcm.img = img;
						SendObject(obcm);
					}
				}
			}
		}
		
		
	

	//ImageIcon icon1 = new ImageIcon("src/icon1.jpg");
	ImageIcon icon1=new ImageIcon("src/answer1.jpg");

	public synchronized void AppendIcon(ImageIcon icon) {
		int len = textArea.getDocument().getLength();
		// 끝으로 이동
		textArea.setCaretPosition(len);
		textArea.insertIcon(icon);
	}
	
	
	
	// 화면에 출력
	public synchronized void AppendText(String msg) {
		// textArea.append(msg + "\n");
		// AppendIcon(icon1);
		msg = msg.trim(); // 占쌌듸옙 blank占쏙옙 \n占쏙옙 占쏙옙占쏙옙占싼댐옙.
		int len = textArea.getDocument().getLength();
		// 占쏙옙占쏙옙占쏙옙 占싱듸옙
		//textArea.setCaretPosition(len);
		//textArea.replaceSelection(msg + "\n");
		
		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		StyleConstants.setForeground(left, Color.BLACK);
	    doc.setParagraphAttributes(doc.getLength(), 1, left, false);
		try {
			doc.insertString(doc.getLength(), msg+"\n", left );
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public synchronized void AppendTextR(String msg) {
		msg = msg.trim(); 	
		StyledDocument doc = textArea.getStyledDocument();
		SimpleAttributeSet right = new SimpleAttributeSet();
		StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
		StyleConstants.setForeground(right, Color.BLUE);	
	    doc.setParagraphAttributes(doc.getLength(), 1, right, false);
		try {
			doc.insertString(doc.getLength(),msg+"\n", right );
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void AppendImage(ImageIcon ori_icon) {
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len); // place caret at the end (with no selection)
		Image ori_img = ori_icon.getImage();
		Image new_img;
		ImageIcon new_icon;
		int width, height;
		double ratio;
		width = ori_icon.getIconWidth();
		height = ori_icon.getIconHeight();
		// Image가 너무 크면 최대 가로 또는 세로 200 기준으로 축소시킨다.
		if (width > 200 || height > 200) {
			if (width > height) { // 가로사진
				ratio = (double) height / width;
				width = 200;
				height = (int) (width * ratio);
			} else { // 세로사진
				ratio = (double) width / height;
				height = 200;
				width = (int) (height * ratio);
			}
			new_img = ori_img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			new_icon = new ImageIcon(new_img);
			textArea.insertIcon(new_icon);
		} else {
			textArea.insertIcon(ori_icon);
			new_img = ori_img;
		}
		len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		textArea.replaceSelection("\n");
		// ImageViewAction viewaction = new ImageViewAction();
		// new_icon.addActionListener(viewaction); 
		gc2.drawImage(ori_img, 0, 0, panel.getWidth(), panel.getHeight(), panel);
		gc.drawImage(panelImage, 0, 0, panel.getWidth(), panel.getHeight(), panel);
	}
	public synchronized void AppendImage2(ImageIcon ori_icon) {
		Image ori_img = ori_icon.getImage();
		Image new_img;
		ImageIcon new_icon;
		int width, height;
		double ratio;
		width = ori_icon.getIconWidth();
		height = ori_icon.getIconHeight();
		// Image가 너무 크면 최대 가로 또는 세로 200 기준으로 축소시킨다.
		if (width > 200 || height > 200) {
			if (width > height) { // 가로사진
				ratio = (double) height / width;
				width = 200;
				height = (int) (width * ratio);
			} else { // 세로사진
				ratio = (double) width / height;
				height = 200;
				width = (int) (height * ratio);
			}
			new_img = ori_img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			new_icon = new ImageIcon(new_img);
			
		} else {
			
			new_img = ori_img;
		}
		gc2.drawImage(ori_img, 0, 0, panel.getWidth(), panel.getHeight(), panel);
		gc.drawImage(panelImage, 0, 0, panel.getWidth(), panel.getHeight(), panel);
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
			System.exit(0);
		}
		for (i = 0; i < bb.length; i++)
			packet[i] = bb[i];
		return packet;
	}

	// Server에게 network으로 전송
	public synchronized void SendMessage(String msg) {
		try {
			// dos.writeUTF(msg);
//			byte[] bb;
//			bb = MakePacket(msg);
//			dos.write(bb, 0, bb.length);
			ChatMsg obcm = new ChatMsg(UserName, "200", msg);
			oos.writeObject(obcm);
		} catch (IOException e) {
			// AppendText("dos.write() error");
			AppendText("oos.writeObject() error");
			try {
//				dos.close();
//				dis.close();
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}

	public synchronized void SendObject(Object ob) {	// 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			// textArea.append("메시지 송신 에러!!\n");
			AppendText("SendObject Error");
		}
	}
}
