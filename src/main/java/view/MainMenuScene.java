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

/**
 *
 * @author peterbencestahorszki
 */
public class MainMenuScene extends Scene{
    
    private ComboBox objectPropertiesSelector;
    private ComboBox dataPropertiesSelector;
    private Label title;
    private Label objectTitle;
    private Label dataTitle;
    
    public MainMenuScene(ArrayList<String> objectPropertiesSelector, ArrayList<String> dataPropertiesSelector, Parent parent, double d, double d1) {
        super(parent, d, d1);
        title = new Label("Chose filter options");
        objectTitle = new Label("Object properties");
        dataTitle = new Label("Data properties");
        this.objectPropertiesSelector.setItems(FXCollections.observableArrayList(objectPropertiesSelector));
        this.objectPropertiesSelector.setItems(FXCollections.observableArrayList(dataPropertiesSelector));
    }
    
    
    public MainMenuScene(Parent parent, double d, double d1) {
        super(parent, d, d1);
        
        title = new Label("Chose filter options");
        objectTitle = new Label("Object properties");
        dataTitle = new Label("Data properties");
        
    }
    
}
