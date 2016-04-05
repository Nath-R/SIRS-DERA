
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
         System.out.println("Event received: "
                            + newData[0].getUnderlying());
    }
}