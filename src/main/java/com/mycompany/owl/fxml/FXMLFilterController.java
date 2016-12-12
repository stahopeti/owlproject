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
import java.io.FileNotFoundException;
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
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
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
    
    File file;
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
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Excel (xlsx)", "*.xlsx"));
    }    
    
    @FXML
    public void openFile() throws IOException{
        file = fileChooser.showOpenDialog(new Stage());
//        file = new File("patients_drug_consumption.xlsx");
        FileInputStream fileInputStream = new FileInputStream(file);
        workbook = new XSSFWorkbook(fileInputStream);
        if (file != null) {
            System.out.println("File name: " + file.getName() + 
                                "\nPath: " + file.getCanonicalPath());
        }
    }
    
    private String getATCMask(){
        String atcMask = "";
        System.out.println("");
        if(firstLevelRadioButton.isSelected()) {
            atcMask += firstLevelTextField.getText();
        } else {
            atcMask += "*";
        }
        if(secondLevelRadioButton.isSelected()) {
            atcMask += secondLevelTextField.getText();
        } else {
            atcMask += "**";
        }
        if(thirdLevelRadioButton.isSelected()) {
            atcMask += thirdLevelTextField.getText();
        } else {
            atcMask += "*";
        }
        if(fourthLevelRadioButton.isSelected()) {
            atcMask += fourthLevelTextField.getText();
        } else {
            atcMask += "*";
        }
        return atcMask;
    }
    
    private ArrayList<String> filterATC(){
        String atcMask = getATCMask();
        if(atcMask.equals("")) return owlgc.getUKBCodeWhereATCCodeIs("", "", "", "");
        
        return owlgc.getUKBCodeWhereATCCodeIs(
                atcMask.substring(0, 1),
                atcMask.substring(1, 3),
                atcMask.substring(3, 4),
                atcMask.substring(4, 5));
    }
    
    private ArrayList<Integer> matchingIndexes(ArrayList<String> inTable){
        ArrayList<Integer> toReturn = new ArrayList<>();
        ArrayList<String> toCompare = filterATC();
        for(int i = 0; i < inTable.size(); i++){
            for (int j = 0; j < toCompare.size(); j++) {
                if(inTable.get(i).equals(toCompare.get(j))){
                    System.out.println(i + "_" + i);
                    toReturn.add(i);
                }
            }
        }
        return toReturn;
    }
    
    @FXML
    public void saveFileTransformed() throws FileNotFoundException, IOException{
        FileInputStream fileInputStream = new FileInputStream(file);
        XSSFWorkbook workbookToModify = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbookToModify.getSheetAt(0);
        XSSFRow row;
        String atcMask = getATCMask();
        ArrayList<String> firstRowCells = new ArrayList<>();
        for(int i = 0; i <= sheet.getLastRowNum(); i++){
            row = sheet.getRow(i);
            for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                System.out.print(row.getCell(j).getRawValue() + "\t");
                if(i == 0) firstRowCells.add(row.getCell(j).getRawValue());
            }
            System.out.println("");
        }
        
        
        XSSFWorkbook transformedWB = new XSSFWorkbook();
        transformedWB.createSheet();
        XSSFSheet transformedS = transformedWB.getSheetAt(0);
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            transformedS.createRow(i);
        }
        /*
        elkezdünk végigmenni az alap sheeten
        ha megvan az index, ahol van match, akkor createrow(0) és bele a többit 0. helyre
        */
        ArrayList<Integer> matchingIndexes = matchingIndexes(firstRowCells);
        for(int i = 0; i <= sheet.getLastRowNum(); i++){
            row = sheet.getRow(i);
            int sum = 0;
            for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                if(i != 0 && j != 0){
                    for(int index : matchingIndexes){
                        if(index == j){
                            sum += Integer.valueOf(row.getCell(j).getRawValue());
                        }
                    }
                }
            }
            System.out.println(sum);
            if(i > 0){
                row = sheet.getRow(i);
                row.createCell(row.getLastCellNum()).setCellValue(sum);
            }
        } 
        for(int index : matchingIndexes){
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                row = sheet.getRow(i);
                row.getCell(index).setCellValue(3.14159);
            }
            /*for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                if(i == index){
                    for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
                        row = sheet.getRow(j);
                        transformedS.getRow(j).createCell(transformedColumnCount).setCellValue(
                                row.getCell(i).getRawValue()
                        );
                    }
                    transformedColumnCount++;
                }
            }*/
        }
        int columnsInTransformed = 0;
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                String cellValue = row.getCell(j).getRawValue();
                if(!cellValue.equals("3.14159")){
                    transformedS.getRow(i).createCell(columnsInTransformed);
                    transformedS.getRow(i).getCell(columnsInTransformed).setCellValue(cellValue);
                    columnsInTransformed++;
                }
            }
            columnsInTransformed = 0;
        }
        
        row = transformedS.getRow(0);
        row.createCell(row.getLastCellNum()).setCellValue(atcMask);
        
        
        File file = fileChooser.showSaveDialog(new Stage());
        if(file!=null){
            try{
                FileOutputStream fop = new FileOutputStream(file);
                transformedWB.write(fop);
                fop.close();
            } catch(Exception e){
                System.out.println("Exception: " + e.getMessage());
            }
        }
        
    }
    
    @FXML
    public void saveFileFiltered() throws FileNotFoundException, IOException{
        FileInputStream fileInputStream = new FileInputStream(file);
        XSSFWorkbook workbookToModify = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbookToModify.getSheetAt(0);
        XSSFRow row;
        String atcMask = getATCMask();
        ArrayList<String> firstRowCells = new ArrayList<>();
        for(int i = 0; i <= sheet.getLastRowNum(); i++){
            row = sheet.getRow(i);
            for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                System.out.print(row.getCell(j).getRawValue() + "\t");
                if(i == 0) firstRowCells.add(row.getCell(j).getRawValue());
            }
            System.out.println("");
        }
            row = sheet.getRow(0);
            row.createCell(row.getLastCellNum()).setCellValue("ATC mask:");
            row.createCell(row.getLastCellNum()).setCellValue(atcMask);
            
        ArrayList<Integer> matchingIndexes = matchingIndexes(firstRowCells);
        System.out.println("SUMS");
        for(int i = 0; i <= sheet.getLastRowNum(); i++){
            row = sheet.getRow(i);
            int sum = 0;
            for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                if(i != 0 && j != 0){
                    for(int index : matchingIndexes){
                        if(index == j){
                            sum += Integer.valueOf(row.getCell(j).getRawValue());
                        }
                    }
                }
            }
            System.out.println(sum);
            if(i > 0){
                row = sheet.getRow(i);
                row.createCell(row.getLastCellNum()+1).setCellValue(sum);
            }
        }
        
        
        File file = fileChooser.showSaveDialog(new Stage());
        if(file!=null){
            try{
                FileOutputStream fop = new FileOutputStream(file);
                workbookToModify.write(fop);
                fop.close();
            } catch(Exception e){
                System.out.println("Exception: " + e.getMessage());
            }
        }
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
