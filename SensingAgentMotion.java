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
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author dell
 */
public class SensingAgentMotion extends Agent {    

    private static final Logger monLog = Logger.getLogger("SensingAgentMotion"); 
    int cTosPortNumber = 4031;
    String str;    
    Socket fromClientSocket= null ;
    PrintWriter pw;    

    public static void initLog(){
        // Test log :      
        monLog.setLevel(Level.ALL); //pour envoyer les messages de tous les niveaux
        FileHandler aFileHandler;
        try {     
            aFileHandler = new FileHandler("logs/motion/trace.log", true);
            monLog.addHandler(aFileHandler);            
            SimpleFormatter formatter = new SimpleFormatter();            
            aFileHandler.setFormatter(formatter);
        } catch (IOException e1) {
            // TODO Bloc catch auto-généré
            e1.printStackTrace();
        }     
    }
      
    protected void setup() {                

        SensingAgentMotion.initLog();     
        try { 
            ServerSocket servSocket = new ServerSocket(cTosPortNumber);
            System.out.println("Sensing Agent Motion on " + cTosPortNumber);
            fromClientSocket = servSocket.accept();
        }
        catch (IOException ex) {
             Logger.getLogger(SensingAgentMotion.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        ParallelBehaviour comportementparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ANY);
        comportementparallele.addSubBehaviour(new TickerBehaviour(this,9000){
                @Override
                protected void onTick() {                        
                        monLog.log(Level.INFO, "Segment END \n");
                }
        });
        comportementparallele.addSubBehaviour(new TickerBehaviour(this,1000){
        
        @Override
        protected void onTick() {
                     try {
                        pw = new PrintWriter(fromClientSocket.getOutputStream(), true);                         
                        BufferedReader br = new BufferedReader(new InputStreamReader(fromClientSocket.getInputStream()));
                        while ((str = br.readLine()) != null) {
                            System.out.println("The message: " + str);                                                                      
                            
                            if (str.equals("bye")) {
                                pw.println("bye");
                                break;
                            } else {
                                //str = "Server returns " + str;                                        
                                pw.println(str);
                                monLog.log(Level.INFO, str);
                                
                                String[] parts = str.split(" ");                            
                                String timestamp = parts[0] + " "+ parts[1];
                                System.out.println("long : "+ parts.length +" " + timestamp);
                                Event e = new Event(timestamp, parts[2], parts[3], 0.0);
                                System.out.println("event motion : " + e.getTimeStamp() + e.getIdSensor() + e.getStatus());
                                
                                //CEP ESPER
                                Configuration cepConfig = new Configuration();
                                cepConfig.addEventType("Event",Event.class.getName());
                                EPServiceProvider cep = EPServiceProviderManager.getProvider("myCEPEngine",cepConfig);
                                EPRuntime cepRT = cep.getEPRuntime();
                                // We register an EPL statement
                                EPAdministrator cepAdm = cep.getEPAdministrator();
                                EPStatement cepStatement = cepAdm.createEPL("select idSensor from Event.win:time(3 sec)");
                                System.out.println("Sending event :" + e);
                                cepRT.sendEvent(e);
                                cepStatement.addListener(new CEPListener());
                            }
                        }
                        } catch (IOException ex) {
                        Logger.getLogger(SensingAgentMotion.class.getName()).log(Level.SEVERE, null, ex);
                    }					
        }
    });	
        addBehaviour(comportementparallele);
  }
}

//pw.close();
//br.close();
//fromClientSocket.close(); 