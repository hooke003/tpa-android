package appfactory.uwp.edu.parksideapp2.Models;

/**
 * Created by mingxi on 7/26/18.
 */

public class LabBuildingObj {

    private String building;
    private int labsNumber;

    public LabBuildingObj(){

    }

    public LabBuildingObj(String building, int labsNumber){
        this.building = building;
        this.labsNumber = labsNumber;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public int getLabsNumber() {
        return labsNumber;
    }

    public void setLabsNumber(int labsNumber) {
        this.labsNumber = labsNumber;
    }
}
