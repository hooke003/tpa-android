package appfactory.uwp.edu.parksideapp2.Event;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import appfactory.uwp.edu.parksideapp2.Models.EventObj;

/**
 * Created by kyluong09 on 3/19/18.
 */

public class SAXHandler extends DefaultHandler {
    private Date date;
    private EventObj eventObj;
    private String tempChar;
    private StringBuilder stringBuilder;
    private ArrayList<EventObj> eventObjsList = new ArrayList<>();
    // Boolean
    private boolean bId;
    private boolean bTitle;
    private boolean bDescription;
    private boolean bEventDate;
    private boolean bStartDate;
    private boolean bEndDate;
    private boolean bStartTime;
    private boolean bEndTime;
    private boolean bLocation;

    // All form of contact
    private boolean bContactName;
    private boolean bContactEmail;
    private boolean bContactNumber;

    public SAXHandler() {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName.toLowerCase()) {
            case "event":
                eventObj = new EventObj();
                break;
            case "eventid":
                tempChar = "";
                bId = true;
                break;
            case "title":
                stringBuilder = new StringBuilder();
                bTitle = true;
                break;
            case "description":
                stringBuilder = new StringBuilder();
                bDescription = true;
                break;
            case "eventdate":
                tempChar = "";
                bEventDate = true;
                break;
            case "startdate":
                tempChar = "";
                bStartDate = true;
                break;
            case "enddate":
                tempChar = "";
                bEndDate = true;
                break;
            case "starttime":
                tempChar = "";
                bStartTime = true;
                break;
            case "enddtime":
                tempChar = "";
                bEndTime = true;
                break;
            case "location":
                tempChar = "";
                bLocation = true;
                break;
            case "contactname":
                tempChar = "";
                bContactName = true;
                break;
            case "contactemail":
                tempChar = " ";
                bContactEmail = true;
                break;
            case "contactphone":
                tempChar = " ";
                bContactNumber = true;
                break;
            default:
                break;
        }
//        
//        if (qName.equalsIgnoreCase("event")) {
//            eventObj = new EventObj();
//        } else if (qName.equalsIgnoreCase("eventid")) {
//            tempChar = "";
//            bId = true;
//        } else if (qName.equalsIgnoreCase("title")) {
//            stringBuilder = new StringBuilder();
//            bTitle = true;
//        } else if (qName.equalsIgnoreCase("description")) {
//            stringBuilder = new StringBuilder();
//            bDescription = true;
//        } else if (qName.equalsIgnoreCase("eventdate")) {
//            tempChar = "";
//            bEventDate = true;
//        } else if (qName.equalsIgnoreCase("startdate")) {
//            tempChar = "";
//            bStartDate = true;
//        } else if (qName.equalsIgnoreCase("enddate")) {
//            tempChar = "";
//            bEndDate = true;
//        } else if (qName.equalsIgnoreCase("starttime")) {
//            tempChar = "";
//            bStartTime = true;
//        } else if (qName.equalsIgnoreCase("enddtime")) {
//            tempChar = "";
//            bEndTime = true;
//        } else if (qName.equalsIgnoreCase("location")) {
//            tempChar = "";
//            bLocation = true;
//        } else if (qName.equalsIgnoreCase("contactname")) {
//            tempChar = "";
//            bContactName = true;
//        } else if (qName.equalsIgnoreCase("contactemail")) {
//            tempChar = " ";
//            bContactEmail = true;
//        } else if (qName.equalsIgnoreCase("contactphone")) {
//            tempChar = " ";
//            bContactNumber = true;
//        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName.toLowerCase()) {
            case "event":
                eventObjsList.add(eventObj);
                break;
            case "eventid":
                eventObj.set_id(tempChar);
                bId = false;
                break;
            case "title":
                eventObj.setTitle(stringBuilder.toString());
                bTitle = false;
                break;
            case "description":
                eventObj.setDescription(stringBuilder.toString());
                bDescription = false;
                break;
            case "eventdate":
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(tempChar);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                eventObj.setEventDate(date);
                eventObj.setEventDateString(tempChar);
                bEventDate = false;
                break;
            case "startdate":
                eventObj.setStartDate(tempChar);
                bStartDate = false;
                break;
            case "enddate":
                eventObj.setEndDate(tempChar);
                bEndDate = false;
                break;
            case "starttime":
                eventObj.setStartTime(tempChar);
                bStartTime = false;
                break;
            case "enddtime":
                eventObj.setEndTime(tempChar);
                bEndTime = false;
                break;
            case "location":
                eventObj.setLocation(tempChar);
                bLocation = false;
                break;
            case "contactname":
                eventObj.setContactName(tempChar);
                bContactName = false;
                break;
            case "contactemail":
                eventObj.setContactEmail(tempChar);
                bContactEmail = false;
                break;
            case "contactphone":
                eventObj.setContactPhone(tempChar);
                bContactNumber = false;
                break;
        }
