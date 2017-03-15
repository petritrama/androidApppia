package aero.project.petritrama.prishtinainternationalairportademjashari;

import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class FlightInfoActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener
{
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_info);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/UnicodSans.ttf");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        toolbar.setNavigationIcon(R.drawable.ic_action);
        toolbar.setNavigationOnClickListener(onClickBack);
        toolbar.setLogo(R.mipmap.ic_launcher);

        final TextView tv_toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tv_toolbarTitle.setText(R.string.text_btnFlight);
        tv_toolbarTitle.setTypeface(custom_font);

        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.pager);

        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new ArrivalFragment(), (String)getText(R.string.tab_arrival));
        mAdapter.addFragment(new DepartureFragment(), (String)getText(R.string.tab_departure));
        viewPager.setAdapter(mAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.arrival_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.departure_icon);

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(custom_font);
                }
            }
        }

        if (isNetworkAvailable())
        {
            try
            {
                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab)
                    {
                        viewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {}

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) { }
                });
            }
            catch (Exception e)
            {
                Log.d("ARRIVAL_ERROR", e.getMessage());
            }
        }
        else
        {
            Toast.makeText(this, R.string.conn_msg,
                    Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_flight_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {

    }

    @Override
    public void onPageSelected(int position)
    {
        for (int i = 0; i < tabLayout.getChildCount(); i++)
        {
            tabLayout.getChildAt(i).setSelected(position == i);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public static class PlaceholderFragment extends Fragment
    {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.activity_flight_info, container, false);
            return rootView;
        }
    }

    private View.OnClickListener onClickBack = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            finish();
        }
    };

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
