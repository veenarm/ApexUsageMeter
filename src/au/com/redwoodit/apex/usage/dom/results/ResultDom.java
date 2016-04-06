package au.com.redwoodit.apex.usage.dom.results;

import java.io.Serializable;

public class ResultDom implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String value;
	
	public ResultDom(String value) {
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
	
}
