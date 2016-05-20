package hcmus1312635.trungnguyen.notepad_sugarorm;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

/**
 * Created by TrungNguyen on 5/5/2016.
 */
@Table
public class Note extends SugarRecord {
    private Long id;
    public Long getId() {
        return id;
    }

    public String title;
    public String body;
    public Category category;

    public Note() {
    }

    public Note(String title, String body, Category category) {
        this.title = title;
        this.body = body;
        this.category = Category.find(Category.class, "name = ?", category.name).get(0);
    }
}
