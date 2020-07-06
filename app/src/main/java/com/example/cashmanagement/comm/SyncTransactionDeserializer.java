package com.example.cashmanagement.comm;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class SyncTransactionDeserializer implements JsonDeserializer<SyncTransaction> {
    @Override
    public SyncTransaction deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();

        JsonElement jsonMethod = jsonObject.get("$type");
        String objectType = jsonMethod.getAsString();
        //we only need the class name
        String className = objectType.replace(", CashManagementServer", "").replace("CashManagementServer.","");


        try {
            Class cls = Class.forName("com.example.cashmanagement.comm.answer." + className);
            return context.deserialize(json, cls);
        }
        catch(Exception e){
            String exception = e.getMessage();
        }

        return null;
    }
}
