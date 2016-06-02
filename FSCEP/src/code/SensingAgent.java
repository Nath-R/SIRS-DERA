package code;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

/**
 * Sensing agent.
 * A jade agent that acquire data from sensors (physical or virtual).
 * This abstraction only deals with UDP communication.
 * It shall then be specialized according to the sensors (data) and the required process.
 * @author ramol_na
 *
 */
public abstract class SensingAgent extends Agent 
{
	///** Attributes **///
	
	/**
	 * Generated serial version
	 */
	private static final long serialVersionUID = -3623219027169689745L;
	
	/**
	 * Reading port.
	 * Given as parameters when creating the agent.
	 */
	protected int port;
	
	/**
	 * Data type acquired by the agent.
	 * TODO given by parameters
	 */
	protected String dataType;
	
	
	
	///** Methods **///
	
	/**
	 * Setup functions
	 */
	protected void setup()
	{
		Object[] args = getArguments();
		port = Integer.parseInt( args[0].toString() );
		addBehaviour( new SensingAgentBehavior(this, 500) );
		
		System.out.println("SensingAgent | "+"Creating agent on port: "+port);
		
		init();
	}
	
	/**
	 * Initialization of the agents.
	 * It can be use to initialize esper stuffs for instance.
	 */
	public abstract void init();
	
	/**
	 * Manage the received data.
	 * Generates an event according to the type of data.
	 * It is abstracted and shall be defined in a child class.
	 */
	public abstract void handle_input_data( String data );
	
	/**
	 * Return the reading port
	 * @see port
	 */
	int get_port()
	{ return port; }
}





/**
 * Main behavior of the agent.
 * Handle the communication itself.
 */
class SensingAgentBehavior extends TickerBehaviour
{
	///** Attributes **///
	
	/**
	 * Default and auto generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parent agent
	 */
	SensingAgent parent;
	
	/**
	 * UDP socket
	 */
	DatagramSocket serverSocket;

	
	
	///** Methods **///
	
	/**
	 * Constructor
	 * @param a
	 * @param period
	 */	
	public SensingAgentBehavior(SensingAgent a, long period) {
		super(a, period);
		
		parent = a;
		
		//Getting the UDP socket ready to go
		try {
			serverSocket = new DatagramSocket( a.get_port() );
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Periodically receive the data and handle them.
	 */
	@Override
	protected void onTick() {
		
		try {
			
			byte[] receivedData = new byte[512];
			//Receiving data
			DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
			serverSocket.receive(receivedPacket);
			
			String dataStr = new String( receivedPacket.getData() );
			System.out.println("SensingAgent | "+"Received data: "+dataStr);
			
			parent.handle_input_data(dataStr);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
