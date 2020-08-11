package studentClient;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;

public class GUIRegistration extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panel;
	
	private JLabel lblRegistration;
	private JLabel lblName;
	private JLabel lblEmail;
	private JLabel lblDate;
	private JLabel lblNumber;
	
	private JTextField name;
	private JTextField email;
	private JTextField number;

	private JComboBox<String> day;
	private JComboBox<String> month;
	private JComboBox<String> year;
	
	private JButton login;
	
	private String monthSelected;
	private String yearSelected;
	private String daySelected;

	private JLabel lblErrorMessage;
	private JTextField password;

	/**
	 * Create the application.
	 */
	public GUIRegistration(studentVariables v) {
		
		setTitle("Entrar");
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				v.setEnd(true);
			}
		});
		
		try {
			
			BufferedImage img = ImageIO.read(new File("WebContent" + File.separator + "photos" + File.separator + "studentbg.jpg"));
			
			setBounds(100, 100, 470, 300);
			//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
	                  
	        setContentPane(new JLabel(new ImageIcon(img)));
	        
	        panel = new JPanel();
    		panel.setBounds(45, 50, 360, 170);
    		getContentPane().add(panel);
    		panel.setLayout(null);
    		
    		lblRegistration = new JLabel("Registo");
    		lblRegistration.setFont(new Font("Tahoma", Font.BOLD, 14));
    		lblRegistration.setBounds(200, 20, 60, 20);
    		getContentPane().add(lblRegistration);
    		
    		lblName = new JLabel("Nome: ");
    		lblName.setFont(new Font("Tahoma", Font.BOLD, 12));
    		lblName.setBounds(10, 29, 60, 14);
    		panel.add(lblName);
    		
    		lblNumber = new JLabel("N\u00FAmero:");
    		lblNumber.setFont(new Font("Tahoma", Font.BOLD, 12));
    		lblNumber.setBounds(230, 30, 71, 14);
    		panel.add(lblNumber);
    		
    		lblEmail = new JLabel("e-mail:");
    		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 12));
    		lblEmail.setBounds(10, 54, 60, 14);
    		panel.add(lblEmail);
    		
    		lblDate = new JLabel("Data de Nascimento:");
    		lblDate.setFont(new Font("Tahoma", Font.BOLD, 12));
    		lblDate.setBounds(10, 79, 149, 14);
    		panel.add(lblDate);
    		
    		name = new JTextField();
//    		name.setText("Luís");
    		name.setBounds(63, 27, 149, 20);
    		panel.add(name);
    		name.setColumns(10);
    		
    		number = new JTextField();
//    		number.setText("42346");
    		number.setBounds(296, 27, 54, 20);
    		panel.add(number);
    		number.setColumns(10);
    		
    		email = new JTextField();
