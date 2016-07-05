package ontology;

/**
 * A simple triple string representation.
 * It also carries fuzzy weights when present.
 * TODO export to Jena triple
 * @author ramol_na
 *
 */
public class FuzzyTriple {

	///** Attributes **///
	
	private String subject;
	private String predicate;
	private String object;
	private String fuzzyAnnot;
	private Double fuzzyWeight; //TODO
	
	
	///** Methods **///
	
	public FuzzyTriple(String subj, String pred, String obj)
	{
		subject = subj;
		predicate = pred;
		object = obj;
	}
	
	public FuzzyTriple(String subj, String pred, String obj, Double trust)
	{
		subject = subj;
		predicate = pred;
		object = obj;
		fuzzyWeight = trust;
	}
	
	/**
	 * Convert a serial to string to separated elements.
	 * Separator is a space.
	 * @param serial the serialized triple
	 */
	public FuzzyTriple(String serial)
	{
		serial.trim(); //Remove the last space and line return if needed
		String splitStr[] = serial.split(" ");
		
		if(splitStr.length < 3) //not enough element
		{
			System.out.println("FuzzyTriple | "+"Could not load FuzzyTriple from string");
			return;
		}
		else
		{
			subject = splitStr[0];
			predicate = splitStr[1];
			object = splitStr[2];
			
			if(splitStr.length > 3) //Forth elements: fuzzy annot
			{ fuzzyAnnot = splitStr[3]; }
		}
		
	}
	
	
	public String getSubject()
	{ return subject; }
	
	public String getPredicate()
	{ return predicate; }
	
	public String getObject()
	{ return object; }
	
	public String getFuzzyAnnotation()
	{ return fuzzyAnnot; }
	
	public Double getFuzzyWeight()
	{ return fuzzyWeight; }
	
	public String toString()
	{
		String ret;
		
		ret = subject+" "+predicate+" "+object+" ";
		if( fuzzyWeight != null )
		{ ret += fuzzyWeight.toString()+" "; }
		else if( fuzzyAnnot != null )
		{ ret += fuzzyAnnot+" ";}
		ret += "\n";
		
		return ret;
	}
}
