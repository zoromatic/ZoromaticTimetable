package com.zoromatic.timetable;

import com.zoromatic.timetable.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

public class ZoromaticTimetableActivity extends ThemeActivity {

	static final String LOG_TAG = "ZoromaticTimetableActivity";
	    
    @SuppressLint("InlinedApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);              
        
        setContentView(R.layout.main);      
        
        Intent timetableIntent = new Intent(getBaseContext(), TimetableActivity.class);
        startActivity(timetableIntent);
		
		finish();
    }                          
}