package com.zoromatic.timetable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

@SuppressLint("SimpleDateFormat")
public class TimetableContentFragment extends Fragment {
    private static final String KEY_TITLE = "title";
    private static final String KEY_INDICATOR_COLOR = "indicator_color";
    private static String LOG_TAG = "TimetableContentFragment";
    private static final String SELECTED_CLASSES = "selected_classes";

    Context mContext = null;
    private String mTitle = "";
    SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;
    static DataProviderTask dataProviderTask;
    private SQLiteDbAdapter mSQLiteDbAdapter;
    private long mSelectedRowId = -1;
    private long mDayId = -1;

    private ArrayList<String> mSelectedClasses = new ArrayList<>();

    public static TimetableContentFragment newInstance(CharSequence title, int indicatorColor, long dayId) {
        Log.i(LOG_TAG, "newInstance");

        Bundle bundle = new Bundle();
        bundle.putCharSequence(KEY_TITLE, title);
        bundle.putInt(KEY_INDICATOR_COLOR, indicatorColor);
        bundle.putLong(SQLiteDbAdapter.KEY_DAY_INDEX, dayId);

        TimetableContentFragment fragment = new TimetableContentFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
        mSQLiteDbAdapter = new SQLiteDbAdapter(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putStringArrayList(SELECTED_CLASSES, mSelectedClasses);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @SuppressLint("InlinedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timetable_page, container, false);

        if (view == null)
            return null;

        mListView = view.findViewById(R.id.listtimetables);

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mSelectedRowId = id;

                Bundle args = getArguments();

                if (args != null) {
                    mDayId = args.getLong(SQLiteDbAdapter.KEY_DAY_INDEX);
                    Intent intentEdit = new Intent(getActivity(), TimetableEditActivity.class);
                    intentEdit.putExtra(SQLiteDbAdapter.KEY_DAY_INDEX, mDayId);
                    intentEdit.putExtra(SQLiteDbAdapter.KEY_ROW_ID, mSelectedRowId);
                    getActivity().startActivityForResult(intentEdit, TimetableActivity.ACTIVITY_EDIT);
                }
            }
        });

        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                ((TimetableActivity) mContext).setActivityDelete(true);
                setListViewItems(true);

                View tempView = getViewByPosition(position, mListView);

                if (tempView != null) {
                    CheckBox checkBox = tempView.findViewById(R.id.checkBoxSelect);

                    if (checkBox != null) {
                        checkBox.setChecked(true);

                        if (!mSelectedClasses.contains(String.valueOf(id))) {
                            mSelectedClasses.add(String.valueOf(id));
                        }
                    }
                }

