package appfactory.uwp.edu.parksideapp2.Map;

import org.osmdroid.util.GeoPoint;

/**
 * Created by kyluong09 on 7/22/18.
 */

/**
 * This class is for holding multiple asynctask parameter
 */
public class AsyncParam {
    private GeoPoint origin;
    private GeoPoint destination;

    public AsyncParam(GeoPoint origin, GeoPoint destination){
        this.origin = origin;
        this.destination = destination;

    }

    public GeoPoint getOrigin() {
        return origin;
    }

    public void setOrigin(GeoPoint origin) {
        this.origin = origin;
    }

    public GeoPoint getDestination() {
        return destination;
    }

    public void setDestination(GeoPoint destination) {
        this.destination = destination;
    }
}
