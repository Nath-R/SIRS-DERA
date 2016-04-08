package code;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import jade.content.lang.sl.ParseException;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import java.util.logging.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author dell
 */
public class SensingAgentPhone extends Agent {    

    //private static final Logger monLog = Logger.getLogger("code/SensingAgentPhone"); 
    int cTosPortNumber = 4030;
    String str;    
    Socket fromClientSocket= null ;
    PrintWriter pw;      

      
    protected void setup() {                

        //SensingAgentPhone.initLog();     
        try { 
            ServerSocket servSocket = new ServerSocket(cTosPortNumber);
            System.out.println("Sensing Agent Phone on " + cTosPortNumber);
            fromClientSocket = servSocket.accept();
        }
        catch (IOException ex) {
             Logger.getLogger(SensingAgentPhone.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        
        addBehaviour(new TickerBehaviour(this, 1000) {
        @Override
        protected void onTick() {
                     try {
                        pw = new PrintWriter(fromClientSocket.getOutputStream(), true);                         
                        BufferedReader br = new BufferedReader(new InputStreamReader(fromClientSocket.getInputStream()));
                        while ((str = br.readLine()) != null) {
                            //System.out.println("The message: " + str);                                                                      
                            
                            if (str.equals("bye")) {
                                pw.println("bye");
                                break;
                            } else {
                                //str = "Server returns " + str;                                        
                                pw.println(str);
                                //monLog.log(Level.INFO, str);
                                
                                String[] parts = str.split(" ");                            
                                String timestamp = parts[0] + " "+ parts[1];
                                //System.out.println("long : "+ parts.length +" " + timestamp);
                                String nameSensor = parts[2];
                                String statusSensor = parts[3];
                                String typeSensor = parts[4];
                                //System.out.println("event phone : " + e.getTimeStamp() + e.getIdSensor() + e.getStatus());
                                
                                //access to ontology
                                Ontology o = new Ontology();                                
                                ArrayList <String> res = o.queryOnto(nameSensor, typeSensor);
                                //System.out.println("resultat : " + res.get(0) + " " + res.get(1));                                
                                Event e = new Event(timestamp, nameSensor, statusSensor, typeSensor, res.get(0), Double.parseDouble(res.get(1)));                               
                                
                                //CEP ESPER
                                Configuration cepConfig = new Configuration();
                                cepConfig.addEventType("Event",Event.class.getName());
                                EPServiceProvider cep = EPServiceProviderManager.getProvider("myCEPEngine",cepConfig);
                                EPRuntime cepRT = cep.getEPRuntime();
                                // We register an EPL statement
                                EPAdministrator cepAdm = cep.getEPAdministrator();
                                EPStatement cepStatement = cepAdm.createEPL("select distinct timeStamp, idSensor, status, location, trust from Event.win:time(30 sec) where status =\"ON\" order by idSensor");
                                System.out.println("Sending event Phone :" + e);
                                cepRT.sendEvent(e);
                                //EPStatement cepStatement3 = cepAdm.createEPL("select sum(trust) as sumTrust from Event.win:time(10 sec) where status =\"ON\" ");                                                      
                                //EPStatement cepStatement2 = cepAdm.createEPL("select location, sum(trust) as sumTrust from Event.win:time(10 sec) where status =\"ON\" group by location ");                        
                                
                                cepStatement.addListener(new CEPListener());                                
                                //cepStatement3.addListener(new CEPListenerCount());
                                //cepStatement2.addListener(new CEPListenerSumTrust());  
                               // System.out.println("End Phone");
                            }
                        }
                        } catch (IOException ex) {
                        Logger.getLogger(SensingAgentPhone.class.getName()).log(Level.SEVERE, null, ex);
                    }					
        }
    });	        
  }
}

//pw.close();
//br.close();
//fromClientSocket.close(); 