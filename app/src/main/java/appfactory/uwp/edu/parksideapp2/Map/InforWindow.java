package appfactory.uwp.edu.parksideapp2.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import appfactory.uwp.edu.parksideapp2.R;

/**
 * Created by kyluong09 on 7/21/18.
 */

public class InforWindow extends InfoWindow {
    private Context context;
    private MapView mapView;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public InforWindow(int layoutResId, MapView mapView, Context context) {
        super(layoutResId, mapView);
        this.context = context;
        this.mapView = mapView;
    }

    @Override
    public void onOpen(final Object item) {
        final Marker currentMarker = (Marker) item;
        // Init UI
        ImageView locationImage = mView.findViewById(R.id.location_image);
        TextView locationName = mView.findViewById(R.id.location_name);
        TextView locationAddress = mView.findViewById(R.id.description);

        Button directionButton = mView.findViewById(R.id.direction_button);
        ImageButton hideButton = mView.findViewById(R.id.hide_button);

        // Set UI
        locationImage.setImageDrawable(currentMarker.getImage());
        locationName.setText(currentMarker.getTitle());

        // Start DirectionActivity
        locationAddress.setText(currentMarker.getSnippet());
        directionButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, DirectionMapActivity.class);
            intent.putExtra("markerBundle", getMarkerInfor(currentMarker));
            context.startActivity(intent);
        });

        hideButton.setOnClickListener(v -> {
            //mapView.getController().setZoom(DEFAULT_ZOOM);
            currentMarker.closeInfoWindow();
        });

        // Hide current infowindow when click on other inforwindow
        for (int i = 0; i < mMapView.getOverlays().size(); ++i) {
            Overlay o = mMapView.getOverlays().get(i);
            if (o instanceof Marker) {
                Marker m = (Marker) o;
                Marker current = (Marker) item;
                if (!m.getTitle().equals(current.getTitle()))
                    m.closeInfoWindow();
            }
        }
    }

    @Override
    public void onClose() {
    }

    private Bundle getMarkerInfor(Marker marker) {

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        Bundle bundle = new Bundle();
        bundle.putString("locationName", marker.getTitle());
        bundle.putParcelable("locationDestination", marker.getPosition());

        //bundle.putParcelable("currentLocationCoordination", LocationHandler.getDeviceLocation(fusedLocationProviderClient));

        return bundle;
    }
}
