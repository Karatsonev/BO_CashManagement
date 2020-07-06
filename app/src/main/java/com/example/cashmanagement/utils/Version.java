package com.example.cashmanagement.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Version
{
    public static String svnVersion = "$Rev: 33193 $";


    Version()
    {
//        assemblyDescription += " ver. " + assemblyVersion + " build-"  + assemblyDescription;
    }

    public static String applicationDescription( String assemblyName )
    {
        String result;
        result = assemblyName + " ver.: " + getRbTok( "VERSION" ) + "."+ getRbTok( "SUBVER" ) + "." + getRbTok( "BUILD" ) +
                " Buld: " + getRbTok( "BUILD.DATE" );
        return result;
    }
    final static ResourceBundle rb = ResourceBundle.getBundle( "SISBaseServer/version" );
    public static final String getRbTok( String propToken )
    {
        String msg = "";
        try
        {
            msg = rb.getString( propToken );
        }
        catch( MissingResourceException e )
        {
            System.err.println( "Token ".concat( propToken ).concat(" not in Propertyfile!"));
        }
        return msg;
    }
}