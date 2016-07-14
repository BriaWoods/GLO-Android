package hackathon.a3ds.com.glo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by frank on 7/9/16.
 */
public class OutingActivity extends Activity {
    // URL for backend API
    private static String url = "https://pure-caverns-99011.herokuapp.com/getPosse";
    private JSONArray mUsers;
    private String mJsonString;
    String test = new String();
    ListView listView;
    ArrayList<String> values;
    final Context mContext = this;
    // Get ListView object from xml

    public static final String NODE_MEMBERS = "allPosses";

    TimePicker timePicker1;

    EditText nameTxt;
    EditText destinationTxt;

    @TargetApi(Build.VERSION_CODES.M)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outing);
        final Context mContext = this;
        Button btnCancelOuting = (Button) findViewById(R.id.cancel);
        Button btnCreateOuting = (Button) findViewById(R.id.create);
        Button btnMembersOuting = (Button) findViewById(R.id.create_members);

        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);

        nameTxt = (EditText) findViewById(R.id.create_name);
        destinationTxt = (EditText) findViewById(R.id.create_destination);

        btnCancelOuting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
            }
        });

        btnCreateOuting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, OutingViewActivity.class);
                startActivity(intent);
            }
        });
        btnCreateOuting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = timePicker1.getCurrentHour();
                int min = timePicker1.getCurrentMinute();

                String str_min;
                if(min <= 9)
                {
                    str_min = "0" + min;
                }
                else{
                    str_min = ""+min;
                }
                String ampm = "AM";
                if(hour >12){
                    ampm = "PM";
                }
                String str_name = nameTxt.getText().toString();
                String str_des = destinationTxt.getText().toString();

                Intent intent = new Intent(getApplicationContext(), OutingViewActivity.class);
                intent.putExtra("hour", hour);
                intent.putExtra("minute", str_min);
                intent.putExtra("ampm",ampm);
                intent.putExtra("name", str_name);
                intent.putExtra("des", str_des);
                startActivity(intent);
            }
        });
        btnMembersOuting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new GetUsersList().execute();


            }
        });
    }
    private class GetUsersList extends AsyncTask<Void, Void, Void> {
        private  String url = "https://pure-caverns-99011.herokuapp.com/getPosse";
        private JSONArray mUsers;
        private String mJsonString;
        public static final String NODE_MEMBERS = "allPosses";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... args) {
            restHandler restHandler = new restHandler();

            // Make HTTP call with params
            List<NameValuePair> params = new LinkedList<NameValuePair>();
            mJsonString = restHandler.makeCall(url, restHandler.GET, null);

            if(mJsonString != null){
                try {
                    JSONObject jsonObject = new JSONObject(mJsonString);
                    mUsers = jsonObject.getJSONArray(NODE_MEMBERS);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                try{
                    // loop through all users
                    for(int i=0; i<mUsers.length(); i++){
                        JSONObject user = mUsers.getJSONObject(i);
                        test += user.getString("name");
                        test += "\n";
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                finally{
                    // Add user info - ensures that if a JSONException occurrs due to
                    // missing JSON response fields, that userinfo is still addded
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);

            // Defined Array values to show in ListView
            listView = (ListView) findViewById(R.id.listView);
            values = new ArrayList<String>();
            values.add(test);
            System.out.println(values);
            // Update usersList ListView with new parsed JSON data

            Intent intent = new Intent(mContext, HomeActivity.class);
            intent.putExtra("posse_list", values);
            startActivity(intent);
            //posList.add("test");

        }
    }
}


