package studentClient;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import server.Stats;
import tools.myBase64;

import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import java.awt.Font;
import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUIStudent extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private studentVariables v;

	private JPanel panel;
	
	private ButtonGroup buttonGroup;
	private JRadioButton rdbtnAws1;
	private JRadioButton rdbtnAws2;
	private JRadioButton rdbtnAws3;
	private JRadioButton rdbtnAws4;
	private boolean multiple;
	
	private JTextPane questionArea;
	private JScrollPane scrollQ;
	
	private JLayeredPane photo;
	private JLabel label;
	private JLabel lblQuestion;
	private JButton btnAns;
	
	private int index = 0;
	private JLabel lblTime;

	private JLayeredPane stats;
	private JLabel lblTotalQuestions;
	private JLabel lblTotalAnswers;
	private JLabel lblTotalCorrectAnswers;
	private JLabel lblTotalWrongAnswers;

	private JLabel totalNumberQuestions;
	private JLabel totalNumberAnswers;
	private JLabel totalCorrectAnswers;
	private JLabel totalWrongAnswers;

	/**
	 * Create the application.
	 * @param sock 
	 */
	public GUIStudent(String title, studentVariables v) {		
		this.v = v;
		
		setTitle("Aluno " + title);
		
		multiple = false;
		buttonGroup = new ButtonGroup();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				v.setEnd(true);
//				try {
//					sock.close();
//				} catch (IOException e) {
//					System.err.println("Class GUIStudent -> Student Socket Closed");
//				}
			}
		});
		
		setBounds(100, 100, 621, 665);
    	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	
    	panel = new JPanel();
    	panel.setBorder(new EmptyBorder(5, 5, 5, 5));
    	setContentPane(panel);
    	panel.setLayout(null);
    	
    	rdbtnAws1 = new JRadioButton(studentVariables.WAITING);
    	rdbtnAws1.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			
    			if (!multiple) {
    				v.setAnswer(new ArrayList<>());
    			}
    			
    			v.addAnswer(rdbtnAws1.getText());
    		}
    	});
    	buttonGroup.add(rdbtnAws1);
    	rdbtnAws1.setFont(new Font("Tahoma", Font.PLAIN, 12));
    	rdbtnAws1.setBounds(16, 400, 274, 35);
    	panel.add(rdbtnAws1);
    		
    	rdbtnAws2 = new JRadioButton(studentVariables.WAITING);
    	rdbtnAws2.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			
    			if (!multiple) {
    				v.setAnswer(new ArrayList<>());
    			}
    			
    			v.addAnswer(rdbtnAws2.getText());
    		}
    	});
    	buttonGroup.add(rdbtnAws2);
    	rdbtnAws2.setFont(new Font("Tahoma", Font.PLAIN, 12));
    	rdbtnAws2.setBounds(313, 400, 274, 35);
    	panel.add(rdbtnAws2);
    	
    	rdbtnAws3 = new JRadioButton(studentVariables.WAITING);
    	rdbtnAws3.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			
    			if (!multiple) {
    				v.setAnswer(new ArrayList<>());
    			}
    			
    			v.addAnswer(rdbtnAws3.getText());
    		}
    	});
    	buttonGroup.add(rdbtnAws3);
    	rdbtnAws3.setFont(new Font("Tahoma", Font.PLAIN, 12));
    	rdbtnAws3.setBounds(16, 438, 274, 35);
    	panel.add(rdbtnAws3);
    	
    	rdbtnAws4 = new JRadioButton(studentVariables.WAITING);
    	rdbtnAws4.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			
    			if (!multiple) {
    				v.setAnswer(new ArrayList<>());
    			}
    			
    			v.addAnswer(rdbtnAws4.getText());
    		}
    	});
    	buttonGroup.add(rdbtnAws4);
    	rdbtnAws4.setFont(new Font("Tahoma", Font.PLAIN, 12));
    	rdbtnAws4.setBounds(313, 438, 274, 35);
    	panel.add(rdbtnAws4);
    	
    	photo = new JLayeredPane();
    	photo.setBounds(16, 83, 571, 318);
    	photo.setBorder(BorderFactory.createTitledBorder(" Clica na imagem para ver mais imagens relacionadas "));
    	panel.add(photo);
    		            
	    label = new JLabel((String) null);
	    label.addMouseListener(new MouseAdapter() {
			@Override
		   	public void mouseClicked(MouseEvent arg0) {
		   				
		   		index++;
		   		changeImage(index);
		   			
		   	}	
	    });
	    label.setIconTextGap(0);
	    label.setHorizontalAlignment(SwingConstants.CENTER);
	    label.setBounds(23, 17, 525, 288);
	    photo.add(label);
	    
	    lblQuestion = new JLabel("Pergunta: ");
	    lblQuestion.setFont(new Font("Tahoma", Font.BOLD, 12));
	    lblQuestion.setBounds(26, 20, 70, 14);
	    panel.add(lblQuestion);
	    
	    questionArea = new JTextPane();
	    questionArea.setText(studentVariables.WAITING);
	    questionArea.setEditable(false);
	    panel.add(questionArea);
	    
	    scrollQ = new JScrollPane(questionArea);
		scrollQ.setBounds(94, 15, 493, 45);
		panel.add(scrollQ);
	    	
	    btnAns = new JButton("Enviar Resposta");
	    btnAns.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (!v.getAnswer().isEmpty()) {
	    			v.setAnswered(true);
	    			reset();
	    		}
	    	}
	    });
	    btnAns.setBackground(new Color(204, 255, 153));
	    btnAns.setBounds(224, 480, 137, 31);
	   	panel.add(btnAns);
	   	
	   	lblTime = new JLabel("Tens 1 minuto para responder a cada pergunta que o professor enviar.");
	   	lblTime.setFont(new Font("Tahoma", Font.PLAIN, 9));
	   	lblTime.setHorizontalAlignment(SwingConstants.RIGHT);
	   	lblTime.setBounds(63, 64, 524, 14);
	   	panel.add(lblTime);
	   	
	   	stats = new JLayeredPane();
	   	stats.setBounds(16, 522, 571, 93);
	   	stats.setBorder(BorderFactory.createTitledBorder(" Contabilidade das respostas "));
	   	panel.add(stats);
	   	
	    lblTotalQuestions = new JLabel("N\u00FAmero de perguntas feitas:");
	   	lblTotalQuestions.setBounds(46, 26, 167, 14);
	   	stats.add(lblTotalQuestions);
	   	
	   	lblTotalAnswers = new JLabel("N\u00FAmero de respostas dadas:");
	   	lblTotalAnswers.setBounds(46, 57, 167, 14);
	   	stats.add(lblTotalAnswers);
	   	
	   	lblTotalCorrectAnswers = new JLabel("N\u00FAmero de respostas corretas:");
	   	lblTotalCorrectAnswers.setBounds(320, 26, 181, 14);
	   	stats.add(lblTotalCorrectAnswers);
	   	
	   	lblTotalWrongAnswers = new JLabel("N\u00FAmero de respostas erradas:");
	   	lblTotalWrongAnswers.setBounds(320, 57, 181, 14);
	   	stats.add(lblTotalWrongAnswers);
	   	
	    totalNumberQuestions = new JLabel("0");
	   	totalNumberQuestions.setHorizontalAlignment(SwingConstants.CENTER);
	   	totalNumberQuestions.setBounds(197, 26, 43, 14);
	   	stats.add(totalNumberQuestions);
	   	
	   	totalNumberAnswers = new JLabel("0");
	   	totalNumberAnswers.setHorizontalAlignment(SwingConstants.CENTER);
	   	totalNumberAnswers.setBounds(197, 57, 43, 14);
	   	stats.add(totalNumberAnswers);
	   	
	   	totalCorrectAnswers = new JLabel("0");
	   	totalCorrectAnswers.setHorizontalAlignment(SwingConstants.CENTER);
	   	totalCorrectAnswers.setBounds(484, 26, 43, 14);
	   	stats.add(totalCorrectAnswers);
	   	
	   	totalWrongAnswers = new JLabel("0");
	   	totalWrongAnswers.setHorizontalAlignment(SwingConstants.CENTER);
	   	totalWrongAnswers.setBounds(484, 57, 43, 14);
	   	stats.add(totalWrongAnswers);
		
	    activateButtons(false);
	    		
	    setVisible(true);
	
	}
	
	private void changeImage(int index) {
		
		ArrayList<String> photos = v.getPhotos();
		int img = index % photos.size();
		
		try {
			
			byte[] bytes64 = myBase64.decode(photos.get(img));
			
//			BufferedImage original = ImageIO.read(new File(photos.get(img)));
			BufferedImage original = ImageIO.read(new ByteArrayInputStream(bytes64));
			
			int width = 520;
			int height = 300;
			BufferedImage resized = new BufferedImage(width, height, original.getType());
            Graphics2D g = resized.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(original, 0, 0, width, height, 0, 0, original.getWidth(), original.getHeight(), null);
            g.dispose();
            
            label.setIcon(new ImageIcon(resized));
            
		} catch (IOException e) {
			System.err.println("Error GUIStudent Buffered Image");
			e.printStackTrace();
		}
		
	}
	
