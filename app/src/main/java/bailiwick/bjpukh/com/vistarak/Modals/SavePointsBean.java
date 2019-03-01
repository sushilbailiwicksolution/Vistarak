package bailiwick.bjpukh.com.vistarak.Modals;

/**
 * Created by User on 29-Nov-16.
 */

public class SavePointsBean {
    String id, mobile, lat, longitude, lessTime, address;

    public SavePointsBean() {
    }

    public SavePointsBean(String id, String mobile, String lat, String longitude, String lessTime, String address) {

        this.id = id;
        this.mobile = mobile;
        this.lat = lat;
        this.longitude = longitude;
        this.lessTime = lessTime;
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLessTime() {
        return lessTime;
    }

    public void setLessTime(String lessTime) {
        this.lessTime = lessTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
