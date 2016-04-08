package code;


import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

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
 public void update(EventBean[] newData, EventBean[] oldData) {
         
    // System.out.println("timeStamp : "+newData[0].get("timeStamp"));
     //System.out.println("idSensor : "+newData[0].get("idSensor"));
     
     System.out.println("Semantic Event received : "
                            + newData[0].getUnderlying());
         
         
         
         
         //fuzzy location
         /*System.out.println("à spliter " + newData[0].getUnderlying().toString());
         String[] parts = newData[0].getUnderlying().toString().split(", ");
         System.out.println(parts[1] + parts[3]);*/
        
    }
}