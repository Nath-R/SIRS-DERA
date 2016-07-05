package code;

import java.io.File;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Imports
///////////////
import java.util.ArrayList;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;

/**
 * <p>TODO class comment</p>
 */
public class Ontology 
{
    /***********************************/
    /* Constants                       */
    /***********************************/

    // Directory where we've stored the local data files, such as pizza.rdf.owl
    public static final String SOURCE = "FSCEP/src/ontology/";

    // Pizza ontology namespace
    public static final String PIZZA_NS = "http://users.abo.fi/ndiaz/public/HumanActivity.owl#";
    //public static final String PIZZA_NS = SOURCE + "event_humanAct.owl#";

    /***********************************/
    /* Static variables                */
    /***********************************/

    @SuppressWarnings( value = "unused" )
    //private static final Logger log = LoggerFactory.getLogger(Ontology.class );

    /***********************************/
    /* Instance variables              */
    /***********************************/

    /***********************************/
    /* Constructors                    */
    /***********************************/

    /***********************************/
    /* External signature methods      */
    /***********************************/

    /**
     * @param args
     */
//    public static void main( String[] args ) {
//        Ontology o = new Ontology();
//        String name = "Motion1";
//        String sensorType = "MotionSensor";
//        ArrayList <String> res = o.queryOnto(name, sensorType);
//        System.out.println("resultat : " + res.get(0) + " " + res.get(1));
//    }

    public ArrayList <String> queryOnto(String name, String sensorType) {
        
        ArrayList <String> results = new ArrayList <String> ();
        
        OntModel m = getModel();
        loadData( m );
        String prefix = "prefix rdf: <" + RDF.getURI() + ">\n" +
                        "prefix owl: <" + OWL.getURI() + ">\n"+
                        "prefix xsd: <"+ XSD.getURI() +"> \n"+
                        "prefix rdfs: <" + RDFS.getURI() + ">\n" +
                        "prefix :<" + PIZZA_NS + ">\n" ;  
        
        String query = prefix +
                "select ?location ?trust \n "+ 
                "where { ?device rdf:type :"+sensorType+" . \n " +  
                //"where { ?device rdf:type :MotionSensor . \n " + 
                "        ?device :isLocatedIn ?location . \n" +
                "        ?device :hasName ?name . \n" +
                "         FILTER (?name = \""+name+"\") . \n"+
                "        ?device :hasTrust ?trust     \n" +        
                "}";
                        

        //System.out.println(query);
        
        results  = showQuery( m, query );
        
        return results;
    }

    protected OntModel getModel() {
        return ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
    }

    protected void loadData( Model m ) {
    	String path = new String(SOURCE + "event_humanAct.owl");
        FileManager.get().readModel( m, SOURCE + "event_humanAct.owl" );
    }

    protected ArrayList <String> showQuery( Model m, String q ) {
        
        ArrayList <String> res = new ArrayList <String> ();
        //System.out.println(q);
        Query query = QueryFactory.create( q );
        QueryExecution qexec = QueryExecutionFactory.create( query, m );
        try {
            ResultSet results = qexec.execSelect();            
            //ResultSetFormatter.out( results, m );
            
            while (results.hasNext()) {
                  QuerySolution solution = results.next();
                  // get the value of the variables in the select clause
                  String location = solution.get("location").asResource().getLocalName();
                  res.add(location);
                  String trust = solution.get("trust").asLiteral().getLexicalForm();
                  res.add(trust);
                  // print the output to stdout
                  //System.out.println("resultat : " + location + " " + trust);
            }
        }
        finally {
            qexec.close();
        }
        
        return res;
    }

    /***********************************/
    /* Internal implementation methods */
    /***********************************/

    /***********************************/
    /* Inner class definitions         */
    /***********************************/

}