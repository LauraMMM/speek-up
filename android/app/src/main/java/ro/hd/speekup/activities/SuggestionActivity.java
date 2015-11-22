package ro.hd.speekup.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ro.hd.speekup.R;
import ro.hd.speekup.classes.ApiManager;
import ro.hd.speekup.entities.ListEvent;
import ro.hd.speekup.fragments.SuggestionFragment;

public class SuggestionActivity extends AppCompatActivity {

    public final static int TAB_SPEAK = 1;
    public final static int TAB_MOVE = 2;

    private SparseArray<Fragment> fragmentList;

    private ViewPager mViewPager;
    public ViewPagerAdapter mAdapter;

    private TabLayout mTabLayout;

    private ListEvent event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

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

        Intent intent = getIntent();
        event = (ListEvent) intent.getSerializableExtra("event");

        getSupportActionBar().setTitle(event.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addSuggestionIntent = new Intent(SuggestionActivity.this, AddSuggestionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("eventId", event.getId());
                addSuggestionIntent.putExtras(bundle);
                startActivityForResult(addSuggestionIntent, 1);
            }
        });
    }

    @Override
    protected void onResume() {
        ApiManager.getSuggestions(getApplicationContext(), event.getId(), "SuggestionActivity");
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_suggestions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actino_refresh:
                ApiManager.getSuggestions(getApplicationContext(), event.getId(), "SuggestionActivity");
                return true;
        }
        finish();
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        SuggestionFragment fragmentSpeek = new SuggestionFragment();
        Bundle bundleCurrent = new Bundle();
        bundleCurrent.putString(SuggestionFragment.ARG_LIST_TYPE, "speek");
        fragmentSpeek.setArguments(bundleCurrent);

        SuggestionFragment fragmentMove = new SuggestionFragment();
        Bundle bundleAttending = new Bundle();
        bundleAttending.putString(SuggestionFragment.ARG_LIST_TYPE, "move");
        fragmentMove.setArguments(bundleAttending);

        fragmentList.put(TAB_SPEAK, fragmentSpeek);
        fragmentList.put(TAB_MOVE, fragmentMove);

        mAdapter.addFragment(fragmentSpeek, getString(R.string.tab_speak).toUpperCase());
        mAdapter.addFragment(fragmentMove, getString(R.string.tab_move).toUpperCase());

        viewPager.setAdapter(mAdapter);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

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
            return mFragmentTitleList.get(position);
        }
    }
}
