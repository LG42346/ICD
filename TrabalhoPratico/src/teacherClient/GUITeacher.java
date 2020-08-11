package teacherClient;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import server.Info;
import server.Stats;
import tools.Constants;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;

public class GUITeacher  extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private teacherVariables v;

	private JPanel panel;

	private JLabel lblThemes;
	private JLabel lblStudents;
	private JLabel lblSelected;
	
	private JButton btnSci;
	private JButton btnCul;
	private JButton btnGeo;
	private JButton btnSendOne;
	private JButton btnSendRandom;
	private JButton btnSendAll;

	private DefaultListModel<String> modelQ;
	private DefaultListModel<String> modelS;
	private JList<String> listQ;
	private JList<String> listS;
	private JScrollPane scrollQ;
	private JScrollPane scrollS;

	private JTextPane textAreaQ;

	private JScrollPane scrollAreaQ;
	private JButton btnRefresh;
	
	private String theme;
	private String themeSelected;
	private String questionNumber;

	private JLayeredPane stats;
	private JLabel lblTotalQuestions;
	private JLabel lblTotalAnswers;
	private JLabel lblTotalCorrectAnswers;
	private JLabel lblTotalWrongAnswers;
	
	private JLabel totalNumberQuestions;
	private JLabel totalNumberAnswers;
	private JLabel totalCorrectAnswers;
	private JLabel totalWrongAnswers;

	private JLabel lblErrorMessage;

	
