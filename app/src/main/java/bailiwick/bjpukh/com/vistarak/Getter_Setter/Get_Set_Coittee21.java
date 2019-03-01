package bailiwick.bjpukh.com.vistarak.Getter_Setter;

/**
 * Created by Prince on 17-12-2018.
 */

public class Get_Set_Coittee21 {
    private String Str_id, str_member_name, str_designation, str_mobile;

    public Get_Set_Coittee21(String str_id, String str_member_name, String str_designation, String str_mobile) {
        Str_id = str_id;
        this.str_member_name = str_member_name;
        this.str_designation = str_designation;
        this.str_mobile = str_mobile;
    }

    public String getStr_id() {
        return Str_id;
    }

    public void setStr_id(String str_id) {
        Str_id = str_id;
    }

    public String getStr_member_name() {
        return str_member_name;
    }

    public void setStr_member_name(String str_member_name) {
        this.str_member_name = str_member_name;
    }

    public String getStr_designation() {
        return str_designation;
    }

    public void setStr_designation(String str_designation) {
        this.str_designation = str_designation;
    }

    public String getStr_mobile() {
        return str_mobile;
    }

    public void setStr_mobile(String str_mobile) {
        this.str_mobile = str_mobile;
    }
}
