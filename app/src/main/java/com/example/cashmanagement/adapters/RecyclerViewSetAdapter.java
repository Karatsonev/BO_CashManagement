package com.example.cashmanagement.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.cashmanagement.R;
import com.example.cashmanagement.models.CashFlowModel;
import com.example.cashmanagement.models.SetDataModel;
import java.util.List;

public class RecyclerViewSetAdapter  extends RecyclerView.Adapter<RecyclerViewSetAdapter.RecyclerViewHolder> {

    private List<SetDataModel> setDataModelList;

    public RecyclerViewSetAdapter(List<SetDataModel> setDataModelList) {
        this.setDataModelList = setDataModelList;
    }

    @NonNull
    @Override
    public RecyclerViewSetAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView  = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_set_item,viewGroup,false);
        return new RecyclerViewSetAdapter.RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int position) {
        SetDataModel model = setDataModelList.get(position);
        recyclerViewHolder.text_view_count.setText("Брой: " + (model.getModelCount()));
        recyclerViewHolder.text_view_currency.setText("Валута: " + model.getModelCurrency());
        recyclerViewHolder.text_view_denomination.setText("Номинал: " + (model.getModelDenomination()));
        recyclerViewHolder.text_view_status_success.setText(model.getStatusSuccess());
        recyclerViewHolder.text_view_status_failed.setText(model.getStatusFailed());
    }

    @Override
    public int getItemCount() {
        return setDataModelList.size();
    }

    public void setModelList(List<SetDataModel> modelList){
        this.setDataModelList = modelList;
        notifyDataSetChanged();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView text_view_currency;
        TextView text_view_denomination;
        TextView text_view_count;
        TextView text_view_status_success;
        TextView text_view_status_failed;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            text_view_currency = itemView.findViewById(R.id.text_view_currency2);
            text_view_denomination = itemView.findViewById(R.id.text_view_denomination);
            text_view_count = itemView.findViewById(R.id.text_view_count);
            text_view_status_success = itemView.findViewById(R.id.text_view_status_success);
            text_view_status_failed = itemView.findViewById(R.id.text_view_status_failed);
        }
    }
}
