package appfactory.uwp.edu.parksideapp2.Models;

/**
 * Created by kyluong09 on 5/28/18.
 */

public class PlaceObj {
    private String name;
    private String address;
    private double longitude;
    private double latude;
    private  int imageId;

    public PlaceObj() {
    }

    public PlaceObj(String name,int imageId,String address, double longitude, double latude) {
        this.name = name;
        this.imageId = imageId;
        this.address = address;
        this.longitude = longitude;
        this.latude = latude;
    }


    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatude() {
        return latude;
    }

    public void setLatude(double latude) {
        this.latude = latude;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
