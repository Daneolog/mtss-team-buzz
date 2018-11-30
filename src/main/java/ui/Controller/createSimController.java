package ui.Controller;

import filedatabase.DBClass;
import filedatabase.SimulationGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

public class createSimController {

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private Button createSim;


    @FXML
    public void createSimFile(ActionEvent event) {
        Window currentWindow = createSim.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(currentWindow);

        if (file != null) {
            try {
               String absolutePath = file.getAbsolutePath();
               String ip = "68.183.20.174";
               int port = 5432;
               String db_prefix = "cs3300";
               String user = "cs3300_readonly";
               String password = "cs3300_b80ed2986e";
               SimulationGenerator fileGen = new SimulationGenerator(new DBClass(ip, port, db_prefix, user, password));

               fileGen.writeSimulationFile(Date.valueOf(startDate.getValue()), Date.valueOf(endDate.getValue()), file.getAbsolutePath());
               System.out.println(Date.valueOf(startDate.getValue()));
               currentWindow.hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
