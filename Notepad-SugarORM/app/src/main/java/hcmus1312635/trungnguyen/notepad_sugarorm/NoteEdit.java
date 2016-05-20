package hcmus1312635.trungnguyen.notepad_sugarorm;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class NoteEdit extends Activity {
    private EditText etTitle;
    private EditText etBody;
    private Long id;
    private Button btnConfirm;
    private Spinner spnCategory;
    private ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        MainActivity.category = MainActivity.categoryLife;

        etTitle = (EditText) findViewById(R.id.title);
        etBody = (EditText) findViewById(R.id.body);
        btnConfirm = (Button) findViewById(R.id.confirm);
        spnCategory = (Spinner)findViewById(R.id.category);
        adapter = ArrayAdapter.createFromResource(this,R.array.category,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter);
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String temp = parent.getItemAtPosition(position).toString();
                if (temp.equals("Life"))
                    MainActivity.category = MainActivity.categoryLife;
                else if (temp.equals("Study"))
                    MainActivity.category = MainActivity.categoryStudy;
                else if (temp.equals("Love"))
                    MainActivity.category = MainActivity.categoryLove;
                else
                    MainActivity.category = MainActivity.categoryUnclassified;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                MainActivity.category = MainActivity.categoryLife;
            }
        });

        id = savedInstanceState != null ? savedInstanceState.getLong("id") : null;
        if (id == null) {
            Bundle extras = getIntent().getExtras();
            id = extras != null ? extras.getLong("id") : null;
        }
        populateFields();
        btnConfirm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void populateFields() {
        if (id != null) {
            Note note = Note.listAll(Note.class).get(Integer.parseInt(id.toString()));
            etTitle.setText(note.title);
            etBody.setText(note.body);
            String cate = note.category.name;
            spnCategory.setSelection(((ArrayAdapter<CharSequence>)spnCategory.getAdapter()).getPosition(cate));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("id", id);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void saveState() {
        String title = etTitle.getText().toString();
        String body = etBody.getText().toString();

        if (id == null) {   //Create new note
            createNote(title, body);
        } else {            //Edit note
            Note note = Note.listAll(Note.class).get(Integer.parseInt(id.toString()));
            if (title.equals(""))
                note.title = "No title";
            else
                note.title = title;
            note.body = body;
            note.category =  Category.find(Category.class, "name = ?", MainActivity.category.name).get(0);;
            note.save();
        }
    }

    private void createNote(String title, String body) {
        if (title.equals(""))
            title = "No title";
        Note note = new Note(title, body, MainActivity.category);
        note.save();
    }
}
