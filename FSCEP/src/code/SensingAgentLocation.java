package code;

import java.util.ArrayList;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

/**
 * A sensing agent that instanciate the received event, filter it and enrich it by querying the ontology.
 * It is dependent on sensor and on the message format. (or not)
 * For the moment it does not identify the user. (TODO add user specific recognition)
 * @author ramol_na
 *
 */
public class SensingAgentLocation extends SensingAgent {

	///** Attributes **///
	
	/**
	 * Auto generated
	 */
	private static final long serialVersionUID = -4802900693800119035L;
	
	//Esper stuff
	Configuration cepConfig;
	EPServiceProvider cep;
	EPRuntime cepRT;
	EPAdministrator cepAdm;
	EPStatement cepStatement;
	
	///*** Methods ***///
	
	@Override
	public void init() {
		cepConfig = new Configuration();
        cepConfig.addEventType("Event",Event.class.getName());
        cep = EPServiceProviderManager.getProvider("myCEPEngine",cepConfig);
        cepRT = cep.getEPRuntime();
        // We register an EPL statement
        cepAdm = cep.getEPAdministrator();
        //With check on status "on"
        //cepStatement = cepAdm.createEPL("select distinct timeStamp, idSensor, status, location, trust, subject, predicate, object from Event.win:time_batch(30 sec) where status =\"ON\" order by idSensor");
        //Without checking
        cepStatement = cepAdm.createEPL("select distinct timeStamp, idSensor, status, location, trust, subject, predicate, object from Event.win:time_batch(30 sec) order by idSensor");
        
        
        cepStatement.addListener(new CEPFuzzyListener(this));		
	}	
	
	
	@Override
	public void handle_input_data(String data) {
		System.out.println("Received: "+data);
		
		//Decomposing the input
		String[] parts = data.split(" ");                            
		String timestamp = parts[0];
		String nameSensor = parts[1];
		String statusSensor = parts[2];  //Value ?? //TODO if value on -> possibilities according to onto. / if value is a person
		String typeSensor = parts[3];
          
		//Query the ontology
		//TODO query/filter according to the type: the agent only handles on type of data 
		//(normally filtered by the 'port' of the agent, but better check)
		Ontology o = new Ontology();                                
        ArrayList <String> res = o.queryOnto(nameSensor.trim(), typeSensor.trim());
        
        String location = res.get(0);
        Double trust = Double.parseDouble(res.get(1));
        
        //Semantic elements from the events:
        String subject = ":User";
        String predicate = ":isLocatedIn";
        String object = ":"+location; //This should be the id of the room, not the name
        
        //Defining the event: (last: location and trust)
        Event e = new Event(timestamp, nameSensor, statusSensor, typeSensor, location, trust, subject, predicate, object);
        //Sending the event
        cepRT.sendEvent(e);
	}

}
