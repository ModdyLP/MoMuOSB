package Storage;

import Util.Console;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple json based config.
 * 
 * @author Jens F. | https://m4taiori.de
 */
public class JsonDriver 
{
    
    //<editor-fold defaultstate="collapsed" desc="Variables">
    
    /**
     * The json file used by an instance of this class.
     */
    private final File JsonFile;
    
    /**
     * The entries contained in the JsonFile variable.
     */
    private JSONObject Content;
    
    /**
     * Do autoflush.
     */
    private boolean AutoFlush = false;
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Construction">
    
    /**
     * Construction from file path.<br>
     * Reading file automaticly if exists.
     * @param path The file-path.
     */
    public JsonDriver( String path )
    {
        
        this.JsonFile = new File( path );   
        
        if ( JsonFile.exists() )
        {
            this.readFile();
        }
        else
        {
            this.Content = new JSONObject();
        }
        
    }
            
    //</editor-fold>
 
    //<editor-fold defaultstate="collapsed" desc="Methods">
    
    /**
     * Set a value.
     * @param key The key of this value.
     * @param value It's value.
     */
    public void set( String key, Object value )
    {
        Content.put(key, value);
        this.optionalFlush();
    }
    
    /**
     * Set a value only if it's not set.
     * @param key The key you want to use.
     * @param value The keys value.
     */
    public void setDefault( String key, Object value )
    {
        if ( !isSet(key) )
        {
            this.set(key,value);
        }
    }
    
    /**
     * Remove a value.
     * @param key The key of the value you want to remove.
     */
    public void remove( String key )
    {
        Content.remove(key);
        this.optionalFlush();
    }
    
    /**
     * Get a value.
     * @param key The key you want to get the value from.
     * @return Object
     */
    public Object get( String key )
    {
        return Content.get(key);
    }
    
    /**
     * Check for a keys existance.
     * @param key The key you're looking for.
     * @return boolean
     */
    public boolean isSet( String key )
    {
        try
        {
            return get( key ) != null;
        }
        catch ( Exception ex )
        {
            return false;
        }
    }
    
    /**
     * Ge the key set of this instance.
     * @return Set<String>
     */
    public Set<String> keys()
    {
        return Content.keySet();
    }
    
    /**
     * Save the values from runtime to file.
     */
    public void flush()
    {
        try
        {
            FileUtils.write(JsonFile, Content.toString( 4 ), "utf-8");
        }
        catch ( IOException ex )
        {
            Console.error( "Error while flushing!" );
            ex.printStackTrace();
        }
    }
    
    /**
     * Change the autoflush option.
     * @param doAutoFlush The new value you want to assign.
     */
    public void autoflush( boolean doAutoFlush )
    {
        this.AutoFlush = doAutoFlush;
    }
    
    /**
     * Save this json object as a string.
     * @param path The file-path you want to save to.
     */
    public void save( String path )
    {
        try
        {
            FileUtils.write( new File( path ), Content.toString( 4 ), "utf-8");
        }
        catch ( Exception ex )
        {
            Console.error( "Error while saving!" );
            ex.printStackTrace();
        }
    }
    
    /**
     * Get the file used by the instance you're working with.
     * @return File
     */
    public File getFile()
    {
        return JsonFile;
    }
            
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Private methods">
    
    /**
     * Read the files current content.
     */
    private void readFile()
    {
        try
        {
            this.Content = new JSONObject( FileUtils.readFileToString( JsonFile, "utf-8" ) );
        }
        catch ( IOException | JSONException ex )
        {
            Console.error( "Error reading json file \"" + JsonFile.getPath() + "\"!" );
        }
    }
    
    /**
     * Do an optional flush.
     */
    private void optionalFlush()
    {
        if ( AutoFlush )
        {
            this.flush();
        }
    }
    
    //</editor-fold>
    
}