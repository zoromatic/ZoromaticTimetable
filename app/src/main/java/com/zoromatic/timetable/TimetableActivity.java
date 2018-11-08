package com.zoromatic.timetable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

@SuppressLint({ "SimpleDateFormat", "RtlHardcoded" })
public class TimetableActivity extends ThemeAppCompatActivity {

	static final int ACTIVITY_CREATE = 10;
    static final int ACTIVITY_EDIT = 11;
    
	private static String LOG_TAG = "TimetableActivity";
	private static final int ACTIVITY_SETTINGS = 0;
	private Toolbar toolbar;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private ListView leftDrawerList;
	private List<RowItem> rowItems;

	public static final String DRAWEROPEN = "draweropen";
	private boolean mDrawerOpen = false;

	private TabLayout mSlidingTabLayout;
	private ViewPager mViewPager;
	private TimetableFragmentPagerAdapter mFragmentPagerAdapter; 

	private List<TimetablePagerItem> mTabs = new ArrayList<TimetablePagerItem>();
	private int mCurrentItem = 0;
	private static final String KEY_CURRENT_ITEM = "key_current_item";

	private MenuItem refreshItem = null;
	Animation rotation = null;
	
	private boolean mActivityDelete = false;
	
	static DataProviderTask mDataProviderTask;
    static TimetableActivity mTimetableActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String lang = Preferences.getLanguageOptions(this);

		if (lang.equals("")) {
			String langDef = Locale.getDefault().getLanguage();

			if (!langDef.equals(""))
				lang = langDef;
			else
				lang = "en";

			Preferences.setLanguageOptions(this, lang);                
		}

		// Change locale settings in the application
		final Resources res = getApplicationContext().getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		android.content.res.Configuration conf = res.getConfiguration();
		conf.locale = new Locale(lang.toLowerCase());
		res.updateConfiguration(conf, dm);

		setContentView(R.layout.timetable);

		initView();
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		initDrawer();

		TypedValue outValue = new TypedValue();
		getTheme().resolveAttribute(R.attr.colorPrimary,
				outValue,
				true);

		rotation = AnimationUtils.loadAnimation(this, R.anim.animate_menu);
		
		mTimetableActivity = this;
		
