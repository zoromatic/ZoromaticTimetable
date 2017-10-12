package com.zoromatic.timetable;

import java.util.List;

import com.alertdialogpro.AlertDialogPro;
import com.zoromatic.timetable.R;
import com.zoromatic.timetable.TimetableActivity.TimetablePagerItem;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.widget.EditText;

public class AlertDialogFragment extends DialogFragment
{
	public static final int TEXT_ID = 0x2906;
	public AlertDialogFragment() {

    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String title = args.getString("title");
        String message = args.getString("message");
        String text = args.getString("text");
        Boolean editBox = args.getBoolean("editbox", false);
        final EditText input = new EditText(getActivity());
        input.setId(TEXT_ID);
        
        if (text != null && !text.equalsIgnoreCase(""))
        	input.setText(text);
        
        String theme = Preferences.getMainTheme(getActivity());    	
    	AlertDialogPro.Builder builder = null;
        
    	if (editBox) {
    		
    		TimetableActivity timetableActivity = (TimetableActivity)getActivity();
    		ViewPager viewPager = null;
    		List<TimetablePagerItem> tabs = null;
    		TimetableContentFragment fragment = null;
    		
    		if (timetableActivity != null) {
    		
    			viewPager = timetableActivity.getViewPager();
    			tabs = timetableActivity.getTabs();
    			
	    		if (tabs != null && viewPager != null) {
	    			fragment = (tabs.get(viewPager.getCurrentItem())).getFragment();	    			
	    		}
	    		
	    		if (fragment != null) {
		    		builder = new AlertDialogPro.Builder(getActivity(),				 
		    				theme.compareToIgnoreCase("light") == 0?R.style.Theme_AlertDialogPro_Material_Light:R.style.Theme_AlertDialogPro_Material)
		    		.setTitle(title)
		    		.setMessage(message)
		    		.setView(input)
		    		.setPositiveButton(android.R.string.yes, null)    					
		    		.setNegativeButton(android.R.string.no,
		    					new DialogInterface.OnClickListener() {
		    						public void onClick(DialogInterface dialog,
		    								int whichButton) {
		    							// Do nothing.
		    						}
		    					});
	    		}
    		}
    	} 
    	
    	AlertDialogPro dialog = builder.show();
        
        return dialog;
    }
    
    public void taskFinished() {
        // Make sure we check if it is resumed because we will crash if trying to dismiss the dialog
        // after the user has switched to another app.
        if (isResumed())
            dismiss();

        // Tell the fragment that we are done.
        //if (getTargetFragment() != null)
        //    getTargetFragment().onActivityResult(TASK_FRAGMENT, Activity.RESULT_OK, null);
    }
}
