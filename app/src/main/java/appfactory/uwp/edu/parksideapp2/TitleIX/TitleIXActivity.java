package appfactory.uwp.edu.parksideapp2.TitleIX;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import appfactory.uwp.edu.parksideapp2.R;
import appfactory.uwp.edu.parksideapp2.ranger_restart.updates.UpdateCardAdapter;

public class TitleIXActivity extends Activity implements View.OnClickListener {

    WebView webview;
    public View cardview;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_title_ix_main);
        Log.d("=============", "webviewclicked");
    }


    @Override
    public void onClick(View view) {
        cardview = findViewById(R.id.reporting_misconduct);
        cardview.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("=============", "webviewclicked");
                webview.getSettings().setJavaScriptEnabled(true);
                webview.setWebViewClient(new WebViewClient());
                webview.loadUrl("https://www.uwp.edu/live/offices/studentaffairs/sexual-misconduct/reporting-sexual-misconduct.cfm");
                webview.setVisibility(View.VISIBLE);
            }

        });

    }
}
