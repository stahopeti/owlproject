/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.owl;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

/**
 *
 * @author peterbencestahorszki
 */
public class OWLGodclass {
    IRI iri;
    OWLOntologyManager manager;
    OWLDataFactory dataFactory;
    OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
    OWLOntology ontology;
    OWLReasoner reasoner;
    ArrayList<OWLClassInstanceEntity> classEntities;
    
    public OWLGodclass() throws OWLOntologyCreationException{
        File f = new File("nbn_pharmacology.owl");
        iri = IRI.create(f);
        manager = create();
        dataFactory = manager.getOWLDataFactory();
        ontology = manager.loadOntologyFromOntologyDocument(iri);
        reasoner = reasonerFactory.createNonBufferingReasoner(ontology);
        classEntities = null;
    }
    
    private OWLOntologyManager create() {
        OWLOntologyManager m = OWLManager.createOWLOntologyManager();
        return m;
    }
    
    public ArrayList<String> getObjectPropertyTypes(){
        ArrayList<String> properties = new ArrayList<String>();
        System.out.println("\n\nObjectProperties:");
            for(OWLObjectProperty op : ontology.getObjectPropertiesInSignature()){
                properties.add(op.getIRI().getShortForm());
                System.out.println("objectproperty: " + op.getIRI().getShortForm());
        }
        return properties;
    }
    
    public ArrayList<String> getDataPropertyTypes(){
        ArrayList<String> properties = new ArrayList<String>();
        
        System.out.println("\n\nDataProperties:");
            for(OWLDataProperty dp : ontology.getDataPropertiesInSignature()){
                properties.add(dp.getIRI().getShortForm());
                System.out.println("objectproperty: " + dp.getIRI().getShortForm());
        }
        return properties;
    }

    public ArrayList<String> getUKBCodeWhereATCCodeIs(
            String firstLevel, String secondLevel, String thirdLevel, String fourthLevel){
        /*lekérjük az összes ukb-atc kód párt*/
        
        System.out.println("ATC code mask: " + 
                firstLevel + secondLevel + thirdLevel + fourthLevel);
        
        boolean matchingMask1 = false;
        boolean matchingMask2 = false;
        boolean matchingMask3 = false;
        boolean matchingMask4 = false;
        
        ArrayList<String> matchingUKBCodes = new ArrayList<String>();
        
        ArrayList<Drug> drug = getDrugs();
        
        for(Drug drugs : drug){
            if(!drugs.getAtc_code().equalsIgnoreCase("")){
                
                if(drugs.getFirstLevelATC().equals(firstLevel)){
                    matchingMask1 = true;
                } else {
                    if(firstLevel.equals("*")) {
                        matchingMask1 = true;
                    } else {
                        matchingMask1 = false;
                    }
                }
                
                if(drugs.getSecondLevelATC().equals(secondLevel)){
                    matchingMask2 = true;
                } else {
                    if(secondLevel.equals("**")) {
                        matchingMask2 = true;
                    } else {
                        matchingMask2 = false;
                    }
                }
                
                if(drugs.getThirdLevelATC().equals(thirdLevel)){
                    matchingMask3 = true;
                } else {
                    if(thirdLevel.equals("*")) {
                        matchingMask3 = true;
                    } else {
                        matchingMask3 = false;
                    }
                }
                if(drugs.getFourthLevelATC().equals(fourthLevel)){
                    matchingMask4 = true;
                } else {
                    if(fourthLevel.equals("*")) {
                        matchingMask4 = true;
                    } else {
                        matchingMask4 = false;
                    }
                }
                
                if(matchingMask1 && matchingMask2 && matchingMask3 && matchingMask4)
                    matchingUKBCodes.add(drugs.getUkb_code());
            } else {
                if(firstLevel.equals("") && secondLevel.equals("") && thirdLevel.equals("") && fourthLevel.equals("")){
                    matchingUKBCodes.add(drugs.getUkb_code());
                }
            }
        }
        
        if(matchingUKBCodes.size() > 0) System.out.println("VAN BENNE");
                else System.out.println("NINCS BENNE");
        return matchingUKBCodes;
    }
    
