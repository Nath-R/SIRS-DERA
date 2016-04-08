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
public class CEPListenerCount implements UpdateListener {
    static double sumTrust;
 public void update(EventBean[] newData, EventBean[] oldData) {     
     
         System.out.println(newData[0].getUnderlying());         
         sumTrust = (Double) (newData[0].get("sumTrust"));    
         System.out.println("nbr tot de trust : " + sumTrust);
        
    }
}