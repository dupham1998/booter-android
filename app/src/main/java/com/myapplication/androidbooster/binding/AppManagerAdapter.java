package com.myapplication.androidbooster.binding;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.myapplication.androidbooster.R;
import com.myapplication.androidbooster.helper.Helper;
import com.myapplication.androidbooster.model.AppInfo;
import java.util.ArrayList;
import java.util.List;

public class AppManagerAdapter  extends RecyclerView.Adapter<AppManagerAdapter.ItemHolder>{

    private List<AppInfo> appInfoList = new ArrayList<>();
    private SparseBooleanArray itemStateArrary = new SparseBooleanArray();
    private Context mContext;
    private OnItemClickListener listener;

    public AppManagerAdapter (Context context){
        this.mContext = context;
    }

    public AppManagerAdapter (){

    }

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
        return appInfoList == null ? 0 : appInfoList.size();
    }

    public void setAppInfoList(List<AppInfo> appInfoList){
        this.appInfoList = appInfoList;
        itemStateArrary = new SparseBooleanArray();
        notifyDataSetChanged();
    }
    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextViewAppName;
        private TextView mTextViewSize;
        private ImageView mImageViewIcon;
        private CheckBox mCheckBoxUninstall;
        private View itemvView;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            this.itemvView = itemView;
            mTextViewAppName = (TextView) itemView.findViewById(R.id.textview_app_name);
            mTextViewSize = (TextView) itemView.findViewById(R.id.textview_size);
            mImageViewIcon = (ImageView) itemView.findViewById(R.id.imageview_icon);
            mCheckBoxUninstall = (CheckBox) itemView.findViewById(R.id.checkbox_uninstall);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.equals(itemvView)){
                listener.onItemClick(appInfoList.get(getAdapterPosition()));
            }
        }

        public  void bind(int position){
            AppInfo app = appInfoList.get(position);

            PackageManager pm = mContext.getPackageManager();

            mTextViewAppName.setText(app.getAppName());
            mTextViewSize.setText(Helper.formatSize(app.getAppSize() + app.getCacheSize() + app.getDataSize()));
            mImageViewIcon.setBackground(app.getIcon());
            mCheckBoxUninstall.setChecked(itemStateArrary.get(position));
            mCheckBoxUninstall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = getAdapterPosition();
                    if(itemStateArrary.get(pos, false) ^ isChecked){
                        if(itemStateArrary.get(pos, false)){
                            mCheckBoxUninstall.setChecked(false);
                            itemStateArrary.put(pos, false);
                            listener.unCheckBoxItem(appInfoList.get(pos));
                        }
                        else {
                            mCheckBoxUninstall.setChecked(true);
                            itemStateArrary.put(pos, true);
                            listener.checkBoxItem(appInfoList.get(pos));
                        }
                    }
                }
            });
        }
    }

    public interface  OnItemClickListener{
        public void checkBoxItem(AppInfo app);
        public void unCheckBoxItem(AppInfo app);
        public void onItemClick(AppInfo app);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
