package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import javafx.fxml.FXML;
@Deprecated
public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        SimpleChatClient.setRoot("primary");
    }
}