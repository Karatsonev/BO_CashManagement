package com.example.cashmanagement.comm;

import android.content.Intent;
import android.util.Base64;
import com.example.cashmanagement.App;
import com.example.cashmanagement.comm.answer.SyncSendPayInResult;
import com.example.cashmanagement.helpers.IntentHelper;
import com.example.cashmanagement.utils.GsonTransactionAdapter;
import com.example.cashmanagement.utils.GsonUTCDateAdapter;
import com.example.cashmanagement.utils.Ini;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.URL;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import microsoft.aspnet.signalr.client.NullLogger;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;

public class CommHelper {

    private KeyPairGenerator keyPairGenerator; //RSA encryption key generator
    private KeyPair keyPair; //RSA keypair
    private PublicKey publicKey; //публичен ключ на приложението, изпращан към сървъра
    private PublicKey serverPublicKey; //публичен ключ на сървъра за криптиране на симетричния ключ
    private PrivateKey privateKey; //частен ключ на приложението за декриптиране на симетричния ключ
    private String word; //симетричен ключ
    public Gson gson;//обект за сериализиране и десериализиране
    private String host; // адрес на сървъра
    private HubConnection connection; //връзка към сигналР
    private HubProxy hub; //хъб за връзка
    public static boolean isConnected; //статус на връзката    // was private
    Thread thread; //

