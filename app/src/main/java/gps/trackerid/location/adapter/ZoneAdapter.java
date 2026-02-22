package gps.trackerid.location.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import gps.trackerid.location.R;
import gps.trackerid.location.models.zonedata.DataZone;

public class ZoneAdapter extends RecyclerView.Adapter<ZoneAdapter.ViewHolder> {
    Context context;
    private List<DataZone> mData=new ArrayList<>();
    OnZoneClick onZoneClick;
    OnDeleteClick onDeleteClick;

    public void setZone(List<DataZone> zones) {
        this.mData = zones;
        notifyDataSetChanged();
    }

    public interface OnZoneClick {
        void OnZonekBy(DataZone dataZone);
    }

    public interface OnDeleteClick {
        void OnDeleteBy(DataZone data);
    }

    public ZoneAdapter(Context context, OnZoneClick onzone, OnDeleteClick onDelele) {
        this.context = context;
        this.onZoneClick = onzone;
        this.onDeleteClick = onDelele;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_zone, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.mTxtTitle.setText(mData.get(position).getName());
        holder.mTxtadress.setText(mData.get(position).getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onZoneClick.OnZonekBy(mData.get(position));
            }
        });
        holder.mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClick.OnDeleteBy(mData.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mIvDelete;
        TextView mTxtTitle, mTxtadress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTxtTitle = itemView.findViewById(R.id.mTxtTitle);
            mTxtadress = itemView.findViewById(R.id.mTxtadress);
            mIvDelete = itemView.findViewById(R.id.mIvDelete);
        }
    }
}
