package agent;

import java.util.ArrayList;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import ontology.FuzzyTriple;
import ontology.OntoManager;

/**
 * Agent in charge of maintaining a context knowledge and analyzing it.
 * Process:
 * - Receive FSCE events
 * - Update the knowledge (Ontology)
 * - Recognize activity (with rules for as a first solution)
 * 
 * The process itself is in the LoopBehavior
 * @author ramol_na
 *
 */
public class CognitionAgent extends Agent 
{
	///** Attributes **///

	/**
	 * Generated serial version (to remove the warning)
	 */
	private static final long serialVersionUID = 2023584584402259697L;
	
	/**
	 * Ontology manager
	 */
	private OntoManager om;
	
	///** Methods **///
	
	protected void setup()
	{
		System.out.println("Starting cognition agent...");
		
		om = new OntoManager();
		
		addBehaviour( new LoopBehavior(this) );
		addBehaviour( new InferenceBehavior(this, 10000) );
		
		//Another agent for the rules ?
	}
	
	public OntoManager getOnto()
	{ return om; }
}


class LoopBehavior extends CyclicBehaviour
{
	/**
	 * Generated serial version (to remove the warning)
	 */
	private static final long serialVersionUID = 3275771007302179671L;

	
	private Agent agent;

	public LoopBehavior(Agent a) {
		super(a);
		
		agent = a;
	}
	
	
	/**
	 * Main run method that does the job.
	 */
	@Override
	public void action() {
		
				
		//Message reception
		ACLMessage receivedMess = agent.receive();
		if(receivedMess != null)
		{
		
			System.out.println("Received <- "+receivedMess.toString());
			
			//Message interpretation
			String splitedMess[] = receivedMess.getContent().split("\n");
			
			for(String tripStr: splitedMess)
			{
				FuzzyTriple ft = new FuzzyTriple(tripStr);
				
				//Ontology update
				((CognitionAgent)agent).getOnto().updateOntology(ft);
			}
			
			
		}
	}
	
}


class InferenceBehavior extends TickerBehaviour
{	
	/**
	 * Generated serial version (to remove the warning)
	 */
	private static final long serialVersionUID = -7902012165672573596L;
	private Agent agent;

	public InferenceBehavior(Agent a, long period) {
		super(a, period);
		agent = a;
	}

	@Override
	protected void onTick() {
		
		System.out.println("Inference...");
		
		((CognitionAgent)agent).getOnto().applyRules();
    	ArrayList<FuzzyTriple> activities = ((CognitionAgent)agent).getOnto().queryActivity();
    	
    	for(FuzzyTriple ft : activities)
    	{
    		System.out.println( ft.toString() );
    	}
	}
	
}