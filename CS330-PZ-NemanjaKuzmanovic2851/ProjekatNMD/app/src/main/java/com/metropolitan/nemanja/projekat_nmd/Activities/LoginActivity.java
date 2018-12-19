package com.metropolitan.nemanja.projekat_nmd.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import com.metropolitan.nemanja.projekat_nmd.Activities.Database.DatabaseHelper;
import com.metropolitan.nemanja.projekat_nmd.Activities.Model.User;
import com.metropolitan.nemanja.projekat_nmd.Activities.Validation.Validation;
import com.metropolitan.nemanja.projekat_nmd.R;

public class LoginActivity extends AppCompatActivity {


    private DatabaseHelper databaseHelper = new DatabaseHelper(this);

    private Validation validation = new Validation(this);

    private DrawerLayout drawerLayout;

    private TextInputLayout emailLayout;
    private TextInputEditText emailEditText;

    private TextInputLayout passwordLayout;
    private TextInputEditText passwordText;

    private AppCompatButton loginBtn;
    private AppCompatButton registerBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        //Views
        emailLayout = (TextInputLayout) findViewById(R.id.emailLayout);
        emailEditText = (TextInputEditText) findViewById(R.id.emailEditText);

        passwordLayout = (TextInputLayout) findViewById(R.id.passwordLayout);
        passwordText = (TextInputEditText) findViewById(R.id.passwordEditText);



    }



    public void registerClick(View v){
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    public void loginClick(View v){
        sendForm();
    }


    private void sendForm() {
        if (!validation.isEditTextFilled(emailEditText, emailLayout, getString(R.string.error_email))) {
            return;
        }
        if (!validation.isEmail(emailEditText, emailLayout, getString(R.string.error_email))) {
            return;
        }
        if (!validation.isEditTextFilled(passwordText, passwordLayout, getString(R.string.error_empty_password))) {
            return;
        }
        if (databaseHelper.checkUser(emailEditText.getText().toString().trim(), passwordText.getText().toString().trim())) {

            Intent mainAct = new Intent(this, MainActivity.class);
            mainAct.putExtra("EMAIL", emailEditText.getText().toString().trim());
            String firstname = databaseHelper.userInfo((emailEditText).getText().toString().trim()).get(0).toString();
            mainAct.putExtra("FIRSTNAME", firstname);
            String lastname = databaseHelper.userInfo((emailEditText).getText().toString().trim()).get(1).toString();
            mainAct.putExtra("LASTNAME", lastname);
            emailEditText.setText(null);
            passwordText.setText(null);
            startActivity(mainAct);

        } else {
           // Toast to show that username or password is wrong
            Toast.makeText(this, "Wrong username or password!", Toast.LENGTH_LONG).show();
        }
    }
}
