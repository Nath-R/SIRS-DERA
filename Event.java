

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
    Double value = 0.0;

    public Event(String timeStamp, String idSensor, String status, Double value) {
        this.timeStamp = timeStamp;
        this.idSensor = idSensor;
        this.status = status;
        this.value = value;
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
        return "CEPStove{" + "timeStamp=" + timeStamp + ", idSensor=" + idSensor + ", status=" + status + ", value=" + value + '}';
    }
    
    
    
}
