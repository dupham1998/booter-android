package com.myapplication.androidbooster.binding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapplication.androidbooster.R;
import com.myapplication.androidbooster.helper.Helper;
import com.myapplication.androidbooster.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class CleanCacheAdapter extends RecyclerView.Adapter<CleanCacheAdapter.ItemHolder>{
    private List<AppInfo> appInfoList = new ArrayList<>();

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cache_item, parent, false);

        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return appInfoList == null ? 0 : appInfoList.size();
    }

    public void setAppInfoList(List<AppInfo> appInfoList){
        this.appInfoList = appInfoList;
        notifyDataSetChanged();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewAppName;
        private TextView mTextViewSize;
        private ImageView mImageViewIcon;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewAppName = (TextView) itemView.findViewById(R.id.textview_app_name_cache);
            mTextViewSize = (TextView) itemView.findViewById(R.id.textview_size_cache);
            mImageViewIcon = (ImageView) itemView.findViewById(R.id.imageview_icon_cache);
        }

        public void bind (int position){
            AppInfo app = appInfoList.get(position);

            mTextViewAppName.setText(app.getAppName());
            mTextViewSize.setText(Helper.formatSize(app.getCacheSize()));
            mImageViewIcon.setBackground(app.getIcon());
        }
    }
}