    public void getAllInstances(){
        Set<OWLNamedIndividual> instances = ontology.getIndividualsInSignature();
        for(OWLNamedIndividual instance : instances){
            
            classEntities.add(new OWLClassInstanceEntity(
            instance.getIRI().getShortForm()
            ,null
            ,null)
            );
        }
        
        System.out.println("\n\nEntities:");
        for (OWLClassInstanceEntity entiti : classEntities) {
            System.out.println(entiti.toString());
        }
    }
    
    public void showInstancesOfClass(String className) throws OWLException{
        NodeSet<OWLNamedIndividual> instances = getInstancesOfOntologyClass(className);
        
        if(!(instances.isEmpty())){
            for(Node<OWLNamedIndividual> individual : instances){
                System.out.println("Individual: " + individual.getRepresentativeElement().getIRI().getShortForm());
                System.out.println("Object property: ");showObjectPropertyOfIndividual(individual.getRepresentativeElement());
                System.out.println("Data property: ");showDataPropertyOfIndividual(individual.getRepresentativeElement());
            }
        }
    }
    
    public void showObjectPropertyOfIndividual(OWLNamedIndividual individual){
        for(OWLObjectProperty op : ontology.getObjectPropertiesInSignature()){
                NodeSet<OWLNamedIndividual> nodeset = reasoner.getObjectPropertyValues(individual, op);
                for(OWLNamedIndividual value : nodeset.getFlattened()){
                   OWLClass range = null;
                   for(Node<OWLClass> temp : reasoner.getObjectPropertyRanges(op, true)){
                       range = temp.getRepresentativeElement();
                   }
                   System.out.println("\t"+ range.getIRI().getShortForm() + ": " +value.getIRI().getShortForm());
            }
        }
    }
    
    public void showDataPropertyOfIndividual(OWLNamedIndividual individual){
        for(OWLDataProperty dp : ontology.getDataPropertiesInSignature()){
                Set<OWLLiteral> nodeset = reasoner.getDataPropertyValues(individual, dp);
                for(OWLLiteral value : nodeset){
                   System.out.println("\t" + dp.getIRI().getShortForm() + ": " + value.getLiteral());
            }
        }
    }
    
    public void searchForClass(String className) throws OWLException{
        System.out.println("The class you are looking for: " + className);
        for(OWLClass cls: ontology.getClassesInSignature()){
            if(cls.getIRI().getShortForm().equals(className))
                printHierarchyStartingHere(cls);
        }
    }
    
    private ArrayList<Drug> getDrugs(){
        NodeSet<OWLNamedIndividual> instances = getInstancesOfOntologyClass("Drug");
        
        ArrayList<Drug> drugList = new ArrayList<Drug>();
        
        if(!(instances.isEmpty())){
            for(Node<OWLNamedIndividual> individual : instances){
                drugList.add(returnUKBATCpair(individual.getRepresentativeElement()));
            }
        }
        return drugList;
    }
    
    private NodeSet<OWLNamedIndividual> getInstancesOfOntologyClass(String className){
        NodeSet<OWLNamedIndividual> instances = null;
        for(OWLClass cls: ontology.getClassesInSignature()){
            if(cls.getIRI().getShortForm().equals(className))
                instances = reasoner.getInstances(cls, true);
        }
        
        return instances;
        /*
        if(!(instances.isEmpty())){
            for(Node<OWLNamedIndividual> individual : instances){
                System.out.println("Individual: " + individual.getRepresentativeElement().getIRI().getShortForm());
                System.out.println("Object property: ");showObjectPropertyOfIndividual(individual.getRepresentativeElement());
                System.out.println("Data property: ");showDataPropertyOfIndividual(individual.getRepresentativeElement());
            }
        }*/
    }
    
    public ArrayList<String> filterByDataPropertyValue(String filterPropertyName, String filterPropertyValue){
        System.out.println(filterPropertyValue);
        Set<OWLNamedIndividual> instances = ontology.getIndividualsInSignature();
        Set<OWLNamedIndividual> filteredInstances = ontology.getIndividualsInSignature();
        filteredInstances.clear();
        
        ArrayList<String> filtered = new ArrayList<String>();
        
        if(!(instances.isEmpty())){
            for(OWLNamedIndividual individual : instances){
                if(isDataPropertyInIndividual(individual, filterPropertyName, filterPropertyValue)) filteredInstances.add(individual);
            }
        }
        
        if(filteredInstances!=null){
            for(OWLNamedIndividual individual : filteredInstances){
                System.out.println("Individual: " + individual.getIRI().getShortForm());
                System.out.println("Object property: ");showObjectPropertyOfIndividual(individual);
                System.out.println("Data property: ");showDataPropertyOfIndividual(individual);
                filtered.add(individual.getIRI().getShortForm());
            }
        }
        return filtered;
    }
    
