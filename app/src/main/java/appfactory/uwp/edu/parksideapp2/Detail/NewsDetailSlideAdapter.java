package appfactory.uwp.edu.parksideapp2.Detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import appfactory.uwp.edu.parksideapp2.Models.NewsObj;
import appfactory.uwp.edu.parksideapp2.R;

import static android.view.View.GONE;

public class NewsDetailSlideAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<String> urlList;
    private String thisUrl;

    public NewsDetailSlideAdapter(Context context, ArrayList<String> urlList) {
        this.context = context;
        this.urlList = urlList;
    }

    @Override
    public int getCount() {
        return urlList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_detail_view,container,false);
        thisUrl = urlList.get(position);

        final WebView webView = (WebView) view.findViewById(R.id.webview);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.news_progress_bar);
        // share button

        final FloatingActionButton shareButton = (FloatingActionButton) view.findViewById(R.id.shareNewsButton);
        shareButton.setVisibility(GONE);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                //webView.loadUrl("javascript:document.getElementById('cs_control_2543')");
                webView.loadUrl("javascript:(function() { " +
                        "var head = document.getElementsByClassName('interior_header')[0].style.display='none'; " +
                        "var head = document.getElementsByClassName('nav_wrapper clearfix')[0].style.display='none'; " +
                        "var head = document.getElementsByClassName('top_wrapper clearfix')[0].style.display='none'; " +
                        "var head = document.getElementsByClassName('be_wrapper clearfix')[0].style.display='none'; " +
                        "var head = document.getElementsByClassName('footer_wrapper clearfix')[0].style.display='none'; " +
                        "var head = document.getElementsByClassName('side_nav')[0].style.display='none'; " +
                        "var head = document.getElementsByClassName('bottom')[0].style.display='none'; " +

                        "})()");

                progressBar.setVisibility(GONE);
                webView.setVisibility(View.VISIBLE);
                shareButton.setVisibility(View.VISIBLE);

                final String myURL = url;
                shareButton.setOnClickListener(v -> {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, myURL);
                    sendIntent.setType("text/plain");
                    sendIntent = sendIntent.createChooser(sendIntent, "share Parkside news");
                    context.startActivity(sendIntent);
                });
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.setVisibility(GONE);
                progressBar.setVisibility(View.VISIBLE);
                view.loadUrl(String.valueOf(thisUrl));
                return false;
            }
        });

        webView.loadUrl(String.valueOf(thisUrl));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
