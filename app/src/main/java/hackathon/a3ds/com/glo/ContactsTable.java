package hackathon.a3ds.com.glo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


/**
 * Created by matcurtis on 7/9/16.
 */
public class ContactsTable extends Activity {


    // URL for all Slack APIs
    private static String url = "https://pure-caverns-99011.herokuapp.com/createPosse";
    //private static String url = "https://www.google.com";

    private static String posseName;

    private static String posseMembers;
    private String mJsonString;

    ListView listView;
    TextView name;
    ArrayList<String> values;
    ArrayList<String> finValues = new ArrayList<String>();
    Button btnSAN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        setContentView(R.layout.contacts_table);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.listView);

        ArrayList<String> posseList = intent.getStringArrayListExtra("posse_list");

        if(posseList == null || posseList.size() == 0) {

            // Defined Array values to show in ListView
            values = new ArrayList<String>();
            values.add("Bran");
            values.add("Hodor");
            values.add("Tyrion");
            values.add("Sasha");
            values.add("Arya");
            values.add("Shania");
            values.add("Rickon");
            values.add("Little_Finger");
            values.add("Shania");
            values.add("Varus");
            values.add("Ramsey");
            values.add("Daenerys");
            values.add("Cersei");

            values.add("Britney");
            values.add("Christina");
            values.add("Tiffany");
            values.add("Christina");
            values.add("Jewel");
            values.add("Shania");
            values.add("Alanis");
            values.add("Shakira");
        }else {
            for (int w = 0; w < posseList.size(); w++){
                values.add(posseList.get(w));
            }
        }


//        values.add("Frank");
//        values.add("Bob");
//        values.add("Toph");
//        values.add("Matthew");
//        values.add("Alex");
//        values.add("Bran");
//        values.add("John");
//        values.add("Aegon_The_Conquer");
//        values.add("T");
//        values.add("Farmer");
//        values.add("John");
//        values.add("Flee");
//        values.add("That");

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                addName(itemValue);

                view.setBackgroundColor(Color.GREEN);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Added : " + itemValue, Toast.LENGTH_SHORT)
                        .show();

            }

        });

        posseName = intent.getStringExtra("posse_name");

        name = (TextView) findViewById(R.id.nameTextView);
        name.setText(posseName);

        Button btnDone = (Button) findViewById(R.id.done);
        btnSAN = (Button) findViewById(R.id.selectAllNone);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Write(posseName);
                //https://pure-caverns-99011.herokuapp.com/
                Intent intent = new Intent(ContactsTable.this, HomeActivity.class);
                intent.putExtra("posse_name", posseName);
                startActivity(intent);
            }
        });

        btnSAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (values.size() == finValues.size()) {
                    finValues.clear();
                } else {
                    finValues = values;
                }

            }
        });
    }

    private void addName(String name) {
        if (values.size() == finValues.size()) {
        }
        if (finValues.contains(name))
            return;
        finValues.add(name);
    }
    public void Write(String name) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < finValues.size()-1; i++) {
            sb.append(finValues.get(i) + "%2C");
        }
        sb.append(finValues.get(finValues.size()-1));
        posseMembers = sb.toString();
        System.out.println("sb "+sb.toString());

        new postPosse().execute();

        SharedPreferences sharedPre = ContactsTable.this.getPreferences(Context.MODE_PRIVATE);
        String temp = sharedPre.getString(name,"");
        System.out.println("temp : "+ temp);

        List elephantList = Arrays.asList(temp.split(","));
        for (int i = 0; i < elephantList.size(); i++) {
            System.out.println("elephantList.get("+i+") "+ elephantList.get(i));
        }
    }


/***
 * Async task to make Slack users.list HTTP call
 */
private class postPosse extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //TODO: Cool loading graphic
    }

    @Override
    protected Void doInBackground(Void... args) {
        restHandler restHandler = new restHandler();

        // Make HTTP call with params
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair(posseName, posseMembers));
        mJsonString = restHandler.makeCall(url, restHandler.POST, params);
        System.out.println("mJsonString "+mJsonString.toString());

        return null;
    }

}

}