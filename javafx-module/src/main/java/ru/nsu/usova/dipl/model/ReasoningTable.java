package ru.nsu.usova.dipl.model;

import javafx.beans.property.SimpleStringProperty;

public class ReasoningTable {
    private SimpleStringProperty premise;
    private SimpleStringProperty result;
    private SimpleStringProperty id;

   public ReasoningTable(String premise, String result, String id) {
       this.premise = new SimpleStringProperty(premise);
       this.result = new SimpleStringProperty(result);
       this.id = new SimpleStringProperty(id);
   }

    public ReasoningTable(String premise, String result) {
        this.premise = new SimpleStringProperty(premise);
        this.result = new SimpleStringProperty(result);
    }

    public void setPremise(String premise) {
        this.premise.set(premise);
    }

    public void setResult(String result) {
        this.result.set(result);
    }

    public String getPremise() {
        return premise.get();
    }

    public String getResult() {
        return result.get();
    }

    public String getId() {
       return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }
}
