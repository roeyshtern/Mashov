package com.example.user.mashov;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;


/**
 * Created by LoginAndRegisterANS on 12/10/17.
 */

public class RegisterActivity extends ServiceHandler implements IJson<String>{
    final int MAX_SIZE_NAME = 15;
    final int SIZE_PHONE = 10;
    final int MIN_LEN_PASSWORD = 5;
    final String PREFIX_PHONE = "05";
    private final int RESULT_LOAD_IMG = 1;
    private static final int REQUEST_READ_PERMISSION = 2;
    boolean pickDateFlag = false;
    boolean pickImageFlag = false;
    boolean pngFlag = false;
    ProgressBar progressBar;
    EditText et_input_first_name;
    EditText et_input_last_name;
    EditText et_input_username;
    EditText et_input_email;
    EditText et_input_password;
    EditText et_input_password_again;
    TextView text_date;
    Button bt_pick_date;
    Button bt_pick_image;
    RadioGroup rg_gender;
    EditText et_input_phone;
    RadioGroup rg_type;
    Button bt_register;
    ImageView iv_image;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        int per = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(per!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermission(REQUEST_READ_PERMISSION);
        }

        //init all the variables
        init();

    }
    private void requestPermission(int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
        }
    }
    @Override
    public void onReceiveResult(String response) {
        progressBar.setVisibility(View.GONE);
        Gson gson = new Gson();
        JsonObject<LoginAndRegisterANS> jsonObject = gson.fromJson(response, new TypeToken<JsonObject<LoginAndRegisterANS>>(){}.getType());
        switch (jsonObject.data.responseCode)
        {
            case 105:
            {
                Toast.makeText(this, "Register successfully!", Toast.LENGTH_LONG).show();
                Intent intentLoginActivity = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intentLoginActivity);
                break;
            }
            case 202:
            {
                et_input_username.setError(getResources().getString(R.string.error_username_used));
                et_input_username.requestFocus();
                break;
            }
            case 203:
            {
                et_input_email.setError(getResources().getString(R.string.error_email_used));
                et_input_email.requestFocus();
                break;
            }
            case 204:
            {
                et_input_phone.setError(getResources().getString(R.string.error_phone_used));
                et_input_phone.requestFocus();
                break;
            }
            default:
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage(response);
                dlgAlert.setTitle("Registeration Failed");
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //dismiss the dialog
                            }
                        });
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                break;
            }
        }
        Log.i("responseFromServer:", response);
    }

    private void init()
    {
        et_input_first_name = (EditText)findViewById(R.id.rl_input_first_name);
        et_input_last_name = (EditText)findViewById(R.id.rl_input_last_name);
        et_input_username = (EditText)findViewById(R.id.rl_input_username);
        et_input_email = (EditText)findViewById(R.id.rl_input_email);
        et_input_password = (EditText)findViewById(R.id.rl_input_password);
        et_input_password_again = (EditText)findViewById(R.id.rl_input_password_again);
        text_date = (TextView)findViewById(R.id.rl_text_date);
        bt_pick_date = (Button)findViewById(R.id.rl_bt_pick_date);
        bt_pick_image = (Button)findViewById(R.id.rl_bt_pick_image);
        rg_gender = (RadioGroup)findViewById(R.id.rl_radio_group_gender);
        et_input_phone = (EditText)findViewById(R.id.rl_input_phone);
        rg_type = (RadioGroup)findViewById(R.id.rl_radio_group_type);
        bt_register = (Button)findViewById(R.id.rl_bt_register);
        iv_image = (ImageView)findViewById(R.id.rl_iv_image);
        progressBar = (ProgressBar) findViewById(R.id.pb);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);

                if(imgDecodableString.substring(imgDecodableString.lastIndexOf(".") + 1).equals("png"))
                {
                    pngFlag = true;
                }

                cursor.close();
                // Set the Image in ImageView after decoding the String
                iv_image.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                pickImageFlag = true;

            }
            else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }
    public void pickImage(View v)
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMG);
    }
    public void pickDate(View v)
    {
        new DateDialog().show(getFragmentManager(), "datepicker");
        pickDateFlag = true;
    }
    public void submitRegistration(View v)
    {
        boolean flagFirstName = isFirstNameValid();
        boolean flagLastName = isLastNameValid();
        boolean flagUsername = isUsernameValid();
        boolean flagEmail = isEmailValid();
        boolean flagPassword = isPasswordValid();
        boolean flagPasswordAgain = isPasswordAgainValid();
        boolean flagGender = isGenderValid();
        boolean flagPhone = isPhoneValid();
        boolean flagType = isTypeVaild();
        boolean flagPickDate = isPickedDateValid();
        boolean flagPickImage = isPickedImageValid();

        if(flagFirstName && flagLastName && flagUsername && flagEmail && flagPassword && flagPasswordAgain && flagGender && flagPhone && flagType && flagPickDate && flagPickImage)
        {
            HashMap<String, String> params = new HashMap<>();
            params.put("firstName", et_input_first_name.getText().toString());
            params.put("lastName", et_input_last_name.getText().toString());
            params.put("username", et_input_username.getText().toString());
            params.put("email", et_input_email.getText().toString());
            params.put("password", et_input_password.getText().toString());
            params.put("gender", Integer.toString(rg_gender.indexOfChild(findViewById(rg_gender.getCheckedRadioButtonId()))));
            params.put("phoneNum", et_input_phone.getText().toString());
            params.put("type", Integer.toString(rg_type.indexOfChild(findViewById(rg_type.getCheckedRadioButtonId()))+2));
            params.put("birthday", text_date.getText().toString());

            //get photo in base64
            Bitmap bm = ((BitmapDrawable)iv_image.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if(pngFlag)
            {
                bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
            }
            else
            {
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            }
            byte[] byteArrayImage = baos.toByteArray();
            Log.i("responseFromServer:", Base64.encodeToString(byteArrayImage, Base64.DEFAULT));

            //params.put("image", Base64.encodeToString(byteArrayImage, Base64.DEFAULT));
            params.put("image", bytesToHex(byteArrayImage));

            String json = makeJson(params);
            try{
                progressBar.setVisibility(View.VISIBLE);
                webService.sendPost("jsonQuery", json);

            }catch (Exception e)
            {
                e.printStackTrace();
            }

            Log.i("responseFromServer:", json);
        }
    }
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    private boolean isFieldEmpty(EditText et)
    {
        boolean flag = true;
        if(et.getText().toString().trim().equalsIgnoreCase(""))
        {
            et.setError(getResources().getString(R.string.error_empty));
            et.requestFocus();
            flag = false;
        }
        return flag;
    }
    private boolean isFieldNotTooLong(EditText et, int maxSize)
    {
        boolean flag = true;
        if(et.getText().toString().length()>maxSize)
        {
            et.setError(getResources().getString(R.string.error_too_long));
            et.requestFocus();
            flag = false;
        }
        return flag;
    }
    private boolean isFirstNameValid()
    {
        boolean flag = true;
        //first name empty
        flag = isFieldEmpty(et_input_first_name);

        //first name length
        flag = isFieldNotTooLong(et_input_first_name, MAX_SIZE_NAME);
        return flag;
    }
    private boolean isLastNameValid()
    {
        boolean flag = true;
        //last name empty
        flag = isFieldEmpty(et_input_last_name);

        //last name length
        flag = isFieldNotTooLong(et_input_last_name, MAX_SIZE_NAME);
        return flag;
    }
    private boolean isUsernameValid()
    {
        boolean flag = true;
        //username empty
        flag = isFieldEmpty(et_input_username);

        //username length
        flag = isFieldNotTooLong(et_input_username, MAX_SIZE_NAME);
        return flag;
    }
    private boolean isEmailValid()
    {
        int cntAt = 0;
        int dotFlag = 0;
        int cntFlag = 0;
        boolean flag = true;
        //email empty
        flag = isFieldEmpty(et_input_email);
        String input = et_input_email.getText().toString();
        for(int i = 0; i<input.length() && flag;i++)
        {
            if(cntAt<=1)
            {
                //count the at times
                if (input.charAt(i) == '@') {
                    cntAt++;
                    cntFlag = i;
                }
                //check that there is not double dots
                if (input.charAt(i) == '.') {
                    if (dotFlag + 1 == i) {
                        flag = false;
                        et_input_email.setError(getResources().getString(R.string.error_email_double_dots));
                        et_input_email.requestFocus();
                    } else {
                        dotFlag = i;
                    }
                }
            }
            else
            {
                flag = false;
                et_input_email.setError(getResources().getString(R.string.error_email_too_much_at));
                et_input_email.requestFocus();
            }

        }
        //check if there is al least one at
        if(cntAt<1)
        {
            flag = false;
            et_input_email.setError(getResources().getString(R.string.error_email_must_1_at));
            et_input_email.requestFocus();
        }
        if(cntFlag>dotFlag)
        {
            flag = false;
            et_input_email.setError(getResources().getString(R.string.error_email_must_have_dot_after_at));
            et_input_email.requestFocus();
        }
        return flag;
    }
    private boolean isPasswordValid()
    {
        boolean flag = true;
        //password empty
        flag = isFieldEmpty(et_input_password);
        if(flag && et_input_password.getText().toString().length() <MIN_LEN_PASSWORD)
        {
            flag = false;
            et_input_password.setError(getResources().getString(R.string.error_password_too_short));
            et_input_password.requestFocus();
        }
        return flag;
    }
    private boolean isPasswordAgainValid()
    {
        boolean flag = true;
        //password again empty
        flag = isFieldEmpty(et_input_password_again);
        //check if the two password are equal
        if(isPasswordValid() && flag)
        {
            if(et_input_password.getText().toString().length() < MIN_LEN_PASSWORD)
            {
                flag = false;
                et_input_password_again.setError(getResources().getString(R.string.error_password_too_short));
                et_input_password_again.requestFocus();
            }
            if(flag && !et_input_password.getText().toString().equals(et_input_password_again.getText().toString()))
            {
                flag = false;
                et_input_password_again.setError(getResources().getString(R.string.error_password_again_not_match));
                et_input_password_again.requestFocus();
            }
        }
        return flag;
    }
    private boolean isPickedImageValid()
    {
        boolean flag = true;
        if(!pickImageFlag)
        {
            flag = false;
            bt_pick_image.setError(getResources().getString(R.string.error_date_not_picked));
        }
        else
        {
            bt_pick_image.setError(null);
        }
        return flag;
    }
    private boolean isPickedDateValid()
    {
        boolean flag = true;
        if(!pickDateFlag)
        {
            flag = false;
            text_date.setError(getResources().getString(R.string.error_date_not_picked));
        }
        else
        {
            text_date.setError(null);
        }
        return flag;
    }
    private boolean isGenderValid()
    {
        boolean flag = true;
        RadioButton lastRadioButton = (RadioButton)findViewById(R.id.rl_radio_button_female);
        if(rg_gender.getCheckedRadioButtonId() == -1)
        {
            lastRadioButton.setError(getResources().getString(R.string.error_empty));
            lastRadioButton.requestFocus();
            flag = false;
        }
        else
        {
            lastRadioButton.setError(null);
        }
        return flag;
    }
    private boolean isPhoneValid()
    {
        boolean flag = true;
        //phone empty
        flag = isFieldEmpty(et_input_phone);
        String phone = et_input_phone.getText().toString();
        //check phone is not too long
        if(phone.length()!=SIZE_PHONE)
        {
            flag = false;
            et_input_phone.setError(getResources().getString(R.string.error_phone_must_10_digits));
            et_input_phone.requestFocus();
        }

        if(!phone.startsWith(PREFIX_PHONE))
        {
            flag = false;
        }
        for (int i = 1; i< phone.length() && flag;i++)
        {
            if((phone.charAt(i) < '0' || phone.charAt(i) > '9'))
            {
                flag = false;
                et_input_phone.setError(getResources().getString(R.string.error_phone_only_digits));
                et_input_phone.requestFocus();
            }
        }
        return flag;
    }
    private boolean isTypeVaild()
    {
        RadioButton lastRadioButton = (RadioButton)findViewById(R.id.rl_radio_button_teacher);
        boolean flag = true;
        if(rg_type.getCheckedRadioButtonId()== -1)
        {
            lastRadioButton.setError(getResources().getString(R.string.error_empty));
            lastRadioButton.requestFocus();
            flag = false;
        }
        else
        {
            lastRadioButton.setError(null);
        }
        return flag;
    }
    @Override
    public String makeJson(HashMap<String, String> params) {
        JsonObject<RegisterREQ> jsonObject = new JsonObject<>();
        jsonObject.command = "signupPrivateUser";
        RegisterREQ registerREQ = new RegisterREQ();
        registerREQ.firstName = params.get("firstName");
        registerREQ.lastName = params.get("lastName");
        registerREQ.username = params.get("username");
        registerREQ.birthday = params.get("birthday");
        registerREQ.email = params.get("email");
        registerREQ.password = params.get("password");
        registerREQ.phoneNum = params.get("phoneNum");
        registerREQ.gender = Integer.parseInt(params.get("gender"));
        registerREQ.image = params.get("image");
        registerREQ.type = Integer.parseInt(params.get("type"));
        jsonObject.data = registerREQ;

        Gson gson = new Gson();
        String json = gson.toJson(jsonObject, new TypeToken<JsonObject<RegisterREQ>>(){}.getType());
        /*
        String command = "{";
        command+="\"command\":\"signupPrivateUser\",";
        command+="\"data\":{";
        command+="\"firstName\":\"" + params.get("firstName") + "\",";
        command+="\"lastName\":\"" + params.get("lastName") + "\",";
        command+="\"username\":\"" + params.get("username") + "\",";
        command+="\"birthday\":\"" + params.get("birthday") + "\",";
        command+="\"email\":\"" + params.get("email") + "\",";
        command+="\"password\":\"" + params.get("password") + "\",";
        command+="\"phoneNum\":\"" + params.get("phoneNum") + "\",";
        command+="\"gender\":\"" + params.get("gender") + "\",";
        command+="\"image\":\"" + params.get("image") + "\",";
        command+="\"type\":\"" + params.get("type") + "\"";
        command+="}";
        command+="}";
        */

        return json;
    }
}