//        if (qName.equalsIgnoreCase("event")) {
//            eventObjsList.add(eventObj);
//        } else if (qName.equalsIgnoreCase("eventid")){
//            eventObj.set_id(tempChar);
//            bId = false;
//        }
//        else if (qName.equalsIgnoreCase("title")) {
//            eventObj.setTitle(stringBuilder.toString());
//            bTitle = false;
//        } else if (qName.equalsIgnoreCase("description")) {
//            eventObj.setDescription(stringBuilder.toString());
//            bDescription = false;
//        }else if(qName.equalsIgnoreCase("eventdate")){
//            try {
//                date = new SimpleDateFormat("yyyy-MM-dd").parse(tempChar);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            eventObj.setEventDate(date);
//            eventObj.setEventDateString(tempChar);
//            bEventDate = false;
//        }
//        else if(qName.equalsIgnoreCase("startdate")){
//            eventObj.setStartDate(tempChar);
//            bStartDate = false;
//        } else if(qName.equalsIgnoreCase("enddate")){
//            eventObj.setEndDate(tempChar);
//            bEndDate = false;
//        } else if(qName.equalsIgnoreCase("starttime")){
//            eventObj.setStartTime(tempChar);
//            bStartTime = false;
//        } else if(qName.equalsIgnoreCase("enddtime")){
//            eventObj.setEndTime(tempChar);
//            bEndTime = false;
//        } else if(qName.equalsIgnoreCase("location")){
//            eventObj.setLocation(tempChar);
//            bLocation = false;
//        } else if (qName.equalsIgnoreCase("contactname")) {
//            eventObj.setContactName(tempChar);
//            bContactName = false;
//        } else if (qName.equalsIgnoreCase("contactemail")) {
//            eventObj.setContactEmail(tempChar);
//            bContactEmail = false;
//        } else if (qName.equalsIgnoreCase("contactphone")) {
//            eventObj.setContactPhone(tempChar);
//            bContactNumber = false;
//        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        if(bId){
            tempChar = new String(ch,start,length);
        }else if (bTitle) {
            stringBuilder.append(new String(ch,start,length));
        } else if (bDescription) {
            stringBuilder.append(new String(ch,start,length));
        } else if(bEventDate){
            tempChar = new String(ch,start,length);
        } else if(bStartDate){
            tempChar = new String(ch,start,length);
        } else if(bEndDate){
            tempChar = new String(ch,start,length);
        } else if(bStartTime){
            tempChar = new String(ch,start,length);
        } else if(bEndTime){
            tempChar = new String(ch,start,length);
        } else if(bLocation){
            tempChar = new String(ch,start,length);
        } else if (bContactName) {
            tempChar = new String(ch,start,length);
        } else if (bContactEmail) {
            tempChar = new String(ch,start,length);
        } else if (bContactNumber) {
            tempChar = new String(ch,start,length);
        }
    }

    public ArrayList<EventObj> returnData() {
        return this.eventObjsList;
    }
}
