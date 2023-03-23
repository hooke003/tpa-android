package appfactory.uwp.edu.parksideapp2.Models;

/**
 * Created by kyluong09 on 6/5/18.
 */

public class StepObj {
    private double distance;
    private double duration;
    private int type;
    private String instruction;

    public StepObj(double distance, double duration, int type, String instruction) {
        this.distance = distance;
        this.duration = duration;
        this.type = type;
        this.instruction = instruction;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

}
