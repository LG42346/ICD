package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.Socket;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public final class XMLReadWrite {

	public static void main(String args[]) throws Exception {
		// exemplo
		System.out.println("Exemplo que usa inlcude do ficheiro 'outroDoc.xml'");
		Document doc = documentFromFile("WebContent" + File.separator + "exemplo" + File.separator + "doc.xml");
		writeDocument(doc, System.out);
	}

	// filtra as linhas no envio
	public static boolean documentToSocket(Document xmlDoc, Socket socket){
		try {
			PrintWriter os = new PrintWriter(socket.getOutputStream(), true);
			String xml = documentToString(xmlDoc);
			os.println(xml.replaceAll("\n|\r", "")); // descarta eventuais linhas
			return true;
		} catch (IOException e) {
			System.err.println("Error: Unable to write XML to socket!\n\t" + e);
			e.printStackTrace();
		}
		return false;
	}
	
	public static final String documentToString(Document xmlDoc) {
		Writer out = new StringWriter();
		try {
			Transformer tf = TransformerFactory.newInstance().newTransformer();
			tf.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1"); // "UTF-8"
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			tf.transform(new DOMSource(xmlDoc), new StreamResult(out));
		} catch (Exception e) {
			System.out.println("Error: Unable to write XML to string!\n\t" + e);
			e.printStackTrace();
		}
		return out.toString();
	}

	public static final Document documentFromSocket(Socket socket){
		Document xmlDoc = null;
		try {
			BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String xml = is.readLine();
			xmlDoc = documentFromString(xml); // sincronização com mudança de linha
		} catch (IOException e) {
			System.err.println("Error -> doc from socket : Unable to read XML from socket!");
			//System.err.println("Error: Unable to read XML from socket!\n\t" + e);
			//e.printStackTrace();
		}
		return xmlDoc;
	}
	
	
	
	public static final Document documentFromString(String strXML) {
		Document xmlDoc = null;
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			xmlDoc = builder.parse(new InputSource(new StringReader(strXML)));
		} catch (Exception e) {
			System.err.println("Error -> doc from socket: Unable to read XML from string!");
			//System.err.println("Error: Unable to read XML from string!\n\t" + e);
			//e.printStackTrace();
		}
		return xmlDoc;
	}

	public static void copy(InputStream input, OutputStream output) throws IOException {
		int DEFAULT_BUFFER_SIZE = 1024 * 4;
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
	}

	public static final Document documentFromFile(String inputFile) {
		try {
			return readDocument(new FileInputStream(inputFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final boolean documentToFile(Document input, String outputFile) {
		try {
			return writeDocument(input, new FileOutputStream(outputFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static final boolean writeDocument(Document input, OutputStream output) {
		try {
			DOMSource domSource = new DOMSource(input);
			StreamResult resultStream = new StreamResult(output);
			TransformerFactory transformFactory = TransformerFactory.newInstance();
			Transformer transformer = transformFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			transformer.transform(domSource, resultStream);
			return true;
		} catch (Exception e) {
			System.err.println("Error: Unable to write XML to stream!\n\t" + e);
			e.printStackTrace();
		}
		return false;
	}

	public static final Document readDocument(InputStream input) {
		Document xmlDoc = null;
		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			// enable XInclude processing
			factory.setNamespaceAware(true);
			factory.setXIncludeAware(true);

			DocumentBuilder parser = factory.newDocumentBuilder();
			xmlDoc = parser.parse(input);

		} catch (Exception e) {
			System.err.println("Error: Unable to read XML from stream!\n\t" + e);
			e.printStackTrace();
		}
		return xmlDoc;
	}

	// usando seriação
	public static final Document documentFromSSocket(Socket socket) {
		Document xmlDoc = null;
		ObjectInputStream iis;
		try {
			iis = new ObjectInputStream(socket.getInputStream());
			String xml = (String) iis.readObject();
			xmlDoc = documentFromString(xml);
		} catch (Exception e) {
			System.err.println("Error: Unable to read XML from socket!\n\t" + e);
			e.printStackTrace();
		}
		return xmlDoc;
	}

	// Usando seriação
	public static boolean documentToSSocket(Document xmlDoc, Socket socket) {
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(documentToString(xmlDoc));
			oos.close();
		} catch (Exception e) {
			System.err.println("Error: Unable to write XML to socket!\n\t" + e);
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
