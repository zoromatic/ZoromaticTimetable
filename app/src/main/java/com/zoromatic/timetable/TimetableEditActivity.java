package com.zoromatic.timetable;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

public class TimetableEditActivity extends ThemeAppCompatActivity {
	
	@SuppressWarnings("unused")
	private static String LOG_TAG = "TimetableEditActivity";
	private Toolbar mToolbar;
	Spinner mSpinner;
	String  mClassText;
	TimePicker mTimePickerStart;
	TimePicker mTimePickerEnd;
	EditText mDescription;
	
	private Long mRowId;
	private Long mDayId;
	private SQLiteDbAdapter mDbHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
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
		final Resources res = getApplicationContext().getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		android.content.res.Configuration conf = res.getConfiguration();
		conf.locale = new Locale(lang.toLowerCase());
		res.updateConfiguration(conf, dm);
		
		if (mDbHelper == null)
    		mDbHelper = new SQLiteDbAdapter(this);

		setContentView(R.layout.timetable_edit);	
		
		mDescription = (EditText) findViewById(R.id.description);
		
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		
		TypedValue outValue = new TypedValue();
		getTheme().resolveAttribute(R.attr.colorPrimary,
				outValue,
				true);
		int primaryColor = outValue.resourceId;

		setStatusBarColor(findViewById(R.id.statusBarBackground), 
				getResources().getColor(primaryColor));
		
		TypedArray arrayClasses = res.obtainTypedArray(R.array.classes);
		/*TypedArray arrayClassesIds = res.obtainTypedArray(R.array.classesIds);
		
		final ClassData classes[] = new ClassData[arrayClassesIds.length()];
		
		for (int  i=0; i<arrayClasses.length(); i++) {
			classes[i] = new ClassData(arrayClasses.getString(i), arrayClassesIds.getInt(i, i));
		}*/
		
