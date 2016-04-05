/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.freedomotic.things.impl;

/**
 *
 * @author dell
 */
import com.freedomotic.things.EnvObjectLogic;
import com.freedomotic.model.ds.Config;
import com.freedomotic.model.object.BooleanBehavior;
import com.freedomotic.behaviors.BooleanBehaviorLogic;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.freedomotic.reactions.Command;
import com.freedomotic.reactions.Trigger;
import com.freedomotic.events.ObjectReceiveClick;
import java.sql.Timestamp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


/**
 *
 * @author Enrico
 */
public class Phone extends EnvObjectLogic {

    protected BooleanBehaviorLogic present;   
    private static final String BEHAVIOR_PRESENT = "present";
    protected final static String ACTION_TURN_ON = "turn on";
    protected final static String ACTION_TURN_OFF = "turn off";
    java.util.Date date= new java.util.Date();
    String str = "initialize";
    PrintWriter pw;
    BufferedReader br;

    @Override
    public void init() {
        
        Socket socket1;
        int portNumber = 4030;     
        
        try {
            socket1 = new Socket(InetAddress.getLocalHost(), portNumber);
            br = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
            pw = new PrintWriter(socket1.getOutputStream(), true);

            present = new BooleanBehaviorLogic((BooleanBehavior) getPojo().getBehavior(BEHAVIOR_PRESENT));
            //add a listener to values changes
            present.addListener(new BooleanBehaviorLogic.Listener() {
                @Override
                public void onTrue(Config params, boolean fireCommand) {
                    str = new Timestamp(date.getTime()) + " " +getPojo().getName() +"Sensor ON ";                 
                    pw.println(str);
                    try {
                        while ((str = br.readLine()) != null) {                            
                            pw.println("bye");
                            if (str.equals("bye"))
                                break;
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Phone.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (fireCommand) {
                        executePowerOn(params); //executes a turn on command and then sets the object behavior to on
                    } else {
                        setOn(); //sets the object behavior to on as a result from a notified value
                    }
                }

                @Override
                public void onFalse(Config params, boolean fireCommand) {
                    str = new Timestamp(date.getTime()) + " " +getPojo().getName() +"Sensor OFF ";
                    pw.println(str);
                    try {
                        while ((str = br.readLine()) != null) {
                            pw.println("bye");                        
                            if (str.equals("bye"))
                                break;
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Phone.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (fireCommand) {
                        executePowerOff(params); //executes a turn off command and then sets the object behavior to off
                    } else {
                        setOff(); //sets the object behavior to off as a result from a notified value
                    }
                }
            });


        //register this behavior to the superclass to make it visible to it
        registerBehavior(present);
        } catch (IOException ex) {
                Logger.getLogger(Phone.class.getName()).log(Level.SEVERE, null, ex);
        }
        //caches hardware level commands and builds user command
        super.init();
    }

    /**
     * Causes the execution of the related hardware command to turn on this
     * electric device, updates the object representation and notifies the
     * changes with an event.
     *
     * @param params
     */
    public void executePowerOn(Config params) {
        boolean executed = executeCommand(ACTION_TURN_ON, params);

        if (executed) {
            setOn();            
	    //System.out.println(new Timestamp(date.getTime()) + " " + getPojo().getName() +"Sensor ON ");
        }
    }

    /**
     * Causes the execution of the related hardware command to turn off this
     * electric device, updates the object representation and notifies the
     * changes with an event.
     *
     * @param params
     */
    public void executePowerOff(Config params) {
        boolean executed = executeCommand(ACTION_TURN_OFF, params);

        if (executed) {
            setOff();
            //System.out.println(new Timestamp(date.getTime()) + " " + getPojo().getName() +"Sensor OFF ");
        }
    }

    private void setOn() {
        LOG.log(Level.CONFIG, "Setting behavior ''present'' of object ''{0}'' to true", getPojo().getName());

        //if not already on
        if (present.getValue() != true) {
            //setting the object as present
            present.setValue(true);
            //setting the second view from the XML list (the one with the on light bulb image)
            getPojo().setCurrentRepresentation(1);
            setChanged(true);
        }
    }

    private void setOff() {
        LOG.log(Level.CONFIG, "Setting behavior ''present'' of object ''{0}'' to false", getPojo().getName());

        //if not already off
        if (present.getValue() != false) {
            present.setValue(false);
            getPojo().setCurrentRepresentation(0);
            setChanged(true);
        }
    }

    @Override
    protected void createCommands() {
        Command setOn = new Command();
        setOn.setName("Turn on " + getPojo().getName());
        setOn.setDescription(getPojo().getName() + " turns on");
        setOn.setReceiver("app.events.sensors.behavior.request.objects");
        setOn.setProperty("object", getPojo().getName());
        setOn.setProperty("behavior", BEHAVIOR_PRESENT);
        setOn.setProperty("value", BooleanBehavior.VALUE_TRUE);
        commandRepository.create(setOn);

        Command setOff = new Command();
        setOff.setName("Turn off " + getPojo().getName());
        setOff.setDescription(getPojo().getName() + " turns off");
        setOff.setReceiver("app.events.sensors.behavior.request.objects");
        setOff.setProperty("object", getPojo().getName());
        setOff.setProperty("behavior", BEHAVIOR_PRESENT);
        setOff.setProperty("value", BooleanBehavior.VALUE_FALSE);
        commandRepository.create(setOff);

        Command switchPower = new Command();
        switchPower.setName("Switch " + getPojo().getName() + " power");
        switchPower.setDescription("switches the power of " + getPojo().getName());
        switchPower.setReceiver("app.events.sensors.behavior.request.objects");
        switchPower.setProperty("object", getPojo().getName());
        switchPower.setProperty("behavior", BEHAVIOR_PRESENT);
        switchPower.setProperty("value", BooleanBehavior.VALUE_OPPOSITE);
        commandRepository.create(switchPower);

    }

    /**
     *
     */
    @Override
    protected void createTriggers() {
        
        Trigger clickedPhone = new Trigger();
        clickedPhone.setName("When " + this.getPojo().getName() + " is clicked");
        clickedPhone.setChannel("app.event.sensor.object.behavior.clicked");
        clickedPhone.getPayload().addStatement("object.name", this.getPojo().getName());
        clickedPhone.getPayload().addStatement("click", ObjectReceiveClick.SINGLE_CLICK);
        clickedPhone.setPersistence(false);        
                   
        triggerRepository.create(clickedPhone);
        
            
        
        
    }
    private static final Logger LOG = Logger.getLogger(Phone.class.getName());
}