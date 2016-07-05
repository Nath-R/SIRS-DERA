package code;

public class SensingAgentTest extends SensingAgent {

	/**
	 * Generated automatically
	 */
	private static final long serialVersionUID = 8797994455677577634L;

	@Override
	public void handle_input_data(String data) {
		
		//Just print the received data
		System.out.println("Test agent received data:"+data);
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
