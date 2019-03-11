package bailiwick.bjpukh.com.vistarak.db;

/**
 * Created by Prince on 11-09-2017.
 */

public class Database_Utils {
    // Databse name
    //  public static final String DB_NAME = "vistarak_newdecember_gen.sqlite";
    //  public static final String DB_NAME = "vistarak_feb_2018.sqlite";
    public static final String DB_NAME = "vistarak_march_2019.sqlite";


    // table name
    public static final String Booth_table = "tbl_booth_detail";
    public static final String Man_Ki_Baat_table = "tbl_man_ki_baat";
    public static final String Swachta_in_Booth_table = "tbl_swachta_in_booth";
    public static final String Whats_app_group_table = "tbl_whats_app_group";
    public static final String Special_area_table = "tbl_special_area";
    public static final String New_Member_table = "tbl_add_new_member";
    public static final String Home_Visit_table = "tbl_Home_visit";
    public static final String booth_meeting_table = "tbl_booth_meeting";
    public static final String Add_Bike_table = "tbl_add_bike";
    public static final String Panna_Pramukh_table = "tbl_panna_pradhan";
    public static final String Add_Smart_phone_user_table = "tbl_smart_phone_user";
    public static final String Add_kameti_member_table = "tbl_kameti";
    public static final String Add_Anusuchit_member_table = "tbl_anusuchit_member";
    public static final String Add_Event_pramukh_table = "tbl_event_pramukh_member";

    public static final String Table_Level = "tbl_level";
    public static final String Table_district = "tbl_district";


    // key voter Table
    public static final String key_voter_NGO_table = "key_voter_ngo_tbl";
    public static final String key_voter_Religious_table = "key_voter_religious_tbl";
    public static final String key_voter_EX_Army_table = "key_voter_exarmy_tbl";
    public static final String key_voter_Shaheed__table = "key_voter_shaheed_tbl";
    public static final String key_voter_other_table = "key_voter_other_tbl";

    // table column name
    public static final String Booth_BID = "bid";
    public static final String Booth_name = "booth_name";
    public static final String Booth_consistency = "consistency_name";
    // Man Ki Baat
    public static final String Vistarak_id = "vistarak_id";
    public static final String Booth_id = "booth_id";
    public static final String PramukhName = "pramukhName";
    public static final String Latitude = "latitude";
    public static final String Logitude = "longitude";
    public static final String PramukhMobile = "pramukhMobile";
    public static final String Entry_no = "entry_no";
    public static final String vechicle_no = "vechicle_no";


    public static final String Group_name = "groupName";
    public static final String Prior_Category = "priorCategory";
    public static final String family_count = "no_of_famliy";

    // New member
    public static final String MEM_full_name = "full_name";
    public static final String MEM_marital_status = "marital_status";
    public static final String MEM_dob = "dob";
    public static final String MEM_mobile = "mobile";
    public static final String MEM_occupation = "occupation";
    public static final String MEM_address = "address";
    public static final String MEM_religion = "religion";
    public static final String MEM_caste = "caste";
    public static final String MEM_member_type = "member_type";
    public static final String MEM_voterid = "voterid";
    // Home_visit
    public static final String Home_head_name = "head_name";
    public static final String Home_family_member = "family_member";
    public static final String Home_address = "address";
    public static final String Home_total_voter = "total_voter";
    public static final String Home_head_voter_id = "head_voter_id";
    public static final String Home_visitType = "visit_type";
    public static final String Home_membership_id = "membership_id";
    public static final String Home_mobile = "home_mobile";

    // Panna Pramukh


    //Key Voter
    public static final String VoterType = "voterType";

    // NGO
    public static final String NG0_owner_name = "owner_name";
    public static final String NG0_name = "ngo_name";
    public static final String NG0_area_of_work = "area_of_work";
    public static final String NG0_affectivity = "affectivity";
    public static final String NG0_owner_mobile = "owner_mobile";
    //Religious
    public static final String Religious_owner_name = "owner_name";
    public static final String Religious_name = "religious_name";
    public static final String Religious_area_of_work = "area_of_work";
    public static final String Religious_affectivity = "affectivity";
    public static final String Religious_owner_mobile = "owner_mobile";
    //Ex Army
    public static final String Army_person = "Army_person";
    public static final String Army_total_family_member = "Army_total_family_member";
    public static final String Army_retired_from = "Army_retired_from";
    public static final String Army_mobile = "Army_mobile";
    // Shaheed Pariwar
    public static final String Shaheed_name = "Shaheed_name";
    public static final String Shaheed_family_member = "Shaheed_family_member";
    public static final String Shaheed_retired_from = "Shaheed_retired_from";
    public static final String Shaheed_mobile = "Shaheed_mobile";
    public static final String Shaheed_work_for = "Shaheed_work_for";
    // other key voter
    public static final String person_name = "person_name";
    public static final String Other_occupation = "other_occupation";
    public static final String Other_Affectivity = "other_affectivity";
    public static final String Other_Mobile = "other_mobile";
    // Booth Meeting
    public static final String Home_male_member = "Home_male_member";
    public static final String Home_female_member = "Home_female_member";
    public static final String Home_total_member = "Home_total_member";
    public static final String Home_meeting_id = "Home_meeting_id";
    public static final String Home_image_name = "Image_name";
    public static final String Home_image_path = "image_path";

    // kameti member
    public static final String kameti_Adyaksh_name = "kameti_Adyaksh_name";
    public static final String kameti_Adyaksh_mobile = "kameti_Adyaksh_mobile";
    public static final String kameti_palk_name = "kameti_palk_name";
    public static final String kameti_palak_mobile = "kameti_palak_mobile";
    public static final String kameti_BLA_name = "kameti_BLA_name";
    public static final String kameti_BLA_mobile = "kameti_BLA_mobile";
    public static final String kameti_Value_type = "value_type";


    public static final String Area_type = "area_type";
// Anusuchit Member
public static final String Catagory = "catagory";

// 05 march 2019 for Shakti Kendra
public static final String district_id = "id";
    public static final String district_name = "district_name";

    public static final String level_id = "id";
    public static final String level_name = "level_name";

    // End ///
}