		mSpinner = (Spinner) findViewById(R.id.spinnerClass);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.classes, android.R.layout.simple_spinner_item);
		
		/*ArrayAdapter<ClassData> adapter = 
	            new ArrayAdapter<ClassData>( 
	                this,
	                android.R.layout.simple_spinner_item,
	                classes );*/
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);		
		mSpinner.setAdapter(adapter);		
		mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> parent, 
                    View view, 
                    int position, 
                    long id) {
            	/*ClassData d = classes[position];
            	mClassText = d.getValue();*/
            	//mClassText = ((TextView) view).getText().toString();
            	mClassText = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            	
            }
        });
		
		arrayClasses.recycle();
		//arrayClassesIds.recycle();
		
		mTimePickerStart = (TimePicker) findViewById(R.id.timePickerStart);
		mTimePickerEnd = (TimePicker) findViewById(R.id.timePickerEnd);
		
		mTimePickerStart.setFocusable(true);
		mTimePickerStart.setFocusableInTouchMode(true);
		mTimePickerStart.setIs24HourView(true);
		
		mTimePickerEnd.setFocusable(true);
		mTimePickerEnd.setFocusableInTouchMode(true); 
		mTimePickerEnd.setIs24HourView(true);
		
		Bundle extras = getIntent().getExtras();
		
		mRowId = (savedInstanceState == null) ? null :
		    (Long) savedInstanceState.getSerializable(SQLiteDbAdapter.KEY_ROW_ID);
		
		if (mRowId == null) {
		    mRowId = extras != null ? extras.getLong(SQLiteDbAdapter.KEY_ROW_ID, -1) : null;
		}
		
		mDayId = (savedInstanceState == null) ? null :
		    (Long) savedInstanceState.getSerializable(SQLiteDbAdapter.KEY_DAY_INDEX);
		
		if (mDayId == null) {
			mDayId = extras != null ? extras.getLong(SQLiteDbAdapter.KEY_DAY_INDEX, -1) : null;
		}
		
		ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
            actionBar.setTitle((mRowId == null || mRowId == -1)? R.string.new_class : R.string.edit_class);
        }

		populateFields();
	}
	
	/*class ClassData {
        public ClassData( String value, long id ) {
            this.value = value;
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public long getId() {
            return id;
        }

        public String toString() {
            return value;
        }

        String value;
        long id;
    }*/
	
	@SuppressWarnings("deprecation")
	private void populateFields() {
	    if (mRowId != null && mRowId != -1) {
	    	if (mDbHelper == null)
	    		mDbHelper = new SQLiteDbAdapter(this);
	    	
	    	mDbHelper.open();
	        
	    	Cursor timetable = mDbHelper.fetchTimetable(mRowId);
	        startManagingCursor(timetable);
	        mSpinner.setSelection(timetable.getInt(
	        		timetable.getColumnIndexOrThrow(SQLiteDbAdapter.KEY_CLASS_INDEX)));
	        //setSelectionById(timetable.getInt(
	        //		timetable.getColumnIndexOrThrow(SQLiteDbAdapter.KEY_CLASS_INDEX)));
	        mDescription.setText(timetable.getString(
	        		timetable.getColumnIndexOrThrow(SQLiteDbAdapter.KEY_DESCRIPTION)));
	        stopManagingCursor(timetable);
	        
	        mTimePickerStart.setCurrentHour(timetable.getInt(
	        		timetable.getColumnIndexOrThrow(SQLiteDbAdapter.KEY_START_HOUR)));
	        mTimePickerStart.setCurrentMinute(timetable.getInt(
	        		timetable.getColumnIndexOrThrow(SQLiteDbAdapter.KEY_START_MINUTE)));
	        
	        mTimePickerEnd.setCurrentHour(timetable.getInt(
	        		timetable.getColumnIndexOrThrow(SQLiteDbAdapter.KEY_END_HOUR)));
	        mTimePickerEnd.setCurrentMinute(timetable.getInt(
	        		timetable.getColumnIndexOrThrow(SQLiteDbAdapter.KEY_END_MINUTE)));
	        
	        mDbHelper.close();
	    } else {
	    	mTimePickerStart.setCurrentHour(8);
	        mTimePickerStart.setCurrentMinute(0);
	        
	        mTimePickerEnd.setCurrentHour(9);
	        mTimePickerEnd.setCurrentMinute(0);
	    }
	}
	
	/*private void setSelectionById(long id) {
		for (int i = 0; i < mSpinner.getCount(); i++) {              
            long itemIdAtPosition2 = mSpinner.getItemIdAtPosition(i);
            if (itemIdAtPosition2 == id) {
            	mSpinner.setSelection(i);
            	break;
            }
		}
	}*/
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case android.R.id.home:
	        onBackPressed();	        
	        return true;
	    default:
	    	return super.onOptionsItemSelected(item);
		}
	}
	
	@SuppressWarnings("unused")
	@Override
    public void onBackPressed() {
		if (mDbHelper == null)
    		mDbHelper = new SQLiteDbAdapter(this);
		
		if (mDayId >= 0) {
			if (mRowId == null || mRowId == -1) {
				mDbHelper.open();
		    	
		    	long id = mDbHelper.createTimetable(mDayId, mSpinner.getSelectedItemPosition(), mTimePickerStart.getCurrentHour(), mTimePickerStart.getCurrentMinute(),
		    			mTimePickerEnd.getCurrentHour(), mTimePickerEnd.getCurrentMinute(), mDescription.getText().toString());
		    	
		    	mDbHelper.close();
			} else {
				mDbHelper.open();
		    	
		    	boolean ok = mDbHelper.updateTimetable(mRowId, mDayId, mSpinner.getSelectedItemPosition(), mTimePickerStart.getCurrentHour(), mTimePickerStart.getCurrentMinute(),
		    			mTimePickerEnd.getCurrentHour(), mTimePickerEnd.getCurrentMinute(), mDescription.getText().toString());
		    	
		    	mDbHelper.close();
			}
		}
		
    	super.onBackPressed();              
    }
	
	@SuppressLint("InlinedApi")
	public void setStatusBarColor(View statusBar,int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window w = getWindow();
			w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			int statusBarHeight = getStatusBarHeight();
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