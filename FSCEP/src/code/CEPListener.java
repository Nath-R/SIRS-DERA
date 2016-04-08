package code;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dell
 */
public class CEPListener implements UpdateListener {    
     double sumTrust = 0.0;     
     ArrayList<String> tabLoc = new ArrayList<String>();
     ArrayList<Event> tabEvent = new ArrayList<Event>();
     double uncertainty=0.0;
     
    public boolean existLocationIntab (String location, ArrayList<String> tabLoc){
        boolean exist = false;
        
            for (int i=0; i<tabLoc.size(); i++){
                if (tabLoc.get(i) != null && tabLoc.get(i).equals(location))
                {
                    exist = true;
                    break;
                }
            }
        
        return exist;
    }
    
    public double getSumTrustPerLoc (String location, ArrayList<Event> tabEvent){
        double sumTrustLoc = 0.0;
        
        for (int i=0; i<tabEvent.size(); i++){
            if (tabEvent.get(i).getLocation().equals(location))
                sumTrustLoc += tabEvent.get(i).getTrust();
        }
        
        return sumTrustLoc;
        
    }
    
 public void update(EventBean[] newData, EventBean[] oldData) {      

    
     System.out.println("Semantic Event received : "
                            + newData[0].getUnderlying());
     
     //insert event in tabEvent     
     Event e = new Event ((String)newData[0].get("timeStamp"), (String)newData[0].get("idSensor"),
             (String)newData[0].get("status"), (String)newData[0].get("location"),
             (Double)newData[0].get("trust"));     
     tabEvent.add(e);
     
     //Total sum of trust for all events
     sumTrust += ((Double)newData[0].get("trust"));         
     //System.out.println ("sumTrust : " + sumTrust);
     
     //Total sum of trust for all events per location
     String location = (String)newData[0].get("location");     
     if (!existLocationIntab (location, tabLoc))
         tabLoc.add(location);
     
     
     System.out.println("Fuzzy SCEP for Location : ");
     for (int i=0; i<tabLoc.size(); i++){
         double sumTrustLoc = getSumTrustPerLoc (tabLoc.get(i), tabEvent);
         if (sumTrust!=0)
            uncertainty = sumTrustLoc/sumTrust;
          
         System.out.println ("{Location="+tabLoc.get(i)+", Uncertainty degree=" + uncertainty + "}");
     }       
        
    }
}