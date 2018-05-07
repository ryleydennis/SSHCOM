package ryley.sshcom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

//Takes login information from main and saves it. Launches fragments to fill the three tabs
public class BottomNavigation extends AppCompatActivity
implements
TerminalPage.OnFragmentInteractionListener,
CommandPage.OnFragmentInteractionListener,
PrefPage.OnFragmentInteractionListener,
CommandEditor.OnFragmentInteractionListener{

  String ip,us,pa,po;

  @Override
  public void onFragmentInteraction(Uri uri){
    //
  }

  //Make login information available to fragments
  public String getIP(){return ip;}
  public String getUs(){return us;}
  public String getPa(){return pa;}
  public String getpo(){return po;}


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bottom_navigation);

    //Initialize login information from intent
    ip = getIntent().getStringExtra("IP");
    us = getIntent().getStringExtra("US");
    pa = getIntent().getStringExtra("PA");
    po = getIntent().getStringExtra("PO");

    BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

    //Launch fragments from bottom navigation choice
    bottomNavigationView.setOnNavigationItemSelectedListener
        (new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
              case R.id.navigation_terminal:
                selectedFragment = TerminalPage.newInstance();
                break;
              case R.id.navigation_commands:
                selectedFragment = CommandPage.newInstance();
                break;
              case R.id.navigation_prefs:
                selectedFragment = PrefPage.newInstance();
                break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, selectedFragment);
            transaction.commit();
            return true;
          }
        });

    //display terminal fragment first
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.content, TerminalPage.newInstance());
    transaction.commit();

  }
}
