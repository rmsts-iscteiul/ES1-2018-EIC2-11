package auxClasses;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.SerializationUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class User {

	/**
	 * fn,ln,pw are the main credentials for our app (First Name, Last Name, Password)
	 */
	private String fn, ln, pw;
	/**
	 * Is our encrypt method made to encrypt passwords
	 */
	private Encryptation encrypt = new Encryptation();
	/**
	 * salt Encryptation key
	 */
	private String salt = "GPIHqhlAdTKLG2EDX8mq3S3EZFuoEM";
	/**
	 * fbToken Token to access facebook
	 */
	private String fbToken;
	/**
	 * twToken Token to access Twitter
	 */
	private String twToken;
	/**
	 * emUsr User Email
	 */
	private String emUsr;
	/**
	 * emPwd User Email password
	 */
	private String emPwd;
	/**
	 * darkThem Setting defined by the player in our account
	 */
	private String darkTheme;
	/**
	 * id Identifies the User in our app
	 */
	private String id;
	/**
	 * timeFilter used to save last TimeFilter user by the user
	 */
	private String timeFilter;
	/**
	 * wordFilter used to save last wordFilter used by the user
	 */
	private String wordFilter;
	
	/**
	 * @param fn
	 * @param ln
	 * @param pw
	 * 
	 *            When a user registers in the app the follwing @param are set as
	 *            null
	 * @param fbToken
	 * @param twToken
	 * @param emUsr
	 * 
	 *            When a user registers in the app the following @param is set as
	 *            '0'
	 * @param darkTheme
	 * @param emPwd
	 *            (This one is due to encryptation requirements)
	 */

	public User(String fn, String ln, String pw) {
		this.fn = fn;
		this.ln = ln;
		this.pw = pw;
		this.fbToken = null;
		this.twToken = null;
		this.emUsr = null;
		this.emPwd = pw;
		this.darkTheme = "0";
		this.timeFilter = null;
		this.wordFilter = null;
	}
	public String getWordFilter() {
		return wordFilter;
	}
	
	public String getTimeFilter() {
		return timeFilter;
	}
	
	public String getID() {
		return id;
	}

	public String getFbToken() {
		return fbToken;
	}

	public String getTwToken() {
		return twToken;
	}

	public String getEmUsr() {
		return emUsr;
	}

	public String getEmPwd() {
		return emPwd;
	}

	public String getDarkTheme() {
		return darkTheme;
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

	public void saveNewUser() {
		try {
			File inputFile = new File("src\\resources\\files\\credentials.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			XPathExpression expr = xpath.compile("/XML/User/@*");
			NodeList nl = doc.getElementsByTagName("User");
			if (!isUsrRegistered(nl, this)) {
				Element newElement1 = doc.createElement("User");
				newElement1.setAttribute("fn", this.getFn());
				newElement1.setAttribute("ln", this.getLn());
				newElement1.setAttribute("pw", encrypt.generateSecurePassword(this.getPw(), salt));
				newElement1.setAttribute("darkTheme", "" + this.getDarkTheme());
				newElement1.setAttribute("fbToken", this.getFbToken());
				newElement1.setAttribute("twToken", this.getTwToken());
				newElement1.setAttribute("emUsr", this.getEmUsr());
				newElement1.setAttribute("emPwd", encrypt.generateSecurePassword(this.getEmPwd(), salt));
				newElement1.setAttribute("id", ""+nl.getLength());
				newElement1.setAttribute("wordF", this.getWordFilter());
				newElement1.setAttribute("timeF", this.getTimeFilter());
				Node n = doc.getDocumentElement();
				n.appendChild(newElement1);
			}
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			StreamResult result = new StreamResult(new FileOutputStream("src\\resources\\files\\credentials.xml"));
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isUsrRegistered(NodeList nl, User usr) {
		for (int i = 0; i < nl.getLength(); i++) {
			NamedNodeMap children = nl.item(i).getAttributes();
			if(children.getNamedItem("fn").getNodeValue().equals(usr.getFn()) && children.getNamedItem("ln").getNodeValue().equals(usr.getLn())) {
				return true;
			}
		}
		return false;
	}

	public void checkInfo() {
		try {
			File inputFile = new File("src\\resources\\files\\credentials.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			XPathExpression expr = xpath.compile("/XML/User/@*");
			NodeList nl = doc.getElementsByTagName("User");
			for (int i = 0; i < nl.getLength(); i++) {
				NamedNodeMap children = nl.item(i).getAttributes();
				if(children.getNamedItem("fn").getNodeValue().equals(this.getFn()) && children.getNamedItem("ln").getNodeValue().equals(this.getLn())) {
					this.setDarkTheme(children.getNamedItem("darkTheme").getNodeValue());
					this.setEmPwd(children.getNamedItem("emPwd").getNodeValue());
					this.setEmUsr(children.getNamedItem("emUsr").getNodeValue());
					this.setFbToken(children.getNamedItem("fbToken").getNodeValue());
					this.setTwToken(children.getNamedItem("twToken").getNodeValue());
					
				}
			}
		}catch(Exception e) {e.printStackTrace();}
	}

	public void updateUsrInfo() {
		try {
			File inputFile = new File("src\\resources\\files\\credentials.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			XPathExpression expr = xpath.compile("/XML/User/@*");
			NodeList nl = doc.getElementsByTagName("User");
			for (int i = 0; i < nl.getLength(); i++) {
				NamedNodeMap children = nl.item(i).getAttributes();
				if(children.getNamedItem("fn").getNodeValue().equals(this.getFn()) && children.getNamedItem("ln").getNodeValue().equals(this.getLn())) {
					Element newElement1 = doc.createElement("User");
					newElement1.setAttribute("fn", this.getFn());
					newElement1.setAttribute("ln", this.getLn());
					newElement1.setAttribute("pw", encrypt.generateSecurePassword(this.getPw(), salt));
					newElement1.setAttribute("darkTheme", "" + this.getDarkTheme());
					newElement1.setAttribute("fbToken", this.getFbToken());
					newElement1.setAttribute("twToken", this.getTwToken());
					newElement1.setAttribute("emUsr", this.getEmUsr());
					newElement1.setAttribute("emPwd", encrypt.generateSecurePassword(this.getEmPwd(),salt));
					newElement1.setAttribute("id", this.getID());
					newElement1.setAttribute("wordF", this.getWordFilter());
					newElement1.setAttribute("timeF", this.getTimeFilter());
					Node n = doc.getDocumentElement();
					n.replaceChild(newElement1, nl.item(i));
				}
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				StreamResult result = new StreamResult(new FileOutputStream("src\\resources\\files\\credentials.xml"));
				DOMSource source = new DOMSource(doc);
				transformer.transform(source, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * type values are:
	 * Facebook
	 * Twitter
	 * Email
	 * 
	 * @param type, data
	 * 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws XPathExpressionException 
	 * @throws TransformerFactoryConfigurationError 
	 * @throws TransformerException 
	 */
	
	
//	private void storeBrowsedData(String type, List<Object> data) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException {
//		File inputFile = new File("src\\resources\\files\\appdata.xml");
//		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//		Document doc = dBuilder.parse(inputFile);
//		doc.getDocumentElement().normalize();
//		XPathFactory xpathFactory = XPathFactory.newInstance();
//		XPath xpath = xpathFactory.newXPath();
//		XPathExpression expr = xpath.compile("/XML/@*");
//		
//		NodeList nl = doc.getElementsByTagName(type);
//		for (int i = 0; i < nl.getLength(); i++) {
//			NamedNodeMap children = nl.item(i).getAttributes();
//			Element newElement1 = doc.createElement(type);
//			newElement1.setAttribute("id", this.getID());
//			for(int j = 0; j != data.size(); j++) {
//				newElement1.setAttribute("id", this.getID());
//			}
//			Node n = doc.getDocumentElement();
//			n.appendChild(newElement1);
//			
//		}
//		Transformer transformer = TransformerFactory.newInstance().newTransformer();
//		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//		StreamResult result = new StreamResult(new FileOutputStream("src\\resources\\files\\credentials.xml"));
//		DOMSource source = new DOMSource(doc);
//		transformer.transform(source, result);
//	}
	public void setWordFilter(String word) {
		this.wordFilter = word;
	}
	
	public void setTimeFilter(String time) {
		this.timeFilter = time;
	}
	
	public void setFbToken(String fbToken) {
		this.fbToken = fbToken;
	}

	public void setTwToken(String twToken) {
		this.twToken = twToken;
	}

	public void setEmUsr(String emUsr) {
		this.emUsr = emUsr;
	}

	public void setEmPwd(String emPwd) {
		this.emPwd = emPwd;
	}

	public void setDarkTheme(String darkTheme) {
		this.darkTheme = darkTheme;
	}


}
