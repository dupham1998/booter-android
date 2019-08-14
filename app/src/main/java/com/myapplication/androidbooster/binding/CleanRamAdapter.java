package com.myapplication.androidbooster.binding;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapplication.androidbooster.R;
import com.myapplication.androidbooster.helper.Helper;
import com.myapplication.androidbooster.model.AppRunningInfo;

import java.util.ArrayList;
import java.util.List;

public class CleanRamAdapter extends RecyclerView.Adapter<CleanRamAdapter.ItemHolder>{

    private List<AppRunningInfo> apps = new ArrayList<>();

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_item, parent, false);

        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return apps ==  null ? 0 : apps.size();
    }

    public void setApps(List<AppRunningInfo> result) {
        this.apps = result;
        notifyDataSetChanged();
    }


    public class ItemHolder extends RecyclerView.ViewHolder{

        private TextView mTextViewAppName;
        private TextView mTextViewSize;
        private CheckBox mCheckBox;
        private ImageView mImageView;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            mTextViewAppName = (TextView) itemView.findViewById(R.id.textview_app_name);
            mTextViewSize = (TextView) itemView.findViewById(R.id.textview_size);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox_uninstall);
            mImageView = (ImageView) itemView.findViewById(R.id.imageview_icon);
        }

        public void bind(int position){
            AppRunningInfo app = apps.get(position);

            mTextViewAppName.setText(app.getAppName());
            mTextViewSize.setText(Helper.formatSizeRam(app.getRamSize()));
            mImageView.setBackground(app.getIcon());
        }
    }
}
