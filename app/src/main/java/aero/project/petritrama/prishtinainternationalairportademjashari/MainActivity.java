package aero.project.petritrama.prishtinainternationalairportademjashari;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ActionBarActivity
{
    private Locale language;

    TextView txtFlightInfo;
    TextView txtExplore;
    TextView txtDestination;
    TextView txtServices;
    TextView txtNews;
    TextView txtInfo;

    Typeface custom_font;
    private Spinner spinnerLang;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        custom_font = Typeface.createFromAsset(getAssets(), "fonts/UnicodSans.ttf");

        final ImageView ivBackground = (ImageView) findViewById(R.id.ivBackground);
        ivBackground.setImageBitmap(getBitmapFromAsset("resource/bcgkr1.png"));

        final TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setTypeface(custom_font);

        final ImageView ivLogo = (ImageView) findViewById(R.id.ivLogo);
        ivLogo.setImageBitmap(getBitmapFromAsset("resource/logo.png"));

        txtFlightInfo = (TextView) findViewById(R.id.txtFlightInfo);
        txtExplore = (TextView) findViewById(R.id.txtExplore);
        txtDestination = (TextView) findViewById(R.id.txtDestination);
        txtServices = (TextView) findViewById(R.id.txtServices);
        txtNews = (TextView) findViewById(R.id.txtNews);
        txtInfo = (TextView) findViewById(R.id.txtInfo);

        txtFlightInfo.setTypeface(custom_font);
        txtExplore.setTypeface(custom_font);
        txtDestination.setTypeface(custom_font);
        txtServices.setTypeface(custom_font);
        txtNews.setTypeface(custom_font);
        txtInfo.setTypeface(custom_font);

        updateTexts();

        final ImageButton btnFlightInfo = (ImageButton)findViewById(R.id.btnFlightInfo);
        final ImageButton btnExplore = (ImageButton)findViewById(R.id.btnExplore);
        final ImageButton btnDestination = (ImageButton)findViewById(R.id.btnDestination);
        final ImageButton btnService = (ImageButton)findViewById(R.id.btnServices);
        final ImageButton btnNews = (ImageButton)findViewById(R.id.btnNews);
        final ImageButton btnInformation = (ImageButton)findViewById(R.id.btnInfo);

        final ImageButton btnInsta = (ImageButton) findViewById(R.id.btnInsta);
        final ImageButton btnFB = (ImageButton) findViewById(R.id.btnFB);
        final ImageButton btnTwitter = (ImageButton) findViewById(R.id.btnTwitter);
        final ImageButton btnMap = (ImageButton) findViewById(R.id.btnMap);
        btnMap.setVisibility(View.INVISIBLE);

        btnFlightInfo.setOnClickListener(onClickFlightInfo);
        btnExplore.setOnClickListener(onClickExplore);
        btnDestination.setOnClickListener(onClickDestination);
        btnService.setOnClickListener(onClickServices);
        btnNews.setOnClickListener(onClickNews);
        btnInformation.setOnClickListener(onClickInformation);

        btnInsta.setOnClickListener(onClickInsta);
        btnFB.setOnClickListener(onClickFB);
        btnTwitter.setOnClickListener(onClickTwitter);
        btnMap.setOnClickListener(onClickMap);

        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");

        spinnerLang = (Spinner) findViewById(R.id.spinnerLang);
        spinnerAdapter objSpinner = new spinnerAdapter(this, R.layout.spinner_list_item, Arrays.asList(getResources().getStringArray(R.array.lang)));
        spinnerLang.setAdapter(objSpinner);
        spinnerLang.setOnItemSelectedListener(onClickLang);

        if(language.equals("en")||language.isEmpty())
        {
            spinnerLang.setSelection(0);
            changeLang("en");
        }
        else if(language.equals("al"))
        {
            spinnerLang.setSelection(1);
            changeLang("al");
        }
        else
        {
            spinnerLang.setSelection(2);
            changeLang("sr");
        }

        loadLocale();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener onClickFlightInfo = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent intentFlightInfo = new Intent(MainActivity.this, FlightInfoActivity.class);
            startActivity(intentFlightInfo);
        }
    };

    private View.OnClickListener onClickExplore = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent intentExplore = new Intent(MainActivity.this, ExploreActivity.class);
            startActivity(intentExplore);
        }
    };

    private View.OnClickListener onClickDestination = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent intentDestination = new Intent(MainActivity.this, DestinationsActivity.class);
            startActivity(intentDestination);
        }
    };

    private View.OnClickListener onClickServices = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent intentServices = new Intent(MainActivity.this, ServicesActivity.class);
            startActivity(intentServices);
        }
    };

    private View.OnClickListener onClickNews = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent intentServices = new Intent(MainActivity.this, NewsActivity.class);
            startActivity(intentServices);
        }
    };

    private View.OnClickListener onClickInformation = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            Intent intentInformation = new Intent(MainActivity.this, InformationActivity.class);
            startActivity(intentInformation);
        }
    };

    private AdapterView.OnItemSelectedListener onClickLang = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
        {
            if(position==0)
                changeLang("en");
            else if(position==1)
                changeLang("al");
            else
                changeLang("sr");
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {

        }
    };

    private View.OnClickListener onClickInsta = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            try
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/airportpristina/"));
                intent.setPackage("com.instagram.android");
                startActivity(intent);
            }
            catch (Exception e)
            {
                Log.d("INSTA_ERROR", e.getMessage());
            }
        }
    };

    private View.OnClickListener onClickFB = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            try
            {
                final String url = "fb://page/1440677106221985";
                Intent facebookAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(facebookAppIntent);
            }
            catch (Exception e)
            {
                Log.d("FB_ERROR", e.getMessage());
            }
        }
    };

    private View.OnClickListener onClickTwitter = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            try
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=2744886516"));
                startActivity(intent);
            }
            catch (Exception e)
            {
                Log.d("TWITTER_ERROR", e.getMessage());
            }
        }
    };

    private View.OnClickListener onClickMap = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            //Intent intentMap = new Intent(MainActivity.this, MapsActivity.class);
            //startActivity(intentMap);
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
        return BitmapFactory.decodeStream(is);
    }

    public void changeLang(String lang)
    {
        if (lang.equalsIgnoreCase("")) return;
        language = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(language);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = language;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        updateTexts();
    }

    public void saveLocale(String lang)
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.apply();
    }

    public void loadLocale()
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language);
    }

    private void updateTexts()
    {
        try
        {
            txtFlightInfo.setText(R.string.text_btnFlight);
            txtExplore.setText(R.string.text_btnExplore);
            txtDestination.setText(R.string.text_btnDestinations);
            txtServices.setText(R.string.text_btnServices);
            txtNews.setText(R.string.text_btnNews);
            txtInfo.setText(R.string.text_btnInfo);
        }
        catch(Exception e)
        {
            Log.d("UPDATE_TEXT_ERROR", e.getMessage());
        }
    }

    private class spinnerAdapter extends ArrayAdapter<String>
    {
        public spinnerAdapter(Context context, int textViewResourceId, List<String> items)
        {
            super(context, textViewResourceId, items);
        }

        @Override
        public TextView getView(int position, View convertView, ViewGroup parent)
        {
            TextView v = (TextView) super.getView(position, convertView, parent);
            v.setTextColor(Color.rgb(255, 255, 255));
            v.setTypeface(custom_font);
            return v;
        }

        @Override
        public TextView getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView v = (TextView) super.getDropDownView(position, convertView, parent);
            v.setTextColor(Color.rgb(1, 27, 30));
            v.setTypeface(custom_font);
            return v;
        }


    }
}
