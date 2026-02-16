package gps.trackerid.location.adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gps.trackerid.location.R;
import gps.trackerid.location.models.users.DataUser;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    Context context;
    OnClickByUser onClickByUser;
    OnClickByAddUser onClickByAddUser;
    private List<DataUser> mList;


    public interface OnClickByAddUser {
        void OnClickAdd(DataUser modelLanguage);
    }

    public interface OnClickByUser {
        void OnClickBy(DataUser modelLanguage);
    }

    public void setUser(List<DataUser> dataUsers) {
        this.mList = dataUsers;
        notifyDataSetChanged();
    }

    public UserListAdapter(Context context, OnClickByUser mOnclick, OnClickByAddUser mOnAdd) {
        this.context = context;
        this.onClickByUser = mOnclick;
        this.onClickByAddUser = mOnAdd;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_userlist, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataUser dataUser = mList.get(position);
        if (dataUser == null) {
            holder.ivAddUser.setVisibility(VISIBLE);
            holder.layoutUser.setVisibility(GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickByAddUser.OnClickAdd(mList.get(position));
                }
            });
        } else {
            holder.ivAddUser.setVisibility(GONE);
            holder.layoutUser.setVisibility(VISIBLE);
            String name = dataUser.getName();
            if (name.isEmpty()) {
                name = "Unknown";
            }
            holder.tvTitle.setText(name);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickByUser.OnClickBy(mList.get(position));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAddUser, ivIcon;
        LinearLayout layoutUser;
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAddUser = itemView.findViewById(R.id.ivAddUser);
            layoutUser = itemView.findViewById(R.id.layoutUser);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
