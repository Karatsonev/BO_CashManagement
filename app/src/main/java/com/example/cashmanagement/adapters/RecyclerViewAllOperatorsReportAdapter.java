package com.example.cashmanagement.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.cashmanagement.R;
import com.example.cashmanagement.models.UserModel;

import java.util.List;

public class RecyclerViewAllOperatorsReportAdapter extends
        RecyclerView.Adapter<RecyclerViewAllOperatorsReportAdapter.ItemViewHolder> {

    private List<UserModel> userModelList;
    private OnItemClickListener mListener;
    private Context context;

    public RecyclerViewAllOperatorsReportAdapter(Context context, List<UserModel> _userModelList, OnItemClickListener onItemClickListener){
        userModelList = _userModelList;
        this.mListener = onItemClickListener;
        this.context = context;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView  = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rec_view_user_report_item,viewGroup,false);
        return new RecyclerViewAllOperatorsReportAdapter.ItemViewHolder(itemView,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int position) {
        UserModel model = userModelList.get(itemViewHolder.getAdapterPosition());
        itemViewHolder.userId.setText(String.valueOf(model.userId));
        //itemViewHolder.loginName.setText(model.loginName);
        itemViewHolder.expired.setText(String.valueOf(model.expired));
        itemViewHolder.enabled.setText(String.valueOf(model.enabled));
        itemViewHolder.dateLastEdit.setText(String.valueOf(model.lastEdit));
        itemViewHolder.fullName.setText(model.fullName);
        itemViewHolder.dateCreated.setText(String.valueOf(model.posCode));
        itemViewHolder.iv_edit_user.setImageResource(R.drawable.ic_edit_red);

        switch(model.typeId){
            case 1:
                itemViewHolder.typeId.setText(context.getString(R.string.administrator));
                break;
            case 2:
                itemViewHolder.typeId.setText(context.getString(R.string.manager_rec_view));
                break;
            case 3:
                itemViewHolder.typeId.setText(context.getString(R.string.cashier));
                break;
            case 4:
                itemViewHolder.typeId.setText(context.getString(R.string.main_cashier));
                break;
            case 5:
                itemViewHolder.typeId.setText(context.getString(R.string.inkaso));
                break;
        }
        
        itemViewHolder.setModelItem(model);

    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView userId;
        TextView loginName;
        TextView fullName;
        TextView expiration;
        TextView expired;
        TextView validity;
        TextView dateLastEdit;
        TextView dateCreated;
        TextView email;
        TextView phone;
        TextView egn;
        TextView typeId;
        TextView card;
        TextView enabled;
        TextView posCode;
        ImageView iv_edit_user;
        UserModel model;


        public ItemViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            userId = itemView.findViewById(R.id.userId_user_report);
            //loginName = itemView.findViewById(R.id.loginName_user_report);
            expired = itemView.findViewById(R.id.expired_user_report);
            dateLastEdit = itemView.findViewById(R.id.lastEdit_user_report);
            dateCreated = itemView.findViewById(R.id.posCode_user_report);
            enabled = itemView.findViewById(R.id.enabled_user_report);
            iv_edit_user = itemView.findViewById(R.id.iv_edit_user_report);
            fullName = itemView.findViewById(R.id.fullName_user_report);
            typeId = itemView.findViewById(R.id.typeId_user_report);
            iv_edit_user.setOnClickListener(v -> listener.onEditClick(model,iv_edit_user, getAdapterPosition()));

        }

        public void setModelItem(UserModel model){
            this.model = model;
        }
    }
    public interface OnItemClickListener{
        void onEditClick(UserModel model, ImageView imageView, int position);
        void onDetailsClick();
    }
}
