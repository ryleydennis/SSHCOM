package ryley.sshcom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.common.base.Joiner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

//Commands are listed here for editing and creation purposes
public class CommandPage extends Fragment implements View.OnClickListener {

  private View  view;
  final String COMMAND_FILE = "CommandList";
  ArrayAdapter<String> arrayAdapter;
  ArrayList<String> commandList = new ArrayList<String>();
  private String selectedCommand;
  private int selectedCommandPosition = -1;
  private OnFragmentInteractionListener mListener;
  LocalIO io = new LocalIO();

  public CommandPage() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   * @return A new instance of fragment CommandPage.
   */
  public static CommandPage newInstance() {
    CommandPage fragment = new CommandPage();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  //Itializes buttons onclick listeners with passed view
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_command_page, container, false);

    Button editButton = (Button) view.findViewById(R.id.button_edit);
    Button newButton = (Button) view.findViewById(R.id.button_new);
    editButton.setOnClickListener(this);
    newButton.setOnClickListener(this);

    return view;
  }

  //Displays list of commmands
  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    ListView lv = (ListView) view.findViewById(R.id.listView_commandList);
    arrayAdapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1, commandList);

    lv.setAdapter(arrayAdapter);

    lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      // argument position gives the index of item which is clicked
      public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
      {
        selectedCommand=commandList.get(position);
        selectedCommandPosition = position;
        Toast.makeText(getContext(), "Command Selected : "+selectedCommand,   Toast.LENGTH_SHORT).show();
      }
    });
  }

  //list is refreshed from memory when fragment is resumed
  @Override
  public void onResume()
  {
    super.onResume();

    commandList.clear();
    commandList = io.readFile(view, COMMAND_FILE);

    arrayAdapter.clear();
    arrayAdapter.addAll(commandList);
    arrayAdapter.notifyDataSetChanged();
  }

  //Command information of selected command is brought to new CommandEditor when edit pushed
  //Else an empty command is sent to CommandEditor fragment when new pushed
  @Override
  public void onClick(View view) {
    Fragment fragment = null;
    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

    if(view.getId()==R.id.button_edit)
    {
      if(selectedCommandPosition >= 0) {
        fragment = new CommandEditor();
        transaction.replace(R.id.content, fragment);

        Bundle b = new Bundle();
        b.putString("COMMAND", commandList.get(selectedCommandPosition));
        b.putString("POSITION", Integer.toString(selectedCommandPosition));
        b.putBoolean("ISNEW", false);
        fragment.setArguments(b);
      }
    }
    else if(view.getId() == R.id.button_new)
    {
      fragment = new CommandEditor();
      transaction.replace(R.id.content, fragment);

      Bundle b = new Bundle();
      b.putString("COMMAND", "");
      b.putString("POSITION", Integer.toString(commandList.size()));
      b.putBoolean("ISNEW", true);
      fragment.setArguments(b);
    }
    if(fragment != null) transaction.commit();
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
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }
}
