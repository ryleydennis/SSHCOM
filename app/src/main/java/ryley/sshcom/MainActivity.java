package ryley.sshcom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

//Acitivity lets user input login information, and passes that information to BottomNavigation
//acitivty
public class MainActivity extends AppCompatActivity {

  //192.168.0.123

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //Initialize local textview variables for use
    final TextView formIP = (TextView) findViewById(R.id.formIP);
    final TextView formUser = (TextView) findViewById(R.id.formUser);
    final TextView formPass = (TextView) findViewById(R.id.formPass);
    final TextView formPort = (TextView) findViewById(R.id.formPort);


    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    SharedPreferences.Editor editor  = sharedPref.edit();

    //Load pref based on prefs page

    boolean saveIP = sharedPref.getBoolean(getResources().getString(R.string.PREF_IP), false);
    boolean saveUser = sharedPref.getBoolean(getResources().getString(R.string.PREF_USERNAME), false);
    boolean savePass = sharedPref.getBoolean(getResources().getString(R.string.PREF_PASS), false);
    boolean savePort = sharedPref.getBoolean(getResources().getString(R.string.PREF_PORT), false);

    if(saveIP){
      String pref = sharedPref.getString(getResources().getString(R.string.SAVE_IP),"");
      formIP.setText(pref);
    }
    if(saveUser){
      String pref = sharedPref.getString(getResources().getString(R.string.SAVE_USERNAME),"");
      formUser.setText(pref);
    }
    if(savePass){
      String pref = sharedPref.getString(getResources().getString(R.string.SAVE_PASS),"");
      formPass.setText(pref);
    }
    if(savePort){
      String pref = sharedPref.getString(getResources().getString(R.string.SAVE_PORT),"");
      formPort.setText(pref);
    }

  }

  /** Called when the user taps the Send button */
  public void sendMessage(View view) {
    if(view.getId() == R.id.login)
    {
      Intent intent = new Intent(this, BottomNavigation.class);

      //Get textviews for storing information
      final TextView formIP = (TextView) findViewById(R.id.formIP);
      final TextView formUser = (TextView) findViewById(R.id.formUser);
      final TextView formPass = (TextView) findViewById(R.id.formPass);
      final TextView formPort = (TextView) findViewById(R.id.formPort);

      //store login information into textView
      intent.putExtra("IP", formIP.getText().toString());
      intent.putExtra("US", formUser.getText().toString());
      intent.putExtra("PA", formPass.getText().toString());
      intent.putExtra("PO", formPort.getText().toString());
      startActivity(intent);
    }

  }
}
