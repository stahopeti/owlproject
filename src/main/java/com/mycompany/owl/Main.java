/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.owl;

import java.io.IOException;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.FilterPane;

/**
 *
 * @author peterbencestahorszki
 */
public class Main extends Application{
 
    public static void main(String[] args) throws OWLOntologyCreationException, OWLException{
        OWLGodclass owlgc = new OWLGodclass();
        System.out.println("Ontology class hierarchy: \n\n");
        owlgc.printHierarchyWhole();
        
        owlgc.showInstancesOfClass("NBN");
        owlgc.showInstancesOfClass("Pharmacology");
        owlgc.showInstancesOfClass("Drug");
//        System.out.println("\n\n\n\n");
//        owlgc.filterByDataPropertyValue("");
//        System.out.println("\n\n\n\n");
//        owlgc.filterByObjectPropertyValue("moa_rec-ant_d2");
        
        owlgc.getObjectPropertyTypes();
        owlgc.getDataPropertyTypes();
        
        //owlgc.getAllInstances();
        launch(args);
    }

    /*
    menubar-> filter menu
    scenes:
    *mainmenu
    *filter
    **filter by object or data property
    
    *show class hierarchy?
    *entity browser
    showclassinstances
    */

    
    @Override
    public void start(Stage primaryStage) throws OWLOntologyCreationException, IOException {
    
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/FXMLFilter.fxml"));
        
        Scene scene = new Scene(root);
        
        primaryStage.setScene(scene);
        primaryStage.show();
        }
}