package com.example.cashmanagement.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import com.example.cashmanagement.models.SetDataModel;
import com.example.cashmanagement.utils.Ini;

import java.util.ArrayList;
import java.util.List;

public class SetScreenViewModel extends AndroidViewModel {

    private MutableLiveData<List<SetDataModel>> modelMutableLiveData;
    private List<SetDataModel> setDataModels = new ArrayList<>();

    public SetScreenViewModel(@NonNull Application application){
        super(application);
        getData();
        //        switch (Ini.Server.SetKind){
//            case "set1":
//                setDataModels.add(new SetDataModel("BGN", 0.5, 1,"",""));
//                setDataModels.add(new SetDataModel("BGN", 0.1, 2,"",""));
//                setDataModels.add(new SetDataModel("BGN", 0.5, 1,"",""));
//                break;
//            case "set2":
//                setDataModels.add(new SetDataModel("BGN", 0.01, 1,"",""));
//                setDataModels.add(new SetDataModel("BGN", 0.02, 2,"",""));
//                setDataModels.add(new SetDataModel("BGN", 0.1, 1,"",""));
//                setDataModels.add(new SetDataModel("BGN", 1.0, 1,"",""));
//                break;
//        }

        //modelMutableLiveData = new MutableLiveData<>();
        // modelMutableLiveData.setValue(setDataModels);
    }

    public MutableLiveData<List<SetDataModel>> getList(){
        return modelMutableLiveData;
    }

    public MutableLiveData<List<SetDataModel>> getData(){
        if(modelMutableLiveData == null) {
            modelMutableLiveData = new MutableLiveData<>();

            switch (Ini.Server.SetKind) {
                case "set1":
                    setDataModels.add(new SetDataModel("BGN", 0.01, 1, "", ""));
                    setDataModels.add(new SetDataModel("BGN", 0.02, 2, "", ""));
                    setDataModels.add(new SetDataModel("BGN", 0.1, 3, "", ""));
                    break;
                case "set2":
                    setDataModels.add(new SetDataModel("BGN", 0.01, 1, "", ""));
                    setDataModels.add(new SetDataModel("BGN", 0.02, 2, "", ""));
                    setDataModels.add(new SetDataModel("BGN", 0.1, 1, "", ""));
                    setDataModels.add(new SetDataModel("BGN", 1.0, 1, "", ""));
                    break;
            }
        }
        modelMutableLiveData.setValue(setDataModels);
        return modelMutableLiveData;
    }

    public void setModelMutableLiveData(List<SetDataModel> setDataModels){
        modelMutableLiveData.setValue(setDataModels);
    }

}