    public ArrayList<String> filterUKBByDataPropertyValue(String filterPropertyName, String filterPropertyValue){
        System.out.println(filterPropertyValue);
        Set<OWLNamedIndividual> instances = ontology.getIndividualsInSignature();
        Set<OWLNamedIndividual> filteredInstances = ontology.getIndividualsInSignature();
        filteredInstances.clear();
        
        ArrayList<String> filtered = new ArrayList<String>();
        
        if(!(instances.isEmpty())){
            for(OWLNamedIndividual individual : instances){
                if(isDataPropertyInIndividual(individual, filterPropertyName, filterPropertyValue)) {
                    filteredInstances.add(individual);
                }
            }
        }
        
        if(filteredInstances!=null){
            for(OWLNamedIndividual individual : filteredInstances){
                System.out.println("Individual: " + individual.getIRI().getShortForm());
                System.out.println("Object property: ");showObjectPropertyOfIndividual(individual);
                System.out.println("Data property: ");showDataPropertyOfIndividual(individual);
                filtered.add(getUKBCode(individual));
           }
        }
        return filtered;
    }

    private String getUKBCode(OWLNamedIndividual individual){
        for(OWLDataProperty dp : ontology.getDataPropertiesInSignature()){
                Set<OWLLiteral> nodeset = reasoner.getDataPropertyValues(individual, dp);
                for(OWLLiteral value : nodeset){
                     if(value.getDatatype().getIRI().getShortForm().equals("ukb_code"));
                         return value.getLiteral(); 
                }
        }
        return "";
    }
    
    public ArrayList<String> filterByObjectPropertyValue(String filterPropertyName, String filterPropertyValue){
        System.out.println(filterPropertyValue);
        Set<OWLNamedIndividual> instances = ontology.getIndividualsInSignature();
        Set<OWLNamedIndividual> filteredInstances = ontology.getIndividualsInSignature();
        filteredInstances.clear();
        
        ArrayList<String> filtered = new ArrayList<String>();
        
        if(!(instances.isEmpty())){
            for(OWLNamedIndividual individual : instances){
                if(isObjectPropertyInIndividual(individual, filterPropertyName, filterPropertyValue)) filteredInstances.add(individual);
            }
        }
        
        if(filteredInstances!=null){
            for(OWLNamedIndividual individual : filteredInstances){
                System.out.println("Individual: " + individual.getIRI().getShortForm());
                System.out.println("Object property: ");showObjectPropertyOfIndividual(individual);
                System.out.println("Data property: ");showDataPropertyOfIndividual(individual);
                filtered.add(individual.getIRI().getShortForm());     
            }
        }
        return filtered;
    }
    
    private boolean isObjectPropertyInIndividual(OWLNamedIndividual individual, String propertyName, String propertyValue){
        for(OWLObjectProperty dp : ontology.getObjectPropertiesInSignature()){
                NodeSet<OWLNamedIndividual> nodeset = reasoner.getObjectPropertyValues(individual, dp);
                for(Node<OWLNamedIndividual> value : nodeset){
//                   System.out.println("\t" + dp.getIRI().getShortForm() + ": " + value.getLiteral());
                    if(dp.getIRI().getShortForm().toString().equals(propertyName))
                        if(value.getRepresentativeElement().getIRI().getShortForm().equalsIgnoreCase(propertyValue))
                            return true;
            }
        }
        return false;
    }
    
    private boolean isDataPropertyInIndividual(OWLNamedIndividual individual, String propertyName, String propertyValue){
        for(OWLDataProperty dp : ontology.getDataPropertiesInSignature()){
                Set<OWLLiteral> nodeset = reasoner.getDataPropertyValues(individual, dp);
                for(OWLLiteral value : nodeset){
                     if(dp.getIRI().getShortForm().equals(propertyName))
                         if(value.getLiteral().equalsIgnoreCase(propertyValue))
                             return true;
            }
        }
        return false;
    }
    
