package storage;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpServerConnection;
import sun.misc.BASE64Encoder;
import sun.net.www.protocol.https.HttpsURLConnectionImpl;
import util.Console;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * A collection of useful methods.
 *
 * @author Jens F. | https://m4taiori.de
 */
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
            StringBuilder output = new StringBuilder();
            URL httpsurl = new URL(url);

            HttpURLConnection connection = null;
            connection = (HttpsURLConnection)httpsurl.openConnection();
            connection.setRequestProperty("Content-Type", "text/plain; charset=\"utf8\"");
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
            int returnCode = connection.getResponseCode();
            InputStream connectionIn = null;
            if (returnCode==200) {
                connectionIn = connection.getInputStream();
            } else {
                connectionIn = connection.getErrorStream();
            }
            BufferedReader buffer = new BufferedReader(new InputStreamReader(connectionIn));
            String inputLine;
            while ((inputLine = buffer.readLine()) != null) {
                output.append(inputLine);
            }
            buffer.close();

            return output.toString();
            
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            //If something went wrong.
            return "{'error' : '"+ex.getMessage()+"'}";
            
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
