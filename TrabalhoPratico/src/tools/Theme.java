package tools;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Theme {
	
	Document D = null;

	/**
	 * construtor por omissao
	 */
	public Theme() {
		
	}
	
	
	public Theme(String themeDocName) {
		D = xmlUtil.parseFile(themeDocName);
	}
	
	
	public Document getDocument() {
		return D;
	}
	
	
	public String getQuestion(int questionNumber) {
		
		Element root = D.getDocumentElement();

		NodeList questions = root.getElementsByTagName("text");
		
		Node question = questions.item(questionNumber - 1);
		
		return question.getTextContent();
	}
	
	
	public ArrayList<String> getAllThemeQuestions(){
		
		ArrayList<String> allQuestions = new ArrayList<String>();
		
		Element root = D.getDocumentElement();
		
		NodeList questions = root.getElementsByTagName("text");
		
		for (int q = 0; q < questions.getLength(); q++) {
			allQuestions.add((q + 1) + " - " + questions.item(q).getTextContent());
		}
		
		return  allQuestions;
	}
	
	
	public ArrayList<String> getOptions(int questionNumber) {
		
		ArrayList<String> options = new ArrayList<String>();
		
		Element root = D.getDocumentElement();
		NodeList questions = root.getElementsByTagName("question");
		Node question = questions.item(questionNumber - 1);
		
		NodeList questionInfo = question.getChildNodes();
		for (int i = 0; i < questionInfo.getLength(); i++) {
		
			String node = questionInfo.item(i).getNodeName();
			
			if(node.equals("correct")) {
				options.add(questionInfo.item(i).getTextContent());
				options.add(questionInfo.item(i+1).getTextContent());
				options.add(questionInfo.item(i+2).getTextContent());
				options.add(questionInfo.item(i+3).getTextContent());
				break;
			}
		}
		return options;
	}
	
	public boolean isMultiple(int questionNumber) {
		
		ArrayList<String> options = new ArrayList<String>();
		
		Element root = D.getDocumentElement();
		NodeList questions = root.getElementsByTagName("question");
		Node question = questions.item(questionNumber - 1);
		
		NodeList questionInfo = question.getChildNodes();
		int corrects = 0;
		for (int i = 0; i < questionInfo.getLength(); i++) {
		
			String node = questionInfo.item(i).getNodeName();
			
			if(node.equals("correct")) {
				corrects++;
			}
		}
		return (corrects > 1);
	}

	
	public ArrayList<String> getImagesPaths(int questionNumber) {
		
		ArrayList<String> images = new ArrayList<String>();
		
		Element root = D.getDocumentElement();
		
		NodeList questions = root.getElementsByTagName("question");
		
		Node question = questions.item(questionNumber - 1);
		NodeList questionInfo = question.getChildNodes();
		
		for(int i = 0; i < questionInfo.getLength(); i++) {
			
			String nodeName = questionInfo.item(i).getNodeName();
			
			if (nodeName.equals("photo")) {
				
				NamedNodeMap attrs = questionInfo.item(i).getAttributes();
				
				for(int a = 0; a < attrs.getLength(); a++) {
					
					if (attrs.item(a).getNodeName().equals("path")) {
						
						String imagePath = attrs.item(a).getTextContent();
						
						File image = new File(imagePath);
						
						byte[] imageBytes = null;
						
						try {
							imageBytes = Files.readAllBytes(image.toPath());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						String image64 = myBase64.encode(imageBytes);
						
						images.add(image64);
					}
				}
			}
			else {
				break;
			}
		}
		
		return images;
	}
	
	public ArrayList<String> getCorrectAnswer(int questionNumber) {
		
		ArrayList<String> correctAnswers = new ArrayList<String>();
					
		Element root = D.getDocumentElement();
		
		NodeList questions = root.getElementsByTagName("question");
		
		Node question = questions.item(questionNumber - 1);
		NodeList questionInfo = question.getChildNodes();
		
		for(int i = 0; i < questionInfo.getLength(); i++) {
			
			String nodeName = questionInfo.item(i).getNodeName();
			
			if (nodeName.equals("correct")) {	
				String correctAnswer = questionInfo.item(i).getTextContent();
				correctAnswers.add(correctAnswer);
			}
			
		}
		return correctAnswers;
	}
	
	
	public static void main(String[] args) {
		
		Theme q = new Theme(Constants.cultureFileName);
				
		if (!xmlUtil.validateDocument(q.getDocument(), Constants.pathProtocolXSD))
			System.out.println("A mensagem não respeita o protocolo!");
		else
			System.out.println("A mensagem respeita o protocolo!");
		System.out.println();
		
		ArrayList<String> b = q.getAllThemeQuestions();
		
		for (int i = 0; i < b.size(); i++) {
			System.out.println(b.get(i));
		}
		
		System.out.println();
		int number = 1;
		System.out.println("Question: " + q.getQuestion(number));
		ArrayList<String> a = q.getOptions(number);
		System.out.println("[" + a.get(0) + ", " + a.get(1) + ", " + a.get(2) + ", " + a.get(3) + "]");
		
		
		ArrayList<String> c = q.getImagesPaths(number);
		
		for (int i = 0; i < c.size(); i++) {
			System.out.println(c.get(i));
		}
		
	}
	
}
