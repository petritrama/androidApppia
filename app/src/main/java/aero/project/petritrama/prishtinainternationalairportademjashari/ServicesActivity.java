package aero.project.petritrama.prishtinainternationalairportademjashari;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class ServicesActivity extends AppCompatActivity implements ListView.OnItemClickListener
{
    private Toolbar toolbar;
    private String[] mDestinationsMenu;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ImageButton btnBack;
    private TextView tv_toolbarTitle;

    Typeface custom_font;
    Typeface custom_font_bold;

    private RelativeLayout relativeLayout;
    private ScrollView scrollView1;
    private ImageView ivImage;
    private TextView txtTitle;
    private TextView txtContent;

    CustomListAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);


        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        custom_font = Typeface.createFromAsset(getAssets(), "fonts/UnicodSans.ttf");
        custom_font_bold = Typeface.createFromAsset(getAssets(), "fonts/UnicodSans-Bold.ttf");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" ");

        tv_toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tv_toolbarTitle.setText(R.string.text_btnServices);
        tv_toolbarTitle.setTypeface(custom_font);

        setSupportActionBar(toolbar);

        btnBack = (ImageButton) toolbar.findViewById(R.id.btnBack);

        relativeLayout = (RelativeLayout) findViewById(R.id.explore_content_frame);
        scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtContent = (TextView) findViewById(R.id.txtContent);

        mDestinationsMenu = getResources().getStringArray(R.array.ServicesMenu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.explore_drawer);

        myAdapter = new CustomListAdapter(this, R.layout.drawer_list_item, mDestinationsMenu,custom_font,custom_font_bold);
        mDrawerList.setAdapter(myAdapter);

        mDrawerList.setOnItemClickListener(this);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0, 0)
        {
            @Override
            public void onDrawerClosed(View view)
            {
            }

            @Override
            public void onDrawerOpened(View drawerView)
            {

            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        btnBack.setOnClickListener(onClickBack);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        txtTitle.setTypeface(custom_font_bold);
        txtContent.setTypeface(custom_font);
        changeContent("resource/services.JPG", R.string.title_services, R.string.text_services);

        mDrawerToggle.syncState();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        mDrawerList.setItemChecked(position, true);
        tv_toolbarTitle.setText(mDestinationsMenu[position]);
        mDrawerLayout.closeDrawer(mDrawerList);

        myAdapter.setSelectedItem(position);

        switch(position)
        {
            case 0: //Services
                changeContent("resource/services.JPG", R.string.title_services, R.string.text_services);
                break;
            case 1: //Passenger Handling Service
                changeContent("resource/phs.JPG", R.string.title_phs, R.string.text_phs);
                break;
            case 2: //Information & Lost and Found
                changeContent("resource/lost_found.JPG", R.string.title_lost, R.string.text_lost);
                break;
            case 3: //CIP and 17/02 Service
                changeContent("resource/cip.jpg", R.string.title_cip, R.string.text_cip);
                break;
            case 4: //Commercial
                changeContent("resource/commercial.JPG", R.string.title_commercial, R.string.text_commercial);
                break;
            case 5: //Ramp Services
                changeContent("resource/ramp.jpg", R.string.title_ramp, R.string.text_ramp);
                break;
            case 6: //Cargo
                changeContent("resource/cargo.JPG", R.string.title_cargo, R.string.text_cargo);
                break;
            case 7: //Security and Screening Unit
                changeContent("resource/security.jpg", R.string.title_security, R.string.text_security);
                break;
            case 8: //Medical Care
                changeContent("resource/medical.jpg", R.string.title_medical, R.string.text_medical);
                break;
            case 9: //Information Communication Technologies
                changeContent("resource/ict.JPG", R.string.title_ict, R.string.text_ict);
                break;
        }
    }

    public void changeContent(String image, int title, int content)
    {
        scrollView1.fullScroll(ScrollView.FOCUS_UP);
        ivImage.setImageBitmap(getBitmapFromAsset(image));
        txtTitle.setText(title);
        txtContent.setText(content);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //getMenuInflater().inflate(R.menu.menu_destinations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private View.OnClickListener onClickBack = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            finish();
        }
    };

    private Bitmap getBitmapFromAsset(String strName)
    {
        AssetManager assetManager = this.getAssets();
        InputStream is = null;
        try
        {
            is = assetManager.open(strName);
        }
        catch (IOException e)
        {
            Log.d("BITMAP_ERROR", e.getMessage());
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }
}
