package teacherClient;

import java.util.ArrayList;

import server.Info;

public class teacherVariables {
	
	private ArrayList<String> questions;
	private boolean end;
	private String theme;
	private boolean refresh;
	private ArrayList<Info> studentsList;
	
	private String studentNumberToSend;
	private String themeToSend;
	private String questionNumberToSend;
	private boolean questionSent;
	private String password;
	
	public teacherVariables() {
		questions = new ArrayList<>();
		end = false;
		theme = "";
		refresh = false;
		studentsList = new ArrayList<>();
		
		studentNumberToSend = "";
		themeToSend = "";
		questionNumberToSend = "";
		questionSent = false;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public ArrayList<String> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<String> questions) {
		this.questions = questions;
	}
	
	public boolean isEnd() {
		return end;
	}
	
	public void setEnd(boolean end) {
		this.end = end;	
	}
	
	public boolean isRefresh() {
		return refresh;
	}

	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}
	
	public ArrayList<Info> getStudentsList(){
		return studentsList;
	}

	public void setStudentsList(ArrayList<Info> studentsList) {
		this.studentsList = studentsList;
	}

	public void sendQuestion(String studentNumber, String themeSelected, String questionNumber) {
		this.studentNumberToSend = studentNumber;
		this.themeToSend = themeSelected;
		this.questionNumberToSend = questionNumber;
		setQuestionSent(true);
	}

	public String getStudentNumberToSend() {
		return studentNumberToSend;
	}

	public String getThemeToSend() {
		return themeToSend;
	}

	public String getQuestionNumberToSend() {
		return questionNumberToSend;
	}

	public boolean isQuestionSent() {
		return questionSent;
	}

	public void setQuestionSent(boolean questionSend) {
		this.questionSent = questionSend;
	}	
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
		
	}
	
	
}
