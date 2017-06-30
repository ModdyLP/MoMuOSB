package storage;

import org.apache.commons.io.FileUtils;
import util.Console;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

public class WebClient 
{

    /**
     * Fetch a urls content.
     *
     * @param url The url you want to fetch.
     * @return The fetched data.
     */
    public static String fetchUrl( String url )
    {
        try
        {
                               
            //Output
            StringBuilder Output = new StringBuilder();
            
            //The download url.
            URL VersionUrl = new URL( url );
            
            //The downloader.
            BufferedReader VersionUrlDownloader = new BufferedReader( new InputStreamReader( VersionUrl.openStream() ) );

            //Loop through lines.
            String Line;
            while ( ( Line = VersionUrlDownloader.readLine() ) != null )
            {
                Output.append(Line);
            }
            
            //Return the current downloader.
            return Output.toString();
            
        }
        catch ( Exception ex )
        {
            
            //If something went wrong.
            return null;
            
        }
    }
    
    /**
     * Download file from url.
     * 
     * @param url The url you want to download from.
     * @param target The target-file.
     * @return boolean
     */
    public static boolean downloadFile( String url, File target )
    {
        try
        {

            //Run download.
            FileUtils.copyURLToFile( new URL( url ), target );
                    
            //Success.
            return true;
            
        }
        catch ( Exception ex )
        {
            
            //Post fail message.
            Console.debug( "Failed to download \"" + url + "\" to " + target.getPath() );

            
            //Failed.
            return false;
            
        }
    }
    
}
