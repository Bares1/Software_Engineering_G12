package il.cshaifasweng.OCSFMediatorExample.server.concreteHandlers;

import il.cshaifasweng.OCSFMediatorExample.entities.DataCommunicationDB;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import il.cshaifasweng.OCSFMediatorExample.server.coreLogic.RequestHandler;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.IOException;
import java.time.LocalDateTime;

public class NewPurchaseHandler implements RequestHandler {

    @Override
    public void handle(Message message, ConnectionToClient client) throws IOException {
        SessionFactory sessionFactory = DataCommunicationDB.getSessionFactory(DataCommunicationDB.getPassword());
        Session session = sessionFactory.openSession();
        DataCommunicationDB.setSession(session);

        try {
            // All requests are to be within the try block -- START HERE
            Message answer = new Message();
            answer.setMessage("New Purchase");
            PriceConstants price = DataCommunicationDB.getPrices();
            // Checking if the request is to create a new purchase.
            if ("New Booklet".equals(message.getMessage().toString())) {
                handleNewPurchase(message, PurchaseType.BOOKLET, session,price);
            } else if ("New MovieLink".equals(message.getMessage().toString())) {
                handleNewPurchase(message, PurchaseType.MOVIE_LINK, session,price);
            } else if ("New MovieTicket".equals(message.getMessage().toString())) {
                handleNewPurchase(message, PurchaseType.MOVIE_TICKET, session,price);
            }

            answer.setData(message.getPurchase().getPurchaseType().toString());
            answer.setPurchase(message.getPurchase());
            client.sendToClient(answer);
            // All requests are to be within the try block -- END HERE
        } catch (Exception e) {
            System.err.println("An error occurred");
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private void handleNewPurchase(Message message, PurchaseType purchaseType, Session session, PriceConstants price) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Purchase purchase = new Purchase();
            purchase.SetPrice(price);
            purchase.setPurchaseType(purchaseType);
            purchase.setDateOfPurchase(LocalDateTime.now());
            purchase.setPriceByItem(purchaseType);

            Customer customer = message.getCustomer();
            Customer existingCustomer = DataCommunicationDB.getCustomerByPersonalID(session, customer.getPersonalID());

            if (existingCustomer != null) {
                System.out.println("Customer exists with ID: " + existingCustomer.getId());
                purchase.setCustomer(existingCustomer);
                purchase.setCustomerPID(existingCustomer.getPersonalID());
                existingCustomer.getPurchases().add(purchase);
                session.save(purchase);
                session.update(existingCustomer);
                System.out.println("Customer's purchases have been updated successfully");
            } else {
                System.out.println("Customer does not exist. Creating new customer.");
                purchase.setCustomer(customer);
                purchase.setCustomerPID(customer.getPersonalID());
                purchase.setDateOfPurchase(LocalDateTime.now());
                customer.getPurchases().add(purchase);
                session.save(customer); // Save customer first to generate ID
                session.save(purchase); // Then save purchase to link it to customer
                System.out.println("Successfully created new customer.");
            }
            message.setPurchase(purchase);
            setPurchaseEntity(purchase, purchaseType, session);
            session.update(purchase); // Update the purchase with the correct entity

            // Log for checking IDs
            if (purchaseType == PurchaseType.BOOKLET) {
                System.out.println("New booklet created with ID: " + purchase.getPurchasedBooklet().getId());
            } else if (purchaseType == PurchaseType.MOVIE_LINK) {
                System.out.println("New movie link created with ID: " + purchase.getPurchasedMovieLink().getId());
            } else if (purchaseType == PurchaseType.MOVIE_TICKET) {
                System.out.println("New movie ticket created with ID: " + purchase.getPurchasedMovieTicket().getId());
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("An error occurred");
            e.printStackTrace();
        }
    }

    private void setPurchaseEntity(Purchase purchase, PurchaseType purchaseType, Session session) {
        switch (purchaseType) {
            case BOOKLET:
                Booklet booklet = new Booklet();
                session.save(booklet);
                purchase.setPurchasedBooklet(booklet);
                break;
            case MOVIE_LINK:
                MovieLink movieLink = new MovieLink();
                session.save(movieLink);
                purchase.setPurchasedMovieLink(movieLink);
                break;
            case MOVIE_TICKET:
                MovieTicket movieTicket = new MovieTicket();
                session.save(movieTicket);
                purchase.setPurchasedMovieTicket(movieTicket);
                break;
            default:
                throw new IllegalArgumentException("Unknown purchase type: " + purchaseType);
        }
    }
}
