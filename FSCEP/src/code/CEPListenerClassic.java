
package code;

import static code.CEPListenerCount.sumTrust;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Listener that handle events without ontology.
 * We use it as a classic CEP without ontology would.
 * This one only deal with location and return the location with the most occurence.
 * @author Nathan
 */
public class CEPListenerClassic implements UpdateListener{
    
    public void update(EventBean[] newData, EventBean[] oldData) {
     
        //Map between locatin and popularity
        HashMap<String, Integer> locations = new HashMap<String, Integer>();
        
        //Read all event
        for (EventBean event : newData) {
            String loc = (String) event.get("location");
            if(locations.containsKey(loc))
            { locations.put(loc, locations.get(loc)+1); }
            else
            { locations.put(loc, 1); }  
        }
        
        //Count the number of occurence for each location        
        //Return the most common one
        Map.Entry<String, Integer> first_entry = locations.entrySet().iterator().next();
        String selected_loc = first_entry.getKey();
        Integer max = first_entry.getValue();
        
        //Finding the location the most popular
        for(Map.Entry<String, Integer> entry: locations.entrySet())
        {
            String key = entry.getKey();
            Integer val = entry.getValue();
            
            if(val > max)
            {
                max = val;
                selected_loc = key;
            }
            
        }
        
        //Returning the result
        System.out.println ("{Location="+selected_loc+", Uncertainty degree=" + 1 + "}");
    }
}
