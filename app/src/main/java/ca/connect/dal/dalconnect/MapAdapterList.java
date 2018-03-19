package ca.connect.dal.dalconnect;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zaher on 2018-03-18.
 */

public class MapAdapterList extends RecyclerView.Adapter<MapAdapterList.MapAdapterHV> {

    ArrayList<String> mapList;
    Context context;
    RVClickListener listener;

    public MapAdapterList(ArrayList<String> mapList, Context context, RVClickListener listener) {
        this.mapList = mapList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MapAdapterHV onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.map_item_view, parent, false);
        return new MapAdapterHV(itemView, listener);
    }

    @Override
    public void onBindViewHolder(MapAdapterHV holder, int position) {
        holder.bindData(mapList.get(position));
    }

    @Override
    public int getItemCount() {
        return mapList.size();
    }

    class MapAdapterHV extends RecyclerView.ViewHolder {
        public TextView textView;
        public Button button;
        RVClickListener listener;

        public MapAdapterHV(View itemView, RVClickListener listener) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_build_name);
            button = itemView.findViewById(R.id.btn_map_direction);
            this.listener = listener;
        }

        public void bindData(String building) {
            textView.setText(building);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.recyclerViewListClicked(getLayoutPosition());
                }
            });
        }
    }

    public interface RVClickListener {

        void recyclerViewListClicked(int position);

        ;
    }
}
