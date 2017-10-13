package com.zoromatic.timetable;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;

public class ZoromaticTimetablePreferenceActivity extends ThemeAppCompatActivity {
    ZoromaticTimetablePreferenceFragment prefs = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_prefs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
	    setSupportActionBar(toolbar);
	    
	    TypedValue outValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary,
                outValue,
                true);

        PreferenceFragment existingFragment = (PreferenceFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        
        if (existingFragment == null || !existingFragment.getClass().equals(ZoromaticTimetablePreferenceFragment.class)) {
        	prefs = new ZoromaticTimetablePreferenceFragment();
        	String action = getIntent().getAction();
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
                actionBar.setTitle(R.string.app_prefs);
            }

            if (action != null) {
            	Bundle bundle = new Bundle();
                bundle.putString("category", action);
                prefs.setArguments(bundle);

                if (actionBar != null) {
    	            if (action.equals(getString(R.string.category_general))) {
    		           actionBar.setTitle(R.string.app_prefs);
    		        } else if (action.equals(getString(R.string.category_theme))) {
    		        	actionBar.setTitle(R.string.theme_colors);
    		        } else {
    		        	actionBar.setTitle(R.string.app_prefs);
    		        }
                }
            }
        	        	
            // Display the fragment as the main content.
        	getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, prefs)
                .commit();
        } else {
            String action = getIntent().getAction();
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
                actionBar.setTitle(R.string.app_prefs);
            }

            if (action != null) {
                if (actionBar != null) {
                    if (action.equals(getString(R.string.category_general))) {
                        actionBar.setTitle(R.string.app_prefs);
                    } else if (action.equals(getString(R.string.category_theme))) {
                        actionBar.setTitle(R.string.theme_colors);
                    } else {
                        actionBar.setTitle(R.string.app_prefs);
                    }
                }
            }
        }
    }
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case android.R.id.home:
	        onBackPressed();	        
	        return true;
	    default:
	    	return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
    public void onBackPressed() {
		String action = getIntent().getAction();
        
        if (action != null) {
        	if (action.equals(getString(R.string.category_theme))) {
        		setResult(RESULT_OK);
	        }	            
        }
        
        super.onBackPressed();
    }
}
