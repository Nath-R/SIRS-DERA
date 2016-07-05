package ontology;

import java.util.ArrayList;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;

public class OntoManager {

	public static final String SOURCE = "Cognition/res/";
	
	private OntModel model;
	private InfModel actModel;
	
	public OntoManager()
	{
		//Loading the model
		//TODO reset the model (copy the T box)
		model = getModel();
		loadData(model);		
	}	
	
	protected OntModel getModel() {
        return ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
    }

    protected void loadData( Model m ) {
    	String path = new String(SOURCE + "event_humanAct_context.owl");
        FileManager.get().readModel( m, path );
    }

	
    /**
     * Add a triple into the ontology
     */
    public void updateOntology( FuzzyTriple t )
    {
    	System.out.println("Updating context ontology...");
    	
    	String prefix = "prefix rdf: <" + RDF.getURI() + ">\n" +
                "prefix owl: <" + OWL.getURI() + ">\n"+
                "prefix xsd: <"+ XSD.getURI() +"> \n"+
                "prefix rdfs: <" + RDFS.getURI() + ">\n" +
                "prefix :<" + "http://users.abo.fi/ndiaz/public/HumanActivity.owl#" + ">\n" ;  
    	
    	String query = prefix + "insert data {"+t.getSubject()+" "+t.getPredicate()+" "+t.getObject()+"}";
    	//TODO fuzzy 
    	
    	System.out.println(query);
    	
    	UpdateAction.parseExecute(query, model);
    	

    }
    
    /**
     * Apply rules for activity recognition
     */
    public void applyRules()
    {
    	Reasoner reasoner = new GenericRuleReasoner( Rule.rulesFromURL(SOURCE+ "rules.txt" ) );
    	
    	actModel = ModelFactory.createInfModel( reasoner, model );
    }
    
    /**
     * Query ongoing activity
     */
    public ArrayList<FuzzyTriple> queryActivity()
    {
    	 ArrayList <FuzzyTriple> ret = new ArrayList <FuzzyTriple> ();

         String prefix = "prefix rdf: <" + RDF.getURI() + ">\n" +
                         "prefix owl: <" + OWL.getURI() + ">\n"+
                         "prefix xsd: <"+ XSD.getURI() +"> \n"+
                         "prefix rdfs: <" + RDFS.getURI() + ">\n" +
                         "prefix :<" + "http://users.abo.fi/ndiaz/public/HumanActivity.owl#" + ">\n" ;  
         
         String queryStr = prefix +
                 "select ?u ?act \n "+ 
                 "where { ?u :performsActivity ?act . \n " +  
                 		" ?u rdf:type :User \n "+
                 "}";
                         

         //System.out.println(query);
         
         Query query = QueryFactory.create( queryStr );
         QueryExecution qexec = QueryExecutionFactory.create( query, actModel );
         
         try {
             ResultSet results = qexec.execSelect();            
             //ResultSetFormatter.out( results, m );
             
             while (results.hasNext()) {
                   QuerySolution solution = results.next();
                   
                   String user = solution.get("u").asResource().getLocalName();
                   String act = solution.get("act").asResource().getLocalName();
                   
                   ret.add( new FuzzyTriple(user, "performsActivity", act) );
             }
         }
         finally {
             qexec.close();
         }
         
         return ret;
    }
    
}
