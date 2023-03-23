package appfactory.uwp.edu.parksideapp2.ComputerLab;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

import appfactory.uwp.edu.parksideapp2.Models.EquipmentObj;

/**
 * Created by mingxi on 6/27/18.
 */

public class EquipmentSaxHandler extends DefaultHandler {
    private static final String TAG = "EquipmentSaxHandler";
    private EquipmentObj equipment;
    private String tempChar;
    private ArrayList<EquipmentObj> equipmentList = new ArrayList<>();

    // Boolean
    private boolean idBool;
    private boolean nameBool;

    public EquipmentSaxHandler(){
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equalsIgnoreCase("equipment")) {
            equipment = new EquipmentObj();
        } else if (qName.equalsIgnoreCase("id")) {
            tempChar = "";
            idBool = true;
        } else if (qName.equalsIgnoreCase("name")) {
            tempChar = "";
            nameBool = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if(idBool) {
            tempChar = new String(ch, start, length);
        } else if(nameBool) {
            tempChar = new String(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if(qName.equalsIgnoreCase("equipment")) {
            equipmentList.add(equipment);
        } else if(qName.equalsIgnoreCase("id")) {
            equipment.setId(Integer.parseInt(tempChar));
            idBool = false;
        } else if(qName.equalsIgnoreCase("name")) {
            equipment.setName(tempChar);
            nameBool = false;
        }
    }

    public ArrayList<EquipmentObj> returnData() {
        return this.equipmentList;
    }
}
