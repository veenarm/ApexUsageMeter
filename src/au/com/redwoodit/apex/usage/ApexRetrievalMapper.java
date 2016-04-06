package au.com.redwoodit.apex.usage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import au.com.redwoodit.apex.usage.dom.AccountDom;
import au.com.redwoodit.apex.usage.dom.ApexInternetUsageMeterDom;
import au.com.redwoodit.apex.usage.dom.ErrorResponseDom;
import au.com.redwoodit.apex.usage.dom.UsageDom;
import au.com.redwoodit.apex.usage.dom.results.ResultDom;
import au.com.redwoodit.apex.usage.xml.XmlFunctions;

public class ApexRetrievalMapper
{

	public static ApexInternetUsageMeterDom mapRetrievedDataToDoms(Document document)
	{
		ApexInternetUsageMeterDom apexInternetUsageMeterDom = new ApexInternetUsageMeterDom();
		// Lets do error DOMs first...
		NodeList errorNodes = document.getElementsByTagName("ErrorResponse");
		if (errorNodes != null && errorNodes.getLength() > 0)
		{
			ErrorResponseDom errorDom = new ErrorResponseDom();
			Element e = (Element) errorNodes.item(0);
			errorDom.setCode(XmlFunctions.getValue(e, "Code"));
			errorDom.setMessage(XmlFunctions.getValue(e, "Message"));
			errorDom.setType(XmlFunctions.getValue(e, "Type"));
			apexInternetUsageMeterDom.setErrorResponseDom(errorDom);
			return apexInternetUsageMeterDom;
		}

		NodeList accountNodes = document.getElementsByTagName("ApexTelecomUsageMeter");
		if (accountNodes != null && accountNodes.getLength() > 0)
		{
			AccountDom account = new AccountDom();
			Element e = (Element) accountNodes.item(0);
			account.setPeriodStart(XmlFunctions.getValue(e, "Start"));
			account.setPeriodEnd(XmlFunctions.getValue(e, "End"));
			account.setPlan(XmlFunctions.getValue(e, "Name"));
			account.setUsername(XmlFunctions.getValue(e, "Username"));
			account.setQuota(XmlFunctions.getValue(e, "Quota"));
			account.setRemaining(XmlFunctions.getValue(e, "Remaining"));
			apexInternetUsageMeterDom.setAccountDetails(account);
		}
		
		apexInternetUsageMeterDom.setLastUpdate(getXmlValue(document, "LastUpdate"));

		apexInternetUsageMeterDom.setUsageDetails(setUsageData(document, accountNodes));
		return apexInternetUsageMeterDom;
	}

	private static UsageDom setUsageData(Document document, NodeList accountNodes)
	{
		UsageDom usage = new UsageDom(); // Create the usageDom to return.

		// Get the Metered Data first
		usage.setMeteredResult(new ResultDom(getXmlValue(document, "MeteredResult")));

		// Set the unmetered result data
		usage.setUnmeteredResult(new ResultDom(getXmlValue(document, "UnmeteredResult")));

		// Set the upload result data
		usage.setUploadResult(new ResultDom(getXmlValue(document, "UploadResult")));

		// Set the combined reuslt result data
		usage.setCombinedResult(new ResultDom(getXmlValue(document, "CombinedResult")));
		
		return usage;
	}
	
	private static String getXmlValue(Document document, String elementTag) {
		NodeList node = document.getElementsByTagName(elementTag);
		
		if (node == null || node.getLength() < 1) 
			return null;
		
		Element e = (Element) node.item(0);
		return XmlFunctions.getValue(e, "value");
	}
}
