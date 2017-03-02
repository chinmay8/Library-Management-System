package library.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import library.model.DBOperation;

public class IRBookController implements Initializable {

    @FXML
    private JFXTextField mem_id;
    @FXML
    private JFXTextField book_id;
    @FXML
    private Label mem_name;
    @FXML
    private Label no_of_books;
    @FXML
    private Label mem_stream;
    @FXML
    private ImageView mem_img;
    @FXML
    private Label book_name;
    @FXML
    private Label book_author;
    @FXML
    private Label book_publisher;
    
     @FXML
    private JFXButton issuebtn;

    @FXML
    private JFXButton returnbtn;

    private Image img;
    
    DBOperation db = new DBOperation();
    PreparedStatement pst1,pst2,pst3,pst4;
    ResultSet rs1,rs2;
    
    int count;
    private String member_name;
    private String MemberID;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    @FXML
    private void showMemberDetails(ActionEvent event) throws FileNotFoundException, IOException {
        MemberID = mem_id.getText();
        db.DbConnector();
        String query1 = "select * from MEMBER where MEMBER_ID='"+MemberID+"'";
        String query2 = "select * from ISSUEBOOK where MEMBER_ID='"+MemberID+"'";
        try {
            pst1 = db.DbConnector().prepareStatement(query1);
            rs1 = pst1.executeQuery();
            rs1.next();
            pst2 = db.DbConnector().prepareStatement(query2);
            rs2 = pst2.executeQuery();
            count=0;
            while(rs2.next()){
                count++;
            }
            
            member_name=rs1.getString("MEMBER_NAME");
                  
            mem_name.setText(member_name);
            mem_stream.setText(rs1.getString("MEMBER_STREAM"));
            no_of_books.setText(Integer.toString(count));
            //Displaying Image:
            InputStream is = rs1.getBinaryStream("MEMBER_IMAGE");
            OutputStream os = new FileOutputStream(new File("photo.jpg"));
            byte[] content= new byte[1024];
            int size = 0;
            while ((size = is.read(content)) != -1 ) {
                os.write(content,0,size);
            }
            os.close();
            is.close();
            img = new Image("file:photo.jpg");
            mem_img.setImage(img);
            
        } catch (SQLException ex) {
            Logger.getLogger(IRBookController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private String BookID ;
    private String bookavail ;

    @FXML
    private void showBookDetails(ActionEvent event) {
        BookID = book_id.getText();
        db.DbConnector();
        String query1 = "select * from BOOKS where BOOK_ID='"+BookID+"'";
        try {
            pst1 = db.DbConnector().prepareStatement(query1);
            rs1 = pst1.executeQuery();
            rs1.next();
            bookavail=rs1.getString("BOOK_ISAVAILABLE");
            book_name.setText(rs1.getString("BOOK_NAME"));
            book_author.setText(rs1.getString("BOOK_AUTHOR"));
            book_publisher.setText(rs1.getString("BOOK_PUBLISHER"));
            
        } catch (SQLException ex) {
            Logger.getLogger(IRBookController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    private String Current_Date;
    String formattedString;
    String notavail;
    
     @FXML
    void issuebook(ActionEvent event) throws SQLException {
        System.out.println(bookavail);
        if(bookavail.equals("Available")){
        
        
        Current_Date = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());

        LocalDate sub_date=LocalDate.now().plusDays(180);
        //System.out.println(sub_date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        formattedString = sub_date.format(formatter);
        //System.out.println(formattedString);
       
        String memaddQuery = "insert into LIBRARY.ISSUEBOOK values (?,?,?,?,?,?)"; 
        pst3 = db.DbConnector().prepareStatement(memaddQuery);
        pst3.setString(1,BookID);
        pst3.setString(2,MemberID);
        pst3.setString(3,member_name);
        pst3.setString(4,Current_Date);
        pst3.setString(5,formattedString);
        pst3.setString(6,null);
        
        if(db.execAction(pst3)){
            notavail="Not Available";
            String updatequery = "update LIBRARY.BOOKS set BOOK_ISAVAILABLE='"+notavail+"' where BOOK_ID='"+BookID+"'"; 
            pst4 = db.DbConnector().prepareStatement(updatequery);
            pst4.execute();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();
            pst3.close();
            pst4.close();
            
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed");
            alert.showAndWait();
            pst3.close();
            
        }

    }
        else{
            
             Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Message");
            alert.setContentText("SORRY!! Book is Not Available.");
            alert.showAndWait();
        }
    }

    @FXML
    void returnbook(ActionEvent event) throws SQLException {
        
        Current_Date = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());
        String avail="Available";
        String query1 = "update LIBRARY.ISSUEBOOK set RETURNED_DATE='"+Current_Date+"' where BOOK_ID='"+BookID+"' AND MEMBER_ID='"+MemberID+"'"; 
        String query2 = "update LIBRARY.BOOKS set BOOK_ISAVAILABLE='"+avail+"' where BOOK_ID='"+BookID+"'"; 
        PreparedStatement pst5 = db.DbConnector().prepareStatement(query1);
        
        PreparedStatement pst6 = db.DbConnector().prepareStatement(query2);
        
        Boolean res=pst5.execute();
        Boolean result= pst6.execute();
        System.out.println(res+"  "+result);
        if(res&&result){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Successfully returned.");
            alert.showAndWait();
            pst5.close();
            pst6.close();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed");
            alert.showAndWait();
            pst5.close();
            pst6.close();
      }


    }
    
}
