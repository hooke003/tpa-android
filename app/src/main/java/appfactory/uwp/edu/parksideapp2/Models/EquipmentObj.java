package appfactory.uwp.edu.parksideapp2.Models;

/**
 * Created by mingxi on 6/27/18.
 */

public class EquipmentObj {

    // Vars
    private int id;
    private String name;

    // Empty Constructor
    public EquipmentObj(){

    }

    // Constructor with para
    public EquipmentObj(int id, String name){
        this.id = id;
        this.name = name;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
