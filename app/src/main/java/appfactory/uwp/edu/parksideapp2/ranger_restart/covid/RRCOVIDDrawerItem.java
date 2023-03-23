package appfactory.uwp.edu.parksideapp2.ranger_restart.covid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.nightwhistler.htmlspanner.HtmlSpanner;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

import appfactory.uwp.edu.parksideapp2.R;
import appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants;
import timber.log.Timber;

/**
 * This class initialized the view for the selected DrawerLayout item.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class RRCOVIDDrawerItem extends Fragment {
    private final String TAG = "RRCOVIDDrawerItem";

    private TextView itemTitleTextView;
    private TextView itemBodyTextView;

    private void initUI(View view) {
        itemTitleTextView = view.findViewById(R.id.itemTitleTextView);
        String title = requireArguments().getString("title");
        itemTitleTextView.setText(title);
        itemBodyTextView = view.findViewById(R.id.itemBodyTextView);

        // Auto assign to column 1
        for (HashMap<String, String> data : UserConstants.COVID_DRAWER_DATA)
            try {
                if (Objects.requireNonNull(title).equalsIgnoreCase(data.get("title"))) {
                    Pattern imgTagRegEx = Pattern.compile("<img.*>");
                    String body = imgTagRegEx.matcher(data.get("body")).replaceAll("");
                    Timber.d(body);
                    itemBodyTextView.setText(new HtmlSpanner().fromHtml(String.format("<style>body {font-family: \"Trebuchet MS\", Helvetica, sans-serif;background-color: #F7F7F7}p {font-size: 50px;}strong {color: #0D8556;}li {font-size: 50px;}</style><body>%s</body>", body)));

                    break;
                }
            } catch (Exception e) {
                itemBodyTextView.setText("");
            }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rr_drawer_item, container, false);
        initUI(view);
        return view;
    }
}
