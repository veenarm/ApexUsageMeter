package au.com.redwoodit.apex.usage.service;

import org.w3c.dom.Document;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import au.com.redwoodit.apex.usage.ApexUsageApplication;
import au.com.redwoodit.apex.usage.common.ApexConstants;
import au.com.redwoodit.apex.usage.dom.ApexInternetUsageMeterDom;
import au.com.redwoodit.apex.usage.dom.mapper.ApexRetrievalMapper;
import au.com.redwoodit.apex.usage.xml.XmlFunctions;

public class ApexRetrievalService extends Service
{
	private static final String TAG = ApexRetrievalService.class.getSimpleName();
	
	private boolean runFlag = false;
	private Retriever retriever;
	private SharedPreferences prefs;
	private FetchDataReceiver fetchDataReceiver;

	@Override
	public void onCreate()
	{
		super.onCreate();
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.retriever = new Retriever();
		this.fetchDataReceiver = new FetchDataReceiver();
		
		registerReceiver(fetchDataReceiver, (IntentFilter) new IntentFilter(ApexConstants.FETCH_DATA_INTENT));

		Log.d(TAG, "onCreated");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		super.onStartCommand(intent, flags, startId);

		// Reset Data Receiver
		if (fetchDataReceiver == null)
			this.fetchDataReceiver = new FetchDataReceiver();
		registerReceiver(fetchDataReceiver, (IntentFilter) new IntentFilter(ApexConstants.FETCH_DATA_INTENT));

		
		if (this.runFlag)
		{
			Log.d(TAG, "serviceAlreadyStarted");
		} else
		{
			this.runFlag = true;
			this.retriever.start();
			Log.d(TAG, "onStarted");
		}

		return START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		this.runFlag = false;
		this.retriever.interrupt();
		this.retriever = null;
		unregisterReceiver(fetchDataReceiver);
		this.fetchDataReceiver = null;
		Log.d(TAG, "onDestroyed");
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	
	/**
	 * Thread that performs the actual retrieve
	 */
	private class Retriever extends Thread
	{
		ImmediateRetrieve immRet = new ImmediateRetrieve();
		
		public Retriever()
		{
			super("Retriever-Service");
		}

		@Override
		public void run()
		{
			ApexRetrievalService retrieverService = ApexRetrievalService.this;
			while (retrieverService.runFlag)
			{
				Log.d(TAG, "Running backround thread");
				try
				{
					ApexUsageApplication apexApp = (ApexUsageApplication) retrieverService.getApplication();
					apexApp.setApexInternetUsageMeterDom(immRet.retrieveUsageData());

					// ...Do stuff that alters the main page (refresh content)
					sendBroadcast(new Intent(ApexConstants.REFRESH_DATA_INTENT));

					// 60000 = 1min get delay and * 60000
					String delay = prefs.getString("refreshInterval", "1800000"); // Default 30 minutes
					Thread.sleep(Integer.valueOf(delay) * 60000);
					
				} catch (InterruptedException ie)
				{
					Log.d(TAG, "InterruptedException - No longer running");
					retrieverService.runFlag = false;
				}
			}
		}


	}
	
	private class ImmediateRetrieve extends Thread
	{
		
		private String getLoginUrl()
		{
			String username = prefs.getString("username", "");
			String password = prefs.getString("password", "");

			if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password))
				return "https://cosmos.apex.net.au/loaded-v1/?usagexml&v=5&username=" + username + "&password=" + password;

			return null;
		}

		private synchronized ApexInternetUsageMeterDom retrieveUsageData()
		{
			Document document = XmlFunctions.XMLfromString(XmlFunctions.getXML(getLoginUrl()));
			if (document == null)
				return null;
			else
				return ApexRetrievalMapper.mapRetrievedDataToDoms(document);
		}
	}

	private class FetchDataReceiver extends BroadcastReceiver
	{
		ImmediateRetrieve immRet = new ImmediateRetrieve();
		
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if (!intent.getAction().equals(ApexConstants.FETCH_DATA_INTENT))
				return;

			ApexUsageApplication apexApp = ((ApexUsageApplication) getApplication());
			apexApp.setApexInternetUsageMeterDom(immRet.retrieveUsageData());
			
			// ...Do stuff that alters the main page (refresh content)
			sendBroadcast(new Intent(ApexConstants.REFRESH_DATA_INTENT));
		}
	}
}
