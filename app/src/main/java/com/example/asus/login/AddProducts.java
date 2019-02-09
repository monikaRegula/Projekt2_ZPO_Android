package com.example.asus.login;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;

/**
 * Class is representation of second intent. It gives an  opportunity to user to add
 * new product/ update / delete form database.
 * @author Monika Regula
 * @version 1.0
 */
public class AddProducts extends Activity {

    /**
     * Represents Connection with database.
     */
    ConnectionClass connectionClass;
    /**
     * Represents Edittexts in fxml.
     */
    EditText edtproname, edtprodesc,edtproprice;
    /**
     * Represents buttons in fxml.
     */
    Button btnadd,btnupdate,btndelete;
    /**
     * Represents progressbar in fxml.
     */
    ProgressBar pbbar;
    /**
     * Represents ListView in fxml.
     */
    ListView lstpro;
    /**
     * Represents variable proid.
     */
    String proid;


    /**
     * Method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addproducts);

        connectionClass = new ConnectionClass();
        edtproname = (EditText) findViewById(R.id.edtproname);
        edtprodesc = (EditText) findViewById(R.id.edtprodesc);
        edtproprice = (EditText) findViewById(R.id.edtproprice);
        btnadd = (Button) findViewById(R.id.btnadd);
        btnupdate = (Button) findViewById(R.id.btnupdate);
        btndelete = (Button) findViewById(R.id.btndelete);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);
        lstpro = (ListView) findViewById(R.id.lstproducts);


        proid = "";

        /**
         * If button DODAJ is cliked then method is activated.
         *
         */
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPro addPro = new AddPro();
                addPro.execute("");

            }
        });
        btnupdate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                UpdatePro updatePro = new UpdatePro();
                updatePro.execute("");

            }
        });

        btndelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DeletePro deletePro = new DeletePro();
                deletePro.execute("");

            }
        });


    }

    public class FillList extends AsyncTask<String, String, String> {
        String z = "";

        List<Map<String, String>> prolist  = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {

            pbbar.setVisibility(View.GONE);
            Toast.makeText(AddProducts.this, r, Toast.LENGTH_SHORT).show();

            String[] from = { "A", "B", "C" ,"D"};
            int[] views = { R.id.lblproid, R.id.lblproname,R.id.lblprodesc,R.id.lblproprice };
            final SimpleAdapter ADA = new SimpleAdapter(AddProducts.this,
                    prolist, R.layout.lsttemplate, from,
                    views);
            lstpro.setAdapter(ADA);

            lstpro.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);
                    proid = (String) obj.get("A");
                    String proname = (String) obj.get("B");
                    String prodesc = (String) obj.get("C");
                    String proprice = (String)obj.get("D");

                    edtprodesc.setText(prodesc);
                    edtproname.setText(proname);
                    edtproprice.setText(proprice);

                }
            });



        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "select * from Produkty3";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();


                    while (rs.next()) {

                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("A", rs.getString("id_produktu"));
                        datanum.put("B", rs.getString("nazwa_produktu"));
                        datanum.put("C", rs.getString("nazwa_producenta"));
                        datanum.put("D",rs.getString("cena_brutto"));

                        prolist.add(datanum);

                    }


                    z = "Success";
                }
            } catch (Exception ex) {
                z = "Error retrieving data from table";

            }
            return z;
        }
    }

    /**
     * Class is responsible for adding new record to database
     * @author Monika Regula
     * @version 1.0
     */
    public class AddPro extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;
        //zczytuje wartości wpisane do textfieldów
        String proname = edtproname.getText().toString();
        String prodesc = edtprodesc.getText().toString();
        String proprice = edtproprice.getText().toString();

        /**
         * This method is responsible for making progressbar visible.
         */
        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(AddProducts.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
                FillList fillList = new FillList();
                fillList.execute("");
            }

        }

        /**
         * This method insert data given in EditTexts in fxml to database.
         * @param params
         * @return z
         */
        @Override
        protected String doInBackground(String... params) {
            if (proname.trim().equals("") || prodesc.trim().equals("")||proprice.trim().equals(""))
                z = "Please enter User Id and Password";
            else {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {

                       String query = " use Kosmetyki insert into dbo.Produkty3 (nazwa_produktu,nazwa_producenta,cena_brutto,nazwa_kategorii) values ('" +
                               proname + "','" + prodesc  +"',"+proprice+",'Makijaz')";
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Added Successfully";
                        isSuccess = true;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions";
                }
            }
            return z;
        }
    }



    public class UpdatePro extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        String proname = edtproname.getText().toString();
        String prodesc = edtprodesc.getText().toString();
        String proprice = edtproprice.getText().toString();

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(AddProducts.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
                FillList fillList = new FillList();
                fillList.execute("");
            }

        }

        @Override
        protected String doInBackground(String... params) {
            if (proname.trim().equals("") || prodesc.trim().equals("")||proprice.trim().equals(""))
                z = "Please enter User Id and Password";
            else {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {

                        String query = "Update Produkty3 set nazwa_produktu='"+proname+"',nazwa_producenta='"+prodesc+",cena_brutto="+proprice+"' where id_produktu="+proid;
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Updated Successfully";

                        isSuccess = true;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions";
                }
            }
            return z;
        }
    }

    /**
     * Class is responsible for deleting record in database
     * @author Monika Regula
     * @version 1.0
     */
    public class DeletePro extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        String proname = edtproname.getText().toString();
        String prodesc = edtprodesc.getText().toString();
        String proprice = edtproprice.getText().toString();

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(AddProducts.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
                FillList fillList = new FillList();
                fillList.execute("");
            }

        }

        @Override
        protected String doInBackground(String... params) {
            if (proname.trim().equals("") || prodesc.trim().equals("")||proprice.trim().equals(""))
                z = "Please enter User Id and Password";
            else {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {


                        String query = "delete from Produkty3 where id_produktu="+proid;
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Deleted Successfully";
                        isSuccess = true;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions";
                }
            }
            return z;
        }
    }

}
