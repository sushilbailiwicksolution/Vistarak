package bailiwick.bjpukh.com.vistarak.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import bailiwick.bjpukh.com.vistarak.Getter_Setter.Get_Set_Coittee21;
import bailiwick.bjpukh.com.vistarak.R;

/**
 * Created by Prince on 17-12-2018.
 */

public class AdapterComitee extends ArrayAdapter<Get_Set_Coittee21> {

    Activity mContext;
    int customSearchList;
    ArrayList<Get_Set_Coittee21> search_list_data;
    AdapterDownload.customProfile_view customListner;

    public AdapterComitee(Activity searchList, int customSearchList, ArrayList<Get_Set_Coittee21> search_list_data) {
        super(searchList, customSearchList, search_list_data);

        this.mContext = searchList;
        this.customSearchList = customSearchList;
        this.search_list_data = search_list_data;
        Log.e("hiiiiiiiii", "  " + search_list_data.size());

    }

    public void setcustomProfile_viewListner(AdapterDownload.customProfile_view listener) {
        this.customListner = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = mContext.getLayoutInflater();
        convertView = inflater.inflate(customSearchList, parent, false);

        TextView txt_mobile = (TextView) convertView.findViewById(R.id.txt_mobile);
        TextView txt_name = (TextView) convertView.findViewById(R.id.txt_name);

        TextView txt_designation = (TextView) convertView.findViewById(R.id.txt_designation);


        final Get_Set_Coittee21 getter_setter = search_list_data.get(position);
        Log.e("check", getter_setter.getStr_member_name());

        txt_designation.setText(getter_setter.getStr_designation());
        txt_name.setText("Name : " + getter_setter.getStr_member_name());
        txt_mobile.setText("M. " + getter_setter.getStr_mobile());

        return convertView;
    }


}

