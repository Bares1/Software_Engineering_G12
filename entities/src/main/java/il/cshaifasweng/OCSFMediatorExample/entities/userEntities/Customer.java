package il.cshaifasweng.OCSFMediatorExample.entities.userEntities;

import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Booklet;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Payment;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.Purchase;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String personalID;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Purchase> purchases = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Payment payment;

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getPersonalID() { return personalID; }

    public void setPersonalID(String personalId) { this.personalID = personalId; }

    public List<Purchase> getPurchases() {return purchases;}

    public void setPurchases(List<Purchase> purchases) {this.purchases = purchases;}

    public void addPurchase(Purchase purchase) {this.purchases.add(purchase);}
}
