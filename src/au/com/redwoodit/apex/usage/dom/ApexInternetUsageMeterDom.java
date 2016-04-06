package au.com.redwoodit.apex.usage.dom;

import java.io.Serializable;

public class ApexInternetUsageMeterDom implements Serializable
{
	private static final long serialVersionUID = 1L;
	/** This object holds details about the account user. */
	private AccountDom accountDetails;
	/** This object holds details about the accounts usage. */
	private UsageDom usageDetails;
	/** This is the last update to the users data */
	private String lastUpdate;
	/** This object holds the failed attempt and why */
	private ErrorResponseDom errorResponseDom;
	
	public AccountDom getAccountDetails()
	{
		return accountDetails;
	}

	public void setAccountDetails(AccountDom accountDetails)
	{
		this.accountDetails = accountDetails;
	}

	public UsageDom getUsageDetails()
	{
		return usageDetails;
	}

	public void setUsageDetails(UsageDom usageDetails)
	{
		this.usageDetails = usageDetails;
	}

	public String getLastUpdate()
	{
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate)
	{
		this.lastUpdate = lastUpdate;
	}

	public ErrorResponseDom getErrorResponseDom()
	{
		return errorResponseDom;
	}

	public void setErrorResponseDom(ErrorResponseDom errorResponseDom)
	{
		this.errorResponseDom = errorResponseDom;
	}
	
	/**
	 * Override Equals for this object.
	 * @param data
	 * @return
	 */
	public boolean equals(ApexInternetUsageMeterDom data) {
		
		if (data == null)
			return false;
		if (this.getClass() != data.getClass())
			return false;
		if (this.getLastUpdate() == null && data.getLastUpdate() == null)
			return true;
		if (this.getLastUpdate().equalsIgnoreCase(data.getLastUpdate()))
			return true;
			
		return false;
	}
}
