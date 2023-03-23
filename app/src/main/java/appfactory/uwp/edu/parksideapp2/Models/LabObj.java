package appfactory.uwp.edu.parksideapp2.Models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kyluong09 on 6/1/18.
 */

public class LabObj extends RealmObject{
    // Vars
    @PrimaryKey
    private int id;
    private String building;
    private String buildingLevel;
    private String roomNumber;
    private int amountMac;
    private int amountWin;
    private String instructorComputerType;
    private String classRoomType;
    private int capacity;
    private String equipmentIdList;
    // This is the list to store equipment with their name
    private RealmList<String> equipmentNewList;

    /**
     * Empty constructor
     */
    public LabObj() {

    }

    /**
     * Constructor with paramters
     *
     * @param id
     * @param building
     * @param buildingLevel
     * @param amountMac
     * @param amountWin
     * @param instructorComputerType
     * @param classRoomType
     * @param capacity
     */
    public LabObj(int id, String building, String roomNumber, String buildingLevel, int amountMac, int amountWin,
                  String instructorComputerType, String classRoomType, int capacity, String equipmentIdList, RealmList<String> equipmentNewList) {
        this.id = id;
        this.building = building;
        this.buildingLevel = buildingLevel;
        this.roomNumber = roomNumber;
        this.amountMac = amountMac;
        this.amountWin = amountWin;
        this.instructorComputerType = instructorComputerType;
        this.classRoomType = classRoomType;
        this.capacity = capacity;
        this.equipmentIdList = equipmentIdList;
        this.equipmentNewList = equipmentNewList;
    }

    // All getter and setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getBuildingLevel() {
        return buildingLevel;
    }

    public void setBuildingLevel(String buildingLevel) {
        this.buildingLevel = buildingLevel;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getAmountMac() {
        return amountMac;
    }

    public void setAmountMac(int amountMac) {
        this.amountMac = amountMac;
    }

    public int getAmountWin() {
        return amountWin;
    }

    public void setAmountWin(int amountWin) {
        this.amountWin = amountWin;
    }

    public String getInstructorComputerType() {
        return instructorComputerType;
    }

    public void setInstructorComputerType(String instructorComputerType) {
        this.instructorComputerType = instructorComputerType;
    }

    public String getClassRoomType() {
        return classRoomType;
    }

    public void setClassRoomType(String classRoomType) {
        this.classRoomType = classRoomType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getEquipmentIdList() {
        return equipmentIdList;
    }

    public void setEquipmentIdList(String equipmentIdList) {
        this.equipmentIdList = equipmentIdList;
    }

    public RealmList<String> getEquipmentNewList() {
        return equipmentNewList;
    }

    public void setEquipmentNewList(RealmList<String> equipmentNewList) {
        this.equipmentNewList = equipmentNewList;
    }
}
