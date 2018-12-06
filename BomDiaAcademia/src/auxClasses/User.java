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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class User {

	private String fn, ln, pw;
	private Encryptation encrypt = new Encryptation();
	private String salt = "GPIHqhlAdTKLG2EDX8mq3S3EZFuoEM";
	private String fbToken;
	private String twToken;
	private String emUsr;
	private String emPwd;
	private String darkTheme;

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

	public User(String fn, String ln, String pw, String fbToken, String twToken, String emUsr, String emPwd,
			String darkTheme) {
		this.fn = fn;
		this.ln = ln;
		this.pw = pw;
		this.fbToken = fbToken;
		this.twToken = twToken;
		this.emUsr = emUsr;
		this.emPwd = emPwd;
		this.darkTheme = darkTheme;
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

	private void saveNewUser(User usr) {
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
			
			if (!isUsrRegistered(nl, usr)) {
				Element newElement1 = doc.createElement("User");
				newElement1.setAttribute("fn", usr.getFn());
				newElement1.setAttribute("ln", usr.getLn());
				newElement1.setAttribute("pw", encrypt.generateSecurePassword(usr.getPw(), salt));
				newElement1.setAttribute("darkThem", "" + usr.getDarkTheme());
				newElement1.setAttribute("fbToken", usr.getFbToken());
				newElement1.setAttribute("twToken", usr.getTwToken());
				newElement1.setAttribute("emUsr", usr.getEmUsr());
				newElement1.setAttribute("emPwd", encrypt.generateSecurePassword(usr.getEmPwd(), salt));
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



	private void updateUsrInfo(User usr) {
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
				if(children.getNamedItem("fn").getNodeValue().equals(usr.getFn()) && children.getNamedItem("ln").getNodeValue().equals(usr.getLn())) {
					Element newElement1 = doc.createElement("User");
					newElement1.setAttribute("fn", usr.getFn());
					newElement1.setAttribute("ln", usr.getLn());
					newElement1.setAttribute("pw", encrypt.generateSecurePassword(usr.getPw(), salt));
					newElement1.setAttribute("darkThem", "" + usr.getDarkTheme());
					newElement1.setAttribute("fbToken", usr.getFbToken());
					newElement1.setAttribute("twToken", usr.getTwToken());
					newElement1.setAttribute("emUsr", usr.getEmUsr());
					newElement1.setAttribute("emPwd", encrypt.generateSecurePassword(usr.getEmPwd(), salt));
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


	public static void main(String[] args) {
		User gay = new User("Leo", "Nardo", "as", null, null, null, "0", "0");
		gay.saveNewUser(gay);
		gay.setDarkTheme("1");
		gay.updateUsrInfo(gay);
		//System.out.println(gay.isUsrRegistered(gay));
	}
}
