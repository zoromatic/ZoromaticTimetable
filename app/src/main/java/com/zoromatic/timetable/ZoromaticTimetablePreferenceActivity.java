package com.zoromatic.timetable;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

@SuppressLint("NewApi")
public class ZoromaticTimetablePreferenceActivity extends ThemeAppCompatActivity {
	public boolean mAboutOpen = false;
    public static final String ABOUT = "about";
    private Toolbar toolbar;
    ZoromaticTimetablePreferenceFragment prefs = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_prefs);    
        
        toolbar = (Toolbar) findViewById(R.id.toolbar);
	    setSupportActionBar(toolbar);
	    
	    TypedValue outValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary,
                outValue,
                true);
        int primaryColor = outValue.resourceId;
        
        setStatusBarColor(findViewById(R.id.statusBarBackground), 
	    		getResources().getColor(primaryColor));        
        
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
        }
        
        //getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, prefs).commit();      
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
    
    @SuppressLint("InlinedApi")
	public void setStatusBarColor(View statusBar,int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
               Window w = getWindow();
               w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
               //status bar height
               //int actionBarHeight = getActionBarHeight();
               int statusBarHeight = getStatusBarHeight();
               //action bar height
               statusBar.getLayoutParams().height = /*actionBarHeight + */statusBarHeight;
               statusBar.setBackgroundColor(color);
         } else {
        	 statusBar.setVisibility(View.GONE);
         }
    }
    
    public int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
           actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }    	
}
