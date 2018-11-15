package com.zoromatic.timetable;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
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

@SuppressLint({"SimpleDateFormat", "RtlHardcoded"})
public class TimetableActivity extends ThemeAppCompatActivity {
    static final int ACTIVITY_CREATE = 10;
    static final int ACTIVITY_EDIT = 11;

    private static String LOG_TAG = "TimetableActivity";
    private static final int ACTIVITY_SETTINGS = 0;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mLeftDrawerList;

    public static final String DRAWER_OPEN = "drawer_open";
    private boolean mDrawerOpen = false;

    private ViewPager mViewPager;
    private TimetableFragmentPagerAdapter mFragmentPagerAdapter;

    private List<TimetablePagerItem> mTabs = new ArrayList<>();
    private int mCurrentItem = 0;
    private static final String KEY_CURRENT_ITEM = "key_current_item";

    private MenuItem mRefreshItem = null;
    Animation mRotation = null;

    private boolean mActivityDelete = false;
    public static final String MULTI_SELECT = "multi_select";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.timetable);

        initView();
        initDrawer();

        loadTabs();
        setFragments();

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        TypedValue outValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary,
                outValue,
                true);

        mRotation = AnimationUtils.loadAnimation(this, R.anim.animate_menu);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
            actionBar.setTitle(R.string.app_name);
        }
    }

    @SuppressLint("NewApi")
    public void refreshData() {
        Log.i(LOG_TAG, "refreshData");

        mDrawerLayout.closeDrawers();
        mDrawerOpen = false;

        if (mRefreshItem != null && mRotation != null) {
            if (mRefreshItem.getActionView() != null) {
                mRefreshItem.getActionView().startAnimation(mRotation);

                readCachedData();
            }
        }
    }

    public void openSettings() {
        mDrawerLayout.closeDrawers();
        mDrawerOpen = false;

        Intent settingsIntent = new Intent(getApplicationContext(), ZoromaticTimetablePreferenceActivity.class);
        startActivityForResult(settingsIntent, ACTIVITY_SETTINGS);
    }

    private void initView() {
        Log.i(LOG_TAG, "initView");

        String theme = Preferences.getMainTheme(this);

        mLeftDrawerList = findViewById(R.id.left_drawer);
        mToolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawerLayout);

        List<RowItem> rowItems = new ArrayList<>();

        RowItem item = new RowItem(theme.compareToIgnoreCase("light") == 0 ? R.drawable.ic_refresh_black_48dp : R.drawable.ic_refresh_white_48dp,
                (String) getResources().getText(R.string.refresh));
        rowItems.add(item);
        item = new RowItem(theme.compareToIgnoreCase("light") == 0 ? R.drawable.ic_settings_black_48dp : R.drawable.ic_settings_white_48dp,
                (String) getResources().getText(R.string.settings));
        rowItems.add(item);

        ItemAdapter adapter = new ItemAdapter(this, rowItems);
        mLeftDrawerList.setAdapter(adapter);

        mLeftDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        if (theme.compareToIgnoreCase("light") == 0)
            mLeftDrawerList.setBackgroundColor(getResources().getColor(android.R.color.white));
        else
            mLeftDrawerList.setBackgroundColor(getResources().getColor(android.R.color.black));
    }

    private void initDrawer() {
        Log.i(LOG_TAG, "initDrawer");

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {

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
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    // The click listener for ListView in the navigation drawer
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        Log.i(LOG_TAG, "initDrawer position=" + position);

        mLeftDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawers();
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
        savedInstanceState.putBoolean(DRAWER_OPEN, mDrawerOpen);

        if (mViewPager != null)
            savedInstanceState.putInt(KEY_CURRENT_ITEM, mViewPager.getCurrentItem());

        savedInstanceState.putBoolean(MULTI_SELECT, mActivityDelete);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        mDrawerOpen = savedInstanceState.getBoolean(DRAWER_OPEN);
        mCurrentItem = savedInstanceState.getInt(KEY_CURRENT_ITEM);

        if (mDrawerOpen) {
            if (mDrawerLayout != null) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
                mDrawerOpen = true;
                mDrawerToggle.syncState();
            }
        }

        mActivityDelete = savedInstanceState.getBoolean(MULTI_SELECT);
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
        } else {
            if (!mDrawerOpen) {
                super.onBackPressed();
                finish();
            } else {
                if (mDrawerLayout != null)
                    mDrawerLayout.closeDrawers();
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

        mRefreshItem = menu.findItem(R.id.refresh);

        if (mRefreshItem != null) {
            final Menu menuFinal = menu;

            if (mRefreshItem.getActionView() != null) {
                TypedValue outValue = new TypedValue();
                getTheme().resolveAttribute(R.attr.iconRefresh,
                        outValue,
                        true);
                int refreshIcon = outValue.resourceId;
                ((ImageView) mRefreshItem.getActionView()).setImageResource(refreshIcon);

                mRefreshItem.getActionView().setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        menuFinal.performIdentifierAction(mRefreshItem.getItemId(), 0);
                    }
                });
            }
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
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
                }

                return true;
            case R.id.delete:
                if (mTabs != null && mViewPager != null) {
                    TimetableContentFragment fragment = (mTabs.get(mViewPager.getCurrentItem())).getFragment();
                    fragment.deleteTimetableClass();
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
    public void loadTabs() {
        Log.i(LOG_TAG, "loadTabs");

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
    }

    public void setFragments() {
        Log.i(LOG_TAG, "setFragments");

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

            TabLayout slidingTabLayout = findViewById(R.id.sliding_tabs);
            mViewPager = findViewById(R.id.viewpager);

            if (mFragmentPagerAdapter == null)
                mFragmentPagerAdapter = new TimetableFragmentPagerAdapter(getSupportFragmentManager());

            if (mViewPager != null) {
                mViewPager.setAdapter(mFragmentPagerAdapter);

                final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                        .getDisplayMetrics());
                mViewPager.setPageMargin(pageMargin);
            }

            if (slidingTabLayout != null) {
                slidingTabLayout.setBackgroundColor(primaryColor);
                slidingTabLayout.setSelectedTabIndicatorColor(tabTextColor);

                int colorScheme = Preferences.getMainColorScheme(this);

                switch (colorScheme) {
                    case 0: // black
                        slidingTabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.sysWhite), tabTextColor);
                        break;
                    case 1: // white
                        slidingTabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.sysBlack), tabTextColor);
                        break;
                    default:
                        slidingTabLayout.setTabTextColors(primaryColorDark, tabTextColor);
                        break;
                }

                slidingTabLayout.setupWithViewPager(mViewPager);
            }

            if (mViewPager != null && mViewPager.getChildCount() > 0)
                mViewPager.setCurrentItem(Math.min(mCurrentItem, mTabs.size() - 1));
        }
    }

    @SuppressLint("NewApi")
    void readCachedData() {
        Log.i(LOG_TAG, "readCachedData");

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

        if (mRefreshItem != null) {
            if (mRefreshItem.getActionView() != null) {
                mRefreshItem.getActionView().clearAnimation();
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

    public boolean isActivityDelete() {
        return mActivityDelete;
    }

    public void setActivityDelete(boolean mActivityDelete) {
        this.mActivityDelete = mActivityDelete;
        supportInvalidateOptionsMenu();
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public void setViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
    }

    public List<TimetablePagerItem> getTabs() {
        return mTabs;
    }

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

        void setTitle(CharSequence title) {
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
