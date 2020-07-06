package com.example.cashmanagement.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

public class LogString
{
    private final int maxLogFileSize = 2097152;// bytes..2 MB
    private final int maxLogFileCount = 3;

    private String logFullPath = "";
    private String logFullFileName = "";
    private String logString = "";
    private String logName = "";
    private String logExt = "";
    private String pathName = "";
    private LoggedObject logged;

    private static final Hashtable<String, LogString> logsTable = new Hashtable<String, LogString>();

    // Constructor
    // If any parameter is missing .. fill it
    public LogString(String pathName, String fileName )
    {
        if( pathName.equals( "" ) )
            pathName = "/data/data/";

        if( fileName.equals( "" ) )
            fileName = "CashManagementServer.log";

        logFullPath = pathName;
        logFullFileName = pathName + fileName;

        logName = FilenameUtils.getBaseName( logFullFileName );
        logExt = FilenameUtils.getExtension( logFullFileName );

        // create folder
        try
        {
            FileUtils.forceMkdir( new File( logFullPath ) );
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
    }

    public void setLoggedObject( LoggedObject _logged )
    {
        logged = _logged;
    }

    public static LogString getLogString()
    {
        return createGetLogString( "", "" );
    }

    public static LogString getLogString( String fileName )
    {
        return createGetLogString( "", fileName );
    }

    public static LogString getLogString(String pathName, String fileName )
    {
        return createGetLogString( pathName, fileName );
    }

    public static LogString createGetLogString(String pathName, String fileName )
    {
        // If it exists, return the existing log.
        String logFilePath = pathName + fileName;
        if( logsTable.containsKey( logFilePath ) )
        {
            return ( LogString ) logsTable.get( logFilePath );
        }

        // Create and return a new log.
        LogString rv = new LogString( pathName, fileName );

        logsTable.put( logFilePath, rv ); // add to table

        return rv;
    }

    // Check for log lenght and rename if needed
    //
    private void CheckFileList()
    {
        // check current file size
        if( FileUtils.sizeOf( new File( logFullFileName ) ) < maxLogFileSize )
        {
            return;
        }

        String logRenamedFileName = logFullPath + logName + "_" + new SimpleDateFormat( "yyyyMMddHHmmss" ).format( Calendar.getInstance().getTime() ) + "." + logExt;

        // rename current
        try
        {
            FileUtils.moveFile( new File( logFullFileName ), new File( logRenamedFileName ) );
        }
        catch( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // remove oldest file
        // get list of files
        List<File> files = (List<File>) FileUtils.listFiles( new File( logFullPath ), new RegexFileFilter( "^(.*?)" ), DirectoryFileFilter.DIRECTORY );

        // comparator to sort collection
        Comparator<File> comparator = new Comparator<File>()
        {
            @Override
            public int compare(File arg0, File arg1 )
            {
                return arg0.compareTo( arg1 );
            }
        };

        // sort collection by file name
        Collections.sort( files, comparator ); // use the comparator as much as u want

        while( files.size() > maxLogFileCount )
        {
            File f = (File) files.get( 0 );
            f.delete();
            files.remove( 0 );
        }
    }

    public void Add( String mess )
    {
        // Add Date/Time to the string
        mess = new SimpleDateFormat( "yyyy.MM.dd-HH:mm:ss.SSS" ).format( Calendar.getInstance().getTime() ) + "   " + mess + "\r\n";

        synchronized( logString ) // lock resource
        {
            if( logged != null ) logged.logIt( mess );
            logString = mess;
            try
            {
                FileUtils.writeStringToFile( new File( logFullFileName ), mess, true );
                System.out.println( mess );// just for trace
                CheckFileList();
            }
            catch( IOException e )
            {
                e.printStackTrace();
            }
        }
    }
}