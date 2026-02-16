package gps.trackerid.location.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gps.trackerid.location.R;
import gps.trackerid.location.models.ItemStopwatch;
import gps.trackerid.location.utils.StopWatchUtils;

public class StopwatchAdapter extends RecyclerView.Adapter<StopwatchAdapter.Holder> {
    private ArrayList<ItemStopwatch> arr;
    Context context;

    public StopwatchAdapter(Context con, ArrayList<ItemStopwatch> arrayList) {
        context = con;
        this.arr = arrayList;
    }

    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new Holder(this, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_stopwatch, viewGroup, false));
    }

    public void onBindViewHolder(Holder holder, int i) {
        ArrayList<ItemStopwatch> arrayList = this.arr;
        ItemStopwatch itemStopwatch = arrayList.get((arrayList.size() - 1) - i);
        TextView textView = holder.tvName;
        textView.setText(context.getResources().getText(R.string.lap)+" "+((this.arr.size() - 1) - i));
        holder.tvTime.setText(StopWatchUtils.getStopwatch(itemStopwatch.getTime()));
    }

    public int getItemCount() {
        return this.arr.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvTime;
        public TextView tvGapTime;

        Holder(StopwatchAdapter thiss, View view) {
            super(view);
            this.tvName = view.findViewById(R.id.mTxtName);
            this.tvTime = view.findViewById(R.id.mTxtTime);
            this.tvGapTime = view.findViewById(R.id.mTxtGapTime);
        }
    }
}