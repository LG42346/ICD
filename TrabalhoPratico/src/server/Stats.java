package server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.util.Hashtable;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import tools.Constants;
import tools.XMLReadWrite;

public class Stats implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String keyNumberQuestions = "questions";
	public static final String keyNumberAnswers = "answers";
	public static final String keyNumberCorrects = "corrects";
	public static final String keyNumberIncorrects = "incorrects";

	private Hashtable<String, Integer> stats;
	private int questions;
	private int answers;
	private int corrects;
	private int incorrects;

	private FileOutputStream fos;
	private ObjectOutputStream oos;
	private FileInputStream fis;
	private ObjectInputStream ois;

	public Stats() {
		stats = new Hashtable<>(4);
		readStats();
		
		if (stats.isEmpty()) {
			questions = new Integer(0);
			answers = new Integer(0);
			corrects = new Integer(0);
			incorrects = new Integer(0);
			refreshStats();
		}
		else {
			questions = stats.get(keyNumberQuestions);
			answers = stats.get(keyNumberAnswers);
			corrects = stats.get(keyNumberCorrects);
			incorrects = stats.get(keyNumberIncorrects);
		}
	}

	public void addQuestion() {
		questions++;
	}

	public void addAnswer() {
		answers++;
	}

	public void addCorrect() {
		corrects++;
	}

	public void addIncorrect() {
		incorrects++;
	}
	
	public void refreshStats() {
		stats.clear();
		stats.put(keyNumberQuestions, questions);
		stats.put(keyNumberAnswers, answers);
		stats.put(keyNumberCorrects, corrects);
		stats.put(keyNumberIncorrects, incorrects);

		try {
			fos = new FileOutputStream(Constants.statsDocName);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(stats);

			oos.close();
			fos.close();
		} catch (IOException e) {
			System.err.println("Serializable Class Stats: writing on doc.dat -> " + e.getMessage());
		}
	}

	public void readStats(){

		try {
			fis = new FileInputStream(Constants.statsDocName);
			ois = new ObjectInputStream(fis);

			Hashtable<String, Integer> readStats = (Hashtable<String, Integer>) ois.readObject();

			ois.close();
			fis.close();

			stats = readStats;

		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Serializable Class Stats: reading doc.dat -> " + e.getMessage());
		}
	}

	public Hashtable<String, Integer> getStats(){
		return stats;
	}

	public byte[] serialize() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(stats);
		return out.toByteArray();
	}

	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(obj);
		return out.toByteArray();
	}
	
	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
		return is.readObject();
	}
}
