package com.example.cashmanagement.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.cashmanagement.R;
import com.example.cashmanagement.models.CashFlowModel;
import com.example.cashmanagement.utils.FormatUtils;
import java.util.List;

public class RecyclerViewTransactionAdapter extends RecyclerView.Adapter<RecyclerViewTransactionAdapter.RecyclerViewHolder> {

    private List<CashFlowModel> cashFlowModels;
    private OnItemClickListener mListener;

    public RecyclerViewTransactionAdapter(List<CashFlowModel> cashFlowModels,OnItemClickListener listener) {
        this.cashFlowModels = cashFlowModels;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView  = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_transaction_history_item,viewGroup,false);
        return new RecyclerViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int position) {
        CashFlowModel model = cashFlowModels.get(position);
        if(model.typeId == 1){
            recyclerViewHolder.text_view_cashFlowType.setText((R.string.cashIn));
        }else {
            recyclerViewHolder.text_view_cashFlowType.setText((R.string.cashOut));
        }
        recyclerViewHolder.text_view_date.setText(model.dateTimeEnd);
        recyclerViewHolder.text_view_amount.setText((FormatUtils.formatDouble(model.amount)) + " BGN");
    }

    @Override
    public int getItemCount() {
        return cashFlowModels.size();
    }

     class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView text_view_cashFlowType;
        TextView text_view_date;
        TextView text_view_amount;
        OnItemClickListener listener;

        public RecyclerViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            text_view_cashFlowType = itemView.findViewById(R.id.text_view_cashFlowType);
            text_view_date = itemView.findViewById(R.id.text_view_date);
            text_view_amount = itemView.findViewById(R.id.text_view_amount);
            this.listener = listener;

            itemView.setOnClickListener(this);
        }

         @Override
         public void onClick(View v) {
            listener.onItemClick(getAdapterPosition());
         }
     }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
