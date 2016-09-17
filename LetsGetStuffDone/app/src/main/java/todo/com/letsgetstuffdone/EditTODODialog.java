package todo.com.letsgetstuffdone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by bpillai on 9/15/2016.
 */
public class EditTODODialog extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText editText;
    private String oldText;
    private String newText;

    public EditTODODialog() {

    }

    public static EditTODODialog newInstance(String title) {
        EditTODODialog editTODODialog = new EditTODODialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        editTODODialog.setArguments(args);
        return editTODODialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.edit_todo_fragment, container);
        editText = (EditText) view.findViewById(R.id.txt_todo_to_edit);
        oldText = editText.getText().toString();
        editText.setOnEditorActionListener(this);
        getDialog().setTitle("Edit TODO");
        editText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_todo_fragment, container);
        editText = (EditText) view.findViewById(R.id.txt_todo_to_edit);
        getArguments().get("position");
        editText.setText(getArguments().get("editTODO").toString());
        editText.setOnEditorActionListener(this);
        getDialog().setTitle("Edit TODO");
        editText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text back to activity through the implemented listener
            EditTODOListner listener = (EditTODOListner) getActivity();

            newText = editText.getText().toString();
            if (editText.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Can't be empty", Toast.LENGTH_SHORT).show();
                editText.requestFocus();
                getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            } else if (oldText.toLowerCase().equals(newText.toLowerCase())) {
                Toast.makeText(getContext(), "Nothing changed", Toast.LENGTH_SHORT).show();
                editText.requestFocus();
                getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            } else {
                listener.onFinishEditTodo(editText.getText().toString(), (Integer) getArguments().get("position"));
                // Close the dialog and return back to the parent activity
                dismiss();
                return true;
            }
        }
        return false;
    }

    public interface EditTODOListner {
        void onFinishEditTodo(String editTODO, int position);
    }
}
