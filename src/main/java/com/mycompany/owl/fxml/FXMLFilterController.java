/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.owl.fxml;

import com.google.common.io.Files;
import com.mycompany.owl.OWLGodclass;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
    Button openButton;
    @FXML
    Button saveButton;
    @FXML
    TextField filterObjectPropertyValue;
    @FXML
    TextField filterDataPropertyValue;
    
    @FXML
    TextField firstLevelTextField;
    @FXML
    TextField secondLevelTextField;
    @FXML
    TextField thirdLevelTextField;
    @FXML
    TextField fourthLevelTextField;
    
    @FXML
    RadioButton firstLevelRadioButton;
    @FXML
    RadioButton secondLevelRadioButton;
    @FXML
    RadioButton thirdLevelRadioButton;
    @FXML
    RadioButton fourthLevelRadioButton;
    
    File input;
    XSSFWorkbook workbook;
    FileChooser fileChooser;
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
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Excel Files", "*.xls", "*.xlsx"));
    }    
    
    @FXML
    public void openFile() throws IOException{
        File file = fileChooser.showOpenDialog(new Stage());
        FileInputStream fileInputStream = new FileInputStream(file);
        workbook = new XSSFWorkbook(fileInputStream);
        if (file != null) {
            System.out.println("File name: " + file.getName() + 
                                "\nPath: " + file.getCanonicalPath());
        }
    }
    
    @FXML
    public void saveFile(){
        String returnValue = "";
        
        if(firstLevelRadioButton.isSelected()) returnValue += "First Level: " + firstLevelTextField.getText() + "\n";
        if(secondLevelRadioButton.isSelected()) returnValue += "Second Level: " + secondLevelTextField.getText() + "\n";
        if(thirdLevelRadioButton.isSelected()) returnValue += "Third Level: " + thirdLevelTextField.getText() + "\n";
        if(fourthLevelRadioButton.isSelected()) returnValue += "Fourth Level: " + fourthLevelTextField.getText() + "\n";
        
        System.out.println(returnValue);
        /*
        File file = fileChooser.showSaveDialog(new Stage());
        if(file!=null){
            try{
                FileOutputStream fop = new FileOutputStream(file);
                workbook.write(fop);
                fop.close();
            } catch(Exception e){
                System.out.println("Exception: " + e.getMessage());
            }
        }*/
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
