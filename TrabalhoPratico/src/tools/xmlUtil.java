package tools;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class xmlUtil {
	
	protected static Document doc = null;
	
	public static boolean validateDocument(Document document, String xsdFile) {
		Schema schema = null;
		try {
			String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
			SchemaFactory factory = SchemaFactory.newInstance(language);
			schema = factory.newSchema(new File(xsdFile));
			Validator validator = schema.newValidator();
			validator.validate(new DOMSource(document)); // se falhar existe excepção
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/* escreve um dom para um stream de output */
	
	public static final void writeDocument(Document input, OutputStream output) {
        try {
        	DOMSource domSource = new DOMSource(input);
        	StreamResult resultStream = new StreamResult(output);
        	TransformerFactory transformFactory = TransformerFactory.newInstance();
        	Transformer transformer = transformFactory.newTransformer();
        	try {
        		transformer.transform(domSource, resultStream);
        	} catch (javax.xml.transform.TransformerException e) {
        	}
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
	/* lê um dom do de um stream de input */
	
	public static final Document readDocument(InputStream input) {
		// create a new DocumentBuilderFactory
	      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	      try {
	         // use the factory to create a documentbuilder
	         DocumentBuilder builder = factory.newDocumentBuilder();
	         doc = builder.parse(input);
	      } catch (Exception ex) {
	         ex.printStackTrace();
	      }
	      return doc;
	}

	/** Parses XML file and returns XML document.
     * @param fileName XML file to parse
     * @return XML document or <B>null</B> if error occured
     */
	public static final Document parseFile(final String fileName) {
		
        DocumentBuilder docBuilder;
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);
		
        try {
            docBuilder = factory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            System.out.println("Wrong parser configuration: " + e.getMessage());
            return null;
        }
        
        String XMLPath;

        switch(fileName) {
	        case Constants.scienceFileName:
	        	XMLPath = Constants.pathScienceXML;
	        	break;
	        case Constants.cultureFileName:
	        	XMLPath = Constants.pathCultureXML;
	        	break;
	        case Constants.geographyFileName:
	        	XMLPath = Constants.pathGeographyXML;
	        	break;
	        default:
	        	XMLPath = "";
	        	break;
        }
        try {
            doc = docBuilder.parse(XMLPath);
        }
        catch (SAXException e) {
            System.out.println("Wrong XML file structure: " + e.getMessage());
            return null;
        }
        catch (IOException e) {
            System.out.println("Could not read source file: " + e.getMessage());
        }
        
        updateXMLFile();
        
        
        if (!validateDocument(doc, Constants.pathProtocolXSD)) {
			System.err.println("The file doesn't respect the XSD protocol");
			return null;
        }
        
        return doc;
    }
	
	private static void updateXMLFile() {
		XPathFactory xpathFactory = XPathFactory.newInstance();

		// XPath to find empty text nodes.
		XPathExpression xpathExp = null;

		try {
			xpathExp = xpathFactory.newXPath().compile("//text()[normalize-space(.) = '']");

			NodeList emptyTextNodes = (NodeList) xpathExp.evaluate(doc, XPathConstants.NODESET);

			// Remove each empty text node from document.
			for (int i = 0; i < emptyTextNodes.getLength(); i++) {
				Node emptyTextNode = emptyTextNodes.item(i);
				emptyTextNode.getParentNode().removeChild(emptyTextNode);
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	/* exemplo de validação com xsd de um protocolo */
	
//	public static void main(String[] args) {
//		 
//		String Listar="<?xml version='1.0' encoding='ISO-8859-1'?>"+ 
//		"<Protocolo>"+
//			"<Listar>"+
//				"<Resposta>"+
//					"<Foto path='fotos/foto1' mime='image/jpg'/>"+
//					"<Foto path='fotos/foto2' mime='image/png'/>"+
//				"</Resposta>"+
//			"</Listar>"+
//		"</Protocolo>";
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder builder = null;
//		Document document = null;
//
//		try {
//			builder = factory.newDocumentBuilder();
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//			return;
//		}
//		try {
//			document = builder.parse(new InputSource(new StringReader(Listar)));
//		} catch (SAXException e) {
//			e.printStackTrace();
//			System.out.println("Não foi possivel analisar a mensagem!");
//			return;
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//			System.out.println("Não foi possivel analisar a mensagem!");
//			return;
//		}
//		if (!validateDocument(document, Constants.pathProtocolXSD))
//			System.out.println("A mensagem não respeita o protocolo!");
//		else
//			System.out.println("A mensagem respeita o protocolo!");
//		
//	}

}
