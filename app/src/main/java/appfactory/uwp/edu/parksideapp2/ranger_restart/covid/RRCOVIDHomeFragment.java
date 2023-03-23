package appfactory.uwp.edu.parksideapp2.ranger_restart.covid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import appfactory.uwp.edu.parksideapp2.R;
import appfactory.uwp.edu.parksideapp2.ranger_restart.covid.questionnaire.RRCOVIDQuestionnaireQ1Fragment;
import appfactory.uwp.edu.parksideapp2.ranger_restart.updates.RecentUpdateCard;
import appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants;

/**
 * This class initializes the home screen.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class RRCOVIDHomeFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "RRCOVIDHomeFragment";
    // TextViews
    private TextView viewAllUpdatesTextView;

    // ImageViews
    private ImageView generalTrainingVideoThumbnailImageView;
    private ImageView uwpTrainingVideoThumbnailImageView;
    private ImageView additionalResourcesImageView;

    // CardViews
    // These all contain click listeners
    private CardView monitoringAndCheckingCardView;
    private CardView symptomsCardView;
    private CardView dosDontsCardView;
    private CardView takeThePledgeCardView;
    private CardView uwp_link;
    private CardView cdc_link;
    private CardView wdhs_link;
    private CardView kcdh_link;
    private CardView ken_resp_link;
    private CardView kch_link;
    private CardView rc_resp_link;
    private CardView crchd_link;
    private ArrayList<CardView> recentUpdateCards;
    private ArrayList<TextView> recentUpdateTitleTextViews;
    private ArrayList<TextView> recentUpdateMessageTextViews;


    private void initUI(View view) {
        // Set the ScrollView to the very top
        view.findViewById(R.id.homeScreenScroll).post(
                () -> (
                        (ScrollView) view.findViewById(
                                R.id.homeScreenScroll)
                ).fullScroll(View.FOCUS_UP)
        );

        //<editor-fold desc="UI Initialization">


        //Layouts
        viewAllUpdatesTextView = view.findViewById(R.id.viewAllUpdatesTextView);
        generalTrainingVideoThumbnailImageView = view.findViewById(R.id.generalTrainingVideoThumbnailImageView);
        uwpTrainingVideoThumbnailImageView = view.findViewById(R.id.uwpTrainingVideoThumbnailImageView);
        monitoringAndCheckingCardView = view.findViewById(R.id.monitoringAndCheckingCardView);
        symptomsCardView = view.findViewById(R.id.symptomsCardView);
        dosDontsCardView = view.findViewById(R.id.dosDontsCardView);
        takeThePledgeCardView = view.findViewById(R.id.takeThePledgeCardView);
        uwp_link = view.findViewById(R.id.uwp_link);
        cdc_link = view.findViewById(R.id.cdc_link);
        wdhs_link = view.findViewById(R.id.wdhs_link);
        kcdh_link = view.findViewById(R.id.kcdh_link);
        ken_resp_link = view.findViewById(R.id.ken_resp_link);
        kch_link = view.findViewById(R.id.kch_link);
        rc_resp_link = view.findViewById(R.id.rc_resp_link);
        crchd_link = view.findViewById(R.id.crchd_link);
        //</editor-fold>

        //<editor-fold desc="RecentUpdateCards">
        recentUpdateTitleTextViews.add(view.findViewById(R.id.covid19ResourcesTitleTextViewPos0));
        recentUpdateTitleTextViews.add(view.findViewById(R.id.covid19ResourcesTitleTextViewPos1));
        recentUpdateTitleTextViews.add(view.findViewById(R.id.covid19ResourcesTitleTextViewPos2));
        recentUpdateTitleTextViews.add(view.findViewById(R.id.covid19ResourcesTitleTextViewPos3));
        recentUpdateTitleTextViews.add(view.findViewById(R.id.covid19ResourcesTitleTextViewPos4));
        recentUpdateMessageTextViews.add(view.findViewById(R.id.covid19ResourcesBodyTextViewPos0));
        recentUpdateMessageTextViews.add(view.findViewById(R.id.covid19ResourcesBodyTextViewPos1));
        recentUpdateMessageTextViews.add(view.findViewById(R.id.covid19ResourcesBodyTextViewPos2));
        recentUpdateMessageTextViews.add(view.findViewById(R.id.covid19ResourcesBodyTextViewPos3));
        recentUpdateMessageTextViews.add(view.findViewById(R.id.covid19ResourcesBodyTextViewPos4));
        recentUpdateCards.add(view.findViewById(R.id.covid19ResourcesCardViewPos0));
        recentUpdateCards.add(view.findViewById(R.id.covid19ResourcesCardViewPos1));
        recentUpdateCards.add(view.findViewById(R.id.covid19ResourcesCardViewPos2));
        recentUpdateCards.add(view.findViewById(R.id.covid19ResourcesCardViewPos3));
        recentUpdateCards.add(view.findViewById(R.id.covid19ResourcesCardViewPos4));
        //</editor-fold>
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rr_covid_home, container, false);
        recentUpdateCards = new ArrayList<>();
        recentUpdateTitleTextViews = new ArrayList<>();
        recentUpdateMessageTextViews = new ArrayList<>();

        initUI(view);

        // Get the updates
        if (UserConstants.RECENT_UPDATE_CARDS.size() >= 5) {
            for (int index = 0; index < 5; index++) {
                recentUpdateTitleTextViews.get(index).setText(UserConstants.RECENT_UPDATE_CARDS.get(index).getTitle());
                recentUpdateMessageTextViews.get(index).setText(UserConstants.RECENT_UPDATE_CARDS.get(index).getDate());
            }
        } else {
            HorizontalScrollView mostRecentUpdatesScrollView = view.findViewById(R.id.mostRecentUpdatesScrollView);
            mostRecentUpdatesScrollView.setVisibility(View.GONE);
        }

        //<editor-fold desc="setOnClickListeners">
        generalTrainingVideoThumbnailImageView.setOnClickListener(this);
        uwpTrainingVideoThumbnailImageView.setOnClickListener(this);
        viewAllUpdatesTextView.setOnClickListener(this);
        monitoringAndCheckingCardView.setOnClickListener(this);
        symptomsCardView.setOnClickListener(this);
        dosDontsCardView.setOnClickListener(this);
        takeThePledgeCardView.setOnClickListener(this);
        uwp_link.setOnClickListener(this);
        cdc_link.setOnClickListener(this);
        wdhs_link.setOnClickListener(this);
        kcdh_link.setOnClickListener(this);
        ken_resp_link.setOnClickListener(this);
        kch_link.setOnClickListener(this);
        rc_resp_link.setOnClickListener(this);
        crchd_link.setOnClickListener(this);
        //</editor-fold>

        for (CardView cardView : recentUpdateCards)
            cardView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(@NotNull View v) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        int i = v.getId();
        Bundle args = new Bundle();
        switch (i) {
            case R.id.viewAllUpdatesTextView: {
                fragmentTransaction.replace(R.id.rr_fragment_container, new RRCOVIDRecentUpdatesFragment(), "RRCOVIDRecentUpdatesFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            //<editor-fold desc="Recent updates">
            case R.id.covid19ResourcesCardViewPos0: {
                args.putString("title", String.valueOf(recentUpdateTitleTextViews.get(0).getText()));
                for (RecentUpdateCard card : UserConstants.RECENT_UPDATE_CARDS) {
                    if (recentUpdateTitleTextViews.get(0).getText().equals(card.getTitle()))
                        args.putString("body", String.valueOf(card.getBody()));
                }
                RRCOVIDViewUpdateFragment viewUpdateFragment = new RRCOVIDViewUpdateFragment();
                viewUpdateFragment.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, viewUpdateFragment, "RRCOVIDViewUpdateFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.covid19ResourcesCardViewPos1: {
                args.putString("title", String.valueOf(recentUpdateTitleTextViews.get(1).getText()));
                for (RecentUpdateCard card : UserConstants.RECENT_UPDATE_CARDS) {
                    if (recentUpdateTitleTextViews.get(0).getText().equals(card.getTitle()))
                        args.putString("body", String.valueOf(card.getBody()));
                }
                RRCOVIDViewUpdateFragment viewUpdateFragment = new RRCOVIDViewUpdateFragment();
                viewUpdateFragment.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, viewUpdateFragment, "RRCOVIDViewUpdateFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.covid19ResourcesCardViewPos2: {
                args.putString("title", String.valueOf(recentUpdateTitleTextViews.get(2).getText()));
                for (RecentUpdateCard card : UserConstants.RECENT_UPDATE_CARDS) {
                    if (recentUpdateTitleTextViews.get(0).getText().equals(card.getTitle()))
                        args.putString("body", String.valueOf(card.getBody()));
                }
                RRCOVIDViewUpdateFragment viewUpdateFragment = new RRCOVIDViewUpdateFragment();
                viewUpdateFragment.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, viewUpdateFragment, "RRCOVIDViewUpdateFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.covid19ResourcesCardViewPos3: {
                args.putString("title", String.valueOf(recentUpdateTitleTextViews.get(3).getText()));
                for (RecentUpdateCard card : UserConstants.RECENT_UPDATE_CARDS) {
                    if (recentUpdateTitleTextViews.get(0).getText().equals(card.getTitle()))
                        args.putString("body", String.valueOf(card.getBody()));
                }
                RRCOVIDViewUpdateFragment viewUpdateFragment = new RRCOVIDViewUpdateFragment();
                viewUpdateFragment.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, viewUpdateFragment, "RRCOVIDViewUpdateFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.covid19ResourcesCardViewPos4: {
                args.putString("title", String.valueOf(recentUpdateTitleTextViews.get(4).getText()));
                for (RecentUpdateCard card : UserConstants.RECENT_UPDATE_CARDS) {
                    if (recentUpdateTitleTextViews.get(0).getText().equals(card.getTitle()))
                        args.putString("body", String.valueOf(card.getBody()));
                }
                RRCOVIDViewUpdateFragment viewUpdateFragment = new RRCOVIDViewUpdateFragment();
                viewUpdateFragment.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, viewUpdateFragment, "RRCOVIDViewUpdateFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            //</editor-fold>
            case R.id.monitoringAndCheckingCardView: {

                fragmentTransaction.replace(
                        R.id.rr_fragment_container,
                        new RRCOVIDQuestionnaireQ1Fragment(),
                        "RRCOVIDQuestionnaireQ1Fragment"
                );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.symptomsCardView: {
                fragmentTransaction.replace(R.id.rr_fragment_container, new RRCOVIDSymptomsFragment(), "RRCOVIDSymptomsFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.dosDontsCardView: {
                fragmentTransaction.replace(R.id.rr_fragment_container, new RRCOVIDDosDontsFragment(), "RRCOVIDDosDontsFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            //<editor-fold desc="Videos">
            case R.id.generalTrainingVideoThumbnailImageView: {
                args.putInt("uri", R.raw.generaltraining);
                RRCOVIDVideoView rrcovidVideoView = new RRCOVIDVideoView();
                rrcovidVideoView.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidVideoView, "RRCOVIDVideoView");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.uwpTrainingVideoThumbnailImageView: {
                args.putInt("uri", R.raw.uwpspecifictraining);
                RRCOVIDVideoView rrcovidVideoView = new RRCOVIDVideoView();
                rrcovidVideoView.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidVideoView, "RRCOVIDVideoView");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            //</editor-fold>
            case R.id.takeThePledgeCardView: {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.uwp.edu/RangerRestart/upload/RR-Pledge-web.pdf"));
                startActivity(intent);
            }
            break;
            //<editor-fold desc="Links">
            case R.id.uwp_link: {
                args.putString("url", "https://www.uwp.edu/RangerRestart/index.cfm");
                RRCOVIDWebView rrcovidWebView = new RRCOVIDWebView();
                rrcovidWebView.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidWebView, "RRCOVIDWebView");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.cdc_link: {
                args.putString("url", "https://www.cdc.gov/coronavirus/2019-ncov/index.html");
                RRCOVIDWebView rrcovidWebView = new RRCOVIDWebView();
                rrcovidWebView.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidWebView, "RRCOVIDWebView");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.wdhs_link: {
                args.putString("url", "https://www.dhs.wisconsin.gov/covid-19/index.htm");
                RRCOVIDWebView rrcovidWebView = new RRCOVIDWebView();
                rrcovidWebView.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidWebView, "RRCOVIDWebView");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.kcdh_link: {
                args.putString("url", "https://www.kenoshacounty.org/297/Health-Services");
                RRCOVIDWebView rrcovidWebView = new RRCOVIDWebView();
                rrcovidWebView.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidWebView, "RRCOVIDWebView");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.ken_resp_link: {
                args.putString("url", "https://kenosha-county-covid-19-response-kenoshacounty.hub.arcgis.com/");
                RRCOVIDWebView rrcovidWebView = new RRCOVIDWebView();
                rrcovidWebView.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidWebView, "RRCOVIDWebView");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.kch_link: {
                args.putString("url", "https://www.facebook.com/kenoshacountyhealth/");
                RRCOVIDWebView rrcovidWebView = new RRCOVIDWebView();
                rrcovidWebView.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidWebView, "RRCOVIDWebView");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.rc_resp_link: {
                args.putString("url", "https://crchd.com/covid-19");
                RRCOVIDWebView rrcovidWebView = new RRCOVIDWebView();
                rrcovidWebView.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidWebView, "RRCOVIDWebView");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.crchd_link: {
                args.putString("url", "https://www.facebook.com/CRCHD/");
                RRCOVIDWebView rrcovidWebView = new RRCOVIDWebView();
                rrcovidWebView.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidWebView, "RRCOVIDWebView");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            //</editor-fold>
            default:
                break;
        }
    }
}
