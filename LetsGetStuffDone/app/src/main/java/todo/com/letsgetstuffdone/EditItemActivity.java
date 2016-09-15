    package todo.com.letsgetstuffdone;

    import android.content.Intent;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.EditText;
    import android.widget.Toast;

    public class EditItemActivity extends AppCompatActivity {

        private String editTODOItem;
        private EditText editTODOTxt;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit_item);
            editTODOItem = getIntent().getStringExtra("editTODOItem");
            ((EditText)findViewById(R.id.editTODOTxt)).setText(editTODOItem);

        }

        public void updateTODOs(View view) {
            editTODOTxt = (EditText)findViewById(R.id.editTODOTxt);
            if (editTODOTxt.getText().toString().equals("")){
                Toast.makeText(this, "Please enter updated TODO", Toast.LENGTH_LONG).show();
            } else {
                    Intent intent = new Intent();
                    intent.putExtra("updatedTODO", editTODOTxt.getText().toString());
                    intent.putExtra("return_code", 200);
                    intent.putExtra("position", getIntent().getIntExtra("position", 0));
                    setResult(200, intent);
                    finish();
                 }
        }
    }
