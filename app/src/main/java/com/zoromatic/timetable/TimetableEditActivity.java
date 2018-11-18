package com.zoromatic.timetable;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

public class TimetableEditActivity extends ThemeAppCompatActivity {
    @SuppressWarnings("unused")
    private static String LOG_TAG = "TimetableEditActivity";

    Spinner mSpinner;
    String mClassText;
    TimePicker mTimePickerStart;
    TimePicker mTimePickerEnd;
    EditText mDescription;

    private Long mRowId;
    private Long mDayId;
    private SQLiteDbAdapter mDbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mDbHelper == null)
            mDbHelper = new SQLiteDbAdapter(this);

        setContentView(R.layout.timetable_edit);

        mDescription = findViewById(R.id.description);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TypedValue outValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary,
                outValue,
                true);

        mSpinner = findViewById(R.id.spinnerClass);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.classes, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> parent,
                    View view,
                    int position,
                    long id) {
                mClassText = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mTimePickerStart = findViewById(R.id.timePickerStart);
        mTimePickerEnd = findViewById(R.id.timePickerEnd);

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
            actionBar.setTitle((mRowId == null || mRowId == -1) ? R.string.new_class : R.string.edit_class);
        }

        populateFields();
    }

    @SuppressWarnings("deprecation")
    private void populateFields() {
        Log.i(LOG_TAG, "populateFields");

        if (mRowId != null && mRowId != -1) {
            if (mDbHelper == null)
                mDbHelper = new SQLiteDbAdapter(this);

            mDbHelper.open();

            Cursor timetable = mDbHelper.fetchTimetableRow(mRowId);
            startManagingCursor(timetable);
            mSpinner.setSelection(timetable.getInt(
                    timetable.getColumnIndexOrThrow(SQLiteDbAdapter.KEY_CLASS_INDEX)));
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timetable_edit, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.done:
                if (saveClass())
                    finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean saveClass() {
        Log.i(LOG_TAG, "saveClass");

        if (mDbHelper == null)
            mDbHelper = new SQLiteDbAdapter(this);

        if (mDayId >= 0) {
            boolean result;

            if (mRowId == null || mRowId == -1) {
                mDbHelper.open();

                long id = mDbHelper.createTimetableClass(mDayId, mSpinner.getSelectedItemPosition(), mTimePickerStart.getCurrentHour(), mTimePickerStart.getCurrentMinute(),
                        mTimePickerEnd.getCurrentHour(), mTimePickerEnd.getCurrentMinute(), mDescription.getText().toString());

                mDbHelper.close();

                result = (id > -1);
            } else {
                mDbHelper.open();

                result = mDbHelper.updateTimetableClass(mRowId, mDayId, mSpinner.getSelectedItemPosition(), mTimePickerStart.getCurrentHour(), mTimePickerStart.getCurrentMinute(),
                        mTimePickerEnd.getCurrentHour(), mTimePickerEnd.getCurrentMinute(), mDescription.getText().toString());

                mDbHelper.close();
            }

            return result;
        } else {
            return false;
        }
    }
}