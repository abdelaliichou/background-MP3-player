package com.example.ilyastp5;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3background.FavoriteActivity;
import com.example.mp3background.MainActivity;
import com.example.mp3background.Music;
import com.example.mp3background.MusicActivity;
import com.example.mp3background.MusicDBHelper;
import com.example.mp3background.R;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {

    private ArrayList<Music> musics;
    private Context mContext;

    public adapter(Context context, ArrayList<Music> list) {
        mContext = context;
        musics = list;
    }

    @NonNull
    @Override
    public adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter.ViewHolder holder, int position) {
        holder.text.setText(musics.get(position).getName());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, MusicActivity.class).putExtra("text", musics.get(position).getName()).putExtra("id", musics.get(position).getId()));
            }
        });
        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                AlertDialog.Builder alertDialogBuilderLabelDelete = new AlertDialog.Builder(mContext);

                alertDialogBuilderLabelDelete.setTitle("Delete Music");
                alertDialogBuilderLabelDelete.setMessage("Are you sure to delete?");

                alertDialogBuilderLabelDelete.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        MusicDBHelper db = new MusicDBHelper(mContext);
                        Music music = musics.get(position);

                        if (mContext instanceof MainActivity){
                            // means that we should delete the music because we are calling this adapter from the main activity
                            // Deleting the note from the database note
                            db.deleteMusic(music.getId());
                            // refresh
                            musics = db.getAllMusic();
                            notifyDataSetChanged();
                            dialog.dismiss();

                        } else if ( mContext instanceof FavoriteActivity){
                            // means that we should just delete the music from the favorites because we are calling this adapter from the favorite activity
                            music.setFav(false);
                            int result = db.updateMusic(music);

                            if ( result == 0 ) {
                                Toast.makeText(mContext, "Error while removing item from favorite !", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Toast.makeText(mContext, "Item removed from favorite successfully !", Toast.LENGTH_SHORT).show();
                            // refresh
                            musics = db.getAllFavorites();
                            notifyDataSetChanged();
                            dialog.dismiss();                        }
                    }
                });
                alertDialogBuilderLabelDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialogBuilderLabelDelete.show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return musics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        CardView item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            text = itemView.findViewById(R.id.discText);
        }
    }
}