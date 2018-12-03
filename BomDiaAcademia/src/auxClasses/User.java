package auxClasses;

public class User {

	private String fn, ln, pw;
	
	
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
	
	
}
