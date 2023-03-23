package appfactory.uwp.edu.parksideapp2.Map;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.location.OverpassAPIProvider;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import appfactory.uwp.edu.parksideapp2.Models.PlaceObj;
import appfactory.uwp.edu.parksideapp2.R;
import timber.log.Timber;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * Created by kyluong09 on 6/27/18.
 */

public class MainMap extends Fragment {
    // LOG TAG
    private final String TAG = "MainMap";
    // Map View
    private MapView mapView;
    private MyLocationNewOverlay locationOverlay;
    private CompassOverlay compassOverlay;
    FusedLocationProviderClient locationRequest = null;
//    private final int LOCATION_REQUEST_CODE = 123;
    // Permission list
    public final static float ZOOM_IN_LEVEL = 16.7f;
    public final static float DEFAULT_ZOOM = 15.3f;
    //UI
    private RecyclerView placeRecyclerView;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private ImageButton myLocationButton;
    // List
    ArrayList<PlaceObj> placeList = new ArrayList<>();
    // Check doQuery
    boolean isDoQuery = false;
    // Saved FolderKML
    private FolderOverlay savedFolderOverlay = null;

    /**
     * Initialize user interface components
     *
     * @param view
     */
    private void initUI(View view) {
        Timber.d("Initializing User Interface");
        placeRecyclerView = view.findViewById(R.id.place_recycler_view);
        slidingUpPanelLayout = view.findViewById(R.id.sliding_layout);
        myLocationButton = view.findViewById(R.id.my_location_button);
    }

    /**
     * Initialize map
     */
    private void initMap(View view) {
        Log.d(TAG, " Initializing Open Street Map");
        // Init FusedLocation
        locationRequest = LocationServices.getFusedLocationProviderClient(getContext());
        IGeoPoint defaultPoint = new GeoPoint(42.646204, -87.852217);
        // Set map ID
        mapView = view.findViewById(R.id.map_view);
        // Default set up
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        // Add  user's device location  overlay and compass
        setUpOverlay(mapView);
        mapView.setTilesScaledToDpi(true);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        mapView.setMultiTouchControls(true);
        // Set default zoom center
        mapView.setExpectedCenter(defaultPoint);
        mapView.getController().setCenter(defaultPoint);
        mapView.getController().setZoom(DEFAULT_ZOOM);
        Timber.d("Default Center GeoPoint: " + defaultPoint + " | Zoom: " + DEFAULT_ZOOM);
        // Map listener
        mapView.addMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                Double level = event.getZoomLevel();

                if (level > 15.5f) {
                    if (!isDoQuery) {
                        new BackgroundWork().execute(event.getSource().getBoundingBox());
                        isDoQuery = true;
                    }
                } else if (level < 18f) {
                    if (isDoQuery) {
                        mapView.getOverlays().remove(savedFolderOverlay);
                        mapView.invalidate();
                        isDoQuery = false;
                    }
                }
                return false;
            }
        });

        // Find current user location
        // Set location button listener to update/ animate to location
        myLocationButton.setOnClickListener(v -> {
            try{
                locationRequest.getLastLocation().addOnSuccessListener(location -> {
                    GeoPoint deviceLocationGeo = new GeoPoint(location.getLatitude(),location.getLongitude());
                    mapView.getController().animateTo(deviceLocationGeo);
                    mapView.getController().setZoom(DEFAULT_ZOOM);

                });
            } catch (SecurityException ignored){ }


        });

        // Add list marker overlay
        ArrayList<Marker> markerList = new ArrayList();
        for (PlaceObj place : placeList) {
            Marker marker = new Marker(mapView);
            marker.setTitle(place.getName());
            marker.setSnippet(place.getAddress());
            marker.setImage(getResources().getDrawable(place.getImageId()));
            marker.setPosition(new GeoPoint(place.getLongitude(), place.getLatude()));
            marker.setIcon(getResources().getDrawable(R.drawable.ic_building_marker));
            InfoWindow infoWindow = new InforWindow(R.layout.marker_view, mapView, getContext());

            marker.setInfoWindow(infoWindow);
            marker.setInfoWindowAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setOnMarkerClickListener((marker1, mapView) -> {
                GeoPoint tempGeo = new GeoPoint(marker1.getPosition().getLatitude() + 0.001, marker1.getPosition().getLongitude());
                mapView.getController().animateTo(tempGeo);
                marker1.showInfoWindow();
                return false;
            });

            markerList.add(marker);
        }
        mapView.getOverlays().addAll(markerList);
        // Set recyclerView adapter
        PlaceAdapter adapter = new PlaceAdapter(placeList, markerList, mapView, slidingUpPanelLayout);
        placeRecyclerView.setAdapter(adapter);
        // Refresh
        mapView.invalidate();
    }

    public MainMap() {
    }

    /**
     * Ask user for a permission
     */
