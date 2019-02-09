package com.example.asus.login;

import android.annotation.SuppressLint;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class is responsible for login user to SQLServer
 * When username and password are correct then new intent is activated.
 * @author Monika Regula
 * @Version 1.0
 */

public class ConnectionClass {
    /**
     * Represents my IPf
     */
    //moje dane pobrane z cmd :
    String ip ="192.168.1.6";
    /**
     * Represents my database name.
     */
    //nazwa bazy danych
    String db ="Kosmetyki";


    /**
     * This method is responsible for connecting with SQL
     * @param user user
     * @param password password
     * @return conn
     */
    @SuppressLint("NewApi")
    public Connection CONN(String user, String password){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection conn = null;

        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(
                    "jdbc:jtds:sqlserver://"+ip+":1433/"+db+";encrypt=false;user="+user+";password="+password+";instance=MSSQLSERVER;"
            );
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }


    /**
     * This method is responsible for connection with SQLServer as well.
     * But it uses default username and password.First step to get into database is logg in with my
     * username and password. If that step is overcomed then i use this method to connect another time but with default variable password and username.
     * @return conn
     */
    //nadpisanie metody, która jest wywoływana dopiero po zalogowaniu do bazy danych
    public Connection CONN(){

            //korzystam z gotowych danych do serwera
            String user = "Monia";
            String password = "beverly90210";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection conn = null;

        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(
                    "jdbc:jtds:sqlserver://"+ip+":1433/"+db+";encrypt=false;user="+user+";password="+password+";instance=MSSQLSERVER;"
            );
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }


}
