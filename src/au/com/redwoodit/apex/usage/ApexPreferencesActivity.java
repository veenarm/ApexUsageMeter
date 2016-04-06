package au.com.redwoodit.apex.usage;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ApexPreferencesActivity extends PreferenceActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
	}
}
