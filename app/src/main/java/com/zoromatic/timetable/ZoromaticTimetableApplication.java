package com.zoromatic.timetable;

import java.util.Locale;

import android.app.Application;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class ZoromaticTimetableApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();   
        
        String lang = Preferences.getLanguageOptions(this);
    	
        if (lang.equals("")) {
    		String langDef = Locale.getDefault().getLanguage();
    		
    		if (!langDef.equals(""))
    			lang = langDef;
    		else
    			lang = "en";    
    		
    		Preferences.setLanguageOptions(this, lang);
    	}			
    	
    	// Change locale settings in the application
    	Resources res = getApplicationContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(lang.toLowerCase());
        res.updateConfiguration(conf, dm); 
    }
}