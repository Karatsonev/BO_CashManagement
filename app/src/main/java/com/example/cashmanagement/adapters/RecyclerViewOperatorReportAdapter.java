package com.example.cashmanagement.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.cashmanagement.R;
import com.example.cashmanagement.models.CashFlowModel;


import java.util.List;

public class RecyclerViewOperatorReportAdapter
        extends RecyclerView.Adapter<RecyclerViewOperatorReportAdapter.ItemViewHolder> {

    private List<CashFlowModel> cashFlowModels;

    public RecyclerViewOperatorReportAdapter(List<CashFlowModel> modelList){
        cashFlowModels = modelList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView  = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rec_view_report_item,viewGroup,false);
        return new RecyclerViewOperatorReportAdapter.ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int position   ) {

        CashFlowModel model = cashFlowModels.get(position);
        itemViewHolder.userId.setText(model.fullName);
        itemViewHolder.amount.setText(String.format("%.2f", model.amount));
        itemViewHolder.dateTime.setText(model.dateTimeStart);
        if(model.typeId == 1){
            itemViewHolder.typeId.setText((R.string.cashIn));
        }else {
            itemViewHolder.typeId.setText((R.string.cashOut));
        }
        itemViewHolder.tvNum.setText(String.valueOf(position + 1));
        //itemViewHolder.posCode.setText("Poscode");
    }

    @Override
    public int getItemCount() {
        return cashFlowModels.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView amount;
        TextView dateTime;
        TextView userId;
        TextView typeId;
        TextView tvNum;
        //TextView posCode;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.text_view_amount);
            dateTime = itemView.findViewById(R.id.text_view_date);
            userId = itemView.findViewById(R.id.text_view_userID);
            typeId = itemView.findViewById(R.id.tv_typeId);
            tvNum = itemView.findViewById(R.id.tvNum);
            //posCode = itemView.findViewById(R.id.text_view_posCode);
        }
    }
}
