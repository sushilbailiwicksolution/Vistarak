package bailiwick.bjpukh.com.vistarak.Getter_Setter;

/**
 * Created by Prince on 05-03-2019.
 */

public class Level_model {
    private String level_id,level_name;

    public Level_model(String level_id, String level_name) {
        this.level_id = level_id;
        this.level_name = level_name;
    }

    public String getLevel_id() {
        return level_id;
    }

    public void setLevel_id(String level_id) {
        this.level_id = level_id;
    }

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }
}
