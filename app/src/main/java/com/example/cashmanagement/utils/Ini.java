package com.example.cashmanagement.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author plamen.peev
 */
@SuppressWarnings( "unchecked" )
public class Ini extends INIFile
{

    public Ini() // was private
    {
    }
    private static Ini _instance = null;
    private static final String timeStampFormat = "yyyy-MM-dd HH:mm:ss";

    public static Ini Instance()
    {
        if( _instance == null )
        {
            _instance = new Ini();
        }
        return _instance;
    }

    public static String Filename()
    {
        return _instance.getFileName();
    }

    /**
     * Real settings
     */

    @IniAnnotation(Section = "Server")
    public static class Server {

        @IniAnnotation(Key = "Address", Default = "http://10.120.20.101:8023/signalr")
        public static String Address;

        @IniAnnotation(Key = "IsOperatorSelected", Default = "false")
        public static boolean IsOperatorSelected;

        @IniAnnotation(Key = "IsTransactionTypeSelected", Default = "false")
        public static boolean IsTransactionTypeSelected;

        @IniAnnotation(Key = "SelectedOperator", Default = "")
        public static String SelectedOperator;

        @IniAnnotation(Key = "SelectedTransactionType", Default = "")
        public static String SelectedTransactionType;

        @IniAnnotation(Key = "Username", Default = "admin")
        public static String Username;

        @IniAnnotation(Key = "savedUser", Default = "")
        public static String SavedUser;

        @IniAnnotation(Key = "Password", Default = "PGqE6U79AmFlB8V8yNt8V38Lk0Lb7bxaNf7k5CXxH7U=")
        public static String Password;

        @IniAnnotation(Key = "Port", Default = "")
        public static String Port;

        @IniAnnotation(Key = "CardNumber", Default = "")
        public static String CardNumber;

        @IniAnnotation(Key = "ApplicationIdent", Default = "")
        public static String ApplicationIdent;


        @IniAnnotation(Key = "DownloadScheduleHours", Default = "1")
        public static String DownloadScheduleHours;

        @IniAnnotation(Key = "DownloadScheduleMinutes", Default = "0")
        public static String DownloadScheduleMinutes;

        @IniAnnotation(Key = "Language", Default = "bg")
        public static String Language;

        @IniAnnotation(Key = "DownloadedDay", Default = "1")
        public static String DownloadedDay;

        @IniAnnotation(Key = "DownloadedMonth", Default = "1")
        public static String DownloadedMonth;

        @IniAnnotation(Key = "LoggedUsername", Default = "")
        public static String LoggedUsername;

        @IniAnnotation(Key = "LoggedUserPass", Default = "")
        public static String LoggedUserPass;

        @IniAnnotation(Key = "LoggedUserType", Default = "0")
        public static int LoggedUserType;

        @IniAnnotation(Key = "SyncPayOutResult", Default = "0")
        public static double SyncPayOutResult;

        @IniAnnotation(Key = "SyncPayInResult", Default = "0")
        public static double SyncPayInResult;

        @IniAnnotation(Key = "SetKind", Default = "")
        public static String SetKind;

        @IniAnnotation(Key = "TotalDepositAmount", Default = "0")
        public static double TotalDepositAmount;

    }

    /**
     * Save settings.
     */
    public static void Save( Ini instance, String file )
    {
        Class[] clases = instance.getClass().getClasses();

        for( Class cl : clases )
        {
            Field[] fields = cl.getFields();
            for( Field fl : fields )
            {
                IniAnnotation fann = fl.getAnnotation( IniAnnotation.class );
                IniAnnotation cann = ( IniAnnotation ) cl.getAnnotation( IniAnnotation.class );

                if(fann != null && cann != null)
                    instance.setStringProperty( cann.Section(), fann.Key(), ConvertToString( fl, cl ), null );
            }
        }

        instance.save( file );
    }

    public static void Load( Ini instance, String file )
    {
        instance.load( file );
        Class[] clases = instance.getClass().getClasses();

        for( Class cl : clases )
        {
            Field[] fields = cl.getFields();
            for( Field fl : fields )
            {
                IniAnnotation fann = fl.getAnnotation( IniAnnotation.class );
                IniAnnotation cann = ( IniAnnotation ) cl.getAnnotation( IniAnnotation.class );

                if(fann != null && cann != null) {
                    String res = instance.getStringProperty(cann.Section(), fann.Key());
                    ConvertFromString(res, fl, cl, fann.Default());
                }
            }
        }
    }

    private static String ConvertToString(Field field, Class clazz )
    {
        String result = "";
        try
        {
            Type fieldType = field.getType();
            Object value = field.get( clazz );

            if( fieldType == int.class )
            {
                result = Integer.toString( (Integer) value, 10 );
            }
            else
            {
                if( fieldType == double.class )
                {
                    result = Double.toString( (Double) value );
                }
                else
                {
                    if( fieldType == boolean.class )
                    {
                        result = Boolean.toString( (Boolean) value );
                    }
                    else
                    {
                        if( fieldType == Date.class )
                        {
                            result = new SimpleDateFormat( timeStampFormat ).toString();
                        }
                        else
                        {
                            if( fieldType == String.class )
                            {
                                result = (String) value;
                            }
                            else
                            {
                                result = "";
                            }
                        }
                    }
                }
            }
        }
        catch( IllegalArgumentException | IllegalAccessException ex )
        {
            String error = ex.getMessage();
        }

        return result;
    }

    private static Object ConvertFromString(String input, Field field, Class clazz, String deflt )
    {
        Object result = null;
        Type fieldType = field.getType();
        try
        {
            result = ConvertTypeFromString( fieldType, input );   // try parse saved parameters
        }
        catch( Exception ex )
        {
            try
            {
                result = ConvertTypeFromString( fieldType, deflt ); // try parse default
            }
            catch( ParseException e )
            {
            }
        }
        try
        {
            if( fieldType == String.class && input==null )
                result = deflt;
            field.set( clazz, result );  // try assign value
        }
        catch( IllegalAccessException x )
        {
        }

        return result;
    }

    private static Object ConvertTypeFromString(Type fieldType, String input ) throws ParseException
    {
        Object result = null;

        if( fieldType == int.class )
        {
            result = Integer.parseInt( input );
        }
        else
        {
            if( fieldType == double.class )
            {
                result = Double.parseDouble( input );
            }
            else
            {
                if( fieldType == boolean.class )
                {
                    result = Boolean.parseBoolean( input );
                }
                else
                {
                    if( fieldType == Date.class )
                    {
                        result = new SimpleDateFormat( timeStampFormat ).parse( input );
                    }
                    else
                    {
                        if( fieldType == String.class )
                        {
                            result = input;
                        }
                    }
                }
            }
        }

        return result;
    }

    @Retention( RetentionPolicy.RUNTIME )
    private @interface IniAnnotation
    {
        String Section() default "";
        String Key() default "";
        String Default() default "";
    }
}

