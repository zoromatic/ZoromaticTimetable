/* The following code was written by Matthew Wiggins 
 * and is released under the APACHE 2.0 license 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.zoromatic.timetable;

import com.zoromatic.timetable.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.LinearLayout;

@SuppressLint("UseValueOf")
public class SeekBarPreference extends DialogPreferencePro implements
		OnSeekBarChangeListener {
	private static final String androidns = "http://schemas.android.com/apk/res/android";

	private SeekBar mSeekBar;
	private TextView mSplashText, mValueText;
	//private Context mContext;

	private String mDialogMessage, mSuffix;
	private int mDefault = 100, mMax, mValue = 100, mOriginalValue = 0;
	
	public SeekBarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		//mContext = context;

		mDialogMessage = context.getResources().getString(R.string.settings);
		mSuffix = attrs.getAttributeValue(androidns, "text");
		mDefault = attrs.getAttributeIntValue(androidns, "defaultValue", 100);
		mMax = attrs.getAttributeIntValue(androidns, "max", 100);		
	}

	public SeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected View onCreateDialogView() {
		LinearLayout.LayoutParams params;
		LinearLayout layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(6, 6, 6, 6);

		mSplashText = new TextView(getContext());
		if (mDialogMessage != null)
			mSplashText.setText(mDialogMessage);
		layout.addView(mSplashText);

		mValueText = new TextView(getContext());
		mValueText.setGravity(Gravity.CENTER_HORIZONTAL);
		//mValueText.setTextSize(32);
		params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		layout.addView(mValueText, params);

		mSeekBar = new SeekBar(getContext());
		mSeekBar.setOnSeekBarChangeListener(this);
		layout.addView(mSeekBar, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

		mValue = shouldPersist() ? getPersistedInt(mDefault) : mDefault;
		
		String t = String.valueOf(mValue);
		mValueText.setText(mSuffix == null ? t : t.concat(mSuffix));

		Drawable drawProgress = getContext().getResources().getDrawable(R.drawable.seekbar_progress);
		mSeekBar.setProgressDrawable(drawProgress);
		
		Drawable drawThumb = getContext().getResources().getDrawable(R.drawable.thumb);
		mSeekBar.setThumb(drawThumb);
		
		mSeekBar.setMax(mMax);
		mSeekBar.setProgress(mValue);
		
		mOriginalValue = mValue;
		
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
		
		return layout;
	}

	@Override
	protected void onBindDialogView(View v) {
		super.onBindDialogView(v);
		mSeekBar.setMax(mMax);
		mSeekBar.setProgress(mValue);
	}

	@Override
	protected void onSetInitialValue(boolean restore, Object defaultValue) {
		super.onSetInitialValue(restore, defaultValue);
		if (restore)
			mValue = shouldPersist() ? getPersistedInt(mDefault) : mDefault;
		else
			mValue = (Integer) defaultValue;
	}

	@Override
	public void onProgressChanged(SeekBar seek, int value, boolean fromTouch) {
		String t = String.valueOf(value);
		mValueText.setText(mSuffix == null ? t : t.concat(mSuffix));
		if (shouldPersist())
			persistInt(value);
		callChangeListener(new Integer(value));
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		//callChangeListener(new Integer(mValue));
	}	
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	    switch (which) {
	    case DialogInterface.BUTTON_POSITIVE:
	    	callChangeListener(new Integer(mValue));
	        break;
	    default:
	    	setProgress(mOriginalValue);
	    	callChangeListener(new Integer(mOriginalValue));
	        break;
	    }
	} 

	public void setMax(int max) {
		mMax = max;
	}

	public int getMax() {
		return mMax;
	}

	public void setProgress(int progress) {
		if (shouldPersist())
			persistInt(progress);
		
		mValue = progress;
		if (mSeekBar != null)
			mSeekBar.setProgress(progress);
	}

	public int getProgress() {
		return shouldPersist() ? getPersistedInt(mValue) : mDefault;
	}
}
