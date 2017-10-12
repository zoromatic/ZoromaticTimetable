package com.zoromatic.timetable;

import com.zoromatic.timetable.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class ToolbarPreference extends Preference {

	public ToolbarPreference(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ToolbarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ToolbarPreference(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public ToolbarPreference(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected View onCreateView(ViewGroup parent) {
		parent.setPadding(0, 0, 0, 0);

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.prefs_toolbar, parent, false);

		Toolbar toolbar = (Toolbar) layout.findViewById(R.id.toolbar);
		toolbar.setTitle(null);

		ImageView back = (ImageView) layout.findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PreferenceScreen prefScreen = (PreferenceScreen) getPreferenceManager().findPreference(getKey());
				prefScreen.getDialog().dismiss();
			}
		});

		TextView title = (TextView) layout.findViewById(R.id.title);
		title.setText(getTitle());

		return layout;
	}

}
