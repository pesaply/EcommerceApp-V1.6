package com.app.theme2;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.app.templateasdemo.R;

public class ActivityLogin extends AppCompatActivity {


    Button buttonLogin,buttonSkip,buttonForgotPass,buttonRegister;
    EditText editTextEmail,editTextEPass;
    LinearLayout linearLayoutFb,linearLayoutMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_green);
        setContentView(R.layout.activity_sign_in_theme2);

        buttonLogin=(Button)findViewById(R.id.btn_login);
        buttonSkip=(Button)findViewById(R.id.btn_skip);
        buttonForgotPass=(Button)findViewById(R.id.btn_forgot);
        buttonRegister=(Button)findViewById(R.id.btn_register);
        editTextEmail=(EditText)findViewById(R.id.edt_email);
        editTextEPass=(EditText)findViewById(R.id.edt_password);
        linearLayoutFb=(LinearLayout)findViewById(R.id.lay_fb_login);
        linearLayoutMail=(LinearLayout)findViewById(R.id.lay_fb_gmail);

        Typeface typeface = Typeface.createFromAsset(ActivityLogin.this.getAssets(), "fonts/Montserrat-Regular_0.ttf");
        editTextEmail.setTypeface(typeface);
        editTextEPass.setTypeface(typeface);

        buttonForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityLogin.this,"Password send your e-mail",Toast.LENGTH_SHORT).show();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login=new Intent(ActivityLogin.this,MainActivity.class);
                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_login);
                finish();
            }
        });

        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_skip=new Intent(ActivityLogin.this,MainActivity.class);
                intent_skip.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_skip);
                finish();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_regs=new Intent(ActivityLogin.this,ActivityRegister.class);
                intent_regs.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_regs);
                finish();
            }
        });
        linearLayoutFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login=new Intent(ActivityLogin.this,MainActivity.class);
                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_login);
                finish();
            }
        });

        linearLayoutMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login=new Intent(ActivityLogin.this,MainActivity.class);
                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_login);
                finish();
            }
        });
    }

}
