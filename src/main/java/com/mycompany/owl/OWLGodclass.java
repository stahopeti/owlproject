/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.owl;

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
    
    OWLGodclass() throws OWLOntologyCreationException{
        iri = IRI.create("file:/C:/nbn_pharmacology.owl");
        manager = create();
        dataFactory = manager.getOWLDataFactory();
        ontology = manager.loadOntologyFromOntologyDocument(iri);
        reasoner = reasonerFactory.createNonBufferingReasoner(ontology);
    }
    
    private OWLOntologyManager create() {
        OWLOntologyManager m = OWLManager.createOWLOntologyManager();
        return m;
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
    
    public void searchForClass(String className) throws OWLException{
        System.out.println("The class you are looking for: " + className);
        for(OWLClass cls: ontology.getClassesInSignature()){
            if(cls.getIRI().getShortForm().equals(className))
                printHierarchyStartingHere(cls);
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
    
    public void filterByDataPropertyValue(String filterProperty){
        System.out.println(filterProperty);
        Set<OWLNamedIndividual> instances = ontology.getIndividualsInSignature();
        Set<OWLNamedIndividual> filteredInstances = ontology.getIndividualsInSignature();
        filteredInstances.clear();
        
        
        if(!(instances.isEmpty())){
            for(OWLNamedIndividual individual : instances){
                if(isPropertyInIndividual(individual, filterProperty)) filteredInstances.add(individual);
            }
        }
        
        if(filteredInstances!=null){
            for(OWLNamedIndividual individual : filteredInstances){
                System.out.println("Individual: " + individual.getIRI().getShortForm());
                    System.out.println("Object property: ");showObjectPropertyOfIndividual(individual);
                    System.out.println("Data property: ");showDataPropertyOfIndividual(individual);

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
    
    private boolean isPropertyInIndividual(OWLNamedIndividual individual, String property){
        for(OWLDataProperty dp : ontology.getDataPropertiesInSignature()){
                Set<OWLLiteral> nodeset = reasoner.getDataPropertyValues(individual, dp);
                for(OWLLiteral value : nodeset){
//                   System.out.println("\t" + dp.getIRI().getShortForm() + ": " + value.getLiteral());
                     if(value.getLiteral().equalsIgnoreCase(property)) return true;
            }
        }
        return false;
    }
    
    private NodeSet<OWLNamedIndividual> getObjectPropertyOfIndividual(){
        return null;
    }    
    
    public void showDataPropertyOfIndividual(OWLNamedIndividual individual){
//        Set<OWLDataProperty> dp = individual.getDataPropertiesInSignature();
        /*Set<OWLLiteral> nodeset = getDataPropertyOfIndividual(individual);
        for(OWLLiteral value : nodeset){
            System.out.println("\t"  + ": " + value.getLiteral());
        }*/
        
        for(OWLDataProperty dp : ontology.getDataPropertiesInSignature()){
                Set<OWLLiteral> nodeset = reasoner.getDataPropertyValues(individual, dp);
                for(OWLLiteral value : nodeset){
                   System.out.println("\t" + dp.getIRI().getShortForm() + ": " + value.getLiteral());
            }
        }
        
    }
    
    private Set<OWLLiteral> getDataPropertyOfIndividual(OWLNamedIndividual individual){
        Set<OWLLiteral> nodeset = null;
        for(OWLDataProperty dp : ontology.getDataPropertiesInSignature()){
                nodeset = reasoner.getDataPropertyValues(individual, dp);
        }
        return nodeset;
    }
    
    public void printDataProperties(){
        System.out.println("DATA PROPERTIES");
        for(OWLDataProperty dp : ontology.getDataPropertiesInSignature())
            System.out.println(dp.getIRI().getShortForm());
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
}