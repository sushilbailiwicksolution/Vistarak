package bailiwick.bjpukh.com.vistarak.Getter_Setter;

/**
 * Created by vikasaggarwal on 05/09/17.
 */

public class BoothSpinnerModal {
    String bid, booth_name, consistency_name;

    public BoothSpinnerModal(String bid, String booth_name, String consistency_name) {

        this.bid = bid;
        this.booth_name = booth_name;
        this.consistency_name = consistency_name;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBooth_name() {
        return booth_name;
    }

    public void setBooth_name(String booth_name) {
        this.booth_name = booth_name;
    }

    public String getConsistency_name() {
        return consistency_name;
    }

    public void setConsistency_name(String consistency_name) {
        this.consistency_name = consistency_name;
    }
}
