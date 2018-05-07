package ryley.sshcom;

import android.content.Context;
import android.view.View;

import com.google.common.base.Joiner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

/*
LocalIO has methods for saving an array of commands to memory and retreiving them from memory.
 */
public class LocalIO {

  //Savefile takes file name and array and writes to memory
  void saveFile (View view, String _fileName, ArrayList<String> _file)
  {
    FileOutputStream outputStream;

    String content = Joiner.on("\t").join(_file);


    try{
      outputStream = view.getContext().openFileOutput(_fileName, Context.MODE_PRIVATE);
      outputStream.write(content.getBytes());
      outputStream.close();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  //readfile reads filename from internal memory and writes it to an arrayList<string>
  ArrayList<String> readFile (View view, String _fileName)
  {
    //Read the file
    ArrayList<String> content = new ArrayList<String>();
    try {
      FileInputStream fis = view.getContext().openFileInput(_fileName);
      Scanner scanner = new Scanner(fis);
      scanner.useDelimiter("\t");

      while(scanner.hasNext())
      {
        content.add(scanner.next());
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return content;
  }

}
