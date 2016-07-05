package code;

import java.util.HashMap;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

/**
 * Analyze a batch of events and return one fuzzyfied event.
 * @author ramol_na
 *
 */
public class CEPFuzzyListener implements UpdateListener {

	/**
	 * Mother agent.
	 */
	private Agent agent;
	
	/**
	 * Constructor
	 * @param Agent A  Reference to the mother agent in order to be able to send messages
	 */
	public CEPFuzzyListener(Agent A)
	{
		super();
		agent = A;
	}
	
	public void update(EventBean[] newData, EventBean[] oldData) {
	     
		System.out.println("Handling events... Number of event in the batch: "+newData.length);
				
		//Step 1: Separate each different couple (subject, predicate)
		//TODO Do the separation, for now, we suppose all the received data are the same
		
		String dataAnalyzed = (String) newData[0].get("predicate");
		String dataSubject = (String) newData[0].get("subject");

		//Step 2: Reference all object (values) and call the fuzzyfication function
		//TODO put in a method
		
		//Counter values (with ponderation)
		HashMap<String, Double> values = new HashMap<String, Double>();
		//Total
		double total = 0;
		
		//Going through events and counting
		for (EventBean event : newData) {
	            String val = (String) event.get("object");
	            double trust = (Double) event.get("trust") ;
	            
	            if(values.containsKey(val)) //In list
	            { values.put(val, values.get(val) + trust ); }
	            else //First time encountered
	            { values.put(val, trust); }  
	            
	            total += trust;
	     }
		
		//Step 3: Set up the result (ontological)
		HashMap<String, Double> trustValues = new HashMap<String, Double>();
		for (String key : values.keySet()) {
			trustValues.put(key, values.get(key)/total );
		}
		
		//Step 4: Deliver the results.
		String result = "";
		
		for (String key : trustValues.keySet()) {
			
			String annotation = "<fuzzyOWL2 fuzzyType=\"axiom\"> <Degree value=\""+ trustValues.get(key) +"\" /> </fuzzyOWL2>";
			
			result += dataSubject+" "+dataAnalyzed+" "+key+ "     " + annotation + "\n";
			System.out.println(dataSubject+" "+dataAnalyzed+" "+key+ "     " + annotation);
			
			//Add: annotation  (weight ?  axiom ? )
		}
		
		//Step 5: Sending the result
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(new AID("brainiac", AID.ISLOCALNAME));
		message.setContent(result);
		agent.send(message);
    }
	
}
