package ro.hd.speekup.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ro.hd.speekup.R;
import ro.hd.speekup.classes.ApiManager;
import ro.hd.speekup.fragments.EventListFragment;

public class EventsActivity extends AppCompatActivity {

    public final static int TAB_CURRENT = 1;
    public final static int TAB_ATTENDING = 2;

    private SparseArray<Fragment> fragmentList;

    private ViewPager mViewPager;
    public ViewPagerAdapter mAdapter;

    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        fragmentList = new SparseArray<>();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent importEventIntent = new Intent(EventsActivity.this, ImportEventActivity.class);
                startActivity(importEventIntent);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        ApiManager.getEvents(this, "EventsActivity");
        super.onResume();
    }

    private void setupViewPager(ViewPager viewPager) {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        EventListFragment fragmentCurrent = new EventListFragment();
        Bundle bundleCurrent = new Bundle();
        bundleCurrent.putString(EventListFragment.ARG_LIST_TYPE, "current");
        fragmentCurrent.setArguments(bundleCurrent);

        EventListFragment fragmentAttending = new EventListFragment();
        Bundle bundleAttending = new Bundle();
        bundleAttending.putString(EventListFragment.ARG_LIST_TYPE, "attending");
        fragmentAttending.setArguments(bundleAttending);

        fragmentList.put(TAB_CURRENT, fragmentCurrent);
        fragmentList.put(TAB_ATTENDING, fragmentAttending);

        mAdapter.addFragment(fragmentCurrent, getString(R.string.current_events).toUpperCase());
        mAdapter.addFragment(fragmentAttending, getString(R.string.my_events).toUpperCase());

        viewPager.setAdapter(mAdapter);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private int newMessageCount = 0;

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            /*if (position == EventsActivity.TAB_ATTENDING && this.newMessageCount > 0) {
                int size = (int) (18 * getResources().getDisplayMetrics().density);
                int textSize = (int) (12 * getResources().getDisplayMetrics().density);
                Drawable image = ImageTools.writeOnDrawable(
                        getApplicationContext(),
                        R.drawable.circle_white,
                        Integer.toString(this.newMessageCount),
                        R.color.cyan,
                        textSize,
                        size,
                        size);
                image.setBounds(0, 0, size, size);

                // Replace blank spaces with image icon
                SpannableString sb = new SpannableString("   " + mFragmentTitleList.get(position) + "   ");
                ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
                sb.setSpan(imageSpan, mFragmentTitleList.get(position).length() + 5, mFragmentTitleList.get(position).length() + 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                return sb;
            } else {*/
                return mFragmentTitleList.get(position);
            /*}*/
        }

        public void setNewMessageCount(int newMessageCount) {
            if (this.newMessageCount != newMessageCount) {
                this.newMessageCount = newMessageCount;
                //save the currently selected tab to switch bag to it after resetting the pager
                int oldTab = mViewPager.getCurrentItem();
                mTabLayout.setupWithViewPager(mViewPager);
                mViewPager.setCurrentItem(oldTab, false);
            }
        }
    }
}
