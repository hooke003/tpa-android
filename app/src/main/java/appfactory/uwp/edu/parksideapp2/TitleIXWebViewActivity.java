package appfactory.uwp.edu.parksideapp2;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import appfactory.uwp.edu.parksideapp2.R;

public class TitleIXWebViewActivity extends AppCompatActivity {
    private Button showWebViewButton;
    private WebView webview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.titleix_webview);
    }

    public void initUI() {
        showWebViewButton = findViewById(R.id.showwebviewbtn);
        webview = findViewById(R.id.titleixwebview);
        webview.setWebViewClient(new MyBrowser());

        showWebViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVisibility();
            }
        });
    }

    private void toggleVisibility() {
        if(webview.getVisibility() != View.VISIBLE) {
            webview.setVisibility(View.VISIBLE);
            showWebViewButton.setVisibility(View.GONE);
            webview.loadUrl("http://www.tutorialspoint.com");
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
