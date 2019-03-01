package bailiwick.bjpukh.com.vistarak.Getter_Setter;

/**
 * Created by Prince on 22-02-2018.
 */

public class SmartPhoneModel {
    private String Vistarak_id, boothID, person_name, lattitudeLocation, longitudeLocation, contact,entry_no;

    public SmartPhoneModel(String vistarak_id, String boothID, String person_name, String lattitudeLocation, String longitudeLocation, String contact, String entry_no) {
        Vistarak_id = vistarak_id;
        this.boothID = boothID;
        this.person_name = person_name;
        this.lattitudeLocation = lattitudeLocation;
        this.longitudeLocation = longitudeLocation;
        this.contact = contact;
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

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEntry_no() {
        return entry_no;
    }

    public void setEntry_no(String entry_no) {
        this.entry_no = entry_no;
    }
}
