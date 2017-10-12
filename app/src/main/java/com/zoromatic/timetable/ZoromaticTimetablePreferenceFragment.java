package com.zoromatic.timetable;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;

import com.zoromatic.timetable.PreferenceFragment;
import com.zoromatic.timetable.R;

import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("NewApi")
public class ZoromaticTimetablePreferenceFragment extends PreferenceFragment  implements OnSharedPreferenceChangeListener {
	public static final int RESULT_CANCELED    = 0;
    public static final int RESULT_OK           = -1;
    public static final int RESULT_FIRST_USER   = 1;
    public static final int REQUEST_THEME       = 0;
    
    Context context = null;
    
    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        
        context = (Context)getActivity();        			   
		
        if (context != null) {
        	String lang = Preferences.getLanguageOptions(context);
        	
            if (lang.equals("")) {
        		String langDef = Locale.getDefault().getLanguage();
        		
        		if (!langDef.equals(""))
        			lang = langDef;
        		else
        			lang = "en";
        		
            	Preferences.setLanguageOptions(context, lang);                
        	}
            
    		// Change locale settings in the application
    		Resources res = context.getResources();
    	    DisplayMetrics dm = res.getDisplayMetrics();
    	    android.content.res.Configuration conf = res.getConfiguration();
    	    conf.locale = new Locale(lang.toLowerCase());
    	    res.updateConfiguration(conf, dm);
    	    
    	    setPreferences(paramBundle);    	       	    
        }                        
    }
    
    private void setPreferences(Bundle paramBundle) {
    	PreferenceManager localPrefs = getPreferenceManager();
        localPrefs.setSharedPreferencesName(Preferences.PREF_NAME);
        
        Bundle bundle = getArguments();
        
        if (bundle != null) {
	        String category = bundle.getString("category");      
	        
	        if (category != null) {
		        if (category.equals(getString(R.string.category_general))) {
		            addPreferencesFromResource(R.xml.zoromatictimetable_prefs);
		        } else if (category.equals(getString(R.string.category_theme))) {
		        	addPreferencesFromResource(R.xml.zoromatictimetable_prefs_theme);		        
		        }
	        } else {
	        	addPreferencesFromResource(R.xml.zoromatictimetable_prefs);
	        }
        } else {
        	addPreferencesFromResource(R.xml.zoromatictimetable_prefs);
        }
        
        ListPreference mainTheme = (ListPreference)findPreference(Preferences.PREF_MAIN_THEME);
        
        if (mainTheme != null) {
        	
        	String theme = Preferences.getMainTheme(context);
        	
        	if (theme.equals("") || mainTheme.findIndexOfValue(theme) < 0) {
        		theme = "dark";
        	}
        	
        	mainTheme.setValueIndex(mainTheme.findIndexOfValue(theme));
        	mainTheme.setSummary(mainTheme.getEntries()[mainTheme.findIndexOfValue(theme)]);
        }
        
        ListPreference mainColorScheme = (ListPreference)findPreference(Preferences.PREF_MAIN_COLOR_SCHEME);
        
        if (mainColorScheme != null)
        {
        	mainColorScheme.setValueIndex(Preferences.getMainColorScheme(context));
        	mainColorScheme.setSummary(mainColorScheme.getEntries()[Preferences.getMainColorScheme(context)]);
        }
        
        ListPreference language = (ListPreference)findPreference(Preferences.PREF_LANGUAGE_OPTIONS);
        
        if (language != null)
        {
        	String lang = Preferences.getLanguageOptions(context);
        	
        	if (lang.equals("") || language.findIndexOfValue(lang) < 0) {
        		lang = "en";
        	}
        	
        	language.setValueIndex(language.findIndexOfValue(lang));
        	language.setSummary(language.getEntries()[language.findIndexOfValue(lang)]);                
        }                
    }   
    
    @Override
	public void onSaveInstanceState(Bundle savedInstanceState) {      	
    	super.onSaveInstanceState(savedInstanceState);        	
    }
    
    @Override
	public void onViewStateRestored(Bundle savedInstanceState) {     	
    	super.onViewStateRestored(savedInstanceState);    	
    }
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	return super.onCreateView(inflater, container, savedInstanceState);    	    	             		       
	}
    
    @Override
    public void onDestroyView() {
    	
    	super.onDestroyView();
    }
    
    @Override
    public boolean onPreferenceTreeClick (PreferenceScreen preferenceScreen, Preference preference) {
    	
    	if (preferenceScreen == null || preference == null)
    		return false;
    	
    	String key = preference.getKey();
    	
    	if (key.equalsIgnoreCase(getResources().getString(R.string.category_theme))) {
			Intent settingsIntent = new Intent(context, ZoromaticTimetablePreferenceActivity.class);
			settingsIntent.setAction(key);
			startActivityForResult(settingsIntent, REQUEST_THEME);
    	}
    	
    	return super.onPreferenceTreeClick(preferenceScreen, preference);        	
    }
	
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
    	case REQUEST_THEME:
    		//if (resultCode == RESULT_OK){
    			Intent intent = getActivity().getIntent();
	        	getActivity().finish();
	        	getActivity().startActivity(intent);
            //}
    		break;		
		}   	
    	
		super.onActivityResult(requestCode, resultCode, data);
    }
    
	@Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
	   
		Context context = (Context)getActivity();
        
        if (context != null) {
    		if (key.equals(Preferences.PREF_MAIN_THEME)) {
    			ListPreference mainTheme = (ListPreference)findPreference(Preferences.PREF_MAIN_THEME);
    	        
    	        if (mainTheme != null)
    	        {
    	        	String value = mainTheme.getValue();
    	        	
    	        	if (value.equals("") || mainTheme.findIndexOfValue(value) < 0)
    	        		value = "dark";	        	
    	        	
    	        	Preferences.setMainTheme(context, value);	    	        	    	        
    	        	
    	        	mainTheme.setValueIndex(mainTheme.findIndexOfValue(value));
    	        	mainTheme.setSummary(mainTheme.getEntries()[mainTheme.findIndexOfValue(value)]);	    
    	        	
    	        	Intent intent = getActivity().getIntent();
    	        	getActivity().finish();
    	        	getActivity().startActivity(intent);
    	        }
    		}
    		
    		if (key.equals(Preferences.PREF_MAIN_COLOR_SCHEME)) {
    			ListPreference mainColorScheme = (ListPreference)findPreference(Preferences.PREF_MAIN_COLOR_SCHEME);
    	        
    	        if (mainColorScheme != null)
    	        {
    	        	Preferences.setMainColorScheme(context, mainColorScheme.findIndexOfValue(mainColorScheme.getValue()));
    	        	mainColorScheme.setSummary(mainColorScheme.getEntries()[Preferences.getMainColorScheme(context)]);
    	        	
    	        	Intent intent = getActivity().getIntent();
    	        	getActivity().finish();
    	        	getActivity().startActivity(intent);
    	        }
    		}
    		
    		if (key.equals(Preferences.PREF_LANGUAGE_OPTIONS)) {
    			ListPreference language = (ListPreference)findPreference(Preferences.PREF_LANGUAGE_OPTIONS);
    	        
    	        if (language != null)
    	        {
    	        	if (!language.getValue().equals("") && language.findIndexOfValue(language.getValue()) >= 0)
    	        		Preferences.setLanguageOptions(context, language.getValue());
    	        	else
    	        		Preferences.setLanguageOptions(context, "en");
    	        	
    	        	Intent intent = getActivity().getIntent();
    	        	getActivity().finish();
    	        	getActivity().startActivity(intent);	            	           
    	        }
    		}
        }
	}
	
	@Override
	public void onResume() {        	
    	super.onResume();        	
        
    	PreferenceScreen preferenceScreen = getPreferenceScreen();
    	
    	if (preferenceScreen == null)
    		return;
        
        // Set up a listener whenever a key changes
    	preferenceScreen.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);       	    	
    }

    @Override
	public void onPause() {
        super.onPause();

        PreferenceScreen preferenceScreen = getPreferenceScreen();
    	
    	if (preferenceScreen == null)
    		return;

        // Unregister the listener whenever a key changes
    	preferenceScreen.getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);    	      
    }
}