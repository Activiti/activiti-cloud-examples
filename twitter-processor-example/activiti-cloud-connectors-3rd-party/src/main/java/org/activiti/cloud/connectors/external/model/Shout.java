package org.activiti.cloud.connectors.external.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Shout {

    @JsonProperty("INPUT")
    private String INPUT;
    @JsonProperty("OUTPUT")
    private String OUTPUT;

    public Shout() {
    }

    public Shout(String INPUT,
                 String OUTPUT) {
        this.INPUT = INPUT;
        this.OUTPUT = OUTPUT;
    }

    public Shout(String INPUT) {
        this.INPUT = INPUT;
    }

    public String getINPUT() {
        return INPUT;
    }

    public void setINPUT(String INPUT) {
        this.INPUT = INPUT;
    }

    public String getOUTPUT() {
        return OUTPUT;
    }

    public void setOUTPUT(String OUTPUT) {
        this.OUTPUT = OUTPUT;
    }

    @Override
    public String toString() {
        return "Shout{" +
                "INPUT='" + INPUT + '\'' +
                ", OUTPUT='" + OUTPUT + '\'' +
                '}';
    }
}
