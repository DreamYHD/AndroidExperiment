package com.example.guohouxiao.accountmanagedemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.guohouxiao.accountmanagedemo.R;
import com.example.guohouxiao.accountmanagedemo.bean.User;

import java.util.List;

/**
 * Created by guohouxiao on 2017/11/19.
 * User适配器
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUserList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View userView;

        TextView tv_username;
        TextView tv_email;
        TextView tv_sex;
        TextView tv_hobby;
        TextView tv_birthday;

        public ViewHolder(View itemView) {
            super(itemView);

            userView = itemView;
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_sex = itemView.findViewById(R.id.tv_sex);
            tv_hobby = itemView.findViewById(R.id.tv_hobby);
            tv_birthday = itemView.findViewById(R.id.tv_birthday);
        }
    }

    public UserAdapter(Context context, List<User> userList) {
        mContext = context;
        mUserList = userList;
    }


    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {
        User user = mUserList.get(position);
        holder.tv_username.setText(user.username);
        holder.tv_email.setText(user.email);
        holder.tv_sex.setText(user.sex);
        holder.tv_hobby.setText(user.hobby);
        holder.tv_birthday.setText(user.birthday);
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }
}
