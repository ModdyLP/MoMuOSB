package storage;

import org.json.JSONObject;
import util.Console;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
/**
 * A simple request for rest based apis.
 *
 * @author Jens F. | https://m4taiori.de
 */
public class RestRequest 
{
    /**
     * The map with the required arguments.
     */
    private final Map<String, String> Arguments = new HashMap<>();
    
    /**
     * The api url.
     */
    private URL RequestUrl;
    
    /**
     * Add a argument to your request.
     * 
     * @param key The key.
     * @param value It's value.
     * @return RestRequest.
     */
    public RestRequest add( String key, String value )
    {
        Arguments.put(key, value);
        return this;
    }
    
    /**
     * Set the url you want to connect to.
     * 
     * @param url The url.
     * @throws MalformedURLException 
     */
    public RestRequest setUrl( String url ) throws MalformedURLException
    {
        RequestUrl = new URL( url );
        return this;
    }
    
    /**
     * Fetch the request result.
     * 
     * @return String
     */
    public String fetchResult()
    {
        
        StringBuilder Request = new StringBuilder();
        Request.append(RequestUrl.toString());
        int count = 0;
        if (Arguments.size() > 0) {
            Request.append("?");
            for ( String Key : Arguments.keySet() )
            {
                Request.append(Key).append("=").append(Arguments.get(Key));
                count++;
                if(count < Arguments.size()) {
                    Request.append("&");
                }
            }
        }
        Console.debug(Request.toString());
        return WebClient.fetchUrl(Request.toString());
    }
    
    /**
     * Fetsch the request result and create a new JSONObject.
     * 
     * @return JSONObject
     */
    public JSONObject fetchJson()
    {

        return new JSONObject( fetchResult() );
        
    }

    
}
