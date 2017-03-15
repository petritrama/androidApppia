package aero.project.petritrama.prishtinainternationalairportademjashari;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsFullActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private ImageButton btnBack;

    private ImageView ivImage;
    private TextView txtTitle;
    private TextView txtContent;

    Typeface custom_font;
    Typeface custom_font_bold;
    Typeface custom_font_italic;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_full);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        String photo = "";
        String title = "";
        String content = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            photo = extras.getString("photo");
            title = extras.getString("title");
            content = extras.getString("content");
        }

        custom_font = Typeface.createFromAsset(getAssets(), "fonts/UnicodSans.ttf");
        custom_font_bold = Typeface.createFromAsset(getAssets(), "fonts/UnicodSans-Bold.ttf");
        custom_font_italic = Typeface.createFromAsset(getAssets(), "fonts/UnicodSans-Italic.ttf");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" ");

        setSupportActionBar(toolbar);

        btnBack = (ImageButton) toolbar.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(onClickBack);

        ivImage = (ImageView) findViewById(R.id.ivPhoto);
        txtTitle = (TextView) findViewById(R.id.txtNewsTitle);
        txtContent = (TextView) findViewById(R.id.txtContent);

        txtTitle.setTypeface(custom_font_bold);
        txtContent.setTypeface(custom_font);

        new DownloadImageTask(ivImage).execute("http://46.99.150.6/images/" + photo);
        txtTitle.setText(title);
        txtContent.setText(content);

        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //getMenuInflater().inflate(R.menu.menu_news_full, menu);
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


    private View.OnClickListener onClickBack = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            finish();
        }
    };

}
