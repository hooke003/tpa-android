package appfactory.uwp.edu.parksideapp2.Map;

import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.List;

/**
 * Created by kyluong09 on 7/25/18.
 */

public class LocationHandler {
    static GeoPoint deviceLocation;

    /**
     * Get user's current location coordination
     *
     * @param locationManager
     * @return
     */
    public static GeoPoint getUserCurrentLocation(LocationManager locationManager) {

        String gpsProvider = LocationManager.GPS_PROVIDER;
        GeoPoint geoPoint = null;
        List locationProviderList = locationManager.getProviders(true);

        if (locationProviderList.contains(gpsProvider)) {
            try {
                Location mLocation = locationManager.getLastKnownLocation(gpsProvider);
                if (mLocation != null) {
                    geoPoint = new GeoPoint(mLocation.getLatitude(), mLocation.getLongitude());
                }
            } catch (SecurityException e) {
                // catches permission problems
                e.printStackTrace();
            }
        }
        return geoPoint;
    }

    public static GeoPoint getDeviceLocation(FusedLocationProviderClient fusedLocationProviderClient) {
        try {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    deviceLocation = new GeoPoint(location.getLatitude(),location.getLongitude());
                }
            });
        } catch (SecurityException e) {
            // catches permission problems
            e.printStackTrace();
        }

        return  deviceLocation;
    }

    /**
     * Find user's current location
     *
     * @param mapView
     * @param locationManager
     */
    public static void findMyLocation(MapView mapView, LocationManager locationManager) {
        GeoPoint newGeoPoint = getUserCurrentLocation(locationManager);

    }
    public static void findMyLocationTesting(MapView mapView, FusedLocationProviderClient fusedLocationProviderClient) {
        getDeviceLocation(fusedLocationProviderClient);

    }

    /**
     * Update waypoint to a new location
     *
     * @param mapView
     * @param geoPoint
     */
    public static void updateLocation(MapView mapView, GeoPoint geoPoint) {
        mapView.getController().setCenter(geoPoint);
    }
}
