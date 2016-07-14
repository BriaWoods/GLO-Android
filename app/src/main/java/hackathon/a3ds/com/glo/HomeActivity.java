package hackathon.a3ds.com.glo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by frank on 7/9/16.
 */
public class HomeActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Context mContext = this;

        Button btnCreatePosse = (Button) findViewById(R.id.button_createPosse);
        Button btnEditPosse = (Button) findViewById(R.id.button_editPosse);
        Button btnCreateOuting = (Button) findViewById(R.id.button_createOuting);
        Button btnViewOuting = (Button) findViewById(R.id.button_viewOuting);

        btnCreatePosse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PosseActivity.class);
                startActivity(intent);
            }
        });

        btnCreateOuting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, OutingActivity.class);
                startActivity(intent);
            }
        });

        btnViewOuting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, OutingViewActivity.class);
                startActivity(intent);
            }
        });
    }

}
