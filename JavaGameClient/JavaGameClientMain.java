package com.tpgml.server;
// JavaObjClient.java
// ObjecStream ����ϴ� ä�� Client

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import java.awt.Font;
public class JavaGameClientMain extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUserName;
	private JTextField txtIpAddress;
	private JTextField txtPortNumber;
	private JLabel lblNewLabel_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaGameClientMain frame = new JavaGameClientMain();
					
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
	public JavaGameClientMain() {
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 600);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("User Name");
		lblNewLabel.setFont(new Font("Baloo", Font.PLAIN, 14));
		lblNewLabel.setBounds(138, 229, 82, 33);
		contentPane.add(lblNewLabel);
		
		txtUserName = new JTextField();
		txtUserName.setFont(new Font("Baloo", Font.PLAIN, 14));
		txtUserName.setHorizontalAlignment(SwingConstants.CENTER);
		txtUserName.setBounds(269, 229, 116, 33);
		contentPane.add(txtUserName);
		txtUserName.setColumns(10);
		
		JLabel lblIpAddress = new JLabel("IP Address");
		lblIpAddress.setFont(new Font("Baloo", Font.PLAIN, 14));
		lblIpAddress.setBounds(138, 303, 82, 33);
		contentPane.add(lblIpAddress);
		
		txtIpAddress = new JTextField();
		txtIpAddress.setFont(new Font("Baloo", Font.PLAIN, 14));
		txtIpAddress.setHorizontalAlignment(SwingConstants.CENTER);
		txtIpAddress.setText("127.0.0.1");
		txtIpAddress.setColumns(10);
		txtIpAddress.setBounds(269, 303, 116, 33);
		contentPane.add(txtIpAddress);
		
		JLabel lblPortNumber = new JLabel("Port Number");
		lblPortNumber.setFont(new Font("Baloo", Font.PLAIN, 14));
		lblPortNumber.setBounds(138, 382, 99, 33);
		contentPane.add(lblPortNumber);
		
		txtPortNumber = new JTextField();
		txtPortNumber.setFont(new Font("Baloo", Font.PLAIN, 14));
		txtPortNumber.setText("30000");
		txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortNumber.setColumns(10);
		txtPortNumber.setBounds(269, 382, 116, 33);
		contentPane.add(txtPortNumber);
		
		JButton btnConnect = new JButton("Game Start");
		btnConnect.setFont(new Font("Baloo", Font.BOLD, 20));
		btnConnect.setBackground(new Color(255, 192, 203));
		btnConnect.setBounds(181, 465, 162, 48);
		contentPane.add(btnConnect);
		
		lblNewLabel_1 = new JLabel("Catch My Soul");
		lblNewLabel_1.setFont(new Font("Baloo", Font.BOLD, 40));
		lblNewLabel_1.setBackground(Color.WHITE);
		
		lblNewLabel_1.setBounds(138, 22, 284, 186);
		contentPane.add(lblNewLabel_1);
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setBounds(181, 303, 107, 21);
		contentPane.add(editorPane);
		Myaction action = new Myaction();
		btnConnect.addActionListener(action);
		txtUserName.addActionListener(action);
		txtIpAddress.addActionListener(action);
		txtPortNumber.addActionListener(action);
	}
	class Myaction implements ActionListener // ����Ŭ������ �׼� �̺�Ʈ ó�� Ŭ����
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String username = txtUserName.getText().trim();
			String ip_addr = txtIpAddress.getText().trim();
			String port_no = txtPortNumber.getText().trim();
			JavaGameClientView view = new JavaGameClientView(username, ip_addr, port_no);
			setVisible(false);
		}
	}
}


