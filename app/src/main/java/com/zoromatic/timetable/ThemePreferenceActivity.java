package com.zoromatic.timetable;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class ThemePreferenceActivity extends PreferenceActivity {
	protected Toolbar mToolBar;

	protected void onCreate(Bundle savedInstanceState) {			
		String theme = Preferences.getMainTheme(this);		
		int colorScheme = Preferences.getMainColorScheme(this);
		
		if (theme.compareToIgnoreCase("light") == 0) {
			switch (colorScheme) {
			case 0:
				setTheme(R.style.ThemeLightBlack);
				break;
			case 1:
				setTheme(R.style.ThemeLightWhite);
				break;
			case 2:
				setTheme(R.style.ThemeLightRed);
				break;
			case 3:
				setTheme(R.style.ThemeLightPink);
				break;
			case 4:
				setTheme(R.style.ThemeLightPurple);
				break;
			case 5:
				setTheme(R.style.ThemeLightDeepPurple);
				break;
			case 6:
				setTheme(R.style.ThemeLightIndigo);
				break;
			case 7:
				setTheme(R.style.ThemeLightBlue);
				break;
			case 8:
				setTheme(R.style.ThemeLightLightBlue);
				break;
			case 9:
				setTheme(R.style.ThemeLightCyan);
				break;
			case 10:
				setTheme(R.style.ThemeLightTeal);
				break;
			case 11:
				setTheme(R.style.ThemeLightGreen);
				break;
			case 12:
				setTheme(R.style.ThemeLightLightGreen);
				break;
			case 13:
				setTheme(R.style.ThemeLightLime);
				break;
			case 14:
				setTheme(R.style.ThemeLightYellow);
				break;
			case 15:
				setTheme(R.style.ThemeLightAmber);
				break;
			case 16:
				setTheme(R.style.ThemeLightOrange);
				break;
			case 17:
				setTheme(R.style.ThemeLightDeepOrange);
				break;
			case 18:
				setTheme(R.style.ThemeLightBrown);
				break;
			case 19:
				setTheme(R.style.ThemeLightGrey);
				break;
			case 20:
				setTheme(R.style.ThemeLightBlueGrey);
				break;
			default:
				setTheme(R.style.ThemeLightBlack);
				break;
			}
			
		} else { 
			switch (colorScheme) {
			case 0:
				setTheme(R.style.ThemeDarkBlack);
				break;
			case 1:
				setTheme(R.style.ThemeDarkWhite);
				break;
			case 2:
				setTheme(R.style.ThemeDarkRed);
				break;
			case 3:
				setTheme(R.style.ThemeDarkPink);
				break;
			case 4:
				setTheme(R.style.ThemeDarkPurple);
				break;
			case 5:
				setTheme(R.style.ThemeDarkDeepPurple);
				break;
			case 6:
				setTheme(R.style.ThemeDarkIndigo);
				break;
			case 7:
				setTheme(R.style.ThemeDarkBlue);
				break;
			case 8:
				setTheme(R.style.ThemeDarkLightBlue);
				break;
			case 9:
				setTheme(R.style.ThemeDarkCyan);
				break;
			case 10:
				setTheme(R.style.ThemeDarkTeal);
				break;
			case 11:
				setTheme(R.style.ThemeDarkGreen);
				break;
			case 12:
				setTheme(R.style.ThemeDarkLightGreen);
				break;
			case 13:
				setTheme(R.style.ThemeDarkLime);
				break;
			case 14:
				setTheme(R.style.ThemeDarkYellow);
				break;
			case 15:
				setTheme(R.style.ThemeDarkAmber);
				break;
			case 16:
				setTheme(R.style.ThemeDarkOrange);
				break;
			case 17:
				setTheme(R.style.ThemeDarkDeepOrange);
				break;
			case 18:
				setTheme(R.style.ThemeDarkBrown);
				break;
			case 19:
				setTheme(R.style.ThemeDarkGrey);
				break;
			case 20:
				setTheme(R.style.ThemeDarkBlueGrey);
				break;
			default:
				setTheme(R.style.ThemeDarkBlack);
				break;
			}
		}
		
		super.onCreate(savedInstanceState);	
		
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
        
        prepareLayout();
        
        TypedValue outValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary,
                outValue,
                true);
        int primaryColor = outValue.resourceId;
        
        setStatusBarColor(findViewById(R.id.statusBarBackground), 
	    		getResources().getColor(primaryColor));
	}
	
	private void prepareLayout() {
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        View content = root.getChildAt(0);
        LinearLayout toolbarContainer = (LinearLayout) View.inflate(this, R.layout.activity_prefs, null);

        root.removeAllViews();
        toolbarContainer.addView(content);
        root.addView(toolbarContainer);

        mToolBar = (Toolbar) toolbarContainer.findViewById(R.id.toolbar);
        mToolBar.setTitle(getResources().getText(R.string.app_prefs));
        mToolBar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
            	onBackPressed();
            }
        });
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

	@Override
    protected void onResume() {
		String theme = Preferences.getMainTheme(this);		
		int colorScheme = Preferences.getMainColorScheme(this);
		
		if (theme.compareToIgnoreCase("light") == 0) {
			switch (colorScheme) {
			case 0:
				setTheme(R.style.ThemeLightBlack);
				break;
			case 1:
				setTheme(R.style.ThemeLightWhite);
				break;
			case 2:
				setTheme(R.style.ThemeLightRed);
				break;
			case 3:
				setTheme(R.style.ThemeLightPink);
				break;
			case 4:
				setTheme(R.style.ThemeLightPurple);
				break;
			case 5:
				setTheme(R.style.ThemeLightDeepPurple);
				break;
			case 6:
				setTheme(R.style.ThemeLightIndigo);
				break;
			case 7:
				setTheme(R.style.ThemeLightBlue);
				break;
			case 8:
				setTheme(R.style.ThemeLightLightBlue);
				break;
			case 9:
				setTheme(R.style.ThemeLightCyan);
				break;
			case 10:
				setTheme(R.style.ThemeLightTeal);
				break;
			case 11:
				setTheme(R.style.ThemeLightGreen);
				break;
			case 12:
				setTheme(R.style.ThemeLightLightGreen);
				break;
			case 13:
				setTheme(R.style.ThemeLightLime);
				break;
			case 14:
				setTheme(R.style.ThemeLightYellow);
				break;
			case 15:
				setTheme(R.style.ThemeLightAmber);
				break;
			case 16:
				setTheme(R.style.ThemeLightOrange);
				break;
			case 17:
				setTheme(R.style.ThemeLightDeepOrange);
				break;
			case 18:
				setTheme(R.style.ThemeLightBrown);
				break;
			case 19:
				setTheme(R.style.ThemeLightGrey);
				break;
			case 20:
				setTheme(R.style.ThemeLightBlueGrey);
				break;
			default:
				setTheme(R.style.ThemeLightBlack);
				break;
			}
			
		} else { 
			switch (colorScheme) {
			case 0:
				setTheme(R.style.ThemeDarkBlack);
				break;
			case 1:
				setTheme(R.style.ThemeDarkWhite);
				break;
			case 2:
				setTheme(R.style.ThemeDarkRed);
				break;
			case 3:
				setTheme(R.style.ThemeDarkPink);
				break;
			case 4:
				setTheme(R.style.ThemeDarkPurple);
				break;
			case 5:
				setTheme(R.style.ThemeDarkDeepPurple);
				break;
			case 6:
				setTheme(R.style.ThemeDarkIndigo);
				break;
			case 7:
				setTheme(R.style.ThemeDarkBlue);
				break;
			case 8:
				setTheme(R.style.ThemeDarkLightBlue);
				break;
			case 9:
				setTheme(R.style.ThemeDarkCyan);
				break;
			case 10:
				setTheme(R.style.ThemeDarkTeal);
				break;
			case 11:
				setTheme(R.style.ThemeDarkGreen);
				break;
			case 12:
				setTheme(R.style.ThemeDarkLightGreen);
				break;
			case 13:
				setTheme(R.style.ThemeDarkLime);
				break;
			case 14:
				setTheme(R.style.ThemeDarkYellow);
				break;
			case 15:
				setTheme(R.style.ThemeDarkAmber);
				break;
			case 16:
				setTheme(R.style.ThemeDarkOrange);
				break;
			case 17:
				setTheme(R.style.ThemeDarkDeepOrange);
				break;
			case 18:
				setTheme(R.style.ThemeDarkBrown);
				break;
			case 19:
				setTheme(R.style.ThemeDarkGrey);
				break;
			case 20:
				setTheme(R.style.ThemeDarkBlueGrey);
				break;
			default:
				setTheme(R.style.ThemeDarkBlack);
				break;
			}
		}
		
		super.onResume();						     
	}
	
	/**
	 * Called by {@link #setTheme} and {@link #getTheme} to apply a theme
	 * resource to the current Theme object.  Can override to change the
	 * default (simple) behavior.  This method will not be called in multiple
	 * threads simultaneously.
	 *
	 * @param theme The Theme object being modified.
	 * @param resid The theme style resource being applied to <var>theme</var>.
	 * @param first Set to true if this is the first time a style is being
	 *              applied to <var>theme</var>.
	 */
	protected void onApplyThemeResource(Resources.Theme theme, int resid, boolean first) {
	    theme.applyStyle(resid, true);
	}
	
	@Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        // Allow super to try and create a view first
		final View result = super.onCreateView(name, context, attrs);

        if (result != null)
        	return result;                
        else
        	return null;
    }
}
