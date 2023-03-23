package appfactory.uwp.edu.parksideapp2.Models;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by kyluong09 on 3/19/18.
 */


public class EventObj extends RealmObject {
    @PrimaryKey
    private String uuid;
    private String _id;
    private String startDate;
    private String endDate;
    @Required
    private Date eventDate;
    @Required
    private String eventDateString;
    @Required
    private String title;
    private String description;
    //private String startTimeString;
    private String startTime;
   // private String endTimeString;
    private String endTime;
    private String webSite;
    private String contactName;
    private String contactEmail;
    private String contactPhone;

    private String location;
    private String address;
    private String city;
    private String state;
    private String zip;

    public EventObj(){
        uuid = UUID.randomUUID().toString();

    }

    public EventObj(String _id, String startDate, String endDate, Date eventDate, String eventDateString, String title, String description, String webSite, String contactName, String contactEmail, String contactPhone, String location, String address, String city, String state, String zip) {

        uuid = UUID.randomUUID().toString();
        this._id = _id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventDate = eventDate;
        this.eventDateString = eventDateString;
        this.startTime = startTime;
        //this.startTimeString = startTimeString;
        this.endTime = endTime;
        //this.endTimeString = endTimeString;
        this.title = title;
        this.description = description;
        this.webSite = webSite;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.location = location;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventDateString() {
        return eventDateString;
    }

    public void setEventDateString(String eventDateString) { this.eventDateString = eventDateString; }

    public String getStartTime() { return startTime; }

    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }

    public void setEndTime(String endTime) { this.endTime = endTime; }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
