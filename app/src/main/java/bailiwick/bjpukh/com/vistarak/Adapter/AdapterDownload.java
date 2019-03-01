package bailiwick.bjpukh.com.vistarak.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import bailiwick.bjpukh.com.vistarak.Getter_Setter.GET_SET_Download;
import bailiwick.bjpukh.com.vistarak.Language.TextUtils;
import bailiwick.bjpukh.com.vistarak.R;

import static bailiwick.bjpukh.com.vistarak.Language.LanguageType.SelectedLanguage;

/**
 * Created by Prince on 07-09-2017.
 */

public class AdapterDownload extends ArrayAdapter<GET_SET_Download> {

    Activity mContext;
    int customSearchList;
    ArrayList<GET_SET_Download> search_list_data;
    customProfile_view customListner;

    public AdapterDownload(Activity searchList, int customSearchList,
                           ArrayList<GET_SET_Download> search_list_data) {
        super(searchList, customSearchList, search_list_data);

        this.mContext = searchList;
        this.customSearchList = customSearchList;
        this.search_list_data = search_list_data;
        Log.e("hiiiiiiiii", "  " + search_list_data.size());

    }

    public void setcustomProfile_viewListner(customProfile_view listener) {
        this.customListner = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = mContext.getLayoutInflater();
        convertView = inflater.inflate(customSearchList, parent, false);

        TextView txt_download = (TextView) convertView
                .findViewById(R.id.txt_download);
        txt_download.setText(SelectedLanguage[TextUtils.download]);

        ImageView img_logo = (ImageView) convertView
                .findViewById(R.id.img_logo_vikas);


        final GET_SET_Download getter_setter = search_list_data.get(position);
        Log.e("check", getter_setter.getStr_app_name());

        if (!getter_setter.getStr_app_icon().equalsIgnoreCase("")) {

            Picasso.with(mContext).load(getter_setter.getStr_app_icon())
                    .placeholder(R.drawable.icon).error(R.drawable.icon).into(img_logo);

        }
        txt_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customListner.onButtonClickListner(position, "");
            }
        });
        return convertView;
    }

    public interface customProfile_view {
        public void onButtonClickListner(int position, String value);
    }

}



