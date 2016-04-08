package code;


import static code.CEPListenerCount.sumTrust;
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
public class CEPListenerSumTrust implements UpdateListener {
    double sumTrustPerLoc;
 public void update(EventBean[] newData, EventBean[] oldData) {
     
         //System.out.println(newData[0].getUnderlying());         
         //System.out.println("test location : " +newData[0].get("location"));         
         
         String location = (String) (newData[0].get("location"));
         //System.out.println("location " + location);
         
         if (newData[0].get("sumTrust")!=null)
         {
             sumTrustPerLoc = (Double) (newData[0].get("sumTrust"));    
             //System.out.println("sumTrust " + sumTrust);
         }
         
         /*String nbrEvent = (String)(newData[0].get("nbrEvent"));
         System.out.println("nbrEvent " + nbrEvent);*/
         
         //System.out.println("conclusion : "+ location + " "+ sumTrust+ " "+ nbrEvent);
         if (sumTrust != 0)
         {
            System.out.println("sumTrustPerLoc : " + sumTrustPerLoc) ;
            System.out.println("sumTrust : " + sumTrust) ;
            double uncertainty = (sumTrustPerLoc/sumTrust);
            System.out.println ("{location="+location+", Uncertainty="+ uncertainty +"}");
         }
        
    }
}