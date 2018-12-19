package com.metropolitan.nemanja.projekat_nmd.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.metropolitan.nemanja.projekat_nmd.Activities.Database.DatabaseHelper;
import com.metropolitan.nemanja.projekat_nmd.Activities.Model.User;
import com.metropolitan.nemanja.projekat_nmd.Activities.Validation.Validation;
import com.metropolitan.nemanja.projekat_nmd.R;

public class RegisterActivity extends AppCompatActivity {

    private User user = new User();

    private Validation validation = new Validation(this);

    private DrawerLayout drawerLayout;

    private DatabaseHelper databaseHelper = new DatabaseHelper(this);

    private TextInputLayout firstnameLayout;
    private TextInputEditText firstnameEditText;

    private TextInputLayout lastnameLayout;
    private TextInputEditText lastnameEditText;

    private TextInputLayout emailLayout;
    private TextInputEditText emailEditText;

    private TextInputLayout passwordLayout;
    private TextInputEditText passwordEditText;

    private TextInputLayout cpasswordLayout;
    private TextInputEditText cpasswordEditText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().hide();

        // Views
        firstnameLayout = (TextInputLayout) findViewById(R.id.firstnameLayout);
        lastnameLayout = (TextInputLayout) findViewById(R.id.lastnameLayout);
        emailLayout = (TextInputLayout) findViewById(R.id.emailLayout);
        passwordLayout = (TextInputLayout) findViewById(R.id.passwordLayout);
        cpasswordLayout = (TextInputLayout) findViewById(R.id.cpasswordLayout);

        firstnameEditText = (TextInputEditText) findViewById(R.id.firstnameEditText);
        lastnameEditText = (TextInputEditText) findViewById(R.id.lastnameEditText);
        emailEditText = (TextInputEditText) findViewById(R.id.emailEditText);
        passwordEditText = (TextInputEditText) findViewById(R.id.passwordEditText);
        cpasswordEditText = (TextInputEditText) findViewById(R.id.cpasswordEditText);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

    }

    public void registerClick(View v) {
        sendForm();
    }

    public void loginClick(View v) {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private void sendForm() {
        if (!validation.isEditTextFilled(firstnameEditText, firstnameLayout, getString(R.string.error_firstname))) {
            return;
        }
        if (!validation.isEditTextFilled(lastnameEditText, lastnameLayout, getString(R.string.error_lastname))) {
            return;
        }
        if (!validation.isEditTextFilled(emailEditText, emailLayout, getString(R.string.error_emailEnter))) {
            return;
        }
        if(!validation.isEmail(emailEditText, emailLayout, getString(R.string.error_email))) {
            return;
        }
        if (!validation.isEditTextFilled(passwordEditText, passwordLayout, getString(R.string.error_passwordEnter))) {
            return;
        }
        if (!validation.isMatched(passwordEditText, cpasswordEditText,
                cpasswordLayout, getString(R.string.error_password_match))) {
            return;
        }

        if (!databaseHelper.checkUser(emailEditText.getText().toString().trim())) {

            user.setFirstName(firstnameEditText.getText().toString().trim());
            user.setLastName(lastnameEditText.getText().toString().trim());
            user.setEmail(emailEditText.getText().toString().trim());
            user.setPassword(passwordEditText.getText().toString().trim());

            databaseHelper.addUser(user);


            // Snack Bar to show success message that record saved successfully
            Snackbar.make(drawerLayout, "Your account was successfully created!", Snackbar.LENGTH_LONG).show();

        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(drawerLayout, "USER ALREADY EXISTS", Snackbar.LENGTH_LONG).show();

        }


    }
}
