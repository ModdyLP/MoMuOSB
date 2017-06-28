package Util;

import Storage.JsonDriver;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple language organisation system.
 * 
 * @author Jens F. | https://m4taiori.de
 */
public class Language 
{
        
    //<editor-fold defaultstate="collapsed" desc="Variables">
    
    /**
     * A hash map of all known languages.
     */
    private Map<String, JsonDriver> Languages = new HashMap<>();
    
    /**
     * The directory for all the languages.
     */
    private final File LangDirectory = new File( "languages" );
    
    /**
     * The current instance of this class.
     */
    private static Language Instance;
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Construction">
    
    /**
     * Create a new instance of this class.<br>
     * <br>
     * <b>USE getInstance() INSTEAD !
     */
    public Language()
    {
        
        //Missing dir prevention.
        if ( !LangDirectory.exists() )
        {
            LangDirectory.mkdirs();
            LangDirectory.mkdir();
        }
        
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Methods">
    
    public static void provideDefaultLanguage( String langCode, Map<String, String> defaults )
    {
        try
        {
            if ( !Instance.Languages.containsKey( langCode ) )
            {
                
                
                
            }
        }
        catch ( Exception ex )
        {
            Console.error( "Something went wrong during language creating of \"" + langCode + ".lang\"!" );
        }
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Static instance">
    
    /**
     * Get the current instance of this class.
     * @return Language
     */
    public static Language getInstance()
    {
        if ( Instance == null )
        {
            Instance = new Language();
        }
        return Instance;
    }
    
    //</editor-fold>
    
}
