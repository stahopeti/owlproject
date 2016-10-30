/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.owl;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
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
    iri = IRI.create("file:/C:/pharmacology_drug.owl");
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
    
    private void labelFor(OWLClass cls) {
        // Get the annotations on the class that use the label property (rdfs:label)
        for (OWLAnnotation annotation : cls.getAnnotations(ontology, dataFactory.getRDFSLabel())) {
          if (annotation.getValue() instanceof OWLLiteral) {
            OWLLiteral val = (OWLLiteral) annotation.getValue();
            // look for portuguese labels - can be skipped
              if (val.hasLang("pt")) {
                //Get your String here
                System.out.println(cls + " labelled " + val.getLiteral());
              }
           }
        }
    }
    
    public void searchForClass(String className) throws OWLException{
        System.out.println("The class you are looking for: Animal");
        
        for(OWLClass cls: ontology.getClassesInSignature()){
            if(cls.getIRI().getShortForm().equals("Animal"))
                printHierarchyStartingHere(cls);
        }
    }
    
        /** Print the class hierarchy from this class down, assuming this class is at
     * the given level. Makes no attempt to deal sensibly with multiple
     * inheritance. */
    private void printHierarchy(OWLClass clazz, int level) throws OWLException {
        /*
         * Only print satisfiable classes -- otherwise we end up with bottom
         * everywhere
         */
        if (reasoner.isSatisfiable(clazz)) {
            //System.out.println("satisfiable: ");
            for (int i = 0; i < level * 4; i++) {
                System.out.print(" ");
            }
            System.out.println((clazz.getIRI().getShortForm()));
            //System.out.println((clazz.getIRI()));
            /* Find the children and recurse */
            for (OWLClass child : reasoner.getSubClasses(clazz, true).getFlattened()) {
                if (!child.equals(clazz)) {
                    printHierarchy(child, level + 1);
                }
            }
        }
    }
    
}