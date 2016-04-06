package au.com.redwoodit.apex.usage.dom;

import java.io.Serializable;

public class ErrorResponseDom implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String type;
	private String code;
	private String message;
	
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code = code;
	}

	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	public String toString() {
		return type + " " + code + " " + message;
	}
	
}
