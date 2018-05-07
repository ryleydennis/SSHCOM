package ryley.sshcom;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.common.collect.ObjectArrays;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;

//Allows commands to be typed and chosen from a spinner and sent to SSH server. Responses from server are displayed
public class TerminalPage extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

  Spinner commmandSpinner;
  ArrayList<String> commandList;
  View view;
  TextView responseField;
  TextView inputField;
  int temp = 1;

  String ip, us, pa, po;


  private OnFragmentInteractionListener mListener;

  public TerminalPage() {
    // Required empty public constructor
  }

  public static TerminalPage newInstance() {
    TerminalPage fragment = new TerminalPage();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //Initialize login information

    ip = ((BottomNavigation)getActivity()).getIP();
    us = ((BottomNavigation)getActivity()).getUs();
    pa = ((BottomNavigation)getActivity()).getPa();
    po = ((BottomNavigation)getActivity()).getpo();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_terminal_page, container, false);

    //Get send button
    Button send = (Button) view.findViewById(R.id.SEND);
    send.setOnClickListener(this);

    //Fill spinner from file
    LocalIO io = new LocalIO();
    commandList = io.readFile(view, "CommandList");
    commmandSpinner = (Spinner) view.findViewById(R.id.commandSpinner);
    ArrayAdapter<String> dataAdatpter = new ArrayAdapter<String>(
        view.getContext(), android.R.layout.simple_spinner_item, commandList);
    dataAdatpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    commmandSpinner.setAdapter(dataAdatpter);

    commmandSpinner = (Spinner) view.findViewById(R.id.commandSpinner);
    commmandSpinner.setOnItemSelectedListener(this);

    responseField = (TextView) view.findViewById(R.id.textView_responseField);
    inputField = (TextView) view.findViewById(R.id.textView_input);

    return view;
  }


  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    if(temp!=1) inputField.append(commandList.get(position));
    else temp =0 ;
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void onClick(View view)
  {
    if(view.getId() == R.id.SEND)
    {
      //Save login information to one string along with command
      String out[] = {ip,us,pa,po,inputField.getText().toString()};
      String in = "";
      //Create new async connection object and begin connection
      SSHConnection newConnection = new SSHConnection();
      newConnection.execute(out);
    }
  }

  //Save preferences marked to be saved. delete the others
  @Override
  public void onStop(){

    super.onStop();
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(view.getContext());
    SharedPreferences.Editor editor  = sharedPref.edit();


    if(sharedPref.getBoolean(getResources().getString(R.string.PREF_IP), false)) {
      editor.putString(getString(R.string.SAVE_IP), ip);
    }
    else {
      editor.putString(getString(R.string.SAVE_IP), "");
    }

    if(sharedPref.getBoolean(getResources().getString(R.string.PREF_USERNAME), false)) {
      editor.putString(getString(R.string.SAVE_USERNAME), us);
    }
    else {
      editor.putString(getString(R.string.SAVE_USERNAME), "");
    }

    if(sharedPref.getBoolean(getResources().getString(R.string.PREF_PASS), false)) {
      editor.putString(getString(R.string.SAVE_PASS), pa);
    }
    else {
      editor.putString(getString(R.string.SAVE_PASS), "");
    }

    if(sharedPref.getBoolean(getResources().getString(R.string.PREF_PORT), false)) {
      editor.putString(getString(R.string.SAVE_PORT),po);
    }
    else {
      editor.putString(getString(R.string.SAVE_PORT), "");
    }

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
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }


//Async task for creating SSH Connection
  public class SSHConnection extends AsyncTask<String, Void, String> {
    Session session;
    String ip,user,pass,command;
    int port;

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      //Log.d("ASYNC TASK: ", "pre execute started...");

    }

    //Establishes connection with SSH server, submits command, saves response
    @Override
    protected String doInBackground(String... login)
    {
      //Log.d("ASYNC TASK: ", "Background started...");
      ip = login[0];
      user = login[1];
      pass = login[2];
      port = Integer.parseInt(login[3]);
      command = login[4];

      String response="";

      try {
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch = new JSch();
        session = jsch.getSession(user, ip, port);
        session.setPassword(pass);
        session.setConfig(config);

        session.connect();
       // Log.d("ASYNC: ", "CONNECTED");

        Channel channel = session.openChannel("exec");
        ((ChannelExec)channel).setCommand(command);
        channel.setInputStream(null);
        ((ChannelExec)channel).setErrStream(System.err);

        InputStream in=channel.getInputStream();
        channel.connect();
        byte[] tmp=new byte[1024];
        while(true){
          while(in.available()>0){
            int i=in.read(tmp, 0, 1024);
            if(i<0)break;
            response+=(new String(tmp, 0, i));
          }
          if(channel.isClosed()){
            response+=("exit-status: "+channel.getExitStatus());
            break;
          }
          try{Thread.sleep(1000);}catch(Exception ee){}
        }
        channel.disconnect();
        session.disconnect();

      } catch(Exception e){
        Log.d("ASYNC ERROR: ", e.toString());
        response+=e.toString();
      }

      StringBuilder outputBuffer = new StringBuilder();

      response += outputBuffer.toString();

      return response;
    }

    //Once task is finished, update response field
    protected void onPostExecute(String response)
    {
      responseField.setText(response);
    }
    }

}
