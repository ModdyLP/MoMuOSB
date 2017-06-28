import Storage.JsonDriver;
import Util.Console;
import java.util.Arrays;
import org.junit.Test;

/**
 * A simple test for the JsonDriver.
 * 
 * @see JsonDriver 
 * @author Jens F. | https://m4taiori.de
 */
public class JsonDriverTest 
{
 
    @Test
    public void test()
    {
        
        Console.println("Solving test for the JsonDriver object...");
        
        String ExecDir = System.getProperty( "user.dir" ) + "/";
        
        JsonDriver TestDriver = new JsonDriver( ExecDir + "JsonDriverTest.json" );
        
        Console.println("Doing first flush...");
        
        TestDriver.flush();
        
        Console.println( "Writing first value to runtime..." );
        
        TestDriver.setDefault( "Test value 1", "Value 1" );
        
        Console.println( "Set first test value..." );
        
        Console.println( "The currently known values are: " + TestDriver.keys().toString() );
        
        TestDriver.setDefault( "Test value 2", 0 );
        
        Console.println( "The currently known values are: " + TestDriver.keys().toString() );
        
        Console.println( "Enabling auto-flush..." );
        
        TestDriver.autoflush(true);
        
        TestDriver.setDefault( "Test value 3", true );
        
        Console.println( "The currently known values are: " + TestDriver.keys().toString() );
        
        Console.println( "Reading test values..." );
        
        for ( String Key : TestDriver.keys() )
        {
            Console.println( "Reading \"" + Key + "\"..." );
            Console.println( "The value of \"" + Key + "\" is \"" + String.valueOf( TestDriver.get( Key ) ) + "\"..." );
        }
        
        Console.println( "Test done. Cleaning up!" );
        
        TestDriver.getFile().delete();
        
    }
    
}
