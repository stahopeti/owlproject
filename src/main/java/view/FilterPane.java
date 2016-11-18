/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author peterbencestahorszki
 */
public class FilterPane extends VBox{
    
    private ComboBox objectPropertiesSelector;
    private ComboBox dataPropertiesSelector;
    private Label title;
    private Label objectTitle;
    private Label dataTitle;
    
    public FilterPane(ArrayList<String> objectProperties, ArrayList<String> dataProperties) {
        title = new Label("Chose filter options");
        objectTitle = new Label("Object properties");
        dataTitle = new Label("Data properties");
        
        objectPropertiesSelector = new ComboBox();
        dataPropertiesSelector = new ComboBox();
        
        if((objectProperties!=null) && (dataProperties != null)){
            objectPropertiesSelector.setItems(FXCollections.observableList(objectProperties));
            dataPropertiesSelector.setItems(FXCollections.observableList(dataProperties));
        }
        this.getChildren().addAll(title,
                objectTitle,
                this.objectPropertiesSelector,
                dataTitle, 
                this.dataPropertiesSelector);
        
    }
    
    
    public FilterPane() {
        
        title = new Label("Chose filter options");
        objectTitle = new Label("Object properties");
        dataTitle = new Label("Data properties");
        
    }
    
}
