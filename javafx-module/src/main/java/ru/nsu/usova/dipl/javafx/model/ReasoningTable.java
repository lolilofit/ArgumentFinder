package ru.nsu.usova.dipl.javafx.model;

import javafx.beans.property.SimpleStringProperty;

public class ReasoningTable {
    private SimpleStringProperty premise;
    private SimpleStringProperty result;
    private SimpleStringProperty samePartRelationType;
    private SimpleStringProperty structuralRelationType;
    private SimpleStringProperty id;

   public ReasoningTable(String premise, String result, String samePartRelationType, String structuralRelationType, String id) {
       this.premise = new SimpleStringProperty(premise);
       this.result = new SimpleStringProperty(result);
       this.samePartRelationType = new SimpleStringProperty(samePartRelationType);
       this.structuralRelationType = new SimpleStringProperty(structuralRelationType);
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

    public String getSamePartRelationType() {
        return samePartRelationType.get();
    }

    public String getStructuralRelationType() {
        return structuralRelationType.get();
    }

    public void setSamePartRelationType(String samePartRelationType) {
        this.samePartRelationType.set(samePartRelationType);
    }

    public void setStructuralRelationType(String structuralRelationType) {
        this.structuralRelationType.set(structuralRelationType);
    }
}
