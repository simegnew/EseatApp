package com.app.myapplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SignupActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText InputFirstName,InputSecondName ,InputLastName , InputePhoneNumber, InputePassword,ConfirmPassword;
    private ProgressDialog loadingBar;
    ProgressBar progressBar;
    LinearLayout lvparent;
   // RelativeLayout lvparent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        CreateAccountButton=(Button) findViewById(R.id.register_btn);
        //TextView linkLogin = (TextView) findViewById(R.id.linkLogin);



        InputFirstName=(EditText) findViewById(R.id.register_firstname_input);
        InputSecondName=(EditText) findViewById(R.id.register_secondname_input);
        InputLastName=(EditText) findViewById(R.id.register_lastname_input);
        InputePhoneNumber=(EditText) findViewById(R.id.register_phone_number_input);
        InputePassword=(EditText) findViewById(R.id.register_passord_input);
        ConfirmPassword=(EditText) findViewById(R.id.confirm_register_passord_input);
        loadingBar=new ProgressDialog(this);
        progressBar = findViewById(R.id.pbbar);
        lvparent = findViewById(R.id.lvparent);
        this.setTitle("User SignUp");

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEmpty(InputFirstName.getText().toString()) ||
                        isEmpty(InputSecondName.getText().toString()) ||
                        isEmpty(InputLastName.getText().toString()) ||
                        isEmpty(InputePhoneNumber.getText().toString()) ||
                        isEmpty(InputePassword.getText().toString()))
                    ShowSnackBar("Please enter all fields");
                else if (!InputePassword.getText().toString().equals(ConfirmPassword.getText().toString()))
                    ShowSnackBar("Password does not match");
                else {
                    loadingBar.setTitle("Create Account");
                    loadingBar.setMessage("Pleas wait, while we are cheking the credentials.");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    AddUsers addUsers = new AddUsers();
                    addUsers.execute("");
                    loadingBar.dismiss();
                }

            }
        });
       /* linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            }
        });*/
    }

    public void ShowSnackBar(String message) {
        Snackbar.make(lvparent, message, Snackbar.LENGTH_LONG)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    public Boolean isEmpty(String strValue) {
        if (strValue == null || strValue.trim().equals(("")))
            return true;
        else
            return false;
    }

    private class AddUsers extends AsyncTask<String, Void, String> {
        String firstname,secondname, lastname, phone, password,ConfirmPass;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            firstname = InputFirstName.getText().toString();
            secondname = InputSecondName.getText().toString();
            lastname = InputLastName.getText().toString();
            phone = InputePhoneNumber.getText().toString();
            password=InputePassword.getText().toString();
            //ConfirmPass=ConfirmPassword.getText().toString();
            //progressBar.setVisibility(View.VISIBLE);
           // btnSignUp.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ConnectionHelper con = new ConnectionHelper();
                Connection connect = ConnectionHelper.CONN();

                String queryStmt = "Insert into Users " +
                        " (firstName,secondName,lastName,phone,pass,UserRole) values "
                        + "('"
                        + firstname
                        + "','"
                        + secondname
                        + "','"
                        + lastname
                        + "','"
                        + phone
                        + "','"
                        + password
                        + "','User')";

                PreparedStatement preparedStatement = connect
                        .prepareStatement(queryStmt);

                preparedStatement.executeUpdate();

                preparedStatement.close();

                return "Added successfully";
            } catch (SQLException e) {
                e.printStackTrace();
                return e.getMessage().toString();
            } catch (Exception e) {
                return e.getMessage().toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {

            //Toast.makeText(SignupActivity.this, result, Toast.LENGTH_SHORT).show();
            ShowSnackBar(result);
            progressBar.setVisibility(View.GONE);
           // btnSignUp.setVisibility(View.VISIBLE);
            if (result.equals("Added successfully")) {
                // Clear();
            }

        }

    }

    public void Login(View v) {
        Intent i = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(i);
    }

}