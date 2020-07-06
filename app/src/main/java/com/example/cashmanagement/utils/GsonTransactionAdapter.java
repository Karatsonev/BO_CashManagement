package com.example.cashmanagement.utils;

import com.example.cashmanagement.comm.CommHelper;
import com.example.cashmanagement.comm.SyncTransaction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by christo.christov on 4.10.2016 г..
 */
public class GsonTransactionAdapter implements JsonSerializer<SyncTransaction>, JsonDeserializer<SyncTransaction> {
    private Gson gson;

    public GsonTransactionAdapter()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new GsonUTCDateAdapter());
        gson = gsonBuilder.create();
    }

    @Override
    public synchronized JsonElement serialize(SyncTransaction transaction, Type type, JsonSerializationContext jsonSerializationContext) {
        String json = gson.toJson(transaction);
        if(transaction.getClass() != SyncTransaction.class)
            return CommHelper.gsonPopTypeAsObject(json);
        else
            return new JsonParser().parse(json).getAsJsonObject();
    }

    @Override
    public SyncTransaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement typeElement = jsonObject.get("$type");
        String className = typeElement.getAsString().replace(", CashManagementServer", "").replace("CashManagementServer.","");
        SyncTransaction result = null;
        try {
            result = (SyncTransaction)gson.fromJson(json, Class.forName("com.example.cashmanagement.utils." + className));
        }catch (Exception ex){
//            if(ex != null && ex.getMessage()!=null)
//                App.writeToLog("deserialize:"+ex.getMessage());
        }
        return result;
    }

}
