//package appfactory.uwp.edu.parksideapp2.geofencing;
//
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.ContextWrapper;
//import android.content.Intent;
//
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.location.Geofence;
//import com.google.android.gms.location.GeofenceStatusCodes;
//import com.google.android.gms.location.GeofencingRequest;
//
//public class GeofenceHelper extends ContextWrapper {
//    PendingIntent pendingIntent;
//
//    public GeofenceHelper(Context base) {
//        super(base);
//    }
//
//    public GeofencingRequest getGeofencingRequest(Geofence geofence) {
//        return new GeofencingRequest.Builder()
//                .addGeofence(geofence)
//                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
//                .build();
//    }
//
//    public Geofence getGeofence() {
//        return new Geofence.Builder()
//                .setCircularRegion(42.6441, -87.8523, (float) 2500.0)
//                .setRequestId("UW-P")
//                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
//                // This delay will be for how long you will need to be in the region until you are considered to be inside
//                .setLoiteringDelay(5000)
//                .setExpirationDuration(Geofence.NEVER_EXPIRE)
//                .build();
//    }
//
//    public PendingIntent getPendingIntent() {
//        if (pendingIntent != null) {
//            return pendingIntent;
//        }
//        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
//        pendingIntent = PendingIntent.getBroadcast(this, 2607, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        return pendingIntent;
//    }
//
//    public String getErrorString(Exception e) {
//        if (e instanceof ApiException) {
//            ApiException apiException = (ApiException) e;
//            switch (apiException.getStatusCode()) {
//                case GeofenceStatusCodes
//                        .GEOFENCE_NOT_AVAILABLE:
//                    return "GEOFENCE_NOT_AVAILABLE";
//                case GeofenceStatusCodes
//                        .GEOFENCE_TOO_MANY_GEOFENCES:
//                    return "GEOFENCE_TOO_MANY_GEOFENCES";
//                case GeofenceStatusCodes
//                        .GEOFENCE_TOO_MANY_PENDING_INTENTS:
//                    return "GEOFENCE_TOO_MANY_PENDING_INTENTS";
//            }
//        }
//        return e.getLocalizedMessage();
//    }
//}
