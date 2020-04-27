package com.pato.notekeeper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder> {


    private final Context mContext;
    private final List<NoteInfo> mNotes;
    private final LayoutInflater mLayoutInflater;  //used to inflate views from the layout resource file.

    // constructor
    public NoteRecyclerAdapter(Context context, List<NoteInfo> notes) {
        mContext = context;
        mNotes = notes;

        // To create views from layout resource we have to inflate layout => use LayoutInflater.
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate views from layout resource and store their references in ViewHolder.
        View itemView = mLayoutInflater.inflate(R.layout.item_note_list, parent, false);  // attach to parent = false.

        return new ViewHolder(itemView);  // create view item and store its reference in ViewHolder.
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //ViewHolder contains references to views that will display data.
        NoteInfo note = mNotes.get(position);  //get note at position.
        holder.mTextCourse.setText(note.getCourse().getTitle());
        holder.mTextTitle.setText(note.getTitle());
        holder.mCurrentPosition = position; // index of selected note.

    }

    @Override
    public int getItemCount() {
        // return total number of items to display.
        return mNotes.size();
    }

    // ViewHolder holds references to View Items that are displayed in the RecyclerView.
    public class ViewHolder extends RecyclerView.ViewHolder {
        public int mCurrentPosition;  //holds index of selected note. must be set when binding data.
        public final TextView mTextCourse;
        public final TextView mTextTitle;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // references to the contained views obtained from item_note_list.xml resource.
            mTextCourse = (TextView) itemView.findViewById(R.id.text_course);
            mTextTitle = (TextView) itemView.findViewById(R.id.text_title);

            //set onClickListener for the top-level view item.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // show note selected at specified index.
                    Intent intent = new Intent(mContext, NoteActivity.class);
                    intent.putExtra(NoteActivity.NOTE_POSITION, mCurrentPosition); //send position of selected note.
                    mContext.startActivity(intent);

                }
            });

        }
    }

}
