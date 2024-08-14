package il.cshaifasweng.OCSFMediatorExample.client.Customer;

import ch.qos.logback.core.net.server.Client;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.purchaseEntities.MovieLink;
import il.cshaifasweng.OCSFMediatorExample.entities.userEntities.Customer;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class ExpiredLinkChecker implements Runnable {

    private SimpleClient client;
    private int id;
    private List<MovieLink> movieLinks;
    private CustomerController customerController;
    private boolean stopChecker = false;

    public ExpiredLinkChecker(SimpleClient client, int id, List<MovieLink> movieLinks, CustomerController customerController) {
        this.client = client;
        this.id = id;
        this.movieLinks = movieLinks;
        this.customerController = customerController;
    }

    @Override
    public void run() {
        while (true) {
            if (stopChecker){
                break;
            }

            try {
                for (MovieLink movieLink : movieLinks) {
                    if (movieLink.isActive()) {
                        LocalDateTime expirationTime = movieLink.getExpirationTime();
                        LocalDateTime now = LocalDateTime.now();


                        long delay = Duration.between(now, expirationTime).toMillis();

                        if (delay < 0) {
                            movieLink.setInactive();
                            Thread.sleep(1000);
                            customerController.expiredLink(movieLink);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void stopChecker(){
        this.stopChecker=true;
    }

}


