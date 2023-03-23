package appfactory.uwp.edu.parksideapp2.ComputerLab;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Arrays;

import appfactory.uwp.edu.parksideapp2.Models.LabObj;
import io.realm.RealmList;

/**
 * Created by kyluong09 on 6/1/18.
 */

public class LabSaxHandler extends DefaultHandler {
    private StringBuilder stringBuilder;
    private LabObj lab;
    private String tempChar;
    private ArrayList<LabObj> labList = new ArrayList<>();


    //Boolean
    private boolean idBool;
    private boolean buildingBool;
    private boolean buildingLevelBool;
    private boolean roomNumberBool;
    private boolean amountMacBool;
    private boolean amountWinBool;
    private boolean instructorComputerTypeBool;
    private boolean classRoomTypeBool;
    private boolean capacityBool;
    private boolean equipmentBool;

    public LabSaxHandler() {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equalsIgnoreCase("lab")) {
            lab = new LabObj();
        } else if (qName.equalsIgnoreCase("id")) {
            tempChar = "";
            idBool = true;
        } else if (qName.equalsIgnoreCase("building")) {
            tempChar = "";
            buildingBool = true;
        } else if (qName.equalsIgnoreCase("buildinglevel")) {
            tempChar = "";
            buildingLevelBool = true;
        } else if (qName.equalsIgnoreCase("roomnumber")) {
            tempChar = "";
            roomNumberBool = true;
        } else if (qName.equalsIgnoreCase("amountmac")) {
            tempChar = "";
            amountMacBool = true;
        } else if (qName.equalsIgnoreCase("amountwin")) {
            tempChar = "";
            amountWinBool = true;
        } else if (qName.equalsIgnoreCase("instructorcomputertype")) {
            tempChar = "";
            instructorComputerTypeBool = true;
        } else if (qName.equalsIgnoreCase("classroomtype")) {
            tempChar = "";
            classRoomTypeBool = true;
        } else if (qName.equalsIgnoreCase("capacity")) {
            tempChar = "";
            capacityBool = true;
        } else if (qName.equalsIgnoreCase("equipment")) {
            tempChar = "";
            equipmentBool = true;
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (qName.equalsIgnoreCase("lab")) {
            labList.add(lab);
        } else if (qName.equalsIgnoreCase("id")) {
            lab.setId(Integer.parseInt(tempChar));
            idBool = false;
        } else if (qName.equalsIgnoreCase("building")) {
            lab.setBuilding(tempChar);
            buildingBool = false;
        } else if (qName.equalsIgnoreCase("buildinglevel")) {
            lab.setBuildingLevel(tempChar);
            buildingLevelBool = false;
        } else if (qName.equalsIgnoreCase("roomnumber")) {
            lab.setRoomNumber(tempChar);
            roomNumberBool = false;
        } else if (qName.equalsIgnoreCase("amountmac")) {
            if(!tempChar.isEmpty()){
                lab.setAmountMac(Integer.parseInt(tempChar));
                amountMacBool = false;
            }else{
                lab.setAmountMac(0);
                amountMacBool = false;
            }
        } else if (qName.equalsIgnoreCase("amountwin")) {
            if(!tempChar.isEmpty()){
                lab.setAmountWin(Integer.parseInt(tempChar));
                amountWinBool = false;
            }else{
                lab.setAmountWin(0);
                amountWinBool = false;
            }
        } else if (qName.equalsIgnoreCase("instructorcomputertype")) {
            lab.setInstructorComputerType(tempChar);
            instructorComputerTypeBool = false;
        } else if (qName.equalsIgnoreCase("classroomtype")) {
            lab.setClassRoomType(tempChar);
            classRoomTypeBool = false;
        } else if (qName.equalsIgnoreCase("capacity")) {
            lab.setCapacity(Integer.parseInt(tempChar));
            capacityBool = false;
        } else if (qName.equalsIgnoreCase("equipment")){

            RealmList<String> list = new RealmList<>();
            list.addAll(Arrays.asList(tempChar.split(",")));
            lab.setEquipmentNewList(list);
            equipmentBool = false;
        }

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (idBool) {
            tempChar = new String(ch, start, length);
        } else if (buildingBool) {
            tempChar = new String(ch, start, length);
        } else if (buildingLevelBool) {
            tempChar = new String(ch, start, length);
        } else if (roomNumberBool) {
            tempChar = new String(ch, start, length);
        } else if (amountMacBool) {
            tempChar = new String(ch, start, length);
        } else if (amountWinBool) {
            tempChar = new String(ch, start, length);
        } else if (instructorComputerTypeBool) {
            tempChar = new String(ch, start, length);
        } else if (classRoomTypeBool) {
            tempChar = new String(ch, start, length);
        } else if (capacityBool) {
            tempChar = new String(ch, start, length);
        } else if (equipmentBool) {
            tempChar = new String(ch, start, length);
        }
    }

    public ArrayList<LabObj> returnData(){
        return this.labList;
    }
}
