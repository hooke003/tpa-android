package appfactory.uwp.edu.parksideapp2.ranger_restart.covid;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import appfactory.uwp.edu.parksideapp2.R;

/**
 * This class initializes the home screen.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class RRCOVIDVideoView extends Fragment implements View.OnClickListener {
    private final String TAG = "RRCOVIDVideoView";
    private ImageView closeVideoViewButton;

    // VideoViews
    private VideoView covidVideoView;

    private void initUI(View view) {
        closeVideoViewButton = view.findViewById(R.id.closeVideoViewButton);
        covidVideoView = view.findViewById(R.id.covidVideoView);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rr_videoview, container, false);
        initUI(view);
        covidVideoView.setOnCompletionListener(mp -> {
            covidVideoView.stopPlayback();
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.rr_fragment_container, new RRCOVIDHomeFragment(), "RRCOVIDHomeFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        closeVideoViewButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playVideo(getArguments().getInt("uri"));
    }

    private void playVideo(int videoPath) {
        MediaController mediaController = new MediaController(requireContext());
        mediaController.setAnchorView(covidVideoView);
        covidVideoView.setMediaController(mediaController);
        covidVideoView.setVideoURI(Uri.parse("android.resource://" + requireActivity().getPackageName() + "/" + videoPath));
        covidVideoView.requestFocus();
        covidVideoView.start();
    }

    private void stopVideo() {
        covidVideoView.stopPlayback();
        covidVideoView.setMediaController(null);
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.rr_fragment_container, new RRCOVIDHomeFragment(), "RRCOVIDHomeFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.closeVideoViewButton: {
                stopVideo();
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