//    		email.setText("a@b.c");
    		email.setBounds(63, 52, 287, 20);
    		panel.add(email);
    		email.setColumns(10);    		
    		
    		monthSelected = "1";
    		yearSelected = "2020";
    		daySelected = "1";
    		
    		day = new JComboBox<>();
    		day.setBounds(158, 77, 54, 20);
    		panel.add(day);
    		day.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				daySelected = (String) ((JComboBox) e.getSource()).getSelectedItem();
    		    }
    		});
    		
    		changeNumberOfDays(monthSelected, yearSelected);
    		
    		month = new JComboBox<>();
    		month.setBounds(222, 77, 47, 20);
    		panel.add(month);
    		month.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
    		        monthSelected = (String) ((JComboBox) e.getSource()).getSelectedItem();
    		        changeNumberOfDays(monthSelected, yearSelected);
    	    		
    		    }
    		});
    		
    		for (int i = 1; i <= 12; i++) {
    			month.addItem("" + i);
    		}
    		
    		year = new JComboBox<>();
    		year.setBounds(279, 77, 71, 20);
    		panel.add(year);    		
    		year.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
    		        yearSelected = (String) ((JComboBox) e.getSource()).getSelectedItem();     
    		        changeNumberOfDays(monthSelected, yearSelected);
    		    }
    		});
    		
    		for (int i = 2020; i > (2020 - 150); i--) {
    			year.addItem("" + i);
    		}    		
    		
    		lblErrorMessage = new JLabel("");
    		lblErrorMessage.setForeground(Color.RED);
    		lblErrorMessage.setHorizontalAlignment(SwingConstants.CENTER);
    		lblErrorMessage.setBounds(0, 111, 360, 14);
    		panel.add(lblErrorMessage);
    		
    		login = new JButton("Entrar");
    		login.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent arg0) { 
    				
    				String studentName = name.getText();
    				String studentNumber = number.getText();
    				String studentEmail = email.getText();
    				String studentPassword = password.getText();
    			
//    				if (validateName(studentName) && validateNumber(studentNumber) && validateEmail(studentEmail)) {
    				if (validateName(studentName) && validateNumber(studentNumber)) {	
    					v.setRegistered(true);
    					
    					String date = daySelected + "-" + monthSelected + "-" + yearSelected;
    					
    					v.setName(studentName);
    					v.setNumber(studentNumber);
    					v.setEmail("");
    					v.setDate(date);
    					v.setPassword(studentPassword);
    					
//	    				if (!validateName(studentName)) {
//	    					v.setName(s);
//	    					v.setNumber("12345");
//	    					v.setEmail("bomdia@tudo.bem");
//	    					v.setDate(date);
//	    				}
//	    				else if (validateNumber(studentNumber)){
//	    					v.setName(name.getText());
//	    					v.setNumber(studentNumber);
//	    					v.setEmail("bomdia@tudo.bem");
//	    					v.setDate(date);
//	    				}
//	    				else {
//	    					lblErrorMessage.setText("Preencha o campo com um número válido.");
//	    				}
    				}
    				
    				else if (!validateName(studentName)) {
    					lblErrorMessage.setText("Preencha o campo com um nome válido.");
    				}
    				else if (!validateNumber(studentNumber)) {
    					lblErrorMessage.setText("Preencha o campo com um número válido.");
    				}
//    				else if (!validateEmail(studentEmail)) {
//    					lblErrorMessage.setText("Preencha o campo com um email válido.");
//    				}
    			}
    		});
    		login.setFont(new Font("Tahoma", Font.PLAIN, 12));
    		login.setBounds(131, 136, 89, 23);
    		panel.add(login);
    		
    		password = new JTextField();
    		password.setColumns(10);
    		password.setBounds(63, 105, 149, 20);
    		panel.add(password);
    		
    		JLabel lblPassword = new JLabel("PW:");
    		lblPassword.setFont(new Font("Dialog", Font.BOLD, 12));
    		lblPassword.setBounds(10, 107, 60, 14);
    		panel.add(lblPassword);
    		
    		KeyListener keyListener = new KeyAdapter(){
    			public void keyPressed(KeyEvent e){
    			    if (e.getKeyCode() == KeyEvent.VK_ENTER){
    			    	String studentName = name.getText();
        				String studentNumber = number.getText();
        				String studentEmail = email.getText();
        				String studentPassword = password.getText();
        			
//        				if (validateName(studentName) && validateNumber(studentNumber) && validateEmail(studentEmail)) {
        				if (validateName(studentName) && validateNumber(studentNumber)) {	
        					v.setRegistered(true);
        					
        					String date = daySelected + "-" + monthSelected + "-" + yearSelected;
        					
        					v.setName(studentName);
        					v.setNumber(studentNumber);
        					v.setEmail("");
        					v.setDate(date);
        					v.setPassword(studentPassword);
        					
//    	    				if (!validateName(studentName)) {
//    	    					v.setName(s);
//    	    					v.setNumber("12345");
//    	    					v.setEmail("bomdia@tudo.bem");
//    	    					v.setDate(date);
//    	    				}
//    	    				else if (validateNumber(studentNumber)){
//    	    					v.setName(name.getText());
//    	    					v.setNumber(studentNumber);
//    	    					v.setEmail("bomdia@tudo.bem");
//    	    					v.setDate(date);
//    	    				}
//    	    				else {
//    	    					lblErrorMessage.setText("Preencha o campo com um número válido.");
//    	    				}
        				}
        				
        				else if (!validateName(studentName)) {
        					lblErrorMessage.setText("Preencha o campo com um nome válido.");
        				}
        				else if (!validateNumber(studentNumber)) {
        					lblErrorMessage.setText("Preencha o campo com um número válido.");
        				}
//        				else if (!validateEmail(studentEmail)) {
//        					lblErrorMessage.setText("Preencha o campo com um email válido.");
//        				}
    			    }
    			}
    		};
    		
    		name.addKeyListener(keyListener);
    		email.addKeyListener(keyListener);
    		number.addKeyListener(keyListener);
	        
	        setVisible(true);
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	private void changeNumberOfDays(String month, String year) {
		
		int numberDays = 31;
		switch(month) {
			case "4":
			case "6":
			case "9":
			case "11":
				numberDays = 30;
				break;
				
			case "2":
				int yearValue = Integer.parseInt(year);
				int leap = yearValue % 4;
				
				if (leap == 0) {
					numberDays = 28;
					break;
				}
				numberDays = 29;
				break;
				
			default:
				numberDays = 31;
		}
		
		for (int i = 1; i <= numberDays; i++) {
			day.addItem("" + i);
		}
	}
	
	private boolean validateName(String n) {
		
		String regex = "^\\p{L}[\\p{L} ]{0,}+$";
		
		Pattern pattern = Pattern.compile(regex);
		 
		Matcher matcher = pattern.matcher(n);
		
		return matcher.matches();
	}
	
	private boolean validateNumber(String n) {
		
		String regex = "^[0-9]{1,}+$";
		
		Pattern pattern = Pattern.compile(regex);
		 
		Matcher matcher = pattern.matcher(n);
		
		if (!matcher.matches()) {
			return matcher.matches();
		}
		
		int num = Integer.parseInt(n);
		
		if (num == 0) {
			return false;
		}
		
		return true;
	}
	
	private boolean validateEmail(String e) {
		
		String regex = "[a-zA-Z0-9.-_]+@[a-zA-Z0-9.]+[.][a-zA-Z0-9]+$";
		
		Pattern pattern = Pattern.compile(regex);
		 
		Matcher matcher = pattern.matcher(e);
		
		return matcher.matches();
	}
	
	
//	public static void main(String[] args) {
//		GUIRegistration gui = new GUIRegistration(new studentVariables());
//		
//	}

	public void sendMessage() {
		lblErrorMessage.setText("Este número de aluno já se encontra registado.");		
	}
}
