package hcmus1312635.trungnguyen.notepad_sugarorm;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TrungNguyen on 5/5/2016.
 */
public class NoteAdapter extends ArrayAdapter<Note> {
    private Context context;
    private Note[] arNote;
    private static LayoutInflater inflater = null;

    public NoteAdapter(Context context, int layoutToBeInflated, Note[] _arNote) {
        super(context, layoutToBeInflated, _arNote);
        try {
            this.context = context;
            this.arNote = _arNote;

        } catch (Exception e) {

        }
    }

    public int getCount()
    {
        return arNote.length;
    }

    public Note getItem(Note position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView title;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = ((Activity)context).getLayoutInflater();
        View row = inflater.inflate(R.layout.note_row, null);
        TextView tvTittle = (TextView)row.findViewById(R.id.tvtitle);
        tvTittle.setText(arNote[position].title);
        return row;
    }
}
