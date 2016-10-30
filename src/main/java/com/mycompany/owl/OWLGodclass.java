/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.owl;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
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
    OWLDataFactory dataFactory = manager.getOWLDataFactory();
    OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
    OWLOntology ontology;
    OWLReasoner reasoner;
    
    OWLGodclass() throws OWLOntologyCreationException{
    iri = IRI.create("file:/C:/buildings_animals.owl");
    manager = create();
    ontology = manager.loadOntologyFromOntologyDocument(iri);
    reasoner = reasonerFactory.createNonBufferingReasoner(ontology);
    }
    
    private OWLOntologyManager create() {
        OWLOntologyManager m = OWLManager.createOWLOntologyManager();
        return m;
    }
}
