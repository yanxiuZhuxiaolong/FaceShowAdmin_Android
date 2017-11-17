package com.yanxiu.gphone.faceshowadmin_android.main;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.customView.recyclerView.BaseRecyclerViewAdapter;
import com.yanxiu.gphone.faceshowadmin_android.login.activity.ClassManageActivity;
import com.yanxiu.gphone.faceshowadmin_android.main.bean.CourseArrangeBean;
import com.yanxiu.gphone.faceshowadmin_android.model.UserInfo;
import com.yanxiu.gphone.faceshowadmin_android.utils.EventUpdate;
import com.yanxiu.gphone.faceshowadmin_android.utils.YXPictureManager;

/**
 * adapter for LeftDrawerRecyclerView in MainActivity
 * Created by frc on 17-10-26.
 */

public class LeftDrawerListAdapter extends BaseRecyclerViewAdapter {
    private Context mContext;
    private final int TYPE_HEAD = 0X01;
    private final int TYPE_NORMAL = 0X02;

    private int[] itemIconArray = new int[]{R.drawable.ic_home_black,
            R.drawable.ic_person_black,};
    //            R.drawable.ic_person_black,
//            R.drawable.ic_settings_black, R.drawable.ic_exit_to_app_black};
    private String[] itemNameArray = null;

    private CourseArrangeBean mCourseData;

    public LeftDrawerListAdapter(Context context, CourseArrangeBean courseData) {
        mContext = context;
        mCourseData = courseData;
        itemNameArray = context.getResources().getStringArray(R.array.left_drawer_item_names);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            return new HeadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_drawer_list_head_layout, parent, false));
        } else if (viewType == TYPE_NORMAL) {
            return new NormalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_drawer_list_normal_layout, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEAD) {
            HeadViewHolder headViewHolder = (HeadViewHolder) holder;
            if (UserInfo.getInstance().getInfo() != null && UserInfo.getInstance().getInfo().getAvatar() != null && UserInfo.getInstance().getInfo().getRealName() != null) {
                YXPictureManager.getInstance().showRoundPic(mContext, UserInfo.getInstance().getInfo().getAvatar(), headViewHolder.user_icon, 6, R.drawable.discuss_user_default_icon);
                headViewHolder.user_name.setText(UserInfo.getInstance().getInfo().getRealName());
            }
            try {
                if (mCourseData != null) {
                    headViewHolder.project_name.setText(mCourseData.getProjectInfo().getProjectName());
                    headViewHolder.class_name.setText(mCourseData.getClazsInfo().getClazsName());
                } else {
                    headViewHolder.project_name.setText("暂未设置项目");
                    headViewHolder.class_name.setText("暂未设置班级");
                    headViewHolder.changeClass_button.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            headViewHolder.changeClass_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventUpdate.onChangeClass(mContext);
                    ClassManageActivity.invoke((Activity) mContext);
                }
            });

        } else {
            ((NormalViewHolder) holder).itemName.setText(itemNameArray[position - 1]);
            ((NormalViewHolder) holder).itemIcon.setImageResource(itemIconArray[position - 1]);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewItemClickListener != null) {
                        recyclerViewItemClickListener.onItemClick(v, holder.getAdapterPosition() - 1);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return itemIconArray.length + 1;
    }

    private class HeadViewHolder extends RecyclerView.ViewHolder {

        public ImageView user_icon;
        public TextView user_name;
        public TextView project_name;
        public TextView class_name;
        public TextView changeClass_button;

        HeadViewHolder(View itemView) {
            super(itemView);
            user_icon = itemView.findViewById(R.id.user_icon);
            user_name = itemView.findViewById(R.id.user_name);
            project_name = itemView.findViewById(R.id.project_name);
            class_name = itemView.findViewById(R.id.class_name);
            changeClass_button = itemView.findViewById(R.id.changeClass_button);
        }
    }

    private class NormalViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemIcon;
        private TextView itemName;

        NormalViewHolder(View itemView) {
            super(itemView);
            itemIcon = itemView.findViewById(R.id.list_item_icon);
            itemName = itemView.findViewById(R.id.list_item_name);
        }
    }
}
