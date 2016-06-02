package code;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dell
 */
public class Event {
    String timeStamp;
    String idSensor;
    String status;
    String typeSensor;
    Double value = 0.0; // = object ?
    String location;
    double trust;
    
    //Semantic elements
    String subject;
    String predicate;
    String object;
    

    public Event(String timeStamp, String idSensor, String status, Double value) {
        this.timeStamp = timeStamp;
        this.idSensor = idSensor;
        this.status = status;
        this.value = value;
    }

    public Event(String timeStamp, String idSensor, String status,String typeSensor, String location, double trust) {
        this.timeStamp = timeStamp;
        this.idSensor = idSensor;
        this.status = status;
        this.typeSensor = typeSensor;
        this.location = location;
        this.trust = trust;
    }
    
    public Event(String timeStamp, String idSensor, String status,String typeSensor, String location, double trust, String subj, String pred, String obj) {
        this.timeStamp = timeStamp;
        this.idSensor = idSensor;
        this.status = status;
        this.typeSensor = typeSensor;
        this.location = location;
        this.trust = trust;
        this.subject = subj;
        this.predicate = pred;
        this.object = obj;
    }

    public Event(String timeStamp, String idSensor, String status, String location, double trust) {
        this.timeStamp = timeStamp;
        this.idSensor = idSensor;
        this.status = status;
        this.location = location;
        this.trust = trust;
    }
    
    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(String idSensor) {
        this.idSensor = idSensor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return " {" + "timeStamp=" + timeStamp + ", idSensor=" + idSensor + ", status=" + status + "}";
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getTrust() {
        return trust;
    }

    public void setTrust(double trust) {
        this.trust = trust;
    }

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}
    
    
    
}
