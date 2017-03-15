package aero.project.petritrama.prishtinainternationalairportademjashari;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

public class DestinationsActivity extends AppCompatActivity implements ListView.OnItemClickListener
{
    private Toolbar toolbar;
    private String[] mDestinationsMenu;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ImageButton btnBack;
    private TextView tv_toolbarTitle;

    private RelativeLayout relativeLayout;
    private ScrollView scrollView1;
    private ImageView ivImage;
    private TextView txtTitle;
    private TextView txtContent;
    private Button btnApplication;

    private LinearLayout llDesRecyclerView;
    private LinearLayout llPubRecyclerView;
    RecyclerView rvDestionations;
    RecyclerView rvPublications;
    publicationAdapter pAdapter;

    Typeface custom_font;
    Typeface custom_font_bold;

    CustomListAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations);


        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        custom_font = Typeface.createFromAsset(getAssets(), "fonts/UnicodSans.ttf");
        custom_font_bold = Typeface.createFromAsset(getAssets(), "fonts/UnicodSans-Bold.ttf");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" ");

        tv_toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tv_toolbarTitle.setText(R.string.text_btnDestinations);
        tv_toolbarTitle.setTypeface(custom_font);

        setSupportActionBar(toolbar);

        btnBack = (ImageButton) toolbar.findViewById(R.id.btnBack);

        relativeLayout = (RelativeLayout) findViewById(R.id.explore_content_frame);
        scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtContent = (TextView) findViewById(R.id.txtContent);

        mDestinationsMenu = getResources().getStringArray(R.array.DestinationsMenu);
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

        llDesRecyclerView = (LinearLayout) findViewById(R.id.llDesRecyclerView);

        llPubRecyclerView = (LinearLayout) findViewById(R.id.llPubRecyclerView);

        rvDestionations = (RecyclerView) findViewById(R.id.rvDestinations);
        rvDestionations.setHasFixedSize(true);

        rvPublications = (RecyclerView) findViewById(R.id.rvPublications);
        rvPublications.setHasFixedSize(true);

        LinearLayoutManager layoutD = new LinearLayoutManager(this);
        layoutD.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager layoutP = new LinearLayoutManager(this);
        layoutP.setOrientation(LinearLayoutManager.VERTICAL);

        rvDestionations.setLayoutManager(layoutD);
        rvPublications.setLayoutManager(layoutP);

        btnApplication = (Button) findViewById(R.id.btnApplication);
        btnApplication.setOnClickListener(onClickApplication);
        btnApplication.setVisibility(View.INVISIBLE);

        llDesRecyclerView.setVisibility(View.VISIBLE);
        llPubRecyclerView.setVisibility(View.INVISIBLE);
        relativeLayout.setVisibility(View.INVISIBLE);

        if(isNetworkAvailable())
        {
            new retrievePublicationInfo(this, rvPublications).execute();
            new retrieveDestinationsInfo(this, rvDestionations).execute();
        }
        else Toast.makeText(DestinationsActivity.this, R.string.conn_msg, Toast.LENGTH_SHORT).show();

        mDrawerToggle.syncState();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        mDrawerList.setItemChecked(position, true);
        tv_toolbarTitle.setText(mDestinationsMenu[position]);
        tv_toolbarTitle.setMaxLines(2);
        tv_toolbarTitle.setEllipsize(TextUtils.TruncateAt.END);
        mDrawerLayout.closeDrawer(mDrawerList);
        btnApplication.setVisibility(View.INVISIBLE);
        llDesRecyclerView.setVisibility(View.INVISIBLE);
        llPubRecyclerView.setVisibility(View.INVISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);
        rvPublications.setVisibility(View.INVISIBLE);

        myAdapter.setSelectedItem(position);

        switch(position)
        {
            case 0: //Destinations
                llDesRecyclerView.setVisibility(View.VISIBLE);
                llPubRecyclerView.setVisibility(View.INVISIBLE);
                relativeLayout.setVisibility(View.INVISIBLE);
                if(!isNetworkAvailable())
                    Toast.makeText(DestinationsActivity.this, R.string.conn_msg, Toast.LENGTH_SHORT).show();
                break;
            case 1: //Schedule
                changeContent("resource/schedule.JPG", R.string.title_schedule, R.string.text_schedule);

                break;
            case 2: //Airport Schedule Coordination and Facilitation
                changeContent("resource/schedule_coordination.JPG", R.string.title_schedule_coordination, R.string.text_schedule_coordination);
                break;
            case 3: //Information on application for flights
                changeContent("resource/application_flights.jpg", R.string.title_application_flights, R.string.text_application_flights);
                btnApplication.setVisibility(View.VISIBLE);
                break;
            case 4: //Publications
                llDesRecyclerView.setVisibility(View.INVISIBLE);
                llPubRecyclerView.setVisibility(View.VISIBLE);
                rvPublications.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.INVISIBLE);
                if(!isNetworkAvailable())
                    Toast.makeText(DestinationsActivity.this, R.string.conn_msg, Toast.LENGTH_SHORT).show();
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

    private View.OnClickListener onClickApplication = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.airportpristina.com/schedule-coordination/information-on-application-for-flights"));
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


    public class publicationAdapter extends RecyclerView.Adapter<publicationAdapter.PublicationsViewHolder>
    {
        ArrayList<HashMap<String, String>> publications;

        public publicationAdapter(ArrayList<HashMap<String, String>> mPublications)
        {
            publications = mPublications;
        }

        public class PublicationsViewHolder extends RecyclerView.ViewHolder
        {
            TextView txtPublications;

            PublicationsViewHolder(View itemView)
            {
                super(itemView);
                try
                {
                    txtPublications = (TextView) itemView.findViewById(R.id.txtPublications);
                }
                catch (Exception e)
                {
                    Log.d("DESTINATION_PUB_ERROR", e.getMessage());
                }

                rvPublications.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        try
                        {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(publications.get(position).get("link")));
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
            return publications.size();
        }

        @Override
        public PublicationsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.publication_card, viewGroup, false);
            return new PublicationsViewHolder(v);
        }

        @Override
        public void onBindViewHolder(PublicationsViewHolder publicationsViewHolder, int i)
        {
            try
            {
                publicationsViewHolder.txtPublications.setTypeface(custom_font);
                publicationsViewHolder.txtPublications.setText(publications.get(i).get("title"));
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


    public class retrievePublicationInfo extends AsyncTask<Void, Integer, Void>
    {
        Context mContext;
        RecyclerView rvPublication;
        ArrayList<HashMap<String, String>> PublicationInfo = null;

        public retrievePublicationInfo(Context mContext, RecyclerView mrvPublication)
        {
            this.mContext = mContext;
            rvPublication = mrvPublication;
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

            JSONparserPublication parser = new JSONparserPublication();
            String info;

            try
            {
                URL url = new URL("http://46.99.150.6/enter/json/jsonp.php");

                is = new InputStreamReader(url.openStream());
                br = new BufferedReader(is);

                info = br.readLine();

                is.close();
                br.close();

                this.PublicationInfo = parser.JSONparserPublication(info);
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

                publicationAdapter objAdapter = new publicationAdapter(PublicationInfo);
                rvPublication.setAdapter(objAdapter);
            }
            catch(Exception e)
            {
                Log.d("ADAPTER_ARRAY_ERROR",e.getMessage());
            }
        }
    }

    public class JSONparserPublication
    {
        String TITLE = "title";
        String LINK = "link";

        public ArrayList<HashMap<String, String>> JSONparserPublication(String jsonString)
        {
            ArrayList<HashMap<String, String>> publicationInfo = new ArrayList<>();

            try
            {
                JSONArray jsonArray = new JSONArray(jsonString);

                for (int i = 0; i <= jsonArray.length() - 1; i++)
                {
                    JSONObject f = jsonArray.getJSONObject(i);

                    String title = f.getString(TITLE);
                    String link = f.getString(LINK);

                    HashMap<String, String> info = new HashMap<>();

                    info.put(TITLE, title);
                    info.put(LINK, link);

                    publicationInfo.add(info);
                }
            }
            catch (JSONException e)
            {
                Log.d("JSON_PUBLICATION_ERROR", e.getMessage());
            }

            return publicationInfo;
        }
    }


    public class destinationsAdapter extends RecyclerView.Adapter<destinationsAdapter.DestinationsViewHolder>
    {
        ArrayList<HashMap<String, String>> destinations;

        public destinationsAdapter(ArrayList<HashMap<String, String>> mDestinations)
        {
            destinations = mDestinations;
        }

        public class DestinationsViewHolder extends RecyclerView.ViewHolder
        {
            TextView txtDestinations;
            TextView txtAirline;
            ImageView ivLogo;

            DestinationsViewHolder(View itemView)
            {
                super(itemView);
                try
                {
                    txtDestinations = (TextView) itemView.findViewById(R.id.txtDestinations);
                    txtAirline = (TextView) itemView.findViewById(R.id.txtAirline);
                    ivLogo = (ImageView) itemView.findViewById(R.id.ivLogo);
                }
                catch (Exception e)
                {
                    Log.d("DESTINATION_DES_ERROR", e.getMessage());
                }
            }
        }

        @Override
        public int getItemCount()
        {
            return destinations.size();
        }

        @Override
        public DestinationsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.destinations_card, viewGroup, false);
            return new DestinationsViewHolder(v);
        }

        @Override
        public void onBindViewHolder(DestinationsViewHolder destinationsViewHolder, int i)
        {
            try
            {
                new DownloadImageTask(destinationsViewHolder.ivLogo).execute("http://46.99.150.6/images/" + destinations.get(i).get("Filename"));

                destinationsViewHolder.txtDestinations.setTypeface(custom_font);
                destinationsViewHolder.txtAirline.setTypeface(custom_font_bold);

                destinationsViewHolder.txtDestinations.setText(destinations.get(i).get("destinations"));
                destinationsViewHolder.txtAirline.setText(destinations.get(i).get("airline"));
            }
            catch(Exception e)
            {
                Log.d("BIND_DESTINATIONS_ERROR",e.getMessage());
            }
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView)
        {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }


    public class retrieveDestinationsInfo extends AsyncTask<Void, Integer, Void>
    {
        Context mContext;
        RecyclerView rvDestinations;
        ArrayList<HashMap<String, String>> DestinationsInfo = null;

        public retrieveDestinationsInfo(Context mContext, RecyclerView mrvDestinations)
        {
            this.mContext = mContext;
            rvDestinations = mrvDestinations;
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

            JSONparserDestinations parser = new JSONparserDestinations();
            String info;

            try
            {
                URL url = new URL("http://46.99.150.6/enter/json/jsond.php");

                is = new InputStreamReader(url.openStream());
                br = new BufferedReader(is);

                info = br.readLine();

                is.close();
                br.close();

                this.DestinationsInfo = parser.JSONparserDestinations(info);
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

                destinationsAdapter objAdapter = new destinationsAdapter(DestinationsInfo);
                rvDestinations.setAdapter(objAdapter);
            }
            catch(Exception e)
            {
                Log.d("ADAPTER_ARRAY_ERROR",e.getMessage());
            }
        }
    }

    public class JSONparserDestinations
    {
        String AIRLINE = "airline";
        String DES = "destinations";
        String LOGO = "Filename";

        public ArrayList<HashMap<String, String>> JSONparserDestinations(String jsonString)
        {
            ArrayList<HashMap<String, String>> destinationsInfo = new ArrayList<>();

            try
            {
                JSONArray jsonArray = new JSONArray(jsonString);

                for (int i = 0; i <= jsonArray.length() - 1; i++)
                {
                    JSONObject f = jsonArray.getJSONObject(i);

                    String airline = f.getString(AIRLINE);
                    String des = f.getString(DES);
                    String logo = f.getString(LOGO);

                    HashMap<String, String> info = new HashMap<>();

                    info.put(AIRLINE, airline);
                    info.put(DES, des);
                    info.put(LOGO, logo);

                    destinationsInfo.add(info);
                }
            }
            catch (JSONException e)
            {
                Log.d("JSON_DESTINATIONS_ERROR", e.getMessage());
            }

            return destinationsInfo;
        }
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
