package bailiwick.bjpukh.com.vistarak.Getter_Setter;

/**
 * Created by Prince on 05-12-2017.
 */

public class PannaPramukhModel {
    private String Str_vistarakID, Str_booth_id, Str_pramukh_name, address, Str_lat, Str_lan, Str_mobile, Str_entry_no;

    public PannaPramukhModel(String str_vistarakID, String str_booth_id, String str_pramukh_name, String address, String str_lat, String str_lan, String str_mobile, String str_entry_no) {
        Str_vistarakID = str_vistarakID;
        Str_booth_id = str_booth_id;
        Str_pramukh_name = str_pramukh_name;
        this.address = address;
        Str_lat = str_lat;
        Str_lan = str_lan;
        Str_mobile = str_mobile;
        Str_entry_no = str_entry_no;
    }

    public String getStr_vistarakID() {
        return Str_vistarakID;
    }

    public void setStr_vistarakID(String str_vistarakID) {
        Str_vistarakID = str_vistarakID;
    }

    public String getStr_booth_id() {
        return Str_booth_id;
    }

    public void setStr_booth_id(String str_booth_id) {
        Str_booth_id = str_booth_id;
    }

    public String getStr_pramukh_name() {
        return Str_pramukh_name;
    }

    public void setStr_pramukh_name(String str_pramukh_name) {
        Str_pramukh_name = str_pramukh_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStr_lat() {
        return Str_lat;
    }

    public void setStr_lat(String str_lat) {
        Str_lat = str_lat;
    }

    public String getStr_lan() {
        return Str_lan;
    }

    public void setStr_lan(String str_lan) {
        Str_lan = str_lan;
    }

    public String getStr_mobile() {
        return Str_mobile;
    }

    public void setStr_mobile(String str_mobile) {
        Str_mobile = str_mobile;
    }

    public String getStr_entry_no() {
        return Str_entry_no;
    }

    public void setStr_entry_no(String str_entry_no) {
        Str_entry_no = str_entry_no;
    }
}
