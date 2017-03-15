package aero.project.petritrama.prishtinainternationalairportademjashari;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ExploreActivity extends AppCompatActivity implements ListView.OnItemClickListener
{
    private Toolbar toolbar;
    private String[] mExploreMenu;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ImageButton btnBack;
    private TextView tv_toolbarTitle;

    private RelativeLayout relativeLayout;
    private ScrollView scrollView1;
    private ScrollView scrollView2;
    private LinearLayout scrollView3;
    private ImageView ivImage;
    private TextView txtTitle;
    private TextView txtContent;
    private ImageView ivImageContent;
    private LinearLayout linearLayout3;
    private ImageView ivMapArrival;
    private ImageView ivMapDeparture;
    private Button btnManagement;

    CustomListAdapter myAdapter;

    private Animator mCurrentAnimator;
    private ImageView expandedImageView;
    private int mShortAnimationDuration;

    private LinearLayout scrollViewAirline;

    RecyclerView rvAirline;
    RecyclerView rvRent;

    Typeface custom_font;
    Typeface custom_font_bold;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        custom_font = Typeface.createFromAsset(getAssets(), "fonts/UnicodSans.ttf");
        custom_font_bold = Typeface.createFromAsset(getAssets(), "fonts/UnicodSans-Bold.ttf");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" ");

        tv_toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tv_toolbarTitle.setText(R.string.text_btnExplore);
        tv_toolbarTitle.setTypeface(custom_font);

        setSupportActionBar(toolbar);

        btnBack = (ImageButton) toolbar.findViewById(R.id.btnBack);

        mExploreMenu = getResources().getStringArray(R.array.ExploreMenu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.explore_drawer);

        myAdapter = new CustomListAdapter(this, R.layout.drawer_list_item, mExploreMenu, custom_font, custom_font_bold);
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

        relativeLayout = (RelativeLayout) findViewById(R.id.explore_content_frame);

        try
        {
            scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
            scrollView2 = (ScrollView) findViewById(R.id.scrollView2);
            scrollView3 = (LinearLayout) findViewById(R.id.scrollView3);
            ivImage = (ImageView) findViewById(R.id.ivImage);
            ivImageContent = (ImageView) findViewById(R.id.ivContent);
            txtTitle = (TextView) findViewById(R.id.txtTitle);
            txtContent = (TextView) findViewById(R.id.txtContent);
            linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
            expandedImageView = (ImageView) findViewById(R.id.ivContainer);
            ivMapArrival = (ImageView) findViewById(R.id.ivMap_arrival);
            ivMapDeparture = (ImageView) findViewById(R.id.ivMap_departure);
            btnManagement = (Button) findViewById(R.id.btnManagement);

            scrollViewAirline = (LinearLayout) findViewById(R.id.linearLayoutAirline);

            btnManagement.setOnClickListener(onClickManagement);
            btnManagement.setVisibility(View.INVISIBLE);

            scrollView2.setVisibility(View.INVISIBLE);
            scrollView3.setVisibility(View.INVISIBLE);
            ivImage.setImageBitmap(getBitmapFromAsset("resource/companyprofile.JPG"));
            txtTitle.setTypeface(custom_font_bold);
            txtTitle.setText(R.string.title_companyprofile);
            txtContent.setTypeface(custom_font);
            txtContent.setText(R.string.text_companyprofile);
            expandedImageView.setVisibility(View.INVISIBLE);
            linearLayout3.setVisibility(View.INVISIBLE);
            ivImageContent.setImageBitmap(getBitmapFromAsset("resource/organizational-chart.png"));
            ivMapArrival.setImageBitmap(getBitmapFromAsset("resource/arrivalmap.png"));
            ivMapDeparture.setImageBitmap(getBitmapFromAsset("resource/departuremap.png"));
            btnManagement.setTypeface(custom_font);

            scrollViewAirline.setVisibility(View.INVISIBLE);

            rvRent = (RecyclerView) findViewById(R.id.rvRent);
            rvRent.setHasFixedSize(true);
            LinearLayoutManager layout = new LinearLayoutManager(this);
            layout.setOrientation(LinearLayoutManager.VERTICAL);
            rvRent.setLayoutManager(layout);
            rentAdapter rAdapter = new rentAdapter();
            rvRent.setAdapter(rAdapter);

            rvAirline = (RecyclerView) findViewById(R.id.rvAirline);
            rvAirline.setHasFixedSize(true);
            LinearLayoutManager layoutA = new LinearLayoutManager(this);
            layoutA.setOrientation(LinearLayoutManager.VERTICAL);
            rvAirline.setLayoutManager(layoutA);

            ivImageContent.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    expandedImageView.setVisibility(View.VISIBLE);
                    zoomImageFromThumb(scrollView2, R.drawable.organizational_chart);
                }
            });
            ivMapArrival.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    expandedImageView.setVisibility(View.VISIBLE);
                    zoomImageFromThumb(scrollView2, R.drawable.arrivalmap);
                }
            });
            ivMapDeparture.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    expandedImageView.setVisibility(View.VISIBLE);
                    zoomImageFromThumb(scrollView2, R.drawable.departuremap);
                }
            });
            mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

            if(isNetworkAvailable())
            {
                new retrieveAirlineInfo(this, rvAirline).execute();
            }

            mDrawerToggle.syncState();
        }
        catch (Exception e)
        {
            Log.d("EXPLORE_ERROR", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        try
        {
            mDrawerList.setItemChecked(position, true);
            tv_toolbarTitle.setText(mExploreMenu[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
            expandedImageView.setVisibility(View.INVISIBLE);
            btnManagement.setVisibility(View.INVISIBLE);
            relativeLayout.setVisibility(View.VISIBLE);
            scrollView2.setVisibility(View.INVISIBLE);
            linearLayout3.setVisibility(View.INVISIBLE);
            scrollView3.setVisibility(View.INVISIBLE);
            scrollViewAirline.setVisibility(View.INVISIBLE);

            myAdapter.setSelectedItem(position);

            switch (position)
            {
                case 0: //Company Profile
                    changeContent("resource/companyprofile.JPG", R.string.title_companyprofile, R.string.text_companyprofile);
                    break;
                case 1: //History
                    changeContent("resource/history.JPG", R.string.title_history, R.string.text_history);
                    break;
                case 2: //Main Info - Limak Holding
                    changeContent("resource/limak.JPG", R.string.title_limak, R.string.text_limak);
                    break;
                case 3: //Main Info - ADLMS
                    changeContent("resource/adlms.jpg", R.string.title_adlms, R.string.text_adlms);
                    break;
                case 4: //Organization
                    changeContent("resource/organization.JPG", R.string.title_organization, R.string.empty);
                    scrollView2.setVisibility(View.INVISIBLE);
                    break;
                case 5: //Mission & Vision
                    changeContent("resource/missionvision.jpg", R.string.title_missionvision, R.string.text_missionvision);
                    break;
                case 6: //Message from CEO
                    changeContent("resource/ceo.jpg", R.string.title_ceo, R.string.text_ceo);
                    break;
                case 7: //Integrated Management System Policy
                    changeContent("resource/management.jpg", R.string.title_management, R.string.text_management);
                    btnManagement.setVisibility(View.VISIBLE);
                    break;
                case 8: //Corporate Social Responsibility
                    changeContent("resource/responsibility.jpg", R.string.title_responsibility, R.string.text_responsibility);
                    break;
                case 9: //Career
                    changeContent("resource/career.JPG", R.string.title_career, R.string.text_career);
                    break;
                case 10: //Terminal
                    relativeLayout.setVisibility(View.INVISIBLE);
                    linearLayout3.setVisibility(View.VISIBLE);
                    break;
                case 11: //Rent a Car
                    changeContent("resource/rent.JPG", R.string.title_rent, R.string.text_rent);
                    scrollView3.setVisibility(View.VISIBLE);
                    break;
                case 12: //Banks
                    changeContent("resource/bank.JPG", R.string.title_bank, R.string.text_bank);
                    break;
                case 13: //Restaurants and Shops
                    changeContent("resource/restaurants.JPG", R.string.title_restaurants, R.string.text_restaurants);
                    break;
                case 14: //Free Shop
                    changeContent("resource/shop.JPG", R.string.title_shop, R.string.text_shop);
                    break;
                case 15: //Airlines
                    relativeLayout.setVisibility(View.INVISIBLE);
                    scrollViewAirline.setVisibility(View.VISIBLE);
                    if(!isNetworkAvailable())
                        Toast.makeText(ExploreActivity.this, R.string.conn_msg, Toast.LENGTH_SHORT).show();
                    break;
                case 16: //Ad
                    changeContent("resource/ad.JPG", R.string.title_ad, R.string.text_ad);
                    break;
                case 17: //Taxi Services
                    changeContent("resource/taxi.jpg", R.string.title_taxi, R.string.text_taxi);
                    break;
            }
        }
        catch(Exception e)
        {
            Log.d("ONCLICK_EXP_ERROR",e.getMessage());
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
        //getMenuInflater().inflate(R.menu.menu_explore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (mDrawerToggle.onOptionsItemSelected(item))
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

    private View.OnClickListener onClickManagement = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://c1940652.r52.cf0.rackcdn.com/549987c8ff2a7c27ba0012e0/Management-Integrated--System-Policy.pdf"));
            startActivity(browserIntent);
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

    private void zoomImageFromThumb(final View thumbView, int imageResId)
    {
        if (mCurrentAnimator != null)
        {
            mCurrentAnimator.cancel();
        }

        expandedImageView.setImageResource(imageResId);

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.ivContainer).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds.width() / startBounds.height())
        {
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        }
        else
        {
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        thumbView.setAlpha(1f);
        expandedImageView.setVisibility(View.VISIBLE);

        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left, finalBounds.left)).with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top, finalBounds.top)).with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mCurrentAnimator != null)
                {
                    mCurrentAnimator.cancel();
                }

                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left)).with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top)).with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScaleFinal)).with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.INVISIBLE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation)
                    {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.INVISIBLE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }


    public class rentAdapter extends RecyclerView.Adapter<rentAdapter.RentViewHolder>
    {
        private List<Rent> rents;

        public rentAdapter()
        {
            this.rents = new ArrayList<>();
            initializeData();
        }

        public class RentViewHolder extends RecyclerView.ViewHolder
        {
            TextView tel1;
            TextView tel2;
            TextView tel3;
            TextView tel4;
            TextView email;
            TextView web;
            TextView schedule;
            ImageView logo;

            TextView lblTel1;
            TextView lblTel2;
            TextView lblTel3;
            TextView lblTel4;
            TextView lblSchedule;

            RentViewHolder(View itemView)
            {
                super(itemView);
                try
                {
                    tel1 = (TextView) itemView.findViewById(R.id.txtMob1);
                    tel2 = (TextView) itemView.findViewById(R.id.txtMob2);
                    tel3 = (TextView) itemView.findViewById(R.id.txtMob3);
                    tel4 = (TextView) itemView.findViewById(R.id.txtMob4);
                    email = (TextView) itemView.findViewById(R.id.txtEmail);
                    web = (TextView) itemView.findViewById(R.id.txtWeb);
                    schedule = (TextView) itemView.findViewById(R.id.txtSchedule);
                    logo = (ImageView) itemView.findViewById(R.id.ivRentLogo);

                    lblTel1 = (TextView) itemView.findViewById(R.id.lblTel1);
                    lblTel2 = (TextView) itemView.findViewById(R.id.lblTel2);
                    lblTel3 = (TextView) itemView.findViewById(R.id.lblTel3);
                    lblTel4 = (TextView) itemView.findViewById(R.id.lblTel4);
                    lblSchedule = (TextView) itemView.findViewById(R.id.lblSchedule);
                }
                catch (Exception e)
                {
                    Log.d("EXPLORE_RV_ERROR", e.getMessage());
                }

                rvRent.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        try
                        {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+rents.get(position).web));
                            startActivity(browserIntent);
                        }
                        catch (Exception e)
                        {
                            Log.d("BIND_ERROR", e.getMessage());
                        }
                    }
                }));
            }
        }

        @Override
        public int getItemCount()
        {
            return rents.size();
        }

        @Override
        public RentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rent_card, viewGroup, false);
            return new RentViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RentViewHolder rentsViewHolder, int i)
        {
            String txtTel1 = rents.get(i).tel1;
            String txtTel2 = rents.get(i).tel2;
            String txtTel3 = rents.get(i).tel3;
            String txtTel4 = rents.get(i).tel4;
            String txtWorkSched = rents.get(i).schedule;

            if (txtTel1.equals("")) rentsViewHolder.lblTel1.setText("");
            else rentsViewHolder.lblTel1.setText("Tel: ");

            if (txtTel2.equals("")) rentsViewHolder.lblTel2.setText("");
            else rentsViewHolder.lblTel2.setText("Tel: ");

            if (txtTel3.equals("")) rentsViewHolder.lblTel3.setText("");
            else rentsViewHolder.lblTel3.setText("Tel: ");

            if (txtTel4.equals("")) rentsViewHolder.lblTel4.setText("");
            else rentsViewHolder.lblTel4.setText("Tel: ");

            if (txtWorkSched.equals("")) rentsViewHolder.lblSchedule.setText("");
            else rentsViewHolder.lblSchedule.setText("Work Schedule: ");

            rentsViewHolder.tel1.setText(txtTel1);
            rentsViewHolder.tel2.setText(txtTel2);
            rentsViewHolder.tel3.setText(txtTel3);
            rentsViewHolder.tel4.setText(txtTel4);
            rentsViewHolder.email.setText(rents.get(i).email);
            rentsViewHolder.web.setText(rents.get(i).web);
            rentsViewHolder.schedule.setText(txtWorkSched);
            rentsViewHolder.logo.setImageResource(rents.get(i).logo);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView)
        {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public void initializeData()
        {
            rents = new ArrayList<>();

            rents.add(new Rent("", "+377 44 240 383", "+377 45 445 533", "", "info@rentacar-prishtina.com", "www.rentacar-prishtina.com", "", R.drawable.prishtina));
            rents.add(new Rent("+377-45-612-613", "+386-49-518-518", "+377-45-665-663", "+377-45-666-779", "rentacar.roberti@gmail.com", "www.rentacar-roberti.com", "24h", R.drawable.roberti));
            rents.add(new Rent("+377-45-967-967", "+386-49-840-840", "+377-44-388-888", "+386-49-388-888", "rent.autoking@gmail.com", "www.king-rent.com", "24h", R.drawable.king));
            rents.add(new Rent("+381 38 594 338", "+377 44 488 666", "+377 44 399 395", "+377 44 566 668", "info@rentacarautolux.com", "www.rentacarautolux.com", "", R.drawable.autolux));
            rents.add(new Rent("+381 38 594 101", "+377 44 116 746", "+377 44 122 040", "", "info@europcar-ks.com", "www.europcar-ks.com", "", R.drawable.europcar));
            rents.add(new Rent("+381 38 594 555", "+377 44 594 555", "+386 49 594 555", "", "info@hertzkosovo.com", "www.hertzkosovo.com", "", R.drawable.hertz));
            rents.add(new Rent("", "", "+377 45 505 101", "", "info@interrent-ks.com", "www.interrent-ks.com", "08:00-24:00 Mo-Su", R.drawable.interrent));
            rents.add(new Rent("+381 38 594 595", "+377 44 665 668", "+386 49 665 668", "", "info@kosovatrade-ks.com", "www.kosovatrade-ks.com", "", R.drawable.kosova));
            rents.add(new Rent("+37744700047", "+37744111417", "+38649100080", "", "nshrentacarmara@outlook.com", "www.rentmara.com", "24h", R.drawable.mara));
            rents.add(new Rent("", "+377 45 535 536", "+381 38 534 517", "", "info@sixtkosovo.com", "www.sixt.com", "(8am - 12pm\nMo - Su)", R.drawable.sixt));
            rents.add(new Rent("+377 44 286 286", "+377 44 283 283", "+377 44 350 970", "+381 38 603 224", "info@rentacarshotani.com", "www.rentacarshotani.com", "24/7", R.drawable.shotani));
            rents.add(new Rent("", "+377 44 721 721", "+381290325021", "", "info@sherretirent.com", "www.sherretirent.com", "", R.drawable.autosherreti));
        }
    }

    public class airlineAdapter extends RecyclerView.Adapter<airlineAdapter.AirlineViewHolder>
    {
        ArrayList<HashMap<String, String>> airlines;

        public airlineAdapter(ArrayList<HashMap<String, String>> mAirlines)
        {
            airlines = mAirlines;
        }

        public class AirlineViewHolder extends RecyclerView.ViewHolder
        {
            CardView cvAirline;
            TextView txtAirline;
            TextView txtWebsite;
            ImageView ivAirlineLogo;

            AirlineViewHolder(View itemView)
            {
                super(itemView);
                try
                {
                    cvAirline = (CardView) itemView.findViewById(R.id.airline_card);
                    txtAirline = (TextView) itemView.findViewById(R.id.txtAirline);
                    txtWebsite = (TextView) itemView.findViewById(R.id.txtWebsite);
                    ivAirlineLogo = (ImageView) itemView.findViewById(R.id.ivAirlineLogo);
                }
                catch (Exception e)
                {
                    Log.d("EXPLORE_AIRLINE_ERROR", e.getMessage());
                }

                rvAirline.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        try
                        {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+airlines.get(position).get("web")));
                            startActivity(browserIntent);
                        }
                        catch (Exception e)
                        {
                            Log.d("BIND_ERROR", e.getMessage());
                        }
                    }
                }));
            }
        }

        @Override
        public int getItemCount()
        {
            return airlines.size();
        }

        @Override
        public AirlineViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.airline_card, viewGroup, false);
            return new AirlineViewHolder(v);
        }

        @Override
        public void onBindViewHolder(AirlineViewHolder airlineViewHolder, int i)
        {
            try
            {
                new DownloadImageTask(airlineViewHolder.ivAirlineLogo).execute("http://46.99.150.6/images/" + airlines.get(i).get("Filename"));

                airlineViewHolder.txtAirline.setTypeface(custom_font);
                airlineViewHolder.txtWebsite.setTypeface(custom_font);

                airlineViewHolder.txtAirline.setText(airlines.get(i).get("airline"));
                airlineViewHolder.txtWebsite.setText(airlines.get(i).get("web"));
            }
            catch(Exception e)
            {
                Log.d("BIND_ERROR",e.getMessage());
            }
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView)
        {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }

    public class retrieveAirlineInfo extends AsyncTask<Void, Integer, Void>
    {
        Context mContext;
        RecyclerView rvAirline;
        ArrayList<HashMap<String, String>> airlineInfo = null;

        public retrieveAirlineInfo(Context mContext, RecyclerView mrvAirline)
        {
            this.mContext = mContext;
            rvAirline = mrvAirline;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            InputStreamReader is;
            BufferedReader br;

            JSONparserAirline parser = new JSONparserAirline();
            String info;

            try
            {
                URL url = new URL("http://46.99.150.6/enter/json/jsona.php");

                is = new InputStreamReader(url.openStream());
                br = new BufferedReader(is);

                info = br.readLine();

                is.close();
                br.close();

                this.airlineInfo = parser.JSONparserAirline(info);
            }
            catch (MalformedURLException me)
            {
                Log.e("URL_AIRLINE_ERROR", me.getMessage());
            }
            catch (IOException io)
            {
                Log.e("IO_AIRLINE_ERROR", io.getMessage());
            }
            catch (Exception e)
            {
                Log.e("E_AIRLINE_ERROR", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try
            {
                super.onPostExecute(aVoid);

                airlineAdapter objAdapter = new airlineAdapter(airlineInfo);
                rvAirline.setAdapter(objAdapter);
            }
            catch(Exception e)
            {
                Log.d("ADAPTER_ARRAY_ERROR",e.getMessage());
            }
        }
    }

    public class JSONparserAirline
    {
        String AIRLINE = "airline";
        String WEB = "web";
        String LOGO = "Filename";

        public ArrayList<HashMap<String, String>> JSONparserAirline(String jsonString)
        {
            ArrayList<HashMap<String, String>> airlineInfo = new ArrayList<>();

            try
            {
                JSONArray jsonArray = new JSONArray(jsonString);

                for (int i = 0; i <= jsonArray.length() - 1; i++)
                {
                    JSONObject f = jsonArray.getJSONObject(i);

                    String airline = f.getString(AIRLINE);
                    String web = f.getString(WEB);
                    String logo = f.getString(LOGO);

                    HashMap<String, String> info = new HashMap<>();

                    info.put(AIRLINE, airline);
                    info.put(WEB, web);
                    info.put(LOGO, logo);

                    airlineInfo.add(info);
                }
            }
            catch (JSONException e)
            {
                Log.d("JSON_AIRLINE_ERROR", e.getMessage());
            }

            return airlineInfo;
        }
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}