package gps.trackerid.location.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.module.util.Preference;

import java.util.ArrayList;

import gps.trackerid.location.R;
import gps.trackerid.location.ui.LangActivity;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
    ArrayList<String> arrayList = new ArrayList<>();

    Context context;
    int selectedPosition = -1;
    int lastSelectedPosition = -1;
    public static String[] lang;
    Preference preference;

    public LanguageAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        preference = new Preference(context);
        if (LangActivity.getInstance() != null) {
            if (LangActivity.getInstance().mType != null) {
                selectedPosition = preference.getInteger("LANG", 0);
            }
        }
        lang = context.getResources().getStringArray(R.array.language_codes);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_language, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.mTxtName.setText(arrayList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LangActivity.getInstance() != null) {
                    LangActivity.getInstance().setvisibility();
                }
                preference.setInteger("LANG", position);
                preference.saveLanguage(lang[position]);
                lastSelectedPosition = selectedPosition;
                selectedPosition = position;
                notifyItemChanged(lastSelectedPosition);
                notifyItemChanged(selectedPosition);
            }
        });

        if (selectedPosition == position) {
            holder.mIvSelect.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_selected));
            holder.mLlMain.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_lang_selected));
//            holder.mLlMain.setBackgroundTintList(null);
        } else {
            holder.mIvSelect.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_unselected));
            holder.mLlMain.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_lang));
//            holder.mLlMain.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F3F4F6")));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTxtName;
        ImageView mIvSelect;
        LinearLayout mLlMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTxtName = itemView.findViewById(R.id.mTxtName);
            mIvSelect = itemView.findViewById(R.id.mIvSelect);
            mLlMain = itemView.findViewById(R.id.mLlMain);
        }
    }

    public void setLanguage() {
        if (selectedPosition == -1) {
            preference.setInteger("LANG", 0);
            preference.saveLanguage(lang[0]);
        }
    }
}
