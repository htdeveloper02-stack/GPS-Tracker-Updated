package gps.trackerid.location.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gps.trackerid.location.R;
import gps.trackerid.location.models.AreaModel;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder> {
    Context context;
    private ArrayList<AreaModel> mData = new ArrayList<>();
    private ArrayList<AreaModel> mFilteredData = new ArrayList<>();

    public void setAreas(ArrayList<AreaModel> models, String searchstr) {
        this.mData = models;
        this.mFilteredData = new ArrayList<>(mData);
        filter(searchstr);
//        notifyDataSetChanged();
    }

    public void filter(String query) {
        mFilteredData.clear();

        if (query.isEmpty()) {
            mFilteredData.addAll(mData);
        } else {
            String search = query.toLowerCase();
            for (AreaModel map : mData) {
                String name = map.getArea().toLowerCase();
                if (name.contains(search)) {
                    mFilteredData.add(map);
                }
            }
        }
        notifyDataSetChanged();
    }

    public AreaAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_area, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.mTxtCity.setText(mFilteredData.get(position).getArea());
        holder.mTxtCode.setText(mFilteredData.get(position).getCode());

    }

    @Override
    public int getItemCount() {
        return mFilteredData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTxtCity, mTxtCode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTxtCity = itemView.findViewById(R.id.mTxtCity);
            mTxtCode = itemView.findViewById(R.id.mTxtCode);
        }
    }
}
