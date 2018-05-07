package ryley.sshcom;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.common.base.Joiner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

//Fragment that holds commands to be created and edited in the middle tab
public class CommandEditor extends Fragment implements View.OnClickListener {

  final String COMMAND_FILE = "CommandList";
  View view;
  TextView textView;
  boolean isNew;
  String command;
  int position;
  LocalIO io = new LocalIO();

  private OnFragmentInteractionListener mListener;

  public CommandEditor() {
    // Required empty public constructor
  }

  public static CommandEditor newInstance() {
    CommandEditor fragment = new CommandEditor();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  //Buttons instantiated and Bundle passed to Fragment is saved
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_command_editor, container, false);

    Button saveButton = (Button) view.findViewById(R.id.button_Save);
    Button deleteButton = (Button) view.findViewById(R.id.button_Delete);
    saveButton.setOnClickListener(this);
    deleteButton.setOnClickListener(this);

    Bundle bundle = getArguments();
    if(bundle != null && bundle.containsKey("COMMAND"))
    {
      textView = (TextView) view.findViewById(R.id.editText2);
      command = bundle.getString("COMMAND");
      position = Integer.parseInt(bundle.getString("POSITION"));
      textView.setText(command);
      isNew = bundle.getBoolean("ISNEW");
    }
    else
    {
      textView = (TextView) view.findViewById(R.id.editText2);
      command = null;
      position = 0;
      isNew = true;
    }

    textView.setText(command);

    return view;
  }

  //Command is saved to content file in memory if save button is pressed
  //Command is erased from content file in memory if delete is pressed
  @Override
  public void onClick(View view) {
    Fragment fragment = null;
    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
    fragment = new CommandPage();
    transaction.replace(R.id.content, fragment);

    if(view.getId()==R.id.button_Save)
    {
      //Read the command list

      ArrayList<String> content = io.readFile(view, COMMAND_FILE);

      //Change command in array
      String text = textView.getText().toString();
      if(!isNew) content.set(position, text);
      else if(isNew) content.add(text);

      //Write array back to file system
      io.saveFile(view, COMMAND_FILE, content);

      //Close fragment
      if(fragment != null) transaction.commit();    }
    else if(view.getId() == R.id.button_Delete)
    {
      if(!isNew){
        ArrayList<String> content = io.readFile(view, COMMAND_FILE);
        content.remove(position);
        io.saveFile(view, COMMAND_FILE, content);
      }
      if(fragment != null) transaction.commit();
    }
  }

  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
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