    /**
     * Този цикъл поддържа връзката с сигналР постоянно активна.
     */
    Runnable startIt = new Runnable() {
        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    while (!isConnected && !Thread.currentThread().isInterrupted()) {
                        try {
                            connection.start().get(10, TimeUnit.SECONDS);
                            if(handShake(makePublicKeyForNET(publicKey)))
                                isConnected = true;
                        }
                        catch (InterruptedException e) {
                            break;
                        }
                        catch (Exception e) {App.writeToLog(e.getMessage());}
                        if (!isConnected)
                            try {
                                Thread.sleep(1000);
                            }
                            catch (InterruptedException e) {
                                break;
                            }
                            catch (Exception ignore)
                            {
                                ignore.printStackTrace();
                            }
                    }
                    if (isConnected)
                        try {
                            Thread.sleep(1000);
                        }
                        catch (Exception ignore)
                        {
                            ignore.printStackTrace();
                        }
                }
            }catch (Exception ignore)
            {
                ignore.printStackTrace();
            }
        }
    };
    public CommHelper(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new GsonUTCDateAdapter());
        gsonBuilder.registerTypeAdapter(SyncTransaction.class, new GsonTransactionAdapter());
        gson = gsonBuilder.create();
        initRSAKeys();
        InitSignalR();
    }

    /**
     * Изпраща заявка и получава отговор като класове.
     * @param request Обект от comm/requests/
     * @param answerType Клас за отговора comm/answers/
     * @param <T>
     * @return
     */
    public <T extends ServerAnswer> T getAnswer(Object request, Class<T> answerType){

        String res = postMessage(gson.toJson(request, request.getClass()));
        T result = gson.fromJson(res, answerType);
//        if(result.opErrorNum > 0 || result.commErrorNum > 0){
//            preHandleErrors( result.commErrorNum , result.opErrorNum);
//        }
        return result;
    }

    /**
     * Предварително обработва критични грешки
     * @param commErrorNum грешка извън базата
     * @param opErrorNum грешка при работа с базата
     */
    /*public void preHandleErrors(int commErrorNum, int opErrorNum){
        try{
            if(commErrorNum > 0){
                ErrorHelper.getInstance().handleError(EnumError.values()[commErrorNum]);
            }else{
                if(opErrorNum > 0){
                    ErrorHelper.getInstance().handleError(EnumError.values()[opErrorNum]);
                }
            }
        }catch (Exception ex){
            if(ex != null && ex.getMessage() != null)
            App.writeToLog("preHandleError:" + ex.getMessage());
        }
    }*/

    /**
     * Инициализира и стартира връзката към сървъра
     */
    public void InitSignalR(){
        host = Ini.Server.Address;
        closeConnection();
        String queryString = "terminalIdent=" + Ini.Server.ApplicationIdent;
        connection = new HubConnection(host,  queryString,  false, new NullLogger());
        hub = connection.createHubProxy("CashManagementHub");
        hub.subscribe(this);
        connection.closed(() -> isConnected = false);
        openConnection();
    }

    /**
     * За проверка дали сме вързани със сървъра
     * @return
     */
    public boolean isConnected(){
        return isConnected;
    }

    /**
     * Не използвай тази функция, ако не си 100% сигурен че знаеш какво прави.
     */
    public void openConnection(){
        thread = new Thread(startIt);
        thread.setName("CommunicationLoop");
        thread.start();
    }

    /**
     * Затваря връзката със сървъра. И спира нишката за поддържането й.
     */
    public void closeConnection(){
        if(null!=thread)
            thread.interrupt();
        if(null!=connection)
            connection.stop();
    }

    /**
     * Обмяна на публични ключове.
     * @param clientPublicKey публичният ключ на терминала.
     * @return Да, ако са обменени успешно.
     */
    public boolean handShake( String clientPublicKey ) {
        try {
            String publicKey = hub.invoke(String.class, "handShake", clientPublicKey).get(10, TimeUnit.SECONDS);
            Document doc = loadXMLFromString(publicKey);

            byte [] mod = Base64.decode(doc.getElementsByTagName("Modulus").item(0).getTextContent(), Base64.DEFAULT);
            byte [] exp = Base64.decode(doc.getElementsByTagName("Exponent").item(0).getTextContent(), Base64.DEFAULT);

            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(new BigInteger(1, mod), new BigInteger(1, exp));
            KeyFactory fact = KeyFactory.getInstance("RSA");
            serverPublicKey = fact.generatePublic(keySpec);
            if(serverPublicKey == null)
                return false;
            return true;
        }catch (Exception ex){
            if(ex.getMessage() != null)
                App.writeToLog("handShake:"+ex.getMessage());
        }
        return false;
    }

    /**
     * Получава съобщение от сървъра към терминала.
     * @param message
     */
    public void syncMessage(DuplexClientMessage message){
        try {
            final String msg = SisPack.DecryptAndDecompress(message, privateKey);

        SyncTransaction response = SyncTransaction.fromJson(msg);
        response.execute();

        }catch (Exception ex){
            if(ex.getMessage()!=null)
                App.writeToLog("syncMessage:"+ex.getMessage());
        }
    }

    /**
     * Изпраща съобщение към сървъра.
     * @param message
     * @return
     */
    public String postMessage(String message){
        try {
            message = gsonPopType(message);
            DuplexClientMessage sending = SisPack.CompressAndCrypt(message, serverPublicKey);
            return SisPack.DecryptAndDecompress(hub.invoke(DuplexClientMessage.class, "postMessage", sending).get(10, TimeUnit.SECONDS), privateKey);
        }catch (Exception ex){
            if(ex != null && ex.getMessage()!=null)
                App.writeToLog("postMessage:"+ex.getMessage());
            return null;
        }
    }

    /**
     * Извлича името на класа от json файл.
     * @param json
     * @return
     */
    public String gsonGetType(String json){
        try {
            JSONObject object = new JSONObject(json);
            String result = object.getString("$type");
            return result.replace(", CashManagementServer", "").replace("CashManagementServer.","");
        }catch (Exception ex){
            if(ex != null && ex.getMessage()!=null)
                App.writeToLog("gsonGetType:"+ex.getMessage());
            return null;
        }
    }

    public static JsonObject gsonPopTypeAsObject(String json){
        JsonObject object = new JsonParser().parse(json).getAsJsonObject();
        JsonElement type = object.get("$type");
        object.remove("$type");

        JsonObject res = new JsonObject();
        res.add("$type", type);

        for (Map.Entry<String,JsonElement> entry : object.entrySet()) {
            res.add(entry.getKey(), entry.getValue());
        }

        return res;
    }

    /**
     * Премахва типа от json обект.
     * @param json
     * @return
     */
    public static String gsonPopType(String json){
        try{
            JsonObject object = new JsonParser().parse(json).getAsJsonObject();
            JsonElement type = object.get("$type");
            object.remove("$type");

            JsonObject res = new JsonObject();
            res.add("$type", type);

            for (Map.Entry<String,JsonElement> entry : object.entrySet()) {
                res.add(entry.getKey(), entry.getValue());
            }

            Gson gson = new Gson();
            return gson.toJson(res);

        }catch (Exception ex){
            if(ex != null && ex.getMessage()!=null)
                App.writeToLog("gsonPopType:" +ex.getMessage());
            return json;
        }
    }

    /**
     * Сериализира JAVA публичен ключ във формат, удобен за ползване в .NET.
     * @param key
     * @return
     */
    public String makePublicKeyForNET(PublicKey key){
        try {
            KeyFactory fact = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec spec = fact.getKeySpec(key, RSAPublicKeySpec.class);
            byte[] bytes = spec.getModulus().toByteArray();
            int length = bytes.length;
            if (length % 2 != 0 && bytes[0] == 0) {
                bytes = Arrays.copyOfRange(bytes, 1, length);
            }
            String mod = Base64.encodeToString(bytes, Base64.NO_WRAP);
            String exp = Base64.encodeToString(spec.getPublicExponent().toByteArray(), Base64.NO_WRAP);
            StringBuilder sb = new StringBuilder();
            sb.append("<RSAKeyValue>");
            sb.append("<Modulus>");
            sb.append(mod);
            sb.append("</Modulus>");
            sb.append("<Exponent>");
            sb.append(exp);
            sb.append("</Exponent>");
            sb.append("</RSAKeyValue>");
            return sb.toString();
        }catch (Exception ex){
            if(ex != null && ex.getMessage()!=null)
                App.writeToLog("makePublicKeyForNET:"+ex.getMessage());
        }

        return null;
    }

    /**
     * Инициализира ключовете за RSA криптиране.
     */
    public void initRSAKeys(){
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
        }catch (Exception ex){
            if(ex != null && ex.getMessage()!=null)
                App.writeToLog("initRSAKeys:"+ex.getMessage());
        }
    }

    /**
     * Превръща XML стринг в документ, удобен за модификация в JAVA.
     * @param xml
     * @return
     */
    public static Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    public static void urlToFile(URL url, File f) throws IOException {
        DataInputStream dis = new DataInputStream(url.openStream());
        FileOutputStream fos = new FileOutputStream(f);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = dis.read(buffer))>0) {
            fos.write(buffer, 0, length);
        }
        fos.flush();
        fos.close();
        dis.close();
    }


}
