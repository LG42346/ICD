package studentClient;

import java.util.ArrayList;

public class studentVariables {

	public static final String WAITING = "a enviar ...";
	private String question;
	private ArrayList<String> photos;
	private ArrayList<String> answers;
	private ArrayList<String> answer;
	private boolean answered;
	private boolean registered;
	private boolean multipleCorrects;
	private String name;
	private String number;
	private String email;
	private String date;
	private boolean end;
	private String password;
	
	public studentVariables() {
		question = WAITING;
		photos = new ArrayList<>();
		answers = new ArrayList<String>();
		for (int i = 0; i < 4; i++) {
			answers.add(WAITING);
		}
		answer = new ArrayList<>();
		answered = false;
		registered = false;
		multipleCorrects = false;
		name = "";
		number = "";
		email = "";
		date = "";
		end = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public ArrayList<String> getPhotos() {
		return photos;
	}

	public void setPhotos(ArrayList<String> photos) {
		this.photos = photos;
	}

	public ArrayList<String> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<String> answers) {
		this.answers = answers;
	}
	
	public ArrayList<String> getAnswer() {
		return answer;
	}
	
	public void setAnswer(ArrayList<String> answer) {
		this.answer = answer;
	}
	
	public void addAnswer (String ans) {
		answer.add(ans);
	}

	public boolean isAnswered() {
		return answered;
	}

	public void setAnswered(boolean answered) {
		this.answered = answered;
	}

	public boolean isEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

	public boolean isRegistered() {
		return registered;
	}
	
	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public boolean isMultipleCorrects() {
		return multipleCorrects;
	}
	
	public void setMultipleCorrects(boolean multipleCorrects) {
		this.multipleCorrects = multipleCorrects;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
		
	}
	
}
