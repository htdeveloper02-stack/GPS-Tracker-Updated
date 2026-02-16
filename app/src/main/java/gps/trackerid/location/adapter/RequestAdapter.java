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
import gps.trackerid.location.models.users.DataUser;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    Context context;
    List<DataUser> mList = new ArrayList<>();
    OnClickUserBy onClickUserBy;

    public interface OnClickUserBy {
        void onClickAdd(DataUser modelLanguage);

        void onClickCancel(DataUser modelLanguage);
    }

    public RequestAdapter(Context context, OnClickUserBy clickUserBy) {
        this.context = context;
        this.onClickUserBy = clickUserBy;
    }

    public final void setRequests(List<DataUser> friends) {
        this.mList = friends;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_request, parent, false);

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
        holder.mTxtadress.setText(dataUser.getCode());
        holder.mIvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUserBy.onClickAdd(dataUser);
            }
        });
        holder.mIvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUserBy.onClickCancel(dataUser);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTxtadress, mTxtTitle;
        TextView mIvAdd, mIvCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTxtadress = itemView.findViewById(R.id.mTxtadress);
            mTxtTitle = itemView.findViewById(R.id.mTxtTitle);
            mIvAdd = itemView.findViewById(R.id.mIvAdd);
            mIvCancel = itemView.findViewById(R.id.mIvCancel);
        }
    }
}
