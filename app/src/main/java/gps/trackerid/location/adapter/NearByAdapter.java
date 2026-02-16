package gps.trackerid.location.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import gps.trackerid.location.R;
import gps.trackerid.location.models.NearbyItem;

public class NearByAdapter extends RecyclerView.Adapter<NearByAdapter.ViewHolder> {
    Context context;
    OnClickByNearBy onClickNearBy;
    List<NearbyItem> mList = new ArrayList<>();

    public interface OnClickByNearBy {
        void OnClickBy(NearbyItem modelLanguage);
    }

    public NearByAdapter(Context context, List<NearbyItem> list, OnClickByNearBy mOnclick) {
        this.context = context;
        this.onClickNearBy = mOnclick;
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_nearby, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.mIvIcon.setImageDrawable(ContextCompat.getDrawable(context, mList.get(position).getIconRes()));
        holder.mTxtName.setText(mList.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNearBy.OnClickBy(mList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mIvIcon;
        TextView mTxtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvIcon = itemView.findViewById(R.id.mIvIcon);
            mTxtName = itemView.findViewById(R.id.mTxtName);
        }
    }
}
