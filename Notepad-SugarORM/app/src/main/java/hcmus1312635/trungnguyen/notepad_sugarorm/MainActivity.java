package hcmus1312635.trungnguyen.notepad_sugarorm;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    static Category categoryAll, categoryLife, categoryStudy, categoryLove, categoryUnclassified;
    static Category category;
    Spinner MainCategory;
    ArrayAdapter<CharSequence> adapter;
    String currentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoryAll = new Category("All");
        categoryLife = new Category("Life"); categoryLife.save();
        categoryStudy = new Category("Study"); categoryStudy.save();
        categoryLove = new Category("Love"); categoryLove.save();
        categoryUnclassified = new Category("Unclassified"); categoryUnclassified.save();

        currentCategory = "All";

        MainCategory = (Spinner)findViewById(R.id.category);
        adapter = ArrayAdapter.createFromResource(this,R.array.Maincategory,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MainCategory.setAdapter(adapter);
        MainCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentCategory = parent.getItemAtPosition(position).toString();
                fillData(currentCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fillData("All");
            }
        });

        fillData(currentCategory);
        registerForContextMenu(getListView());
    }

    private void fillData(String category) {
        List<Note> notes;

        if (!category.equals("All")) {
            Category temp = Category.find(Category.class, "name = ?", category).get(0);
            notes = temp.getNotes();
        }
        else
            notes = Note.listAll(Note.class);

        Note[] arr = new Note[notes.size()];
        for (int i = 0; i < notes.size(); i++)
            arr[i] = notes.get(i);

        NoteAdapter adapter = new NoteAdapter(this, R.layout.note_row, arr);
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, INSERT_ID, 0, "Add Note");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case INSERT_ID:
                createNote();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private void createNote() {
        Intent i = new Intent(this, NoteEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, NoteEdit.class);
        i.putExtra("id", id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Note deletedNote = Note.listAll(Note.class).get((int) info.id);
                deletedNote.delete();
                fillData(currentCategory);
                return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData(currentCategory);
    }
}
