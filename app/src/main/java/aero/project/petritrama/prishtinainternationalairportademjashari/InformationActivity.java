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

public class InformationActivity extends AppCompatActivity implements ListView.OnItemClickListener
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

    private ImageView ivLyon;
    private ImageView ivLimak;
    private ImageView ivISO1;
    private ImageView ivISO2;
    private TextView tvFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        custom_font = Typeface.createFromAsset(getAssets(), "fonts/UnicodSans.ttf");
        custom_font_bold = Typeface.createFromAsset(getAssets(), "fonts/UnicodSans-Bold.ttf");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        tv_toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tv_toolbarTitle.setText(R.string.text_btnInfo);
        tv_toolbarTitle.setTypeface(custom_font);
        setSupportActionBar(toolbar);

        btnBack = (ImageButton) toolbar.findViewById(R.id.btnBack);

        relativeLayout = (RelativeLayout) findViewById(R.id.explore_content_frame);
        scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtContent = (TextView) findViewById(R.id.txtContent);

        ivLyon = (ImageView) findViewById(R.id.ivLyon);
        ivLimak = (ImageView) findViewById(R.id.ivLimak);
        ivISO1 = (ImageView) findViewById(R.id.ivISO1);
        ivISO2 = (ImageView) findViewById(R.id.ivISO2);
        tvFooter = (TextView) findViewById(R.id.tvFooter);
        ivLyon.setImageBitmap(getBitmapFromAsset("resource/lyon.png"));
        ivLimak.setImageBitmap(getBitmapFromAsset("resource/PIA_logo.png"));
        ivISO1.setImageBitmap(getBitmapFromAsset("resource/iso12.png"));
        ivISO2.setImageBitmap(getBitmapFromAsset("resource/iso34.png"));
        tvFooter.setTypeface(custom_font);

        mDestinationsMenu = getResources().getStringArray(R.array.InfoMenu);
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
        changeContent("resource/info.JPG", R.string.title_info, R.string.text_info);

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
            case 0: //Contacts
                changeContent("resource/info.JPG", R.string.title_info, R.string.text_info);
                break;
            case 1: //Passenger Info
                changeContent("resource/info.JPG", R.string.title_passinfo, R.string.text_passinfo);
                break;
            case 2: //Schedule Facilitation
                changeContent("resource/coordination.jpg", R.string.title_coordination, R.string.text_coordination);
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
