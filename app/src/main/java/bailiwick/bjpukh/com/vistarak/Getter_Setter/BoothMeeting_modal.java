package bailiwick.bjpukh.com.vistarak.Getter_Setter;

/**
 * Created by Prince on 29-09-2017.
 */

public class BoothMeeting_modal {
    private String Str_VistarakID, Str_BoothID, Str_lat, Str_lan, Str_mal_mem, Str_female_mem, Str_total_MEM, Str_entry_no, Str_image_name;
    private byte Str_path[];

    public BoothMeeting_modal(String str_VistarakID, String str_BoothID, String str_lat, String str_lan, String str_mal_mem, String str_female_mem, String str_total_MEM, String str_entry_no, String str_image_name, byte[] str_path) {
        Str_VistarakID = str_VistarakID;
        Str_BoothID = str_BoothID;
        Str_lat = str_lat;
        Str_lan = str_lan;
        Str_mal_mem = str_mal_mem;
        Str_female_mem = str_female_mem;
        Str_total_MEM = str_total_MEM;
        Str_entry_no = str_entry_no;
        Str_image_name = str_image_name;
        Str_path = str_path;
    }

    public String getStr_VistarakID() {
        return Str_VistarakID;
    }

    public void setStr_VistarakID(String str_VistarakID) {
        Str_VistarakID = str_VistarakID;
    }

    public String getStr_BoothID() {
        return Str_BoothID;
    }

    public void setStr_BoothID(String str_BoothID) {
        Str_BoothID = str_BoothID;
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

    public String getStr_mal_mem() {
        return Str_mal_mem;
    }

    public void setStr_mal_mem(String str_mal_mem) {
        Str_mal_mem = str_mal_mem;
    }

    public String getStr_female_mem() {
        return Str_female_mem;
    }

    public void setStr_female_mem(String str_female_mem) {
        Str_female_mem = str_female_mem;
    }

    public String getStr_total_MEM() {
        return Str_total_MEM;
    }

    public void setStr_total_MEM(String str_total_MEM) {
        Str_total_MEM = str_total_MEM;
    }

    public String getStr_entry_no() {
        return Str_entry_no;
    }

    public void setStr_entry_no(String str_entry_no) {
        Str_entry_no = str_entry_no;
    }

    public String getStr_image_name() {
        return Str_image_name;
    }

    public void setStr_image_name(String str_image_name) {
        Str_image_name = str_image_name;
    }

    public byte[] getStr_path() {
        return Str_path;
    }

    public void setStr_path(byte[] str_path) {
        Str_path = str_path;
    }
}
