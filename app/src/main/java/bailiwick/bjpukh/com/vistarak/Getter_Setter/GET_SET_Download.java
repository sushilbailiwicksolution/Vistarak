package bailiwick.bjpukh.com.vistarak.Getter_Setter;

/**
 * Created by Prince on 06-09-2017.
 */

public class GET_SET_Download {
    private String Str_id, Str_app_name, Str_app_icon, Str_app_url, Str_create_date;

    public GET_SET_Download(String str_id, String str_app_name, String str_app_icon, String str_app_url, String str_create_date) {
        Str_id = str_id;
        Str_app_name = str_app_name;
        Str_app_icon = str_app_icon;
        Str_app_url = str_app_url;
        Str_create_date = str_create_date;
    }

    public String getStr_id() {
        return Str_id;
    }

    public void setStr_id(String str_id) {
        Str_id = str_id;
    }


    public String getStr_app_name() {
        return Str_app_name;
    }

    public void setStr_app_name(String str_app_name) {
        Str_app_name = str_app_name;
    }

    public String getStr_app_icon() {
        return Str_app_icon;
    }

    public void setStr_app_icon(String str_app_icon) {
        Str_app_icon = str_app_icon;
    }

    public String getStr_app_url() {
        return Str_app_url;
    }

    public void setStr_app_url(String str_app_url) {
        Str_app_url = str_app_url;
    }

    public String getStr_create_date() {
        return Str_create_date;
    }

    public void setStr_create_date(String str_create_date) {
        Str_create_date = str_create_date;
    }
}
