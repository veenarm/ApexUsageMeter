package au.com.redwoodit.apex.usage.dom;

import android.text.TextUtils;

public class ApexLoginDetailsDom
{
	private String username;
	private String password;
	private static final String staticUrlPiece1 = "https://cosmos.apex.net.au/loaded-v1/?usagexml&v=5&username=";
	private static final String staticUrlPiece2 = "&password=";
	
	public ApexLoginDetailsDom(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public String getUrl()
	{
		
		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
			return staticUrlPiece1 + username + staticUrlPiece2 + password;
		}
		
		return null;
	}
}