//	public static void main(String[] args) {
//		GUITeacher gui = new GUITeacher(new teacherVariables(), null);
//	}
	
	/**
	 * Create the application.
	 */
	public GUITeacher(teacherVariables v, Socket sock) {
		this.v = v;
		
		setTitle("Professor");
		
		theme = "";
		themeSelected = "";
		questionNumber = "";
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				v.setEnd(true);
				try {
					sock.close();
				} catch (IOException e) {
					System.err.println("Class GUITeacher -> Teacher Socket Closed");
				}
			}
		});
		
        try {
            // Load the background image
            BufferedImage img = ImageIO.read(new File("WebContent" + File.separator + "photos" + File.separator + "teacherbg.jpg"));
            
            setBounds(100, 100, 621, 665);
    		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    		getContentPane().setLayout(null);
                      
            setContentPane(new JLabel(new ImageIcon(img)));

            panel = new JPanel();
    		panel.setBounds(30, 30, 545, 565);
    		getContentPane().add(panel);
    		panel.setLayout(null);
    		
    		lblThemes = new JLabel("Temas");
    		lblThemes.setFont(new Font("Tahoma", Font.BOLD, 12));
    		lblThemes.setHorizontalAlignment(SwingConstants.CENTER);
    		lblThemes.setBounds(115, 25, 46, 14);
    		panel.add(lblThemes);
    		
    		btnSci = new JButton("Ci\u00EAncias");
    		btnSci.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				
    				theme = Constants.scienceFileName;
    				v.setTheme(theme);
    			}
    		});
    		btnSci.setBackground(Color.GREEN);
    		btnSci.setBounds(15, 50, 83, 31);
    		panel.add(btnSci);
    		
    		btnCul = new JButton("Geografia");
    		btnCul.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				
    				theme = Constants.geographyFileName;
    				v.setTheme(theme);
    			}
    		});
    		btnCul.setBackground(Color.CYAN);
    		btnCul.setBounds(101, 50, 89, 31);
    		panel.add(btnCul);
    		
    		btnGeo = new JButton("Cultura");
    		btnGeo.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				
    				theme = Constants.cultureFileName;
    				v.setTheme(theme);
    				
    			}
    		});
    		btnGeo.setBackground(Color.YELLOW);
    		btnGeo.setBounds(193, 50, 75, 31);
    		panel.add(btnGeo);
    		
    		modelQ = new DefaultListModel<>();
    		listQ = new JList<>(modelQ);
    		listQ.setLayoutOrientation(JList.VERTICAL);
    		panel.add(listQ);
    		
    		scrollQ = new JScrollPane(listQ);
    		scrollQ.setBounds(15, 95, 253, 245);
    		panel.add(scrollQ);
    		
    		MouseListener mouseListener = new MouseAdapter() {
    		    public void mouseClicked(MouseEvent e) {
    		    	
    		    	int index = listQ.getSelectedIndex();
    		    	
    		    	questionNumber = "" + (index + 1);
    		    	System.out.println(questionNumber);
    		    	
    		    	textAreaQ.setText(modelQ.get(index));
    		    	
    		    	themeSelected = theme;
    		    }
    		};
    		listQ.addMouseListener(mouseListener);
    		
    		lblStudents = new JLabel("Alunos Online");
    		lblStudents.setHorizontalAlignment(SwingConstants.LEFT);
    		lblStudents.setFont(new Font("Tahoma", Font.BOLD, 12));
    		lblStudents.setBounds(285, 25, 112, 14);
    		panel.add(lblStudents);
    		
    		modelS = new DefaultListModel<>();
    		listS = new JList<>(modelS);
    		listS.setLayoutOrientation(JList.VERTICAL);
    		panel.add(listS);
    		
    		scrollS = new JScrollPane(listS);
    		scrollS.setBounds(285, 58, 245, 198);
    		panel.add(scrollS);
    		
    		btnSendOne = new JButton("Enviar para aluno");
    		btnSendOne.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				
    				String studentNumber = "";
    				
    				int indexS = listS.getSelectedIndex();
    				
    				if (indexS != -1) {
    					String student = modelS.get(indexS);
    					String[] splitted = student.split(" ");
    					studentNumber = splitted[splitted.length - 1];
    				}
    				
    				if (studentNumber != "" && questionNumber != "") {
    					v.sendQuestion(studentNumber, themeSelected, questionNumber);
    					lblErrorMessage.setText("");
    				}
    				else if (questionNumber == "") {
    					lblErrorMessage.setText("Escolha uma pergunta de um dos três temas disponivéis.");
    				}
    				else if (studentNumber == "") {
    					lblErrorMessage.setText("Escolha um aluno para enviar uma pergunta.");
    				}
    			}
    		});
    		btnSendOne.setBounds(320, 270, 176, 25);
    		panel.add(btnSendOne);
    		
    		btnSendRandom = new JButton("Envio aleat\u00F3rio");
    		btnSendRandom.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				
    				String studentNumber = "";
    				
    				int index = (int) (Math.random() * modelS.size());
    				
    				String student = modelS.get(index);
    				String[] splitted = student.split(" ");
    				studentNumber = splitted[splitted.length - 1];
    				
    				if (questionNumber != "") {
    					v.sendQuestion(studentNumber, themeSelected, questionNumber);
    					lblErrorMessage.setText("");
    				}
    				else {
    					lblErrorMessage.setText("Escolha uma pergunta de um dos três temas disponivéis.");
    				}
    			}
    		});
    		btnSendRandom.setBounds(320, 302, 176, 25);
    		panel.add(btnSendRandom);
    		
    		btnSendAll = new JButton("Enviar para todos");
    		btnSendAll.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				
    				if (questionNumber != "") {
    					v.sendQuestion("0", themeSelected, questionNumber);
    					lblErrorMessage.setText("");
    				}
    				else if (modelS.size() == 0) {
    					lblErrorMessage.setText("Ainda não estão alunos online.");
    				}
    				else {
    					lblErrorMessage.setText("Escolha uma pergunta de um dos três temas disponivéis.");
    				}
    			}
    		});
    		btnSendAll.setBounds(320, 334, 176, 25);
    		panel.add(btnSendAll);
    		
    		lblSelected = new JLabel("Pergunta Selecionada: ");
    		lblSelected.setFont(new Font("Tahoma", Font.PLAIN, 12));
    		lblSelected.setBounds(15, 355, 128, 14);
    		panel.add(lblSelected);
    		
    		textAreaQ = new JTextPane();
    		textAreaQ.setEditable(false);
    		panel.add(textAreaQ);
    		
    		scrollAreaQ = new JScrollPane(textAreaQ);
    		scrollAreaQ.setBounds(15, 375, 515, 30);
    		panel.add(scrollAreaQ);
    		
    		btnRefresh = new JButton("Atualizar presen\u00E7as");
    		btnRefresh.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent arg0) {
    				
    				v.setRefresh(true);
    				
    			}
    		});
    		btnRefresh.setBounds(376, 22, 154, 23);
    		panel.add(btnRefresh);    
    		
    		stats = new JLayeredPane();
    	   	stats.setBounds(15, 439, 515, 104);
    	   	stats.setBorder(BorderFactory.createTitledBorder(" Contabilidade das respostas "));
    	   	panel.add(stats);
    	   	
    	    lblTotalQuestions = new JLabel("N\u00FAmero de perguntas feitas:");
    	   	lblTotalQuestions.setBounds(20, 33, 167, 14);
    	   	stats.add(lblTotalQuestions);
    	   	
    	   	lblTotalAnswers = new JLabel("N\u00FAmero de respostas dadas:");
    	   	lblTotalAnswers.setBounds(20, 64, 167, 14);
    	   	stats.add(lblTotalAnswers);
    	   	
    	   	lblTotalCorrectAnswers = new JLabel("N\u00FAmero de respostas corretas:");
    	   	lblTotalCorrectAnswers.setBounds(278, 33, 180, 14);
    	   	stats.add(lblTotalCorrectAnswers);
    	   	
    	   	lblTotalWrongAnswers = new JLabel("N\u00FAmero de respostas erradas:");
    	   	lblTotalWrongAnswers.setBounds(278, 64, 180, 14);
    	   	stats.add(lblTotalWrongAnswers);
    	   	
    	    totalNumberQuestions = new JLabel("0");
    	   	totalNumberQuestions.setHorizontalAlignment(SwingConstants.CENTER);
    	   	totalNumberQuestions.setBounds(173, 33, 43, 14);
    	   	stats.add(totalNumberQuestions);
    	   	
    	   	totalNumberAnswers = new JLabel("0");
    	   	totalNumberAnswers.setHorizontalAlignment(SwingConstants.CENTER);
    	   	totalNumberAnswers.setBounds(173, 64, 43, 14);
    	   	stats.add(totalNumberAnswers);
    	   	
    	   	totalCorrectAnswers = new JLabel("0");
    	   	totalCorrectAnswers.setHorizontalAlignment(SwingConstants.CENTER);
    	   	totalCorrectAnswers.setBounds(445, 33, 43, 14);
    	   	stats.add(totalCorrectAnswers);
    	   	
    	   	totalWrongAnswers = new JLabel("0");
    	   	totalWrongAnswers.setHorizontalAlignment(SwingConstants.CENTER);
    	   	totalWrongAnswers.setBounds(445, 64, 43, 14);
    	   	stats.add(totalWrongAnswers);
    	   	
    	   	lblErrorMessage = new JLabel("");
    	   	lblErrorMessage.setForeground(Color.RED);
    	   	lblErrorMessage.setHorizontalAlignment(SwingConstants.CENTER);
    	   	lblErrorMessage.setBounds(15, 416, 515, 14);
    	   	panel.add(lblErrorMessage);
    		
            //pack();
            
            setVisible(true);
            
        } catch (IOException exp) {
            exp.printStackTrace();
        }
	}
	
	public void setListOfQuestions() {
		
		modelQ = (DefaultListModel<String>) listQ.getModel();
		modelQ.clear();
		ArrayList<String> questions = v.getQuestions();
		for (int i = 0; i < questions.size(); i++) { 
			modelQ.addElement(questions.get(i));
		}
		//listQ.ensureIndexIsVisible(modelQ.size() - 1);
	}
	
	public void refreshStudentsList() {
		
		modelS = (DefaultListModel<String>) listS.getModel();
		modelS.clear();
		ArrayList<Info> students = v.getStudentsList();
		for (int i = 0; i < students.size(); i++) { 
			modelS.addElement(students.get(i).getName() + " " + students.get(i).getNumber());
		}
		listS.ensureIndexIsVisible(modelS.size() - 1);
		
		if (modelS.size() == 0) {
			lblErrorMessage.setText("Ainda não estão alunos online.");
		}
		else {
			lblErrorMessage.setText("");
		}
	}
	
	public void RefreshStats(Hashtable<String, Integer> stats) {
		totalNumberQuestions.setText("" + stats.get(Stats.keyNumberQuestions));
		totalNumberAnswers.setText("" + stats.get(Stats.keyNumberAnswers));
		totalCorrectAnswers.setText("" + stats.get(Stats.keyNumberCorrects));
		totalWrongAnswers.setText("" + stats.get(Stats.keyNumberIncorrects));
	}
	
	public void enableButtons(boolean enable) {
		btnSendOne.setEnabled(enable);
		btnSendRandom.setEnabled(enable);
		btnSendAll.setEnabled(enable);
	}
}
