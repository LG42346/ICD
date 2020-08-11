package server;

import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Info {
	
	private Socket socket;
	private String name;
	private int number;
	private String email;
	private String date;
	private Semaphore semaphore;
	private long loginTime;
	
	public Info(Socket socket, Semaphore semaphore) {
		this.socket = socket;
		this.semaphore = semaphore;
		
		loginTime = System.currentTimeMillis();
	}

	public Semaphore getSemaphore() {
		return semaphore;
	}
	
	public void releaseSemaphore() {
		semaphore.release();
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setDate(String date) {
		this.date = date;
	}

	public Socket getSocket() {
		return socket;
	}

	public String getName() {
		return name;
	}

	public int getNumber() {
		return number;
	}
	
	public String getEmail() {
		return email;
	}

	public String getDate() {
		return date;
	}
	
	public void setSemaphore(Semaphore semaphore) {
		this.semaphore = semaphore;
	}

	public long getLoginTime() {
		return loginTime;
	}
	
	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}
}
