package com.zoromatic.timetable;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.KeyEvent;

public class ProgressDialogFragment extends DialogFragment {

	static boolean mFinish = false;
	
	public ProgressDialogFragment() {
		
    }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }
	
	public void onDismiss(DialogInterface dialog)
    {
        super.onDismiss(dialog);        
    }
	
	@NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	Bundle args = getArguments();
        String title = args.getString("title");
        String message = args.getString("message");
        
        String theme = Preferences.getMainTheme(getActivity());
    	
        ProgressDialog dialog = new ProgressDialog(getActivity(), 
        		theme.compareToIgnoreCase("light") == 0?R.style.AppCompatAlertDialogStyleLight:R.style.AppCompatAlertDialogStyle);
        
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

        if (mFinish)
            dismiss();
    }
}
