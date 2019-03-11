package bailiwick.bjpukh.com.vistarak.Getter_Setter;

/**
 * Created by Prince on 05-03-2019.
 */

public class District_model {
private     String district_id,district_name;

    public District_model(String district_id, String district_name) {
        this.district_id = district_id;
        this.district_name = district_name;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }
}
