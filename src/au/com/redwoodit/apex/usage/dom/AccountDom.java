package au.com.redwoodit.apex.usage.dom;

import java.io.Serializable;

public class AccountDom implements Serializable
{
	private static final long serialVersionUID = 1L;
	/** Account login/username */
	private String username;
	/** Account Plan*/
	private String plan;
	/** This objects period start period */
	private String periodStart;
	/** This objects period end period */
	private String periodEnd;
	private String quota;
	private String remaining;
	
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public String getPlan()
	{
		return plan;
	}
	public void setPlan(String plan)
	{
		this.plan = plan;
	}
	public String getPeriodStart()
	{
		return periodStart;
	}
	public void setPeriodStart(String periodStart)
	{
		this.periodStart = periodStart;
	}
	public String getPeriodEnd()
	{
		return periodEnd;
	}
	public void setPeriodEnd(String periodEnd)
	{
		this.periodEnd = periodEnd;
	}
	public String getQuota()
	{
		return quota;
	}
	public void setQuota(String quota)
	{
		this.quota = quota;
	}
	public String getRemaining()
	{
		return remaining;
	}
	public void setRemaining(String remaining)
	{
		this.remaining = remaining;
	}
}
