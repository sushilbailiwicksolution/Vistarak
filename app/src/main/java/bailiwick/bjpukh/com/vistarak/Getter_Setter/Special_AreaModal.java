package bailiwick.bjpukh.com.vistarak.Getter_Setter;

/**
 * Created by Prince on 27-09-2017.
 */

public class Special_AreaModal {
    private String Str_vistarak_id, Str_booth_id, Str_prior_catagory, Str_lat, Str_lan, Str_Family_count, str_area_type, Str_entry_no;

    public Special_AreaModal(String str_vistarak_id, String str_booth_id, String str_prior_catagory, String str_lat, String str_lan, String str_Family_count, String str_area_type, String str_entry_no) {
        Str_vistarak_id = str_vistarak_id;
        Str_booth_id = str_booth_id;
        Str_prior_catagory = str_prior_catagory;
        Str_lat = str_lat;
        Str_lan = str_lan;
        Str_Family_count = str_Family_count;
        this.str_area_type = str_area_type;
        Str_entry_no = str_entry_no;
    }

    public String getStr_vistarak_id() {
        return Str_vistarak_id;
    }

    public void setStr_vistarak_id(String str_vistarak_id) {
        Str_vistarak_id = str_vistarak_id;
    }

    public String getStr_booth_id() {
        return Str_booth_id;
    }

    public void setStr_booth_id(String str_booth_id) {
        Str_booth_id = str_booth_id;
    }

    public String getStr_prior_catagory() {
        return Str_prior_catagory;
    }

    public void setStr_prior_catagory(String str_prior_catagory) {
        Str_prior_catagory = str_prior_catagory;
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

    public String getStr_Family_count() {
        return Str_Family_count;
    }

    public void setStr_Family_count(String str_Family_count) {
        Str_Family_count = str_Family_count;
    }

    public String getStr_area_type() {
        return str_area_type;
    }

    public void setStr_area_type(String str_area_type) {
        this.str_area_type = str_area_type;
    }

    public String getStr_entry_no() {
        return Str_entry_no;
    }

    public void setStr_entry_no(String str_entry_no) {
        Str_entry_no = str_entry_no;
    }
}