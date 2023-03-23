package appfactory.uwp.edu.parksideapp2.Models;

import java.util.ArrayList;

/**
 * Created by kyluong09 on 5/29/18.
 */

public class RouteObj {
    private ArrayList<ArrayList<Double>> route;
    private ArrayList<StepObj> stepList;

    public RouteObj(ArrayList<ArrayList<Double>> route, ArrayList<StepObj> stepList) {

        this.route = route;
        this.stepList = stepList;
    }

    public ArrayList<ArrayList<Double>> getRoute() {
        return route;
    }

    public void setRoute(ArrayList<ArrayList<Double>> route) {
        this.route = route;
    }

    public ArrayList<StepObj> getStepList() {
        return stepList;
    }

    public void setStepList(ArrayList<StepObj> stepList) {
        this.stepList = stepList;
    }
}