//    private void getPermission() {
//        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
//        // if permissions already granted
//        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                this.requestPermissions(permission, LOCATION_REQUEST_CODE);
//            }
//            else {
//                ActivityCompat.requestPermissions(getActivity(), permission, LOCATION_REQUEST_CODE);
//            }
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == LOCATION_REQUEST_CODE) {
//            if (grantResults.length > 0) {
//                for (int grantResult : grantResults) {
//                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
//                        Navigation.findNavController(getActivity(), R.id.navHostFragment).popBackStack();
//                        return;
//                    }
//                }
////                    getBuildingData();
//            }
//        }
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getPermission();
    }

    private void getBuildingData() {
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        //There are new requirements for pendingintent flags that are new to 23+ and required for 31+
        // This line separates out the test for them to a separate assignment.
        final int testFlags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_CANCEL_CURRENT|PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_CANCEL_CURRENT;
        final PendingIntent test = PendingIntent.getBroadcast(getContext(), 0, intent, testFlags);
        AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.currentThreadTimeMillis(), 1000, test);

        // Generate Parkside's Building data from json file
        try {
            JSONObject jsonObject = new JSONObject(loadJsonFile("parkside_buildings.json"));
            JSONArray buildingList = jsonObject.getJSONArray("building");
            for(int i = 0; i < buildingList.length(); i++){
                JSONObject buildingListJSONObject = buildingList.getJSONObject(i);
                // Get name
                String name = buildingListJSONObject.getString("name");
                // Get images from Drawable
                String imageName = buildingListJSONObject.getString("image_name");
                int resourceId = getResources().getIdentifier(imageName, "drawable", getContext().getPackageName());
                // Get address
                String address = buildingListJSONObject.getString("address");
                // Get longtitude
                double longitude = buildingListJSONObject.getDouble("longitude");
                // get latitude
                double latitude = buildingListJSONObject.getDouble("latitude");
                // Create an object and insert into arrayList
                PlaceObj building = new PlaceObj(name, resourceId, address, longitude, latitude);
                placeList.add(building);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method loads a json file from asset folder and return as a string
     * @return
     */
    private String loadJsonFile(String fileName){
        String json = "";
        try {
            InputStream inputStream = getActivity().getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_map, container, false);
        // Init UI
        initUI(view);
        // Set up place recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        placeRecyclerView.setLayoutManager(layoutManager);
        // Check permission and map async
        getBuildingData();
        initMap(view);
        return view;
    }

    @Override
    public void onResume() {
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


    /**
     * Set user's device location overlay
     *
     * @param mapView
     */
    private synchronized void setUpOverlay(final MapView mapView) {
        Timber.d("Setting up overlay");
        // Convert drawable icon to bitmap
        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_user_location);
        // Enable location and follow location
        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getContext()), mapView);
        locationOverlay.setPersonIcon(icon);
        locationOverlay.enableMyLocation();
        mapView.getOverlays().add(locationOverlay);

        // Enable compass overlay
        compassOverlay = new CompassOverlay(getActivity(), new InternalCompassOrientationProvider(getActivity()), mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);
    }

    private class BackgroundWork extends AsyncTask<BoundingBox, String, FolderOverlay> {
        @Override
        protected FolderOverlay doInBackground(BoundingBox... boundingBoxes) {
            FolderOverlay folderOverlay = new FolderOverlay();
            BoundingBox boundingBox = new BoundingBox();
            boundingBox.set(42.64831, -87.85086, 42.64472, -87.8584);
            OverpassAPIProvider overpassAPIProvider = new OverpassAPIProvider();
            String url = overpassAPIProvider.urlForTagSearchKml("natural=tree", boundingBoxes[0], 5000, 30);
            KmlDocument kmlDocument = new KmlDocument();
            overpassAPIProvider.addInKmlFolder(kmlDocument.mKmlRoot, url);
            FolderOverlay tempFolderOverlay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mapView, null, null, kmlDocument);

            for (Overlay overlay : tempFolderOverlay.getItems()) {
                if (overlay instanceof Marker) {
                    // Init marker
                    Marker marker = markerHandler(overlay);
                    folderOverlay.add(marker);
                }
            }

            return folderOverlay;
        }

        @Override
        protected void onPostExecute(FolderOverlay s) {
            super.onPostExecute(s);
            mapView.getOverlays().add(s);
        }
    }

    /**
     * This handler tree marker to making better format tree inforwindow
     *
     * @param overlay
     * @return
     */
    private Marker markerHandler(Overlay overlay) {
        // Variables
        String title ="";
        String description="";
        Marker thisMarker = (Marker) overlay;
        // Remove current overlap icon
        // Get subDescription and store into array string
        String[] tempString = thisMarker.getSubDescription().split("<br>");
        thisMarker.setSubDescription("");
        // Run through tempString to get subString for title and description
        for (String s : tempString) {
            if (s.contains("species=")) {
                title = s.replace("species=", "");
                if (title.contains("null")) {
                    thisMarker.setTitle("No Info");
                } else {
                    thisMarker.setTitle(title);
                }
            }

            if (s.contains("species:en=")) {
                description = s.replace("species:en=", "");
                if (description.contains("null")) {
                    thisMarker.setSnippet("");
                } else {
                    thisMarker.setSnippet(description);
                }
            }
        }

        thisMarker.setIcon(getResources().getDrawable(R.drawable.ic_tree));
        return thisMarker;
    }
}
