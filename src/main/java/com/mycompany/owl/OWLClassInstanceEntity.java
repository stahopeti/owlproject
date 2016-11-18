/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.owl;

import java.util.ArrayList;

/**
 *
 * @author peterbencestahorszki
 */
public class OWLClassInstanceEntity {
    private String className;
    private ArrayList<OWLDataPropertyEntity> dataProperties;
    private ArrayList<OWLClassInstanceEntity> objectProperties;
   
    public OWLClassInstanceEntity(String className, ArrayList<OWLDataPropertyEntity> dataProperties, ArrayList<OWLClassInstanceEntity> objectProperties) {
        this.className = className;
        this.dataProperties = dataProperties;
        this.objectProperties = objectProperties;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void addObjectProperty(OWLClassInstanceEntity param){
        objectProperties.add(param);
    }
    
    public void addDataProperty(OWLDataPropertyEntity param){
        dataProperties.add(param);
    }
    
    public ArrayList<OWLDataPropertyEntity> getDataProperties() {
        return dataProperties;
    }

    public void setDataProperties(ArrayList<OWLDataPropertyEntity> dataProperties) {
        this.dataProperties = dataProperties;
    }

    public ArrayList<OWLClassInstanceEntity> getObjectProperties() {
        return objectProperties;
    }

    public void setObjectProperties(ArrayList<OWLClassInstanceEntity> objectProperties) {
        this.objectProperties = objectProperties;
    }
    
    public class OWLDataPropertyEntity{
        private String type;
        private String value;

        public OWLDataPropertyEntity(String type, String value) {
            this.type = type;
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "OWLDataProperty{" + "type=" + type + ", value=" + value + '}';
        }
    }
}