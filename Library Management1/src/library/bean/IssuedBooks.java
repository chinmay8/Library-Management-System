
package library.bean;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class IssuedBooks {

        private final StringProperty BookID;
        private final StringProperty BookName;
        private final StringProperty BookAuthor;
        private final StringProperty BookPublisher;
        private final StringProperty Issue_Date;
        private final StringProperty Submission_Date;
        private final StringProperty Return_Date;
        
public IssuedBooks(String BookID, String BookName, String BookAuthor, String BookPublisher, String Issue_Date, String Submission_Date, String Return_Date) {
            
            this.BookID = new SimpleStringProperty(BookID);
            this.BookName = new SimpleStringProperty(BookName);
            this.BookPublisher = new SimpleStringProperty(BookPublisher);
            this.Issue_Date = new SimpleStringProperty(Issue_Date);
            this.BookAuthor = new SimpleStringProperty(BookAuthor);
            this.Submission_Date = new SimpleStringProperty(Submission_Date);
            this.Return_Date = new SimpleStringProperty(Return_Date);
        }

// Getter Methods

 public String getBookID() {
        return BookID.get();
    }

    public String getBookName() {
        return BookName.get();
    }

    public String getBookAuthor() {
        return BookAuthor.get();
    }

    public String getBookPublisher() {
        return BookPublisher.get();
    }

    public String getIssue_Date() {
        return Issue_Date.get();
    }

    public String getSubmission_Date() {
        return Submission_Date.get();
    }

    public String getReturn_Date() {
        return Return_Date.get();
    }  
}
