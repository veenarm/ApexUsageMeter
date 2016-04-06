package au.com.redwoodit.apex.usage.dom;

import java.io.Serializable;
import java.util.List;

import au.com.redwoodit.apex.usage.dom.results.DayDom;
import au.com.redwoodit.apex.usage.dom.results.ResultDom;

public class UsageDom implements Serializable
{
	private static final long serialVersionUID = 1L;
	private ResultDom meteredResult;
	private ResultDom unmeteredResult;
	private ResultDom uploadResult;
	private ResultDom combinedResult;
	private String dailyRows;
	private List/*<DayDom>*/<DayDom> dailyDay; 
	private DayDom dailyAverage;
	
	public ResultDom getMeteredResult()
	{
		return meteredResult;
	}
	public void setMeteredResult(ResultDom meteredResult)
	{
		this.meteredResult = meteredResult;
	}
	public ResultDom getUnmeteredResult()
	{
		return unmeteredResult;
	}
	public void setUnmeteredResult(ResultDom unmeteredResult)
	{
		this.unmeteredResult = unmeteredResult;
	}
	public ResultDom getUploadResult()
	{
		return uploadResult;
	}
	public void setUploadResult(ResultDom uploadResult)
	{
		this.uploadResult = uploadResult;
	}
	public ResultDom getCombinedResult()
	{
		return combinedResult;
	}
	public void setCombinedResult(ResultDom combinedResult)
	{
		this.combinedResult = combinedResult;
	}
	public List<DayDom> getDailyDay()
	{
		return dailyDay;
	}
	public void setDailyDay(List<DayDom> dailyDay)
	{
		this.dailyDay = dailyDay;
	}
	public String getDailyRows()
	{
		return dailyRows;
	}
	public void setDailyRows(String dailyRows)
	{
		this.dailyRows = dailyRows;
	}
	public DayDom getDailyAverage()
	{
		return dailyAverage;
	}
	public void setDailyAverage(DayDom dailyAverage)
	{
		this.dailyAverage = dailyAverage;
	}
	
}
