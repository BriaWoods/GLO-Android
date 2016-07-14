package hackathon.a3ds.com.glo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static hackathon.a3ds.com.glo.R.id.posse_name;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
/**
 * Created by frank on 7/9/16.
 */
public class PosseActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posse);
        final Context mContext = this;
        Button btnNextPosse = (Button) findViewById(R.id.next);
        Button btnBackPosse = (Button) findViewById(R.id.cancel);

        btnNextPosse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText edText1 = (EditText) findViewById(R.id.posse_name);
                edText1.setInputType(InputType.TYPE_CLASS_TEXT);
                String str = edText1.getText().toString();
                if(!str.equals("")) {
                    Intent intent = new Intent(mContext, ContactsTable.class);
                    intent.putExtra("posse_name", str);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Please give your posse a name." , Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        btnBackPosse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
