package il.cshaifasweng.OCSFMediatorExample.entities.userRequests;

import il.cshaifasweng.OCSFMediatorExample.entities.cinemaEntities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.PurchaseType;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table (name = "complaints")
public class Complaint implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String complaintTitle;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String complaintContent;

    private LocalDateTime dateOfComplaint;
    private String complaintStatus;
    private PurchaseType purchaseType = null;
    private String customerPId; 

    @ManyToOne (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = true)
    private Branch branch;

    public double getMoneyToReturn() {
        return moneyToReturn;
    }

    public void setMoneyToReturn(double moneyToReturn) {
        this.moneyToReturn = moneyToReturn;
    }

    public void setId(int id) {
        this.id = id;
    }

    private double moneyToReturn;

    public Complaint(String complaintTitle, String complaintContent, LocalDateTime dateOfComplaint
            , String complaintStatus, PurchaseType purchaseType, String customerPId, Customer customer, double money) {
        this.complaintTitle = complaintTitle;
        this.complaintContent = complaintContent;
        this.dateOfComplaint = dateOfComplaint;
        this.complaintStatus = complaintStatus;
        this.purchaseType = purchaseType;
        this.customerPId = customerPId;
        this.customer = customer;
        this.moneyToReturn = money;
    }

    public Complaint(String complaintTitle, String complaintContent, LocalDateTime dateOfComplaint, String complaintStatus, PurchaseType purchaseType, String customerPId, Customer customer, double money, Branch branch) {
        this(complaintTitle, complaintContent, dateOfComplaint, complaintStatus, purchaseType, customerPId, customer, money);
        setBranch(branch);
    }

    public Complaint() {}

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public String getComplaintTitle() {
        return complaintTitle;
    }

    public void setComplaintTitle(String complaintTitle) {
        this.complaintTitle = complaintTitle;
    }

    public String getComplaintContent() {
        return complaintContent;
    }

    public void setComplaintContent(String complaintContent) {
        this.complaintContent = complaintContent;
    }

    public LocalDateTime getDateOfComplaint() { return dateOfComplaint; }

    public void setDateOfComplaint(LocalDateTime dateOfComplaint) { this.dateOfComplaint = dateOfComplaint; }

    public String getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(String complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public PurchaseType getPurchaseType() {return purchaseType;}

    public void setPurchaseType(PurchaseType purchaseType) {this.purchaseType = purchaseType;}

    public String getCustomerPId() {return customerPId;}

    public void setCustomerPId(String customerPId) {this.customerPId = customerPId;}

}
