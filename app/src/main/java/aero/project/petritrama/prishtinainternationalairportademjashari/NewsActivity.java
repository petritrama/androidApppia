package aero.project.petritrama.prishtinainternationalairportademjashari;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

public class NewsActivity extends AppCompatActivity implements ListView.OnItemClickListener
{
    private Toolbar toolbar;
    private String[] mNewsMenu;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ImageButton btnBack;
    private TextView tv_toolbarTitle;

    private LinearLayout llNews;
    private RelativeLayout rlMedia;
    private ImageView ivImage;
    private TextView txtTitle;
    private TextView txtContent;

    Typeface custom_font;
    Typeface custom_font_bold;
    Typeface custom_font_italic;

    RecyclerView rvNews;

    CustomListAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        custom_font = Typeface.createFromAsset(getAssets(), "fonts/UnicodSans.ttf");
        custom_font_bold = Typeface.createFromAsset(getAssets(), "fonts/UnicodSans-Bold.ttf");
        custom_font_italic = Typeface.createFromAsset(getAssets(), "fonts/UnicodSans-Italic.ttf");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" ");

        tv_toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tv_toolbarTitle.setText(R.string.text_btnNews);
        tv_toolbarTitle.setTypeface(custom_font);

        setSupportActionBar(toolbar);

        btnBack = (ImageButton) toolbar.findViewById(R.id.btnBack);

        mNewsMenu = getResources().getStringArray(R.array.NewsMenu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.explore_drawer);

        llNews = (LinearLayout) findViewById(R.id.llNews);
        rlMedia = (RelativeLayout) findViewById(R.id.explore_content_frame);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtContent = (TextView) findViewById(R.id.txtContent);

        llNews.setVisibility(View.VISIBLE);
        rlMedia.setVisibility(View.INVISIBLE);
        txtTitle.setTypeface(custom_font);
        txtContent.setTypeface(custom_font);

        rvNews = (RecyclerView) findViewById(R.id.rvNews);
        rvNews.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        rvNews.setLayoutManager(layout);

        myAdapter = new CustomListAdapter(this, R.layout.drawer_list_item, mNewsMenu,custom_font,custom_font_bold);
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

        if (isNetworkAvailable())
            new retrieveNewsInfo(this, rvNews).execute();
        else Toast.makeText(this, R.string.conn_msg, Toast.LENGTH_SHORT).show();

        mDrawerToggle.syncState();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        mDrawerList.setItemChecked(position, true);
        tv_toolbarTitle.setText(mNewsMenu[position]);
        mDrawerLayout.closeDrawer(mDrawerList);

        myAdapter.setSelectedItem(position);

        switch(position)
        {
            case 0: //News
                rlMedia.setVisibility(View.INVISIBLE);
                llNews.setVisibility(View.VISIBLE);
                break;
            case 1: //Media Enquiry
                rlMedia.setVisibility(View.VISIBLE);
                llNews.setVisibility(View.INVISIBLE);
                changeContent("resource/media.JPG", R.string.title_media, R.string.text_media);
                break;
        }
    }

    public void changeContent(String image, int title, int content)
    {
        ivImage.setImageBitmap(getBitmapFromAsset(image));
        txtTitle.setText(title);
        txtContent.setText(content);
    }

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


    public class newsAdapter extends RecyclerView.Adapter<newsAdapter.NewsViewHolder>
    {
        String language = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE).getString("Language", "");

        ArrayList<HashMap<String, String>> news;

        public newsAdapter(ArrayList<HashMap<String, String>> mNews)
        {
            news = mNews;
        }

        public class NewsViewHolder extends RecyclerView.ViewHolder
        {
            CardView cvNews;
            ImageView ivNewsPhoto;
            TextView txtNewsTitle;
            TextView txtNewsContent;
            TextView txtDate;
            TextView txtMore;

            NewsViewHolder(View itemView)
            {
                super(itemView);
                try
                {
                    cvNews = (CardView) itemView.findViewById(R.id.news_card);
                    ivNewsPhoto = (ImageView) itemView.findViewById(R.id.ivNewsPhoto);
                    txtNewsTitle = (TextView) itemView.findViewById(R.id.txtNewsTitle);
                    txtNewsContent = (TextView) itemView.findViewById(R.id.txtNewsContent);
                    txtDate = (TextView) itemView.findViewById(R.id.txtDate);
                    txtMore = (TextView) itemView.findViewById(R.id.txtMore);
                }
                catch (Exception e)
                {
                    Log.d("EXPLORE_NEWS_ERROR", e.getMessage());
                }


                rvNews.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(View view, int position)
                            {
                                String itemPhoto = news.get(position).get("Filename");
                                String itemTitle;
                                String itemContent;

                                if(language.equals("al"))
                                {
                                    itemTitle = news.get(position).get("titulli_al");
                                    itemContent = news.get(position).get("pershkrimi_al");
                                }
                                else
                                {
                                    itemTitle = news.get(position).get("titulli_en");
                                    itemContent = news.get(position).get("pershkrimi_en");
                                }

                                Intent intent = new Intent(NewsActivity.this, NewsFullActivity.class);

                                intent.putExtra("photo", itemPhoto);
                                intent.putExtra("title", itemTitle);
                                intent.putExtra("content", itemContent);

                                startActivity(intent);
                            }
                        }));
            }
        }

        @Override
        public int getItemCount()
        {
            return news.size();
        }

        @Override
        public NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_card, viewGroup, false);
            return new NewsViewHolder(v);
        }

        @Override
        public void onBindViewHolder(NewsViewHolder newsViewHolder, int i)
        {
            try
            {
                new DownloadImageTask(newsViewHolder.ivNewsPhoto).execute("http://46.99.150.6/images/" + news.get(i).get("Filename"));

                newsViewHolder.txtNewsTitle.setTypeface(custom_font_bold);
                newsViewHolder.txtNewsContent.setTypeface(custom_font);
                newsViewHolder.txtDate.setTypeface(custom_font_bold);
                newsViewHolder.txtMore.setTypeface(custom_font_italic);

                newsViewHolder.txtDate.setText(news.get(i).get("data"));

                if(language.equals("al"))
                {
                    newsViewHolder.txtNewsTitle.setText(news.get(i).get("titulli_al"));
                    newsViewHolder.txtNewsContent.setText(news.get(i).get("pershkrimi_al"));
                }
                else
                {
                    newsViewHolder.txtNewsTitle.setText(news.get(i).get("titulli_en"));
                    newsViewHolder.txtNewsContent.setText(news.get(i).get("pershkrimi_en"));
                }

                newsViewHolder.txtMore.setText(R.string.readMore_msg);
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

    public class retrieveNewsInfo extends AsyncTask<Void, Integer, Void>
    {
        Context mContext;
        RecyclerView rvNews;
        ProgressDialog pDialog;
        ArrayList<HashMap<String, String>> newsInfo = null;

        public retrieveNewsInfo(Context mContext, RecyclerView mrvNews)
        {
            this.mContext = mContext;
            rvNews = mrvNews;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage(mContext.getText(R.string.wait_msg));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            InputStreamReader is;
            BufferedReader br;

            JSONparserNews parser = new JSONparserNews();
            String info;

            try
            {
                URL url = new URL("http://46.99.150.6/enter/json/jsonn.php");

                is = new InputStreamReader(url.openStream());
                br = new BufferedReader(is);

                info = br.readLine();

                is.close();
                br.close();

                this.newsInfo = parser.JSONparserNews(info);
            }
            catch (MalformedURLException me)
            {
                Log.e("URL_NEWS_ERROR", me.getMessage());
            }
            catch (IOException io)
            {
                Log.e("IO_NEWS_ERROR", io.getMessage());
            }
            catch (Exception e)
            {
                Log.e("E_NEWS_ERROR", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try
            {
                super.onPostExecute(aVoid);
                pDialog.dismiss();

                newsAdapter nAdapter = new newsAdapter(newsInfo);
                rvNews.setAdapter(nAdapter);
            }
            catch(Exception e)
            {
                Log.d("ADAPTER_ARRAY_ERROR",e.getMessage());
            }
        }
    }

    public class JSONparserNews
    {
        String language = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE).getString("Language", "");

        String TITLE;
        String CONTENT;
        String PHOTO = "Filename";
        String DATA = "data";

        public ArrayList<HashMap<String, String>> JSONparserNews(String jsonString)
        {
            if(language.equals("al"))
            {
                TITLE = "titulli_al";
                CONTENT = "pershkrimi_al";
            }
            else
            {
                TITLE = "titulli_en";
                CONTENT = "pershkrimi_en";
            }

            ArrayList<HashMap<String, String>> newsInfo = new ArrayList<>();

            try
            {
                JSONArray jsonArray = new JSONArray(jsonString);

                for (int i = 0; i <= jsonArray.length() - 1; i++)
                {
                    JSONObject f = jsonArray.getJSONObject(i);

                    String title = f.getString(TITLE);
                    String content = f.getString(CONTENT);
                    String photo = f.getString(PHOTO);
                    String date = f.getString(DATA);

                    HashMap<String, String> info = new HashMap<>();

                    info.put(TITLE, title);
                    info.put(CONTENT, content);
                    info.put(PHOTO, photo);
                    info.put(DATA, date);

                    newsInfo.add(info);
                }
            }
            catch (JSONException e)
            {
                Log.d("JSON_NEWS_ERROR", e.getMessage());
            }

            return newsInfo;
        }
    }


    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
