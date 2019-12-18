package com.app.theme2;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.app.templateasdemo.R;

public class ActivityRegister extends AppCompatActivity {


    Button buttonSubmit;
    TextView text_login;
    EditText editTextFirst,editTextLast,editTextEmail,editTextPassword,editTextRePassword;
    LinearLayout linearLayoutFb,linearLayoutMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_green);
        setContentView(R.layout.activity_sign_up_theme2);

        buttonSubmit=(Button)findViewById(R.id.btn_submit);
        text_login=(TextView)findViewById(R.id.text_login);
        editTextFirst=(EditText)findViewById(R.id.edt_first);
        editTextLast=(EditText)findViewById(R.id.edt_last_name);
        editTextEmail=(EditText)findViewById(R.id.edt_email);
        editTextPassword=(EditText)findViewById(R.id.edt_password);
        editTextRePassword=(EditText)findViewById(R.id.edt_conform_pass);
        linearLayoutFb=(LinearLayout)findViewById(R.id.lay_fb_login);
        linearLayoutMail=(LinearLayout)findViewById(R.id.lay_fb_gmail);

        Typeface typeface = Typeface.createFromAsset(ActivityRegister.this.getAssets(), "fonts/Montserrat-Regular_0.ttf");
        editTextFirst.setTypeface(typeface);
        editTextLast.setTypeface(typeface);
        editTextEmail.setTypeface(typeface);
        editTextPassword.setTypeface(typeface);
        editTextRePassword.setTypeface(typeface);

        text_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login=new Intent(ActivityRegister.this,ActivityLogin.class);
                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_login);
                finish();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login=new Intent(ActivityRegister.this,ActivityLogin.class);
                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_login);
                finish();
            }
        });

        linearLayoutFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login=new Intent(ActivityRegister.this,MainActivity.class);
                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_login);
                finish();
            }
        });

        linearLayoutMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login=new Intent(ActivityRegister.this,MainActivity.class);
                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_login);
                finish();
            }
        });

    }

}
