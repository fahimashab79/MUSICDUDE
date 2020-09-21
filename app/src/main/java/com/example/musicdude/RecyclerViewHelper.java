package com.example.musicdude;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewHelper extends RecyclerView.Adapter<RecyclerViewHelper.ViewHolder> implements Filterable {

    List<MusicModel>musicModels=new ArrayList<>();
    List<MusicModel>musicModelsFull;
    Context context;
    onSongListener onSongListener;

    public RecyclerViewHelper(List<MusicModel> musicModels, Context context, RecyclerViewHelper.onSongListener onSongListener) {
        this.musicModels = musicModels;
        musicModelsFull=new ArrayList<>(musicModels);
        this.context = context;
        this.onSongListener = onSongListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerviewitem,parent,false);

        return new ViewHolder(v,onSongListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        MusicModel musicModel=musicModels.get(position);
        holder.textView1.setText(musicModel.getAudioName());


       }


    @Override
    public int getItemCount() {
        return musicModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textView1;

       onSongListener onSongListener;

        public ViewHolder(@NonNull View itemView,onSongListener onSongListener) {
            super(itemView);
            textView1=(TextView)itemView.findViewById(R.id.songname);
            //albumimage=itemView.findViewById(R.id.albumimage);
            this.onSongListener=onSongListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
          onSongListener.onSongClickListner(getAdapterPosition());
        }
    }
    public interface onSongListener{
        public void onSongClickListner(int position);
    }

    public Filter getFilter(){
        return musicFilter;
    }
    private Filter musicFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<MusicModel>filteredList=new ArrayList<>();
            if(charSequence==null||charSequence.length()==0){
                filteredList.addAll(musicModelsFull);
            }
            else {
                String filterPattern=charSequence.toString().toLowerCase().trim();
                for(MusicModel model:musicModelsFull){
                    if (model.getAudioName().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(model);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            musicModels.clear();
            musicModels.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };
}
