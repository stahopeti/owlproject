/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.owl;

import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 *
 * @author peterbencestahorszki
 */
public class Main {
 
    public static void main(String[] args) throws OWLOntologyCreationException, OWLException{
    
        OWLGodclass owlgc = new OWLGodclass();

        owlgc.printHierarchyWhole();
        
        owlgc.searchForClass("string");
    }
    
}