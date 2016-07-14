package hackathon.a3ds.com.glo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {

    private EditText[] mFields;
    private String[] mValues;

    private View mProgressView;

    public static final int NUM_FIELDS = 7;
    private static final int INDEX_EMAIL = 0;
    private static final int INDEX_USERNAME = 1;
    private static final int INDEX_PASSWORD = 2;
    private static final int INDEX_PASSWORDCOPY = 3;
    private static final int INDEX_PHONE = 4;
    private static final int INDEX_PIN = 5;
    private static final int INDEX_HOMEBASE = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mProgressView = findViewById(R.id.register_progress);

        EditText fieldEmail = (EditText) findViewById(R.id.register_email);
        EditText fieldUsername = (EditText) findViewById(R.id.register_username);
        EditText fieldPassword = (EditText) findViewById(R.id.register_password);
        EditText fieldPasswordCopy = (EditText) findViewById(R.id.register_passwordcopy);
        EditText fieldPhone = (EditText) findViewById(R.id.register_phone);
        EditText fieldPin = (EditText) findViewById(R.id.register_pin);
        EditText fieldHomeBase = (EditText) findViewById(R.id.register_homebase);

        mFields = new EditText[NUM_FIELDS];
        mFields[0] = fieldEmail;
        mFields[1] = fieldUsername;
        mFields[2] = fieldPassword;
        mFields[3] = fieldPasswordCopy;
        mFields[4] = fieldPhone;
        mFields[5] = fieldPin;
        mFields[6] = fieldHomeBase;

        Button buttonRegister = (Button) findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the field values
                mValues = new String[NUM_FIELDS];
                for(int i=0; i<mValues.length; i++){
                    mValues[i] = mFields[i].getText().toString();
                }
                // Fields are non-empty
                //if(validateFields()){
                    registerUser();
                //}
            }
        });
    }

    /***
     * Check if the fields are valid - currently just checks if they are empty
     * @return
     */
    private boolean validateFields(){
        boolean clear = true;
        if(mValues[1] != mValues[2]){
            mFields[2].setError("Passwords do not match!");
            return false;
        }
        for(int i=0; i<mValues.length; i++) {
            if (TextUtils.isEmpty(mValues[i])) {
                clear = false;
                mFields[i].setError("This field is required!");
            }
        }
        return clear;
    }

    private void clearFieldErrors(){
        for(int i=0; i<mFields.length;i++){
            mFields[i].setError(null);
        }
    }

    private void registerUser(){
        showProgress(true);
        System.out.println("*************START PARSE*******************");
        ParseUser userObject = new ParseUser();
        userObject.setUsername(mValues[INDEX_USERNAME]);
        userObject.setPassword(mValues[INDEX_PASSWORD]);
        userObject.setEmail(mValues[INDEX_EMAIL]);
        userObject.put("phone",mValues[INDEX_PHONE]);
        userObject.put("pin", mValues[INDEX_PIN]);
        userObject.put("homebase", mValues[INDEX_HOMEBASE]);

        // Save default homebase
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("homebase_default", mValues[INDEX_HOMEBASE]);
        editor.commit();


        userObject.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    System.out.println("***********NO PARSE ERROR************");
                    showProgress(false);
                    finish();
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    System.out.println(e.getCode());
                    switch(e.getCode()){
                        case ParseException.INVALID_EMAIL_ADDRESS:
                            Toast.makeText(getApplicationContext(), "Invalid Email Address", Toast.LENGTH_SHORT).show();
                            break;
                        case ParseException.EMAIL_TAKEN:
                            Toast.makeText(getApplicationContext(), "An account with this email already exists.", Toast.LENGTH_SHORT).show();
                            break;
                        case ParseException.USERNAME_TAKEN:
                            Toast.makeText(getApplicationContext(), "This username is already taken!", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    showProgress(false);
                }
            }
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
