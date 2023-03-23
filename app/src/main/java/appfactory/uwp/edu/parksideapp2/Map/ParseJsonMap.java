package appfactory.uwp.edu.parksideapp2.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import appfactory.uwp.edu.parksideapp2.Models.RouteObj;
import appfactory.uwp.edu.parksideapp2.Models.StepObj;

/**
 * Created by kyluong09 on 5/29/18.
 */

public class ParseJsonMap {
    private ArrayList<StepObj> stepObjArrayList = new ArrayList<>();

    public RouteObj parseJson(String data) throws JSONException{
        if(data == null){
            return null;
        }
        else{
            JSONObject mainObject = new JSONObject(data);
            JSONArray routes = mainObject.getJSONArray("features");
            // Run through all routes
            JSONObject jsonObject = routes.getJSONObject(0);
//            String polyline = jsonObject.getString("geometry");
            JSONArray pl = jsonObject
                    .getJSONObject("geometry")
                    .getJSONArray("coordinates");

            ArrayList<ArrayList<Double>> polyline = new ArrayList<>();
            for (int i = 0; i < pl.length(); i ++) {
                ArrayList<Double> temp = new ArrayList<>();
                for (int j = 0; j < pl.getJSONArray(i).length(); j ++)
                    temp.add(pl.getJSONArray(i).getDouble(j));
                polyline.add(temp);
            }

            JSONObject propertiesObject = jsonObject.getJSONObject("properties");
            JSONArray segmentsArray = propertiesObject.getJSONArray("segments");
//            JSONObject fastestRoute = segmentsArray.getJSONObject(0);
//            JSONArray stepList = fastestRoute.getJSONArray("steps");
            JSONObject fastestRoute = segmentsArray.getJSONObject(0);
            JSONArray stepList = fastestRoute.getJSONArray("steps");

            // Go through all array and add into arrayList
            for(int i = 0; i < stepList.length(); i++){
               JSONObject object =  stepList.getJSONObject(i);
               double distance = object.getDouble("distance");
               double duration = object.getDouble("duration");
               int type = object.getInt("type");
               String instruction = object.getString("instruction");
               // Create Step Object
                StepObj step = new StepObj(distance,duration,type,instruction);
               stepObjArrayList.add(step);
            }

            RouteObj routeObj = new RouteObj(polyline, stepObjArrayList);
            return routeObj;
        }
    }
}
