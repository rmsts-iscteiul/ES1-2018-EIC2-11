package auxClasses;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class User {

	private String fn, ln, pw;
	private Encryptation encrypt = new Encryptation();
	private String salt = "GPIHqhlAdTKLG2EDX8mq3S3EZFuoEM";
	
	public User(String fn, String ln, String pw) {
		this.fn = fn;
		this.ln = ln;
		this.pw = pw;
	}


	public String getFn() {
		return fn;
	}


	public String getLn() {
		return ln;
	}


	public String getPw() {
		return pw;
	}
	
	public void saveNewUser(User usr) {
		try {	
	         File inputFile = new File("src\\resources\\files\\credentials.xml");
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();         	      
	         XPathFactory xpathFactory = XPathFactory.newInstance();
	         XPath xpath = xpathFactory.newXPath();
	         XPathExpression expr = xpath.compile("/XML/User/@*");
	         NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
	         	        	         
	             
	         if(!isUsrRegistered(nl, usr)) {
	        	 Element newElement1 = doc.createElement("User");
		         newElement1.setAttribute("fn", usr.getFn());
		         newElement1.setAttribute("ln", usr.getLn());
		         newElement1.setAttribute("pw", encrypt.generateSecurePassword(usr.getPw(), salt));
		         Node n = doc.getDocumentElement();
		         n.appendChild(newElement1); 
	         }
	       
	         Transformer transformer = TransformerFactory.newInstance().newTransformer();
	         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	         StreamResult result = new StreamResult(new FileOutputStream("src\\resources\\files\\credentials.xml"));
	         DOMSource source = new DOMSource(doc);
	         transformer.transform(source, result);
	      } catch (Exception e) { e.printStackTrace(); }
	}
	
	private boolean isUsrRegistered(NodeList nl, User usr) {
		for (int i = 0; i < nl.getLength(); i++) {
       	 	if(nl.item(i).getFirstChild().getNodeValue().equals(usr.getFn()) && nl.item(i+1).getFirstChild().getNodeValue().equals(usr.getLn())) {
       	 		return true;
       	 	}
        }
		return false;
	}
	
	protected boolean isUsrRegistered(User usr) {
		
		try {
			File inputFile = new File("src\\resources\\files\\credentials.xml");
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();         
	         XPathFactory xpathFactory = XPathFactory.newInstance();
	         XPath xpath = xpathFactory.newXPath();
	         XPathExpression expr = xpath.compile("/XML/User/@*");
	         NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
	         for (int i = 0; i < nl.getLength(); i++) {
	        	 	if(nl.item(i).getFirstChild().getNodeValue().equals(usr.getFn()) && nl.item(i+1).getFirstChild().getNodeValue().equals(usr.getLn()) && encrypt.verifyUserPassword(usr.getPw(),nl.item(i+2).getFirstChild().getNodeValue() , salt)) {
	        	 		return true;
	        	 	}
	         }
		}catch (Exception e) { e.printStackTrace(); }		
		return false;
		
	}
	
//	public static void main(String[] args) {
//		User gay = new User("ma","ti","as");
//		gay.saveNewUser(gay);
//		System.out.println(gay.isUsrRegistered(gay));
//	}
}
