package hcmus1312635.trungnguyen.notepad_sugarorm;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.List;

/**
 * Created by TrungNguyen on 5/20/2016.
 */
@Table
public class Category extends SugarRecord {
    private Long id;
    public Long getId() {
        return id;
    }

    public String name;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    List<Note> getNotes() {
        return Note.find(Note.class, "category = ?", this.getId().toString());
    }
}