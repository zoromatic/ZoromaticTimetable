/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zoromatic.timetable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.zoromatic.timetable.TimetableActivity.TimetablePagerItem;

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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
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
	
	Context mContext = null;
	private static String LOG_TAG = "TimetableContentFr";
	private String mTitle = "";   
	SwipeRefreshLayout swipeLayout;
	private ListView mListView;
	static DataProviderTask mDataProviderTask;
    static TimetableContentFragment mTimetableFragment;
    private SQLiteDbAdapter mDbHelper;
    private long mSelectedRowId = -1;
    private long mDayId = -1;

	public static TimetableContentFragment newInstance(CharSequence title, int indicatorColor, long dayId) {
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

		mContext = (Context)getActivity();
		mTimetableFragment = this;
        mDbHelper = new SQLiteDbAdapter(mContext);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	@SuppressLint("InlinedApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {    	
		View view = inflater.inflate(R.layout.timetable_page, container, false);
		
		if (view == null)
			return null;
		
		mListView = (ListView)view.findViewById(R.id.listtimetables);
		
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
				
				((TimetableActivity)mContext).setActivityDelete(true);
		        setListViewItems(true);
		        
		        View tempView = getViewByPosition(position, mListView);
        		
        		if (tempView != null) {
	        		CheckBox checkBox = (CheckBox) tempView.findViewById(R.id.checkBoxSelect);
	        		
	        		if (checkBox != null) {
	        			checkBox.setChecked(true);
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
        		
        		if (swipeLayout != null)
        			swipeLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);				
        	}
        });

		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				((TimetableActivity)getActivity()).refreshData();				
			}
		});

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, 
					android.R.color.holo_green_light, 
					android.R.color.holo_orange_light, 
					android.R.color.holo_red_light);
		} else {
			swipeLayout.setColorSchemeColors(Color.BLUE, 
					Color.GREEN, 
					Color.YELLOW, 
					Color.RED);
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

	public SwipeRefreshLayout getSwipeLayout() {
		return swipeLayout;
	}
	
	public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
    
    void setListViewItems(final boolean activityDelete) {
    	
    	((Activity) mContext).runOnUiThread(new Runnable() {
			@Override
			public void run() {
		    	((AppCompatActivity) mContext).supportInvalidateOptionsMenu();
		    	
		    	if (mListView != null && mListView.getCount() > 0) {
		        	for (int i=0; i<mListView.getCount(); i++) {
		        		View view = getViewByPosition(i, mListView);
		        		
		        		if (view != null) {
		        			setListViewItem(view, activityDelete);
		        		}
		        	}
		        }
		    	
		    	if (!activityDelete) {
		    		((TimetableActivity)getActivity()).readCachedData();
		    	}
			}
    	});
    }
    
    private void setListViewItem(View view, boolean activityDelete) {
    	if (view == null)
    		return;
    	
    	TextView text = (TextView) view.findViewById(R.id.label);
		String strLabel = "L"; 
		
		if (text != null) {
			strLabel = text.getText().toString();
			
			if (strLabel.length() > 0)
				strLabel = strLabel.subSequence(0, 1).toString();
			else
				strLabel = "L";
		}
		
		ImageView image = (ImageView) view.findViewById(R.id.iconTimetable);
		if (image != null) {
			image.setVisibility(activityDelete?View.GONE:View.VISIBLE);	
			
			final Resources res = mContext.getResources();
		    final int tileSize = res.getDimensionPixelSize(R.dimen.letter_tile_size);

		    final LetterTileProvider tileProvider = new LetterTileProvider(mContext);
		    final Bitmap letterTile = tileProvider.getLetterTile(strLabel, strLabel, tileSize, tileSize);
		    
		    image.setImageBitmap(letterTile);		     		   
		}
		
		CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBoxSelect);
		if (checkBox != null) {
			checkBox.setVisibility(activityDelete?View.VISIBLE:View.GONE);
			
			if (!activityDelete)
				checkBox.setChecked(false);
		}
    }
    
    private class CustomSimpleCursorAdapter extends SimpleCursorAdapter {
    	@SuppressWarnings("deprecation")
		public CustomSimpleCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to); 
		}

		@Override
    	public View getView(int position, View convertView, ViewGroup parent) {  
    		View view = super.getView(position, convertView, parent);   		
    		
    		if (view != null) {
    			setListViewItem(view, false);
    		}
    		
       		return view;  
    	}
    }
    
    private class DataProviderTask extends AsyncTask<Void, Void, Void> {
    	
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
			Log.i(LOG_TAG, "ConfigureLocationActivity - Background thread starting");
        	
			timetableFragment.fillData();
        	
			return null;
		}
		
		@Override
	    protected void onPostExecute(Void result) {
        	
			((TimetableActivity)timetableFragment.mContext).setActivityDelete(false);
			timetableFragment.setListViewItems(false);
        }        	
   }
    
    void fillData() {
    	
    	((Activity) mContext).runOnUiThread(new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
		    	if (mDbHelper == null)
		    		mDbHelper = new SQLiteDbAdapter(mContext);
		    	
		    	Bundle args = getArguments();
				long dayId = -1;

				if (args != null) {
					dayId = args.getLong(SQLiteDbAdapter.KEY_DAY_INDEX);
				}
		    	
		    	mDbHelper.open();
		    	
		    	// Get all of the rows from the database and create the item list
		    	final Cursor timetableCursor = mDbHelper.fetchTimetables(dayId);
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
		                	String time = String.format(sdfTime.format(calendar.getTime()));
		                	
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
		                	String time = String.format(sdfTime.format(calendar.getTime()));
		                			            			
	            			TextView textView = (TextView) view;
		                	textView.setText(time);
		                	
		                	return true;
		                }
		                return false;
		            }
		        });
		       
		       	mListView.setAdapter(notes);  
		       	((Activity) mContext).stopManagingCursor(timetableCursor);
		       	mDbHelper.close();		       		       	
			}
        });              	       	       	           
    }        
    
    public void refreshFragment() {
    	mDataProviderTask = new DataProviderTask();
        mDataProviderTask.setFragment(this);
        mDataProviderTask.execute();
    }
    
    public void deleteTimetable() {
    	if (mListView != null && mListView.getCount() > 0) {
    		CustomSimpleCursorAdapter cursorAdapter = (CustomSimpleCursorAdapter)mListView.getAdapter();
    		
        	for (int i=mListView.getCount()-1; i>=0; i--) {
        		View tempView = getViewByPosition(i, mListView);
        		
        		if (tempView != null) {
	        		CheckBox checkBox = (CheckBox) tempView.findViewById(R.id.checkBoxSelect);
	        		
	        		if (checkBox != null && checkBox.isChecked()) {
	        			int id = (int) cursorAdapter.getItemId(i);			
						
	        			mDbHelper.open();
			            mDbHelper.deleteTimetable(id);
			            mDbHelper.close();			           			            
	        		}	        			
        		}
        	}
        	
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
			    	mDataProviderTask = new DataProviderTask();
			        mDataProviderTask.setFragment(fragment);
			        mDataProviderTask.execute();
	    		}
    		}                        
        }
    }

	public static long daysBetween(final Calendar startDate, final Calendar endDate) {  
		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;  
		long endInstant = endDate.getTimeInMillis();  
		int presumedDays = (int) ((endInstant - startDate.getTimeInMillis()) / MILLIS_IN_DAY);  
		Calendar cursor = (Calendar) startDate.clone();  
		cursor.add(Calendar.DAY_OF_YEAR, presumedDays);  
		long instant = cursor.getTimeInMillis();  
		if (instant == endInstant)  
			return presumedDays;  
		final int step = instant < endInstant ? 1 : -1;  
		do {  
			cursor.add(Calendar.DAY_OF_MONTH, step);  
			presumedDays += step;  
		} while (cursor.getTimeInMillis() != endInstant);  
		return presumedDays;  
	}
}
