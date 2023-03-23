package appfactory.uwp.edu.parksideapp2.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.PolyUtil;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import appfactory.uwp.edu.parksideapp2.HttpRequest.HttpRequest;
import appfactory.uwp.edu.parksideapp2.Models.PlaceObj;
import appfactory.uwp.edu.parksideapp2.Models.RouteObj;
import appfactory.uwp.edu.parksideapp2.R;
import timber.log.Timber;


public class DirectionMapActivity extends AppCompatActivity {
    // Vars
    private float defaultZoom = 16.0f;
    private MapView mapView;
    private FusedLocationProviderClient locationRequest = null;
    private MyLocationNewOverlay locationOverlay;
    private CompassOverlay compassOverlay;
    private final String TAG = "DirectionMapActivity";
    private String locationName;
    private GeoPoint destinationCoord;
    private GeoPoint currentLocationCoordination;
    private Bundle bundle;
    private String routeResult;
    // List
    ArrayList<PlaceObj> placeList = new ArrayList<>();
    private boolean didRequest = false;
    // UI
    private RecyclerView stepRecyclerView;
    private StepAdapter stepAdapter;
    private ImageButton myLocationButton;

    private void initUI() {
        stepRecyclerView = (RecyclerView) findViewById(R.id.step_recycler_view);
        myLocationButton = (ImageButton) findViewById(R.id.my_location_button);

    }

    private void initMap(GeoPoint destination) {
        // Find map ID
        mapView = findViewById(R.id.map_view);
        // Init FusedLocation
        locationRequest = LocationServices.getFusedLocationProviderClient(this);
        // Set title source
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        getDeviceLocationOverlay(mapView);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        mapView.setMultiTouchControls(true);
        // Add user's device location overlay and compass
        // Default Camera Zoom
        IMapController mapController = mapView.getController();
        mapController.setZoom(defaultZoom);
        mapController.setCenter(destinationCoord);
        // Set marker for destination
        Marker destinationMarker = new Marker(mapView);
        destinationMarker.setPosition(destination);
        destinationMarker.setIcon(getResources().getDrawable(R.drawable.ic_destination));
        destinationMarker.setAnchor(0.5f,Marker.ANCHOR_BOTTOM);
        destinationMarker.setOnMarkerClickListener((marker, mapView) -> false);
        mapView.getOverlays().add(destinationMarker);
        // Refresh map
        mapView.invalidate();

        // Set location button listener to update/ animate to location
        myLocationButton.setOnClickListener(v -> {
            try{
                locationRequest.getLastLocation().addOnSuccessListener(location -> {
                    GeoPoint deviceLocationGeo = new GeoPoint(location.getLatitude(),location.getLongitude());
                    mapView.getController().animateTo(deviceLocationGeo);
                });
            } catch (SecurityException e){
                Timber.d("SecurityException:" + e);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
        if (locationOverlay != null) {
            locationOverlay.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
        if (locationOverlay != null) {
            locationOverlay.onPause();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        setContentView(R.layout.activity_direction_map);
        // get bundle from InforWindow class
        bundle = getIntent().getBundleExtra("markerBundle");
        locationName = bundle.getString("locationName");
        destinationCoord = bundle.getParcelable("locationDestination");
        //currentLocationCoordination = bundle.getParcelable("currentLocationCoordination");
        Timber.d("Location name: " + locationName + " - Origin: " + currentLocationCoordination + " - Destination: " + destinationCoord);
        // Init UI
        initUI();
        // Init Map
        initMap(destinationCoord);
        try{
            locationRequest.getLastLocation().addOnSuccessListener(location -> {
                currentLocationCoordination = new GeoPoint(location.getLatitude(),location.getLongitude());
                // Create AsyncParam to have multiple input
                AsyncParam asyncParam = new AsyncParam(currentLocationCoordination, destinationCoord);
                // Pull route data from API and draw polyline
                new BackgroundTask().execute(asyncParam);
            });
        } catch (SecurityException e){
            Timber.d("SecurityException: " + e);
        }

        // Set up recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        stepRecyclerView.setLayoutManager(layoutManager);
        // Find current user location button
        final FusedLocationProviderClient locationRequest = LocationServices.getFusedLocationProviderClient(this);

    }

    /**
     * Background Task
     */
    @SuppressLint("StaticFieldLeak")
    public class BackgroundTask extends AsyncTask<AsyncParam, String, RouteObj> {

        @Override
        protected RouteObj doInBackground(AsyncParam... asyncParams) {
            // Create coordinate string
            String origin = asyncParams[0].getOrigin().getLongitude() + "," + asyncParams[0].getOrigin().getLatitude();
            String destination = asyncParams[0].getDestination().getLongitude() + "," + asyncParams[0].getDestination().getLatitude();

            // GET data
            try {
                routeResult = new HttpRequest().getUrlDirection(origin, destination);
                RouteObj routeObj = new ParseJsonMap().parseJson(routeResult);
                return routeObj;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Draw or update polyline base on route from api request
         *
         * @param route
         */
        @Override
        protected void onPostExecute(RouteObj route) {
            super.onPostExecute(route);
            org.osmdroid.views.overlay.Polyline line = new org.osmdroid.views.overlay.Polyline();
            line.setPoints(decodePolyline(route.getRoute()));
            line.getOutlinePaint().setColor(getResources().getColor(R.color.colorPrimaryDark));
            mapView.getOverlayManager().add(line);
            stepAdapter = new StepAdapter(route.getStepList());
            stepRecyclerView.setAdapter(stepAdapter);
        }
    }

    /**
     * decode polyline-code and return a list of geopoint
     * @param polylineCode
     * @return
     */
    private ArrayList<GeoPoint> decodePolyline(ArrayList<ArrayList<Double>> polylineCode) {
        //Var
        ArrayList<GeoPoint> geoPointsList = new ArrayList<>();
        //Decode polineCode into LatLong List
//        List<LatLng> latLngList = PolyUtil.decode(polylineCode);
        List<LatLng> latLngList = new ArrayList<>();
        for (int i = 0; i < polylineCode.size(); i++)
            latLngList.add(new LatLng(polylineCode.get(i).get(0), polylineCode.get(i).get(1)));

        for (int i = 0; i < latLngList.size(); i++) {
            double latitude = latLngList.get(i).latitude;
            double longitude = latLngList.get(i).longitude;
            GeoPoint geoPoint = new GeoPoint(latitude, longitude);
            geoPointsList.add(geoPoint);
        }
        return geoPointsList;
    }

    /**
     * Set user's device location overlay
     * @param mapView
     */
    private synchronized void getDeviceLocationOverlay(MapView mapView) {
            // Convert drawble icon to bitmap
            Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_user_location);
            // Enable location and follow location
            locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
            locationOverlay.setPersonIcon(icon);
            locationOverlay.enableMyLocation();
            //locationOverlay.enableFollowLocation();
            mapView.getOverlays().add(locationOverlay);

            // Enable compass overlay
            compassOverlay = new CompassOverlay(this, new InternalCompassOrientationProvider(this), mapView);
            compassOverlay.enableCompass();
            mapView.getOverlays().add(this.compassOverlay);
    }
}
