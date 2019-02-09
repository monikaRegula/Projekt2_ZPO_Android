package com.example.asus.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Class is the first Intent that loggs user to database.
 * When username and password are correct then new intent is activated.
 * @author Monika Regula
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Represents Connection with database.
     */
    private ConnectionClass connectionClass;
    /**
     * Represents Edittext in fxml
     */
    private EditText edtuserid,edtpass;
    /**
     * Represents button in fxml.
     */
    private Button btnLogin;
    /**
     * Represents Progressbar in fxml.
     */
    private ProgressBar pbbar;


    /**
     * Method initialize components from fxml and reacts on clicked buttons with specific methods.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            connectionClass = new ConnectionClass();
        edtuserid = (EditText) findViewById(R.id.edtuserid);
        edtpass = (EditText) findViewById(R.id.edtpass);
        btnLogin = (Button) findViewById(R.id.btnlogin);
        pbbar = (ProgressBar)findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);


        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DoLogin doLogin = new DoLogin();
                doLogin.execute("");
            }
        });
    }
    public class DoLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;


        String userid = edtuserid.getText().toString();
        String password = edtpass.getText().toString();


        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this,r,Toast.LENGTH_SHORT).show();

            if(isSuccess) {
                Intent i = new Intent(MainActivity.this, AddProducts.class);
                startActivity(i);
                finish();
            }

        }


        /**
         * This method returns information wheteher connection with database is succeful or failure.
         * @param params params
         * @return z
         */
        @Override
        protected String doInBackground(String... params) {

                try {
                    //pierwszy intent
                    //jak poprawne logowanie do bazy to przechodzi do nowego intentu
                    Connection con = connectionClass.CONN(edtuserid.getText().toString(), edtpass.getText().toString());
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String query = "select * from dbo.Produkty3";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        if(rs.next())
                        {
                            z = "Login successfull";
                            isSuccess=true;
                        }
                        else
                        {
                            z = "Invalid Credentials";
                            isSuccess = false;
                        }

                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions";
                }

            return z;
        }
    }



}