//	public static void main(String[] args) {
//		GUIStudent gui = new GUIStudent("b", null, null);
//	}

	public void showQuestionInfo() {
		
		String question = v.getQuestion();
		questionArea.setText(question);
		
		index = 0;
		changeImage(index);
		
		ArrayList<String> answers = v.getAnswers();
		Collections.shuffle(answers);
		
		rdbtnAws1.setText(answers.get(0));
		rdbtnAws2.setText(answers.get(1));
		rdbtnAws3.setText(answers.get(2));
		rdbtnAws4.setText(answers.get(3));
		
		multiple = v.isMultipleCorrects();
		
		if (multiple) {
			buttonGroup.remove(rdbtnAws1);
		    buttonGroup.remove(rdbtnAws2);
		    buttonGroup.remove(rdbtnAws3);
		    buttonGroup.remove(rdbtnAws4);
		}
		else {
			buttonGroup.add(rdbtnAws1);
		    buttonGroup.add(rdbtnAws2);
		    buttonGroup.add(rdbtnAws3);
		    buttonGroup.add(rdbtnAws4);
		}
		
		activateButtons(true);
		
	}
	
	public void reset() {
		
		v.setQuestion(studentVariables.WAITING);
		questionArea.setText(studentVariables.WAITING);
		
		label.setIcon(null);
		ArrayList<String> photos = new ArrayList<>();
		v.setPhotos(photos);
		
		ArrayList<String> answers = new ArrayList<>();
		answers.add(studentVariables.WAITING);
		answers.add(studentVariables.WAITING);
		answers.add(studentVariables.WAITING);
		answers.add(studentVariables.WAITING);
		
		v.setAnswers(answers);
		
		rdbtnAws1.setText(answers.get(0));
		rdbtnAws2.setText(answers.get(1));
		rdbtnAws3.setText(answers.get(2));
		rdbtnAws4.setText(answers.get(3));
		
		activateButtons(false);
		
	}
	
	public void RefreshStats(Hashtable<String, Integer> stats) {
		totalNumberQuestions.setText("" + stats.get(Stats.keyNumberQuestions));
		totalNumberAnswers.setText("" + stats.get(Stats.keyNumberAnswers));
		totalCorrectAnswers.setText("" + stats.get(Stats.keyNumberCorrects));
		totalWrongAnswers.setText("" + stats.get(Stats.keyNumberIncorrects));
	}
	
	private void activateButtons(boolean answered) {
		btnAns.setEnabled(answered);
		rdbtnAws1.setEnabled(answered);
		rdbtnAws2.setEnabled(answered);
		rdbtnAws3.setEnabled(answered);
		rdbtnAws4.setEnabled(answered);
		if (multiple) {
			rdbtnAws1.setSelected(false);
			rdbtnAws2.setSelected(false);
			rdbtnAws3.setSelected(false);
			rdbtnAws4.setSelected(false);
		}
		else {
			buttonGroup.clearSelection();
		}
	}
}
