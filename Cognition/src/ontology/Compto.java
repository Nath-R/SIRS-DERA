package ontology;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

/**
 * Compto (Compare Ontologies)
 * 
 * Compto is a tool to compare ontologies though Jena by using different operators, distances and metrics.
 * Compto works is a set of functions that works on Jena's model.
 * Standard operation (union, intersection, etc...) are already supported by the model.
 * @author ramol_na
 *
 */
public class Compto 
{

	/**
	 * Check if A is included in B
	 * @return Rate of inclusion: 1.0 A is fully included in B, 0.0 A as not elements in B, otherwise some elements of A are in B
	 */
	static double inclusion(Model ontA, Model ontB)
	{
		double nbStmt = 0.0; //Total number of statements of B
		double nbComStmt = 0.0; //Number of statement of B that are also in A
		long startTime = System.nanoTime(); //Time measurement
		
		//Going through all statement of ontA
		StmtIterator itr = ontA.listStatements();		
		while(itr.hasNext())
		{
			Statement stmt = itr.next();
			nbStmt++;
			
			if(  stmt.getSubject().isAnon() && ontB.contains(null, stmt.getPredicate(), stmt.getObject()) ) 
			{
				nbComStmt++;
			}
			else if( stmt.getObject().isAnon() && ontB.contains(stmt.getSubject(), stmt.getPredicate()) )
			{
				nbComStmt++;
			}
			else if(stmt.getSubject().isAnon() && stmt.getObject().isAnon()) //Exclude property on two anonymous nodes
			{
				nbComStmt++;
			}
			else if(  ontB.contains(stmt) )
			{
				nbComStmt++;
			}
			else
			{
				System.out.println("Statement "+stmt.toString()+" not in B ! " );
			}
		}
		
		long estimatedTime = System.nanoTime() - startTime;
		System.out.println("Inclusion operation done in: "+estimatedTime/1000000000.0+" seconds");
		
		return nbComStmt / nbStmt;
	}
	
	/**
	 * Check if A and B are equal
	 * TODO
	 * @return Rate of similarity: 1.0 A and B are fully equal, 0.0 A and B have nothing in common, otherwise some elements of A and B are shared
	 */
	static double equality(Model ontA, Model ontB)
	{
		double nbComStmt = 0.0; //Number of statement of B that are also in A
		long startTime = System.nanoTime(); //Time measurement
		
		//Going through all statement of ontA
		StmtIterator itr = ontA.listStatements();		
		while(itr.hasNext())
		{
			Statement stmt = itr.next();
			
			if(  stmt.getSubject().isAnon() && ontB.contains(null, stmt.getPredicate(), stmt.getObject()) ) 
			{
				nbComStmt++;
			}
			else if( stmt.getObject().isAnon() && ontB.contains(stmt.getSubject(), stmt.getPredicate()) )
			{
				nbComStmt++;
			}
			else if(stmt.getSubject().isAnon() && stmt.getObject().isAnon()) //Exclude property on two anonymous nodes
			{
				nbComStmt++;
			}
			else if(  ontB.contains(stmt) )
			{
				nbComStmt++;
			}
		}
		
		long estimatedTime = System.nanoTime() - startTime;
		System.out.println("Equality operation done in: "+estimatedTime/1000000000.0+" seconds");
		
		//Compare the total number of common statement vs the total max number of statement 
		return nbComStmt / Math.max(ontA.size(), ontB.size()); //Caution: Size is an estimation
	}
	
	
}
