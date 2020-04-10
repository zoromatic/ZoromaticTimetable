package com.zoromatic.timetable;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import android.widget.EditText;

public class AlertDialogFragment extends DialogFragment {
    public static final int TEXT_ID = 0x2906;

    public AlertDialogFragment() {

    }

    @NonNull
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
        AlertDialog.Builder builder = null;

        if (editBox) {
            TimetableActivity timetableActivity = (TimetableActivity) getActivity();
            if (timetableActivity != null) {
                ViewPager viewPager = timetableActivity.getViewPager();
                TimetableContentFragment fragment;
                TimetableActivity.TimetableFragmentPagerAdapter fragmentPagerAdapter = timetableActivity.getFragmentPagerAdapter();

                if (fragmentPagerAdapter != null && viewPager != null && viewPager.getCurrentItem() >= 0) {
                    fragment = (TimetableContentFragment) fragmentPagerAdapter.getFragment(viewPager.getCurrentItem());

                    if (fragment != null) {
                        builder = new AlertDialog.Builder(getActivity(),
                                theme.compareToIgnoreCase("light") == 0 ? R.style.AppCompatAlertDialogStyleLight : R.style.AppCompatAlertDialogStyle)
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
        }

        return builder.show();
    }
}
