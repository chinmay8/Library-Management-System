
package library.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import library.model.DBOperation;

public class MemberDetailsController extends DBOperation implements Initializable {
    DBOperation db = new DBOperation();
    
    @FXML
    private JFXTextField mem_id;

    @FXML
    private JFXTextField mem_name;

    @FXML
    private JFXTextField mem_stream;

    @FXML
    private JFXTextField mem_status;

    @FXML
    private ImageView MemberImage;

    @FXML
    private TableView<Issued> tableView;
    
    @FXML
    private TableColumn<Issued, String> BkID;
    
    @FXML
    private TableColumn<Issued, String> BkName;
    
    @FXML
    private TableColumn<Issued, String> BkAuthor;
    
    @FXML
    private TableColumn<Issued, String> BkPublisher;
    
    @FXML
    private TableColumn<Issued, String> IssueDate;
    
    @FXML
    private TableColumn<Issued, String> SubmissionDate;
    
    @FXML
    private TableColumn<Issued, String> ReturnDate;
    
     @FXML
    private JFXButton cancel;

    PreparedStatement pst1,pst2;
    ResultSet rs1,rs2;
    
     String data;
     
      String id;
      String name;
      String stream;
      String status;
     
    public void setData1(String data){
        this.data=data;
    }
    
     @FXML
    void openHome(ActionEvent event) throws IOException {
       Stage stage = (Stage) cancel.getScene().getWindow();
       stage.close();

    }
    
    
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        db.DbConnector();
            String query = "select * "
                    + "from MEMBER "
                    + "where MEMBER_ID='"+data+"'"
                    + "OR MEMBER_NAME='"+data+"'";
            
        try {
            PreparedStatement pst = DbConnector().prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                 id=rs.getString("MEMBER_ID");
                 name=rs.getString("MEMBER_NAME");
                 stream=rs.getString("MEMBER_STREAM");
                 status=rs.getString("MEMBER_STATUS");
                
                mem_id.setText(id);
                mem_name.setText(name);
                mem_stream.setText(stream);
                mem_status.setText(status);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        tableView.setTableMenuButtonVisible(true);

        TableColumn<Issued, String> BkID1 = new TableColumn<>("Book ID");
        BkID1.setPrefWidth(150);
        BkID1.setCellValueFactory((TableColumn.CellDataFeatures<Issued, String> param) -> param.getValue().BookID);
        
        TableColumn<Issued, String> BkName1 = new TableColumn<>("Book Name");
        BkName1.setPrefWidth(150);
        BkName1.setCellValueFactory((TableColumn.CellDataFeatures<Issued, String> param) -> param.getValue().BookName);
        
        TableColumn<Issued, String> BkAuthor1 = new TableColumn<>("Author Name");
        BkAuthor1.setPrefWidth(150);
        BkAuthor1.setCellValueFactory((TableColumn.CellDataFeatures<Issued, String> param) -> param.getValue().BookAuthor);
        
        TableColumn<Issued, String> BkPublisher1 = new TableColumn<>("Publisher");
        BkPublisher1.setPrefWidth(150);
        BkPublisher1.setCellValueFactory((TableColumn.CellDataFeatures<Issued, String> param) -> param.getValue().BookPublisher);
        
        TableColumn<Issued, String> IssueDate1 = new TableColumn<>("Issue Date");
        IssueDate1.setPrefWidth(150);
        IssueDate1.setCellValueFactory((TableColumn.CellDataFeatures<Issued, String> param) -> param.getValue().Issue_Date);
        
        TableColumn<Issued, String> SubmissionDate1 = new TableColumn<>("Submission Date");
        SubmissionDate1.setPrefWidth(150);
        SubmissionDate1.setCellValueFactory((TableColumn.CellDataFeatures<Issued, String> param) -> param.getValue().Submission_Date);
        
        TableColumn<Issued, String> ReturnDate1 = new TableColumn<>("Return Date");
        ReturnDate1.setPrefWidth(150);
        ReturnDate1.setCellValueFactory((TableColumn.CellDataFeatures<Issued, String> param) -> param.getValue().Return_Date);
        
        ObservableList<Issued> issued_data = FXCollections.observableArrayList();
        try {
            db.DbConnector();
            String query1 = "select * from ISSUEBOOK where MEMBER_ID='"+id+"'";
            pst1 = DbConnector().prepareStatement(query1);
            rs1 = pst1.executeQuery();
            while(rs1.next()){
                String query2 = "select BOOK_NAME,BOOK_AUTHOR,BOOK_PUBLISHER from BOOKS where BOOK_ID='" + rs1.getString("BOOK_ID") + "'";
                pst2 = DbConnector().prepareStatement(query2);
                rs2 = pst2.executeQuery();
                rs2.next();
                issued_data.add(new Issued(rs1.getString("BOOK_ID"),rs2.getString("BOOK_NAME"),rs2.getString("BOOK_AUTHOR"),rs2.getString("BOOK_PUBLISHER"),
                        rs1.getString("ISSUE_DATE").substring(0, 10),rs1.getString("SUBMISSION_DATE").substring(0, 10),rs1.getString("RETURNED_DATE").substring(0, 10)));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MemberDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        tableView.getColumns().setAll(BkID1, BkName1, BkAuthor1, BkPublisher1, IssueDate1, SubmissionDate1, ReturnDate1);
        tableView.setItems(issued_data);
    }    
    
    class Issued extends RecursiveTreeObject<Issued> {
        StringProperty BookID;
        StringProperty BookName;
        StringProperty BookAuthor;
        StringProperty BookPublisher;
        StringProperty Issue_Date;
        StringProperty Submission_Date;
        StringProperty Return_Date;
        public Issued(String BookID, String BookName, String BookAuthor, String BookPublisher, String Issue_Date, String Submission_Date, String Return_Date) {
            this.BookID = new SimpleStringProperty(BookID);
            this.BookName = new SimpleStringProperty(BookName);
            this.BookPublisher = new SimpleStringProperty(BookPublisher);
            this.Issue_Date = new SimpleStringProperty(Issue_Date);
            this.BookAuthor = new SimpleStringProperty(BookAuthor);
            this.Submission_Date = new SimpleStringProperty(Submission_Date);
            this.Return_Date = new SimpleStringProperty(Return_Date);
        }
    }
    
    
}
