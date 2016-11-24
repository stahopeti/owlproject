/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.owl.fxml;

import com.mycompany.owl.OWLGodclass;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * FXML Controller class
 *
 * @author peterbencestahorszki
 */
public class FXMLFilterController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    ListView objectPropertyList;
    @FXML
    ListView dataPropertyList;
    @FXML
    Button filterButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        OWLGodclass owlgc = null;
        try {
            owlgc = new OWLGodclass();
        } catch (OWLOntologyCreationException ex) {
            Logger.getLogger(FXMLFilterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        objectPropertyList.setItems(
            FXCollections.observableList(owlgc.getObjectPropertyTypes())
        );
        
        dataPropertyList.setItems(
            FXCollections.observableList(owlgc.getDataPropertyTypes())
        );
    }    
    
    @FXML
    public void filterObjectButtonPressed(){
        if(objectPropertyList.isFocused()){
        }
        if(dataPropertyList.isFocused()){
        }
    }
    
    @FXML
    public void filterDataButtonPressed(){
        if(objectPropertyList.isFocused()){
        }
        if(dataPropertyList.isFocused()){
        }
    }
}
