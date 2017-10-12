package com.zoromatic.timetable;

import com.alertdialogpro.ProgressDialogPro;
import com.zoromatic.timetable.R;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;

public class ProgressDialogFragment extends DialogFragment {

	static boolean mFinish = false;
	
	public ProgressDialogFragment() {
		
    }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this instance so it isn't destroyed when MainActivity and
        // MainFragment change configuration.
        setRetainInstance(true);        
    }
	
	public void onDismiss(DialogInterface dialog)
    {
        super.onDismiss(dialog);        
    }
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	Bundle args = getArguments();
        String title = args.getString("title");
        String message = args.getString("message");
        
        String theme = Preferences.getMainTheme(getActivity());
    	
        ProgressDialogPro dialog = new ProgressDialogPro(getActivity(), 
        		theme.compareToIgnoreCase("light") == 0?R.style.Theme_AlertDialogPro_Material_Light:R.style.Theme_AlertDialogPro_Material);
        
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
        	public void onDismiss(DialogInterface dialogInterface) {
        		
        	}
    	});
        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode,
                    KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                	dismiss();                	
                }
                
                return true;
            }
        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        
        mFinish = false;
        
        return dialog;
    }
	
	// This is to work around what is apparently a bug. If you don't have it
    // here the dialog will be dismissed on rotation, so tell it not to dismiss.
    @Override
    public void onDestroyView()
    {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }
	
	@Override
    public void onResume() {
        super.onResume();
        // This is a little hacky, but we will see if the task has finished while we weren't
        // in this activity, and then we can dismiss ourselves.
        if (mFinish)
            dismiss();
    }
    
    public void taskFinished() {
        // Make sure we check if it is resumed because we will crash if trying to dismiss the dialog
        // after the user has switched to another app.
    	mFinish = false;
    	
    	try {
    		if (isResumed())
    			dismiss();
    		else
    			mFinish = true;
    		
    		// If we aren't resumed, setting the task to null will allow us to dismiss ourselves in
            // onResume().            
            
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

        // Tell the fragment that we are done.
        //if (getTargetFragment() != null)
        //    getTargetFragment().onActivityResult(TASK_FRAGMENT, Activity.RESULT_OK, null);
    }

}
