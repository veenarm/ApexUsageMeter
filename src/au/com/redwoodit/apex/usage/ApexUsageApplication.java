package au.com.redwoodit.apex.usage;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;
import au.com.redwoodit.apex.usage.dom.ApexInternetUsageMeterDom;

public class ApexUsageApplication extends Application implements OnSharedPreferenceChangeListener
{
	private static final String TAG = ApexUsageApplication.class.getSimpleName();
	private SharedPreferences prefs;
	private ApexInternetUsageMeterDom apexInternetUsageMeterDom;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.prefs.registerOnSharedPreferenceChangeListener(this);
		this.apexInternetUsageMeterDom = new ApexInternetUsageMeterDom();
		Log.i(TAG, "onCreated");
	}

	@Override
	public void onTerminate()
	{
		super.onTerminate();
		Log.i(TAG, "onTerminated");
	}
	
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		//Nothing to invalidate as url gets rebuilt from sharedPreferences.
	}


	public ApexInternetUsageMeterDom getApexInternetUsageMeterDom()
	{
		return apexInternetUsageMeterDom;
	}

	public void setApexInternetUsageMeterDom(ApexInternetUsageMeterDom apexInternetUsageMeterDom)
	{
		this.apexInternetUsageMeterDom = apexInternetUsageMeterDom;
	}
}
