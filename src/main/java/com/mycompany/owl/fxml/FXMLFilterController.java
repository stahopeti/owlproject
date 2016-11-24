/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.owl.fxml;

import com.mycompany.owl.OWLGodclass;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
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
    ListView instancesFilteredByObjectProperty;
    @FXML
    ListView instancesFilteredByDataProperty;
    @FXML
    Button filterButton;
    @FXML
    TextField filterObjectPropertyValue;
    @FXML
    TextField filterDataPropertyValue;
    
    OWLGodclass owlgc;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        owlgc = null;
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
        if(objectPropertyList.getSelectionModel().getSelectedItem() == null){
            showAlert(" filter", "Select a property!");
            return;
        }
        
        System.out.println("Selected: " + objectPropertyList.getSelectionModel().getSelectedItem().toString());
        ArrayList<String> filtered = owlgc.filterByObjectPropertyValue(
                objectPropertyList.getSelectionModel().getSelectedItem().toString(),
                filterObjectPropertyValue.getText()
        );
        
        if(filtered.size() == 0){
            showAlert(" filter", "Query returned with zero values.");
        } else {
            instancesFilteredByObjectProperty.setItems(
                FXCollections.observableList(filtered)
            );
        }
    }
    
    @FXML
    public void filterDataButtonPressed(){
        if(dataPropertyList.getSelectionModel().getSelectedItem() == null){
            showAlert(" filter", "Select a property!");
            return;
        }
        
        System.out.println("Selected: " + dataPropertyList.getSelectionModel().getSelectedItem().toString());
        ArrayList<String> filtered = owlgc.filterByDataPropertyValue(
                dataPropertyList.getSelectionModel().getSelectedItem().toString(),
                filterDataPropertyValue.getText()
        );
        
        if(filtered.size() == 0){
            showAlert(" filter", "Query returned with zero values.");
        } else {
//        instancesFilteredByDataProperty.
            instancesFilteredByDataProperty.setItems(
                FXCollections.observableList(filtered)
            );
        }
    }
    
    private void showAlert(String header, String content){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Something went wrong with" + header + "!");
            alert.setContentText(content);
            alert.showAndWait();
    }
}
