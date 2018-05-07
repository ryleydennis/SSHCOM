package ryley.sshcom;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

//Preference page contains checkboxes for user peferences
public class PrefPage extends Fragment implements View.OnClickListener {
  View view;
  SharedPreferences sharedPref;
  SharedPreferences.Editor editor;


  private OnFragmentInteractionListener mListener;

  public PrefPage() {
    // Required empty public constructor
  }


  public static PrefPage newInstance() {
    PrefPage fragment = new PrefPage();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  //Intialize checkboxes and set to last saved state
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    view =  inflater.inflate(R.layout.fragment_pref_page, container, false);
    sharedPref = PreferenceManager.getDefaultSharedPreferences(view.getContext());
    editor = sharedPref.edit();

    CheckBox ipChck = (CheckBox) view.findViewById(R.id.chckIP);
    CheckBox userChck = (CheckBox) view.findViewById(R.id.chckUser);
    CheckBox passChck = (CheckBox) view.findViewById(R.id.chckPass);
    CheckBox portChck = (CheckBox) view.findViewById(R.id.chckPort);

    ipChck.setOnClickListener(this);
    userChck.setOnClickListener(this);
    passChck.setOnClickListener(this);
    portChck.setOnClickListener(this);

    ipChck.setChecked(sharedPref.getBoolean(getResources().getString(R.string.PREF_IP), false));
    userChck.setChecked(sharedPref.getBoolean(getResources().getString(R.string.PREF_USERNAME), false));
    passChck.setChecked(sharedPref.getBoolean(getResources().getString(R.string.PREF_PASS), false));
    portChck.setChecked(sharedPref.getBoolean(getResources().getString(R.string.PREF_PORT), false));

    return view;
  }

  //Save changes to checkboxes
  public void onClick(View view){
    boolean checked = ((CheckBox) view).isChecked();

    if(view.getId() == R.id.chckIP)editor.putBoolean(getString(R.string.PREF_IP), checked);
    else if (view.getId() == R.id.chckUser)editor.putBoolean(getString(R.string.PREF_USERNAME), checked);
    else if (view.getId() == R.id.chckPass)editor.putBoolean(getString(R.string.PREF_PASS), checked);
    else if (view.getId() == R.id.chckPort)editor.putBoolean(getString(R.string.PREF_PORT), checked);

    editor.commit();
  }


  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {
    void onFragmentInteraction(Uri uri);
  }
}
