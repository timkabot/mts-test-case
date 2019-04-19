package com.example.mtstestcase;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ListOfStreamAdapter extends RecyclerView.Adapter<ListOfStreamAdapter.ViewHolder> {
    private List<StreamInfo> streamInfos;
    private static ClickListener clickListener;

    ListOfStreamAdapter(List<StreamInfo> streamInfos){
        this.streamInfos = streamInfos;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stream_info_view, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.content.setText(streamInfos.get(i).getContent());
        viewHolder.duration.setText(streamInfos.get(i).getDuration());
        viewHolder.resolution.setText(streamInfos.get(i).getResolution());
    }

    @Override
    public int getItemCount() {
        return streamInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView content;
        TextView duration;
        TextView resolution;


        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            cv = itemView.findViewById(R.id.cv);
            duration =  itemView.findViewById(R.id.duration);
            resolution =  itemView.findViewById(R.id.resolution);
            content = itemView.findViewById(R.id.content);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(streamInfos.get(getAdapterPosition()).getLink());
        }

    }
    void setOnItemClickListener(ClickListener clickListener) {
        ListOfStreamAdapter.clickListener = clickListener;
    }
    public interface ClickListener {
        void onItemClick(String url);
    }

}
