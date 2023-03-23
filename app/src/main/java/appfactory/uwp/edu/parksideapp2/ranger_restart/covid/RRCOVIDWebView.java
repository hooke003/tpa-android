package appfactory.uwp.edu.parksideapp2.ranger_restart.covid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import appfactory.uwp.edu.parksideapp2.R;


/**
 * This class initializes a WebView fragment to display the given URL.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class RRCOVIDWebView extends Fragment implements View.OnClickListener {
    private final String TAG = "RRCOVIDWebView";
    private ConstraintLayout popupWebViewConstraintLayout;
    private ImageView closeWebViewButton;
    private ImageView openInBrowserButton;
    private WebView currentPhaseWebView;
    private TextView webViewId;

    /**
     * @param view Current view to initialize the UI for
     */
    private void initUI(View view) {
        openInBrowserButton = view.findViewById(R.id.openInBrowserButton);
        closeWebViewButton = view.findViewById(R.id.closeWebViewButton);
        popupWebViewConstraintLayout = view.findViewById(R.id.popupWebViewConstraintLayout);
        currentPhaseWebView = view.findViewById(R.id.currentPhaseWebView);
        webViewId = view.findViewById(R.id.webViewURL);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rr_webview, container, false);
        initUI(view);
        openInBrowserButton.setOnClickListener(this);
        closeWebViewButton.setOnClickListener(this);
        String url = "https://uwp.edu/";
        if (getArguments() != null)
            url = getArguments().getString("url");
        currentPhaseWebView.setWebViewClient(new WebViewClient());
        currentPhaseWebView.getSettings().setLoadsImagesAutomatically(true);
        currentPhaseWebView.getSettings().setJavaScriptEnabled(true);
        currentPhaseWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        currentPhaseWebView.loadUrl(url);
        webViewId.setText(url);
        popupWebViewConstraintLayout.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        int i = v.getId();
        switch (i) {
            // Open the current page in the phone's browser
            case R.id.openInBrowserButton: {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentPhaseWebView.getUrl()));
                startActivity(intent);
            }
            break;
            // Close this WebView fragment
            case R.id.closeWebViewButton: {
                fragmentTransaction.replace(R.id.rr_fragment_container, new RRCOVIDHomeFragment(), "RRCOVIDHomeFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Hide the toolbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Show the toolbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}
