package com.zoromatic.timetable;

import com.alertdialogpro.AlertDialogPro;
import com.zoromatic.timetable.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;

public class DialogPreferencePro extends DialogPreference {
	private AlertDialogPro.Builder mBuilder;
	private Dialog mDialog;
	private int mWhichButtonClicked;
	
	private Context context;
	
	public DialogPreferencePro(Context context, AttributeSet attrs) {
		super(context, attrs); 	
		
		this.context = context;			
	}

	public DialogPreferencePro(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);			
	}	
	
	@Override
	protected void showDialog(Bundle state) {
		mWhichButtonClicked = DialogInterface.BUTTON_NEGATIVE;
		
		String theme = Preferences.getMainTheme(getContext());    

		mBuilder = new AlertDialogPro.Builder(context, 
				theme.compareToIgnoreCase("light") == 0?R.style.Theme_AlertDialogPro_Material_Light:R.style.Theme_AlertDialogPro_Material);
		mBuilder.setTitle(getTitle());
		mBuilder.setIcon(getDialogIcon());
		mBuilder.setNegativeButton(getNegativeButtonText(), this);
		mBuilder.setPositiveButton(getPositiveButtonText(), this);

		final View contentView = onCreateDialogView();
		
	    if (contentView != null) {
	      onBindDialogView(contentView);
	      mBuilder.setView(contentView);
	    }
	    else
	      mBuilder.setMessage(getDialogMessage());
	    
	    // Create the dialog
        final Dialog dialog = mDialog = mBuilder.create();
        
        if (state != null) {
            dialog.onRestoreInstanceState(state);
        }
        
        dialog.setOnDismissListener(this);
        dialog.show();
	}
	
	public void onClick(DialogInterface dialog, int which) {
        mWhichButtonClicked = which;
    }
    
    public void onDismiss(DialogInterface dialog) {
        
        mDialog = null;
        onDialogClosed(mWhichButtonClicked == DialogInterface.BUTTON_POSITIVE);
    }

    /**
     * Called when the dialog is dismissed and should be used to save data to
     * the {@link SharedPreferences}.
     * 
     * @param positiveResult Whether the positive button was clicked (true), or
     *            the negative button was clicked or the dialog was canceled (false).
     */
    protected void onDialogClosed(boolean positiveResult) {
    }

    /**
     * Gets the dialog that is shown by this preference.
     * 
     * @return The dialog, or null if a dialog is not being shown.
     */
    public Dialog getDialog() {
        return mDialog;
    }

    /**
     * {@inheritDoc}
     */
    public void onActivityDestroy() {
        
        if (mDialog == null || !mDialog.isShowing()) {
            return;
        }
        
        mDialog.dismiss();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (mDialog == null || !mDialog.isShowing()) {
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.isDialogShowing = true;
        myState.dialogBundle = mDialog.onSaveInstanceState();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        if (myState.isDialogShowing) {
            showDialog(myState.dialogBundle);
        }
    }

    private static class SavedState extends BaseSavedState {
        boolean isDialogShowing;
        Bundle dialogBundle;
        
        public SavedState(Parcel source) {
            super(source);
            isDialogShowing = source.readInt() == 1;
            dialogBundle = source.readBundle();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(isDialogShowing ? 1 : 0);
            dest.writeBundle(dialogBundle);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @SuppressWarnings("unused")
		public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
