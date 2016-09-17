package todo.com.letsgetstuffdone;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DoIt extends FragmentActivity implements EditTODODialog.EditTODOListner {
    private final int REQUEST_CODE = 200;
    EditText editText;
    ListView list_todos;
    ArrayList<String> todoItems;
    ArrayAdapter<String> todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_it);
        todoItems = new ArrayList<String>();
        try {
            readTodoToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        list_todos = (ListView)findViewById(R.id.list_todos);
        todoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
        list_todos.setAdapter(todoAdapter);
        // Below code will delete the TODO on long click
        list_todos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(DoIt.this);
                builder1.setTitle("Confirm");
                builder1.setMessage("Are you sure you want to delete?");
                builder1.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        todoItems.remove(position);
                        todoAdapter.notifyDataSetChanged();
                        Toast.makeText(DoIt.this, "TODO removed", Toast.LENGTH_SHORT).show();
                        try {
                            writeTodoToFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                builder1.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       // Toast.makeText(DoIt.this, "TODO")
                    }
                });
                AlertDialog alertDialog = builder1.create();
                alertDialog.show();

                return true;
            }
        });
    // Below code will open a new window with selected item in the TODO List
        list_todos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // launchEditScreen(todoItems.get(position).toString(), position);
                Bundle arg = new Bundle();
                arg.putInt("position", position);
                arg.putString("editTODO", todoItems.get(position).toString());
                FragmentManager fm = getSupportFragmentManager();
                EditTODODialog editTODODialog = EditTODODialog.newInstance("Edit TODO");
                editTODODialog.setArguments(arg);
                editTODODialog.show(fm, "Edit_TODO");
            }
        });
    }



    private void launchEditScreen (String todoItem, int position) {

        Intent intent = new Intent(this, EditItemActivity.class);
        intent.putExtra("editTODOItem", todoItem);
        intent.putExtra("position", position);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
        String updatedTODO = data.getExtras().getString("updatedTODO");
        int position = data.getExtras().getInt("position");
        todoItems.remove(position);
        todoItems.add(position, updatedTODO);
        todoAdapter.notifyDataSetChanged();
        Toast.makeText(this,"TODO list updated", Toast.LENGTH_LONG ).show();
        try {
            writeTodoToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    }

    // Event called when addToDo button is submitted
    public void addTODOtoList(View view) {
        editText = (EditText)findViewById(R.id.editText);
        if (!editText.toString().equals("")) {
            todoAdapter.add(editText.getText().toString());
            editText.setText("");
        }
        try {
            writeTodoToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeTodoToFile () throws IOException {
        File file_TODOStuffFile = new File(getFilesDir(),"TODOStuff.txt");
        FileUtils.writeLines(file_TODOStuffFile, todoItems);
   }

    private void readTodoToFile () throws IOException {
        File file_TODOStuffFile = new File(getFilesDir(),"TODOStuff.txt");
        todoItems = new ArrayList<String>(FileUtils.readLines(file_TODOStuffFile));
    }

    @Override
    public void onFinishEditTodo(String editTODO, int position) {
        todoItems.remove(position);
        todoItems.add(position, editTODO);
        todoAdapter.notifyDataSetChanged();
        try {
            writeTodoToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "TODO updated.", Toast.LENGTH_SHORT).show();
    }
}
