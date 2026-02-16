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
import gps.trackerid.location.models.users.DataUser;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {
    Context context;
    List<DataUser> mList = new ArrayList<>();
    OnClickUserBy onClickUserBy;

    public interface OnClickUserBy {
        void onClickUser(DataUser modelLanguage);
    }

    public FriendListAdapter(Context context, OnClickUserBy clickUserBy) {
        this.context = context;
        this.onClickUserBy = clickUserBy;
    }

    public final void setFriends(List<DataUser> friends) {
        this.mList = friends;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_frdlist, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DataUser dataUser = mList.get(holder.getAbsoluteAdapterPosition());
        String name = dataUser.getName();
        if (name.isEmpty()) {
            name = "Unknown";
        }
        holder.mTxtTitle.setText(name);
        if (dataUser.isSelected()) {
            holder.mTxtadress.setTextColor(ContextCompat.getColor(this.context, R.color.green));
            holder.mTxtadress.setText("Available for tracking");
            holder.mIvSwitch.setImageResource(R.drawable.ic_switch_on);
        } else {
            holder.mTxtadress.setTextColor(ContextCompat.getColor(this.context, R.color.red));
            holder.mTxtadress.setText("Not available for tracking");
            holder.mIvSwitch.setImageResource(R.drawable.ic_switch_off);
        }

        holder.mIvSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataUser.setSelected(!dataUser.isSelected());
                onClickUserBy.onClickUser(dataUser);
                notifyItemChanged(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mIvSwitch;
        TextView mTxtadress, mTxtTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvSwitch = itemView.findViewById(R.id.mIvSwitch);
            mTxtadress = itemView.findViewById(R.id.mTxtadress);
            mTxtTitle = itemView.findViewById(R.id.mTxtTitle);
        }
    }
}