    private Drug returnUKBATCpair(OWLNamedIndividual individual){
        String UKB = "";
        String ATC = "";
        for(OWLDataProperty dp : ontology.getDataPropertiesInSignature()){
            Set<OWLLiteral> nodeset = reasoner.getDataPropertyValues(individual, dp);
            for(OWLLiteral value : nodeset){
                if(dp.getIRI().getShortForm().equalsIgnoreCase("ukb_code"))
                    UKB = value.getLiteral();
                
                if(dp.getIRI().getShortForm().equalsIgnoreCase("atc_code"))
                    ATC = value.getLiteral();
            } 
        }
        return (new Drug(UKB, ATC));
    }
    
    private Set<OWLNamedIndividual> getObjectPropertyOfIndividual(OWLNamedIndividual individual){
        NodeSet<OWLNamedIndividual> nodeset = null;
        for(OWLObjectProperty op : ontology.getObjectPropertiesInSignature()){
                nodeset = reasoner.getObjectPropertyValues(individual, op);
                for(OWLNamedIndividual value : nodeset.getFlattened()){
                   OWLClass range = null;
                   for(Node<OWLClass> temp : reasoner.getObjectPropertyRanges(op, true)){
                       range = temp.getRepresentativeElement();
                   }
                   System.out.println("\t"+ range.getIRI().getShortForm() + ": " +value.getIRI().getShortForm());
            }
        }
        return nodeset.getFlattened();
    }    
    
    private Set<OWLLiteral> getDataPropertyOfIndividual(OWLNamedIndividual individual){
        Set<OWLLiteral> nodeset = null;
        for(OWLDataProperty dp : ontology.getDataPropertiesInSignature()){
                nodeset = reasoner.getDataPropertyValues(individual, dp);
        }
        return nodeset;
    }
        
    public void printHierarchyWhole() throws OWLException {
        printHierarchy(dataFactory.getOWLThing(), 0);
        /* Now print out any unsatisfiable classes */
        System.out.println("unsatisfiable: ");
        for (OWLClass cl : ontology.getClassesInSignature()) {
            if (!reasoner.isSatisfiable(cl)) {
                System.out.println("XXX: " + (cl));
            }
        }
        reasoner.dispose();
    }
    
    public void printHierarchyStartingHere(OWLClass clazz) throws OWLException{
        printHierarchy(clazz, 0);
        /* Now print out any unsatisfiable classes */
        for (OWLClass cl : ontology.getClassesInSignature()) {
            if (!reasoner.isSatisfiable(cl)) {
                System.out.println("XXX: " + (cl));
            }
        }
        reasoner.dispose();
    }
    
    private void printHierarchy(OWLClass clazz, int level) throws OWLException {
        if (reasoner.isSatisfiable(clazz)) {
        
            for (int i = 0; i < level * 4; i++) {
                System.out.print(" ");
            }
            System.out.println((clazz.getIRI().getShortForm()));
            
            for (OWLClass child : reasoner.getSubClasses(clazz, true).getFlattened()) {
                if (!child.equals(clazz)) {
                    printHierarchy(child, level + 1);
                }
            }
        }
    }

    private class Drug{
    
        private String ukb_code;
        private String atc_code;

        @Override
        public String toString() {
            return "Drug{" + "ukb_code=" + ukb_code + ", atc_code=" + atc_code + '}';
        }

        public String getFirstLevelATC(){
            return "" + atc_code.charAt(0);
        }
        
        public String getSecondLevelATC(){
            return (String.valueOf(atc_code.charAt(1)) + String.valueOf(atc_code.charAt(2)));
        }
        
        public String getThirdLevelATC(){
            return atc_code.substring(3,4);
        }
        
        public String getFourthLevelATC(){
            return atc_code.substring(4, 5);
        }
        
        public Drug(String ukb_code, String atc_code) {
            this.ukb_code = ukb_code;
            this.atc_code = atc_code;
        }

        public String getUkb_code() {
            return ukb_code;
        }

        public void setUkb_code(String ukb_code) {
            this.ukb_code = ukb_code;
        }

        public String getAtc_code() {
            return atc_code;
        }

        public void setAtc_code(String atc_code) {
            this.atc_code = atc_code;
        }
        
    }
}