                return true;
            }
        });

        mListView.setOnScrollListener(new ListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (mListView == null || mListView.getChildCount() == 0) ?
                                0 : mListView.getChildAt(0).getTop();

                if (mSwipeLayout != null)
                    mSwipeLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

        mSwipeLayout = view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((TimetableActivity) getActivity()).refreshData();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        } else {
            mSwipeLayout.setColorSchemeColors(Color.BLUE,
                    Color.GREEN,
                    Color.YELLOW,
                    Color.RED);
        }

        if (savedInstanceState != null) {
            mSelectedClasses = savedInstanceState.getStringArrayList(SELECTED_CLASSES);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();

        if (args != null) {
            setTitle((String) args.getCharSequence(KEY_TITLE));
            refreshFragment();
        }
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    void setListViewItems(final boolean activityDelete) {
        Log.i(LOG_TAG, "setListViewItems activityDelete=" + activityDelete);

        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mListView != null && mListView.getCount() > 0) {
                    CustomSimpleCursorAdapter cursorAdapter = (CustomSimpleCursorAdapter) mListView.getAdapter();

                    for (int i = 0; i < mListView.getCount(); i++) {
                        View view = getViewByPosition(i, mListView);
                        long id = cursorAdapter.getItemId(i);

                        if (view != null) {
                            setListViewItem(view, id, activityDelete);
                        }
                    }
                }
            }
        });
    }

    private void setListViewItem(View view, final long itemId, boolean activityDelete) {
        Log.i(LOG_TAG, "setListViewItem activityDelete=" + activityDelete);

        if (view == null)
            return;

        TextView text = view.findViewById(R.id.label);
        String strLabel = "L";

        if (text != null) {
            strLabel = text.getText().toString();

            if (strLabel.length() > 0)
                strLabel = strLabel.subSequence(0, 1).toString();
            else
                strLabel = "L";
        }

        ImageView image = view.findViewById(R.id.iconTimetable);
        if (image != null) {
            image.setVisibility(activityDelete ? View.GONE : View.VISIBLE);

            final Resources res = mContext.getResources();
            final int tileSize = res.getDimensionPixelSize(R.dimen.letter_tile_size);

            final LetterTileProvider tileProvider = new LetterTileProvider(mContext);
            final Bitmap letterTile = tileProvider.getLetterTile(strLabel, strLabel, tileSize, tileSize);

            image.setImageBitmap(letterTile);
        }

        final CheckBox checkBox = view.findViewById(R.id.checkBoxSelect);
        if (checkBox != null) {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (!mSelectedClasses.contains(String.valueOf(itemId)))
                            mSelectedClasses.add(String.valueOf(itemId));
                    } else {
                        mSelectedClasses.remove(String.valueOf(itemId));
                    }
                }
            });

            checkBox.setVisibility(activityDelete ? View.VISIBLE : View.GONE);

            if (!activityDelete) {
                checkBox.setChecked(false);
            } else {
                if (mSelectedClasses.contains(String.valueOf(itemId))) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
            }
        }
    }

    public SwipeRefreshLayout getSwipeLayout() {
        return mSwipeLayout;
    }

    private class CustomSimpleCursorAdapter extends SimpleCursorAdapter {
        @SuppressWarnings("deprecation")
        Context mContext;

        CustomSimpleCursorAdapter(Context context, int layout, Cursor c,
                                  String[] from, int[] to) {
            super(context, layout, c, from, to);

            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            if (view != null) {
                setListViewItem(view, getItemId(position), ((TimetableActivity) mContext).isActivityDelete());
            }

            return view;
        }
    }

    private static class DataProviderTask extends AsyncTask<Void, Void, Void> {

        TimetableContentFragment timetableFragment = null;

        void setFragment(TimetableContentFragment fragment) {
            timetableFragment = fragment;
        }

        @SuppressWarnings("unused")
        TimetableContentFragment getFragment() {
            return timetableFragment;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(LOG_TAG, "DataProviderTask.doInBackground");

            timetableFragment.fillData();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(LOG_TAG, "DataProviderTask.onPostExecute");

            //((TimetableActivity) timetableFragment.mContext).setActivityDelete(false);
            timetableFragment.setListViewItems(((TimetableActivity) timetableFragment.mContext).isActivityDelete());
        }
    }

    void fillData() {
        Log.i(LOG_TAG, "fillData");

        ((Activity) mContext).runOnUiThread(new Runnable() {
            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                if (mSQLiteDbAdapter == null)
                    mSQLiteDbAdapter = new SQLiteDbAdapter(mContext);

                Bundle args = getArguments();
                long dayId = -1;

                if (args != null) {
                    dayId = args.getLong(SQLiteDbAdapter.KEY_DAY_INDEX);
                }

                mSQLiteDbAdapter.open();

                // Get all of the rows from the database and create the item list
                final Cursor timetableCursor = mSQLiteDbAdapter.fetchTimetableByDay(dayId);
                ((Activity) mContext).startManagingCursor(timetableCursor);

                // Create an array to specify the fields we want to display in the list (only TITLE)
                final String[] from = new String[]{SQLiteDbAdapter.KEY_DESCRIPTION, SQLiteDbAdapter.KEY_START_HOUR, SQLiteDbAdapter.KEY_END_HOUR};

                // and an array of the fields we want to bind those fields to (in this case just text1)
                final int[] to = new int[]{R.id.label, R.id.timeFrom, R.id.timeTo};

                // Now create a simple cursor adapter and set it to display
                CustomSimpleCursorAdapter notes =
                        new CustomSimpleCursorAdapter(mContext, R.layout.timetable_row, timetableCursor, from, to);

                notes.setViewBinder(new CustomSimpleCursorAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                        if (view.getId() == R.id.label) {
                            int getIndex = cursor.getColumnIndex(SQLiteDbAdapter.KEY_CLASS_INDEX);
                            long classId = cursor.getLong(getIndex);

                            final Resources res = getResources();
                            TypedArray arrayClasses = res.obtainTypedArray(R.array.classes);

                            String name = arrayClasses.getString((int) classId);

                            TextView textView = (TextView) view;
                            textView.setText(name);

                            arrayClasses.recycle();

                            return true;
                        }

                        if (view.getId() == R.id.timeFrom) {
                            int getIndexHour = cursor.getColumnIndex(SQLiteDbAdapter.KEY_START_HOUR);
                            int getIndexMinute = cursor.getColumnIndex(SQLiteDbAdapter.KEY_START_MINUTE);

                            int hour = cursor.getInt(getIndexHour);
                            int minute = cursor.getInt(getIndexMinute);

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR_OF_DAY, hour);
                            calendar.set(Calendar.MINUTE, minute);

                            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
                            String time = sdfTime.format(calendar.getTime());

                            TextView textView = (TextView) view;
                            textView.setText(time);

                            return true;
                        }

                        if (view.getId() == R.id.timeTo) {
                            int getIndexHour = cursor.getColumnIndex(SQLiteDbAdapter.KEY_END_HOUR);
                            int getIndexMinute = cursor.getColumnIndex(SQLiteDbAdapter.KEY_END_MINUTE);

                            int hour = cursor.getInt(getIndexHour);
                            int minute = cursor.getInt(getIndexMinute);

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR_OF_DAY, hour);
                            calendar.set(Calendar.MINUTE, minute);

                            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
                            String time = sdfTime.format(calendar.getTime());

                            TextView textView = (TextView) view;
                            textView.setText(time);

                            return true;
                        }
                        return false;
                    }
                });

                mListView.setAdapter(notes);
                ((Activity) mContext).stopManagingCursor(timetableCursor);
                mSQLiteDbAdapter.close();
            }
        });
    }

    public void refreshFragment() {
        Log.i(LOG_TAG, "refreshFragment");

        dataProviderTask = new DataProviderTask();
        dataProviderTask.setFragment(this);
        dataProviderTask.execute();
    }

    public void deleteTimetableClass() {
        Log.i(LOG_TAG, "deleteTimetableClass");

        if (mListView != null && mListView.getCount() > 0) {
            CustomSimpleCursorAdapter cursorAdapter = (CustomSimpleCursorAdapter) mListView.getAdapter();

            for (int i = mListView.getCount() - 1; i >= 0; i--) {
                View tempView = getViewByPosition(i, mListView);

                if (tempView != null) {
                    CheckBox checkBox = tempView.findViewById(R.id.checkBoxSelect);

                    if (checkBox != null && checkBox.isChecked()) {
                        int id = (int) cursorAdapter.getItemId(i);

                        mSQLiteDbAdapter.open();
                        mSQLiteDbAdapter.deleteTimetableClass(id);
                        mSQLiteDbAdapter.close();
                    }
                }
            }

            TimetableActivity timetableActivity = (TimetableActivity) getActivity();

            if (timetableActivity != null) {
                ViewPager viewPager = timetableActivity.getViewPager();
                TimetableContentFragment fragment;
                TimetableActivity.TimetableFragmentPagerAdapter fragmentPagerAdapter = timetableActivity.getFragmentPagerAdapter();

                if (fragmentPagerAdapter != null && viewPager != null && viewPager.getCurrentItem() >= 0) {
                    fragment = (TimetableContentFragment) fragmentPagerAdapter.getFragment(viewPager.getCurrentItem());

                    if (fragment != null) {
                        refreshFragment();
                    }
                }
            }
        }
    }
}
