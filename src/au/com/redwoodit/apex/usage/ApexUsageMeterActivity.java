package au.com.redwoodit.apex.usage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import au.com.redwoodit.apex.usage.common.ApexConstants;
import au.com.redwoodit.apex.usage.dom.AccountDom;
import au.com.redwoodit.apex.usage.dom.ApexInternetUsageMeterDom;
import au.com.redwoodit.apex.usage.service.ApexRetrievalService;

public class ApexUsageMeterActivity extends Activity implements OnSharedPreferenceChangeListener, OnClickListener
{
	private static final String TAG = ApexUsageMeterActivity.class.getSimpleName();

	TextView plan;
	TextView quota;
	TextView planPeriod;
	TextView meteredDownload;
	TextView unmeteredDownload;
	TextView uploadResult;
	TextView lastUpdated;
	Button retrieveButton;
	private DataUpdateReceiver dataUpdateReceiver;
	private ProgressBar mProgress;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		plan = (TextView) findViewById(R.id.textPlan);
		planPeriod = (TextView) findViewById(R.id.textUsagePeriod);
		meteredDownload = (TextView) findViewById(R.id.textMeteredDownload);
		lastUpdated = (TextView) findViewById(R.id.textLastUpdated);
		retrieveButton = (Button) findViewById(R.id.buttonRetrieve);
		retrieveButton.setOnClickListener(this);
		mProgress = (ProgressBar) findViewById(R.id.usage_bar);
		quota = (TextView) findViewById(R.id.textQuota);
		unmeteredDownload = (TextView) findViewById(R.id.textUnmeteredDownload);
		uploadResult = (TextView) findViewById(R.id.textUploadResult);

		// Data Receiver Setup
		dataUpdateReceiver = new DataUpdateReceiver();
		IntentFilter intentFilter = new IntentFilter(ApexConstants.REFRESH_DATA_INTENT);
		registerReceiver(dataUpdateReceiver, intentFilter);

		// Start service upon start up.
		// Service is required to do any data retrieval.
		startService(new Intent(this, ApexRetrievalService.class));
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (dataUpdateReceiver == null)
			dataUpdateReceiver = new DataUpdateReceiver();
		IntentFilter intentFilter = new IntentFilter(ApexConstants.REFRESH_DATA_INTENT);
		registerReceiver(dataUpdateReceiver, intentFilter);

		// Do stuff that alters the main page (refresh content)
		sendBroadcast(new Intent(ApexConstants.FETCH_DATA_INTENT));
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if (dataUpdateReceiver != null)
		{
			unregisterReceiver(dataUpdateReceiver);
			dataUpdateReceiver = null;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.itemPrefs)
			startActivity(new Intent(this, ApexPreferencesActivity.class));

		return true;
	}

	public void refreshPageData()
	{
		ApexUsageApplication apexApp = ((ApexUsageApplication) getApplication());
		ApexInternetUsageMeterDom apexDomain = apexApp.getApexInternetUsageMeterDom();

		if (apexDomain == null || apexDomain.getAccountDetails() == null)
		{
			plan.setText("");
			meteredDownload.setText("");
			planPeriod.setText("");
			lastUpdated.setText("");
			mProgress.setMax(100);
			mProgress.setProgress(0);
			quota.setText("");
			unmeteredDownload.setText("");
			uploadResult.setText("");
			return;
		}
		
		AccountDom accountDom = apexDomain.getAccountDetails();
		plan.setText(accountDom.getPlan());
		lastUpdated.setText(apexDomain.getLastUpdate());
		String start = accountDom.getPeriodStart();
		String end = accountDom.getPeriodEnd();

		try
		{ // Ugly, parsing dates to a Date so can re-output nicely...
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date startDate = sdf.parse(start);
			Date endDate = sdf.parse(end);
			sdf.applyPattern("dd/MM/yyyy");
			start = sdf.format(startDate);
			end = sdf.format(endDate);
		} catch (ParseException e)
		{
			Log.w(TAG, "ParseException on data retrieved");
		}
		planPeriod.setText(start + " -> " + end);

		String totalQuota = accountDom.getQuota();
		String remainingQuota = accountDom.getRemaining();
		quota.setText(totalQuota + "MB");

		int usedQuota = 0;
		// Update the progressbar
		try
		{
			int intTotalQuota = Integer.valueOf(totalQuota);
			int intRemainingQuota = Integer.valueOf(remainingQuota);
			usedQuota = intTotalQuota - intRemainingQuota;
			mProgress.setMax(Integer.valueOf(totalQuota));
			mProgress.setProgress(usedQuota);
		} catch (NumberFormatException nfe)
		{
			// WTF?
		}

		meteredDownload.setText(usedQuota + "MB Remaining: " + remainingQuota + "MB");

		if (apexDomain.getUsageDetails() != null)
		{
			if (apexDomain.getUsageDetails().getUnmeteredResult() != null)
				unmeteredDownload.setText(apexDomain.getUsageDetails().getUnmeteredResult().getValue() + "MB");
			if (apexDomain.getUsageDetails().getUploadResult() != null)
				uploadResult.setText(apexDomain.getUsageDetails().getUploadResult().getValue() + "MB");
		}

	}

	public void onClick(View v)
	{
		// Service needs to be running (This just makes sure it is)...
		startService(new Intent(this, ApexRetrievalService.class));

		// Do stuff that alters the main page (refresh content)
		sendBroadcast(new Intent(ApexConstants.FETCH_DATA_INTENT));

		Log.d(TAG, "Refresh Data Button Tagged");
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		// ...Do stuff that alters the main page (refresh content)
		sendBroadcast(new Intent(ApexConstants.FETCH_DATA_INTENT));
	}

	private class DataUpdateReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if (intent.getAction().equals(ApexConstants.REFRESH_DATA_INTENT))
				refreshPageData();
		}
	}

}