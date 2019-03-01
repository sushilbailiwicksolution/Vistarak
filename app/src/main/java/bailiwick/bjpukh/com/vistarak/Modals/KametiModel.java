package bailiwick.bjpukh.com.vistarak.Modals;

/**
 * Created by Prince on 23-02-2018.
 */

public class KametiModel {
    String Vistarak_id, boothID,  lattitudeLocation,  longitudeLocation,  str_adykash_name,  str_adykash_mobile, str_palak_name, str_palak_mobile, str_bla_name, str_bla_mobile,  valuetype,entry_no;

    public KametiModel(String vistarak_id, String boothID, String lattitudeLocation, String longitudeLocation, String str_adykash_name, String str_adykash_mobile, String str_palak_name, String str_palak_mobile, String str_bla_name, String str_bla_mobile, String valuetype, String entry_no) {
        Vistarak_id = vistarak_id;
        this.boothID = boothID;
        this.lattitudeLocation = lattitudeLocation;
        this.longitudeLocation = longitudeLocation;
        this.str_adykash_name = str_adykash_name;
        this.str_adykash_mobile = str_adykash_mobile;
        this.str_palak_name = str_palak_name;
        this.str_palak_mobile = str_palak_mobile;
        this.str_bla_name = str_bla_name;
        this.str_bla_mobile = str_bla_mobile;
        this.valuetype = valuetype;
        this.entry_no = entry_no;
    }

    public String getVistarak_id() {
        return Vistarak_id;
    }

    public void setVistarak_id(String vistarak_id) {
        Vistarak_id = vistarak_id;
    }

    public String getBoothID() {
        return boothID;
    }

    public void setBoothID(String boothID) {
        this.boothID = boothID;
    }

    public String getLattitudeLocation() {
        return lattitudeLocation;
    }

    public void setLattitudeLocation(String lattitudeLocation) {
        this.lattitudeLocation = lattitudeLocation;
    }

    public String getLongitudeLocation() {
        return longitudeLocation;
    }

    public void setLongitudeLocation(String longitudeLocation) {
        this.longitudeLocation = longitudeLocation;
    }

    public String getStr_adykash_name() {
        return str_adykash_name;
    }

    public void setStr_adykash_name(String str_adykash_name) {
        this.str_adykash_name = str_adykash_name;
    }

    public String getStr_adykash_mobile() {
        return str_adykash_mobile;
    }

    public void setStr_adykash_mobile(String str_adykash_mobile) {
        this.str_adykash_mobile = str_adykash_mobile;
    }

    public String getStr_palak_name() {
        return str_palak_name;
    }

    public void setStr_palak_name(String str_palak_name) {
        this.str_palak_name = str_palak_name;
    }

    public String getStr_palak_mobile() {
        return str_palak_mobile;
    }

    public void setStr_palak_mobile(String str_palak_mobile) {
        this.str_palak_mobile = str_palak_mobile;
    }

    public String getStr_bla_name() {
        return str_bla_name;
    }

    public void setStr_bla_name(String str_bla_name) {
        this.str_bla_name = str_bla_name;
    }

    public String getStr_bla_mobile() {
        return str_bla_mobile;
    }

    public void setStr_bla_mobile(String str_bla_mobile) {
        this.str_bla_mobile = str_bla_mobile;
    }

    public String getValuetype() {
        return valuetype;
    }

    public void setValuetype(String valuetype) {
        this.valuetype = valuetype;
    }

    public String getEntry_no() {
        return entry_no;
    }

    public void setEntry_no(String entry_no) {
        this.entry_no = entry_no;
    }
}
