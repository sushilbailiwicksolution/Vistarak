package bailiwick.bjpukh.com.vistarak.Getter_Setter;

/**
 * Created by Prince on 17-09-2018.
 */

public class Event_Pramukh_model {
    private String Str_vistarakID, Str_boothID, Str_PramukhName, Str_PramukhMobile, Str_lat, Str_lon, Str_entry_no;

    public Event_Pramukh_model(String str_vistarakID, String str_boothID, String str_PramukhName, String str_PramukhMobile, String str_lat, String str_lon, String str_entry_no) {
        Str_vistarakID = str_vistarakID;
        Str_boothID = str_boothID;
        Str_PramukhName = str_PramukhName;
        Str_PramukhMobile = str_PramukhMobile;
        Str_lat = str_lat;
        Str_lon = str_lon;
        Str_entry_no = str_entry_no;
    }

    public String getStr_vistarakID() {
        return Str_vistarakID;
    }

    public void setStr_vistarakID(String str_vistarakID) {
        Str_vistarakID = str_vistarakID;
    }

    public String getStr_boothID() {
        return Str_boothID;
    }

    public void setStr_boothID(String str_boothID) {
        Str_boothID = str_boothID;
    }

    public String getStr_PramukhName() {
        return Str_PramukhName;
    }

    public void setStr_PramukhName(String str_PramukhName) {
        Str_PramukhName = str_PramukhName;
    }

    public String getStr_PramukhMobile() {
        return Str_PramukhMobile;
    }

    public void setStr_PramukhMobile(String str_PramukhMobile) {
        Str_PramukhMobile = str_PramukhMobile;
    }

    public String getStr_lat() {
        return Str_lat;
    }

    public void setStr_lat(String str_lat) {
        Str_lat = str_lat;
    }

    public String getStr_lon() {
        return Str_lon;
    }

    public void setStr_lon(String str_lon) {
        Str_lon = str_lon;
    }

    public String getStr_entry_no() {
        return Str_entry_no;
    }

    public void setStr_entry_no(String str_entry_no) {
        Str_entry_no = str_entry_no;
    }
}
