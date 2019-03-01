package bailiwick.bjpukh.com.vistarak.db;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import bailiwick.bjpukh.com.vistarak.Event_Pradhan;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.AnusuchitModel;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Army_Model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Bike_Model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.BoothMeeting_modal;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Event_Pramukh_model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Home_visitModal;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Influence_Model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Man_Ki_BaatModal;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Ngo_Model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.PannaPramukhModel;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Religious_Model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Shaheed_Model;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.SmartPhoneModel;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Special_AreaModal;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.SwachtaModel;
import bailiwick.bjpukh.com.vistarak.Getter_Setter.Whats_AppModal;
import bailiwick.bjpukh.com.vistarak.Modals.KametiModel;

/**
 * Created by Prince on 11-09-2017.
 */

public class DB_Function {

    public static boolean INSERT_BOOTH(Context con, String bid, String booth_name, String consistency) {
        boolean isSucess = false;

        isSucess = DBOperation.insertBooth(con, bid, booth_name, consistency);
        return isSucess;
    }

    public static boolean Get_BOOTH(Context con) {
        boolean isSucess = false;
        DBOperation.getAllBooth(con);
        return isSucess;
    }

    public static ArrayList<Man_Ki_BaatModal> Get_manKibaat(Context con) {
        ArrayList<Man_Ki_BaatModal> List = new ArrayList<>();
        List = DBOperation.getMan_ki_baat(con);

        return List;
    }

    public static ArrayList<Home_visitModal> Get_Home_visit(Context con) {
        ArrayList<Home_visitModal> List = new ArrayList<>();
        List = DBOperation.getHome_Visit(con);

        return List;
    }

    public static ArrayList<Special_AreaModal> Get_Special_Area(Context con) {
        ArrayList<Special_AreaModal> List = new ArrayList<>();
        List = DBOperation.getSpecial_Area(con);

        return List;
    }

    public static ArrayList<Whats_AppModal> Get_Whats_App(Context con) {
        ArrayList<Whats_AppModal> List = new ArrayList<>();
        List = DBOperation.getWhatsApp(con);

        return List;
    }

    public static ArrayList<SwachtaModel> Get_Swachta(Context con) {
        ArrayList<SwachtaModel> List = new ArrayList<>();
        List = DBOperation.getSwachta(con);

        return List;
    }

    public static ArrayList<Ngo_Model> Get_Ngo(Context con) {
        ArrayList<Ngo_Model> List = new ArrayList<>();
        List = DBOperation.getNGO(con);

        return List;
    }

    public static ArrayList<Religious_Model> Get_Religious(Context con) {
        ArrayList<Religious_Model> List = new ArrayList<>();
        List = DBOperation.getRelious(con);

        return List;
    }

    public static ArrayList<Army_Model> Get_Army(Context con) {
        ArrayList<Army_Model> List = new ArrayList<>();
        List = DBOperation.getArmy(con);

        return List;
    }

    public static ArrayList<Shaheed_Model> Get_Shaheed(Context con) {
        ArrayList<Shaheed_Model> List = new ArrayList<>();
        List = DBOperation.getShaheed(con);

        return List;
    }

    public static ArrayList<Influence_Model> Get_Influence(Context con) {
        ArrayList<Influence_Model> List = new ArrayList<>();
        List = DBOperation.getOther(con);

        return List;
    }

    public static ArrayList<Bike_Model> Get_Bike(Context con) {
        ArrayList<Bike_Model> List = new ArrayList<>();
        List = DBOperation.getBike(con);

        return List;
    }

    public static ArrayList<PannaPramukhModel> GetPannaPramkh(Context con) {
        ArrayList<PannaPramukhModel> List = new ArrayList<>();
        List = DBOperation.getPannaPramukh(con);

        return List;
    }

    public static ArrayList<BoothMeeting_modal> Get_Boothmeeting(Context con) {
        ArrayList<BoothMeeting_modal> List = new ArrayList<>();
        List = DBOperation.getmeeting(con);

        return List;
    }
    public static ArrayList<SmartPhoneModel> Get_SmartPhoneUser(Context con) {
        ArrayList<SmartPhoneModel> List = new ArrayList<>();
        List = DBOperation.getSmartPhone(con);

        return List;
    }

    public static ArrayList<KametiModel> Get_KametiMember(Context con) {
        ArrayList<KametiModel> List = new ArrayList<>();
        List = DBOperation.getKametiMember(con);

        return List;
    }

    public static ArrayList<AnusuchitModel> Get_Anusuchitmember(Context con) {
        ArrayList<AnusuchitModel> List = new ArrayList<>();
        List = DBOperation.getAnusuchit(con);

        return List;
    }
    public static ArrayList<Event_Pramukh_model> Get_Event_member(Context con) {
        ArrayList<Event_Pramukh_model> List = new ArrayList<>();
        List = DBOperation.getEventPramukh(con);

        return List;
    }

    public static void ExportDatabasee(Context con, String DATABASE_NAME) {
        String databasePath = con.getDatabasePath(DATABASE_NAME).getPath();
        String DB_PATH = "/data/data/bailiwick.bjpukh.com.vistarak/";

        String DB_NAME = Database_Utils.DB_NAME;
        databasePath = DB_PATH + DB_NAME;
        File f = new File(databasePath);
        OutputStream myOutput = null;
        InputStream myInput = null;
        Log.d("testing", " testing db path " + databasePath);
        Log.d("testing", " testing db exist " + f.exists());

        if (f.exists()) {
            try {

                File directory = new File("/mnt/sdcard/Vistraka_prince");
                if (!directory.exists())
                    directory.mkdir();

                myOutput = new FileOutputStream(directory.getAbsolutePath()
                        + "/" + DATABASE_NAME);
                myInput = new FileInputStream(databasePath);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                Toast.makeText(con, "Export Succesfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            } finally {
                try {
                    if (myOutput != null) {
                        myOutput.close();
                        myOutput = null;
                    }
                    if (myInput != null) {
                        myInput.close();
                        myInput = null;
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public static void SavetextFile(Context con, String string) {

        try {
            File myFile = new File("/mnt/sdcard/Vistraka_prince/VistarkFcm.txt");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(string);
            myOutWriter.close();
            fOut.close();
            Toast.makeText(con,
                    "'VistarkFcm.txt'",
                    Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Toast.makeText(con, e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

    }
}