		mDataProviderTask = new DataProviderTask();
        mDataProviderTask.setActivity(mTimetableActivity);
        mDataProviderTask.execute(); 
        
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
            actionBar.setTitle(R.string.app_name);
        }
	}
	
	@SuppressLint("NewApi")
	public void refreshData() {
		drawerLayout.closeDrawers();
		mDrawerOpen = false;
			
		if (refreshItem != null && rotation != null) {
			
			if (refreshItem.getActionView() != null) {
				refreshItem.getActionView().startAnimation(rotation);
				
				readCachedData();
			}									
		}		
	}

	public void openSettings() {
		drawerLayout.closeDrawers();
		mDrawerOpen = false;

		Intent settingsIntent = new Intent(getApplicationContext(), ZoromaticTimetablePreferenceActivity.class);				

		if (settingsIntent != null) {
			startActivityForResult(settingsIntent, ACTIVITY_SETTINGS);
		}
	}

	private class DataProviderTask extends AsyncTask<Void, Void, Void> {
    	
	TimetableActivity timetableActivity = null;
    	
    	void setActivity(TimetableActivity activity) {
    		timetableActivity = activity;
		}
    	
    	@SuppressWarnings("unused")
		TimetableActivity getActivity() {
    		return timetableActivity;
		}
    	
    	@Override
		protected void onPreExecute() {
    		 	
        }

		@Override
		protected Void doInBackground(Void... params) {
			Log.i(LOG_TAG, "TimetableActivity - Background thread starting");
        	
			timetableActivity.loadData();
        	
			return null;
		}
		
		@Override
	    protected void onPostExecute(Void result) {
			timetableActivity.setSliders();	
        }        	
   }

	private void initView() {
		String theme = Preferences.getMainTheme(this);

		leftDrawerList = findViewById(R.id.left_drawer);
		toolbar = findViewById(R.id.toolbar);
		drawerLayout = findViewById(R.id.drawerLayout);

		rowItems = new ArrayList<>();

		RowItem item = new RowItem(theme.compareToIgnoreCase("light") == 0?R.drawable.ic_refresh_black_48dp:R.drawable.ic_refresh_white_48dp, 
				(String) getResources().getText(R.string.refresh));
		rowItems.add(item);
		item = new RowItem(theme.compareToIgnoreCase("light") == 0?R.drawable.ic_settings_black_48dp:R.drawable.ic_settings_white_48dp, 
				(String) getResources().getText(R.string.settings));
		rowItems.add(item);

		ItemAdapter adapter = new ItemAdapter(this, rowItems);
		leftDrawerList.setAdapter(adapter);        

		leftDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		if (theme.compareToIgnoreCase("light") == 0)
			leftDrawerList.setBackgroundColor(getResources().getColor(android.R.color.white));
		else 
			leftDrawerList.setBackgroundColor(getResources().getColor(android.R.color.black));
	}

	private void initDrawer() {

		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				mDrawerOpen = false;
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				mDrawerOpen = true;
			}
		};
		drawerLayout.addDrawerListener(drawerToggle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();		
	}
	
	// The click listener for ListView in the navigation drawer 
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		leftDrawerList.setItemChecked(position, true);
		drawerLayout.closeDrawers(); 
		mDrawerOpen = false;

		switch (position) {
		case 0: // Refresh
			refreshData();
			break;
		case 1: // Settings
			openSettings();
			break;
		default:
			break;
		}            	
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);		
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putBoolean(DRAWEROPEN, mDrawerOpen);

		if (mViewPager != null)
			savedInstanceState.putInt(KEY_CURRENT_ITEM, mViewPager.getCurrentItem());

		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// Always call the superclass so it can restore the view hierarchy
		super.onRestoreInstanceState(savedInstanceState);

		mDrawerOpen = savedInstanceState.getBoolean(DRAWEROPEN);
		mCurrentItem = savedInstanceState.getInt(KEY_CURRENT_ITEM);

		if (mDrawerOpen) {
			if (drawerLayout != null) {
				drawerLayout.openDrawer(Gravity.LEFT);
				mDrawerOpen = true;
				drawerToggle.syncState();
			}		
		}			
	}
	
	@Override
    public void onBackPressed() {
    	if (isActivityDelete()) {
    		setActivityDelete(false);
    		
    		if (mTabs != null && mViewPager != null) {
    			TimetableContentFragment fragment = (mTabs.get(mViewPager.getCurrentItem())).getFragment();   
    			
    			if (fragment != null)
    				fragment.setListViewItems(false);	
    		}
    	}
    	else {
    		if (mDataProviderTask != null)
    			mDataProviderTask.cancel(true);
    		
    		if (!mDrawerOpen) {
    			super.onBackPressed();
    			finish();
    		} else {
    			if (drawerLayout != null)
    				drawerLayout.closeDrawers();
    			mDrawerOpen = false;
    		}
    	}              
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.insert).setVisible(!isActivityDelete());
        menu.findItem(R.id.delete).setVisible(isActivityDelete());
        return true;
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.timetable, menu);
		
		refreshItem = menu.findItem(R.id.refresh);
		
		if (refreshItem != null) {
			final Menu menuFinal = menu;

			if (refreshItem.getActionView() != null) {
				TypedValue outValue = new TypedValue();
				getTheme().resolveAttribute(R.attr.iconRefresh,
						outValue,
						true);
				int refreshIcon = outValue.resourceId;
				((ImageView)refreshItem.getActionView()).setImageResource(refreshIcon);

				refreshItem.getActionView().setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {   
						menuFinal.performIdentifierAction(refreshItem.getItemId(), 0);
					}
				});
			}
		}
		
		return true;				
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.refresh:
			refreshData();

			return true;
		case R.id.settings:	    	
			openSettings();

			return true;			
		case R.id.insert:
            if (mTabs != null && mViewPager != null) {
    			TimetableContentFragment fragment = (mTabs.get(mViewPager.getCurrentItem())).getFragment();
    			
    			if (fragment != null) {
	    			Bundle args = fragment.getArguments();
					long dayId;
	
					if (args != null) {
						dayId = args.getLong(SQLiteDbAdapter.KEY_DAY_INDEX);
						
						Intent intentAdd = new Intent(this, TimetableEditActivity.class);
						intentAdd.putExtra(SQLiteDbAdapter.KEY_DAY_INDEX, dayId);
				    	startActivityForResult(intentAdd, ACTIVITY_CREATE);
					}
    			}
				
    			//fragment.addTimetable();	
    		}
			
			return true;
		case R.id.delete:
			if (mTabs != null && mViewPager != null) {
    			TimetableContentFragment fragment = (mTabs.get(mViewPager.getCurrentItem())).getFragment();    		
    			fragment.deleteTimetable();	
    		}
            
            return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		TimetableContentFragment fragment = null;
		
		switch (requestCode) {
		case ACTIVITY_SETTINGS:
			Intent intentLocal = getIntent();
        	finish();
        	startActivity(intentLocal);
			break;
		case ACTIVITY_CREATE:
		case ACTIVITY_EDIT:
			if (mTabs != null && mViewPager != null) {
    			fragment = (mTabs.get(mViewPager.getCurrentItem())).getFragment();	    			
    		}
    		
    		if (fragment != null) {			    	
		    	fragment.refreshFragment();
    		}
    		
			break;
		default:
			break;
		}		    	    	     
	}

	@SuppressLint("ResourceType")
	public boolean loadData() {
		try {
			if (mTabs != null) {
				for (TimetablePagerItem tab : mTabs) {
					tab.setFragment(null);
				}

				mTabs.clear();			        

				TypedValue outValue = new TypedValue();
				getTheme().resolveAttribute(R.attr.tabTextColor, outValue, true);
				int textColor = outValue.resourceId;
				int colorIndicator = getResources().getColor(textColor);	
				
				final Resources res = getApplicationContext().getResources();
				TypedArray array = res.obtainTypedArray(R.array.days_week); 
				
				mTabs.add(new TimetablePagerItem(array.getString(0), colorIndicator, 0));
				mTabs.add(new TimetablePagerItem(array.getString(1), colorIndicator, 1));
				mTabs.add(new TimetablePagerItem(array.getString(2), colorIndicator, 2));
				mTabs.add(new TimetablePagerItem(array.getString(3), colorIndicator, 3));
				mTabs.add(new TimetablePagerItem(array.getString(4), colorIndicator, 4));
				
				array.recycle();
			}

			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public boolean setSliders() {
		if (mTabs != null) {
			TypedValue outValue = new TypedValue();
			getTheme().resolveAttribute(R.attr.colorPrimary,
					outValue,
					true);
			int primaryColor = getResources().getColor(outValue.resourceId);

			getTheme().resolveAttribute(R.attr.colorPrimaryDark,
					outValue,
					true);
			int primaryColorDark = getResources().getColor(outValue.resourceId);

			getTheme().resolveAttribute(R.attr.tabTextColor,
					outValue,
					true);
			int tabTextColor = getResources().getColor(outValue.resourceId);

			mSlidingTabLayout = findViewById(R.id.sliding_tabs);
			mViewPager = findViewById(R.id.viewpager);
			
			if (mFragmentPagerAdapter == null)
				mFragmentPagerAdapter = new TimetableFragmentPagerAdapter(getSupportFragmentManager());

			if (mViewPager != null) {
				mViewPager.setAdapter(mFragmentPagerAdapter);			

				final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
				mViewPager.setPageMargin(pageMargin);								
			}

			if (mSlidingTabLayout != null) {
				mSlidingTabLayout.setBackgroundColor(primaryColor);
				mSlidingTabLayout.setSelectedTabIndicatorColor(tabTextColor);

				int colorScheme = Preferences.getMainColorScheme(this);

				switch (colorScheme) {
					case 0: // black
						mSlidingTabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.sysWhite), tabTextColor);
						break;
					case 1: // white
						mSlidingTabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.sysBlack), tabTextColor);
						break;
					default:
						mSlidingTabLayout.setTabTextColors(primaryColorDark, tabTextColor);
						break;
				}

				mSlidingTabLayout.setupWithViewPager(mViewPager);
			}

			if (mViewPager != null && mViewPager.getChildCount() > 0) 
				mViewPager.setCurrentItem(Math.min(mCurrentItem, mTabs.size()-1));
		}

		return true;
	}

	@SuppressLint("NewApi")
	void readCachedData() {
		if (refreshItem != null) {
			
			if (refreshItem.getActionView() != null) {
				refreshItem.getActionView().clearAnimation();
			}
		}
		
		if (mTabs != null) {
			for (TimetablePagerItem tab : mTabs) {
				TimetableContentFragment fragment = tab.getFragment();

				if (fragment != null && fragment.getView() != null) {
					fragment.fillData();
					
					SwipeRefreshLayout swipeLayoutFragment = fragment.getSwipeLayout();
					if (swipeLayoutFragment != null) {
						swipeLayoutFragment.setRefreshing(false);
					}
				}	            				            			                	
			}  	                		                
		}		
	}

	@Override
	protected void onResume() {
		super.onResume();		             
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		if (mViewPager != null) { 
			mViewPager.getAdapter().notifyDataSetChanged();    		
			mCurrentItem = mViewPager.getCurrentItem();
		}
	}

	/**
	 * @return the mActivityDelete
	 */
	public boolean isActivityDelete() {
		return mActivityDelete;
	}

	/**
	 * @param mActivityDelete the mActivityDelete to set
	 */
	public void setActivityDelete(boolean mActivityDelete) {
		this.mActivityDelete = mActivityDelete;
	}

	/**
	 * @return the mViewPager
	 */
	public ViewPager getViewPager() {
		return mViewPager;
	}

	/**
	 * @param mViewPager the mViewPager to set
	 */
	public void setViewPager(ViewPager mViewPager) {
		this.mViewPager = mViewPager;
	}

	/**
	 * @return the mTabs
	 */
	public List<TimetablePagerItem> getTabs() {
		return mTabs;
	}

	/**
	 * @param mTabs the mTabs to set
	 */
	public void setTabs(List<TimetablePagerItem> mTabs) {
		this.mTabs = mTabs;
	}

	static class TimetablePagerItem {
		private CharSequence mTitle;
		private final int mIndicatorColor;
		private long mDayId;

		private TimetableContentFragment mFragment;

		TimetablePagerItem(CharSequence title, int indicatorColor, long dayId) {
			mTitle = title;
			mIndicatorColor = indicatorColor;
			mDayId = dayId;
		}

		Fragment createFragment() {
			Fragment fragment = TimetableContentFragment.newInstance(mTitle, mIndicatorColor, mDayId);
			((TimetableContentFragment) fragment).setTitle((String) mTitle);
			mFragment = (TimetableContentFragment) fragment;            

			return fragment;
		}

		CharSequence getTitle() {
			return mTitle;
		}

		void setTitle (CharSequence title) {
			mTitle = title;
		}

		int getIndicatorColor() {
			return mIndicatorColor;
		}

		public TimetableContentFragment getFragment() {
			return mFragment;
		}

		public void setFragment(TimetableContentFragment mFragment) {
			this.mFragment = mFragment;
		}        
	}

	class TimetableFragmentPagerAdapter extends FragmentStatePagerAdapter {

		TimetableFragmentPagerAdapter(FragmentManager fm) {
			super(fm);                        	       	        
		}

		@Override
		public Fragment getItem(int i) {
			return mTabs.get(i).createFragment();        	
		}

		@Override
		public int getCount() {
			return mTabs.size();        	
		}

		@Override
		public CharSequence getPageTitle(int position) {        	
			return mTabs.get(position).getTitle();        	
		}                             
	}
}
