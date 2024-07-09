package il.cshaifasweng.OCSFMediatorExample.client.ContentChange;

import il.cshaifasweng.OCSFMediatorExample.client.ClientDependent;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.client.StyleUtil;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.MovieGenre;
import il.cshaifasweng.OCSFMediatorExample.entities.movieDetails.TypeOfMovie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javassist.Loader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static il.cshaifasweng.OCSFMediatorExample.client.StyleUtil.*;

public class MovieAdditionController implements ClientDependent {

    private Movie movie;
    private TypeOfMovie movieType;
    private SimpleClient client;
    private Message localMessage;
    private final String NEW_MOVIE_TEXT_REQUEST = "new movie";



    @FXML
    private Button browseBtn;

    @FXML // fx:id="castTextField"
    private TextField castTextField; // Value injected by FXMLLoader

    @FXML // fx:id="chooseGenreComboBox"
    private ComboBox<MovieGenre> chooseGenreComboBox; // Value injected by FXMLLoader

    @FXML // fx:id="descriptionTextArea"
    private TextArea descriptionTextArea; // Value injected by FXMLLoader

    @FXML // fx:id="englishTitleTextField"
    private TextField englishTitleTextField; // Value injected by FXMLLoader

    @FXML // fx:id="filePathTextField"
    private TextField filePathTextField; // Value injected by FXMLLoader

    @FXML // fx:id="hebrewTitleTextField"
    private TextField hebrewTitleTextField; // Value injected by FXMLLoader

    @FXML // fx:id="homeScreenBtn"
    private Button homeScreenBtn; // Value injected by FXMLLoader

    @FXML // fx:id="inTheatersCheckBox"
    private CheckBox inTheatersCheckBox; // Value injected by FXMLLoader

    @FXML // fx:id="movieImageView"
    private ImageView movieImageView; // Value injected by FXMLLoader

    @FXML // fx:id="packageCheckBox"
    private CheckBox packageCheckBox; // Value injected by FXMLLoader

    @FXML // fx:id="previewImageButton"
    private Button previewImageButton; // Value injected by FXMLLoader

    @FXML // fx:id="producerTextField"
    private TextField producerTextField; // Value injected by FXMLLoader

    @FXML // fx:id="soonCheckBox"
    private CheckBox soonCheckBox; // Value injected by FXMLLoader

    @FXML // fx:id="submitMovieBtn"
    private Button submitMovieBtn; // Value injected by FXMLLoader

    private final String errorColor = "red";
    private final String normalColor = "black";
    private final String borderDefault = null;



    @FXML
    public void initialize() {
        if(localMessage.getMessage().equals(NEW_MOVIE_TEXT_REQUEST)) {
            movie = new Movie();
            movieType = new TypeOfMovie();
            movie.setMovieType(movieType);
            chooseGenreComboBox.getItems().addAll(MovieGenre.values());
            addTextInputListener(descriptionTextArea,borderDefault);
            addTextInputListener(englishTitleTextField,borderDefault);
            addTextInputListener(filePathTextField,borderDefault);
            addTextInputListener(hebrewTitleTextField,borderDefault);
            addTextInputListener(producerTextField,borderDefault);
            addTextInputListener(castTextField,borderDefault);
        }
        //Loading an existing movie.
        else{
            movie = localMessage.getSpecificMovie();

            //Setting up checkboxes according to the movie given
            packageCheckBox.setSelected(movie.getMovieType().isPurchasable());
            soonCheckBox.setSelected(movie.getMovieType().isUpcoming());
            inTheatersCheckBox.setSelected(movie.getMovieType().isCurrentlyRunning());

            //Setting up text fields.
            castTextField.setText(movie.getMainCast());
            producerTextField.setText(movie.getProducer());
            descriptionTextArea.setText(movie.getMovieDescription());
            hebrewTitleTextField.setText(movie.getHebrewMovieName());
            englishTitleTextField.setText(movie.getMovieName());

            //Populating Combobox
            chooseGenreComboBox.setValue(movie.getMovieGenre());

            //Loading movie image
            if (movie.getImage() != null) {
                InputStream imageStream = new ByteArrayInputStream(movie.getImage());
                Image image = new Image(imageStream);
                movieImageView.setImage(image);
            }
        }
    }




    @FXML
    void checkComingSoon(ActionEvent event) {
        if(soonCheckBox.isSelected()) {
            //Disabling the other checkboxes
            inTheatersCheckBox.setSelected(false);
            inTheatersCheckBox.setDisable(true);
            packageCheckBox.setSelected(false);
            packageCheckBox.setDisable(true);

            //Setting up the correct movieType details.
            movie.getMovieType().setUpcoming(true);
            movie.getMovieType().setCurrentlyRunning(false);
            movie.getMovieType().setPurchasable(false);
        }
        else{
            inTheatersCheckBox.setDisable(false);
            packageCheckBox.setDisable(false);
            movie.getMovieType().setUpcoming(false);
        }
        setCheckBoxesColor(normalColor);
    }

    @FXML
    void checkInTheaters(ActionEvent event) {
        if(inTheatersCheckBox.isSelected()) {
            movie.getMovieType().setCurrentlyRunning(true);
        }
        else {
            movie.getMovieType().setCurrentlyRunning(false);
        }
        setCheckBoxesColor(normalColor);
    }

    @FXML
    void checkViewingPackage(ActionEvent event) {
        if(packageCheckBox.isSelected()) {
            movie.getMovieType().setPurchasable(true);
        }
        else{
            movie.getMovieType().setPurchasable(false);
        }
        setCheckBoxesColor(normalColor);
    }

    @FXML
    void backToHomeScreen(ActionEvent event) {

    }

    @FXML
    void chooseGenre(ActionEvent event) {
        MovieGenre genreSelection = chooseGenreComboBox.getValue();
        movie.setMovieGenre(genreSelection);
        changeControlBorderColor(chooseGenreComboBox,borderDefault);
    }

    @FXML
    void previewImage(ActionEvent event) {
        //Loading movie image from local path given by the user or from Movie.
        if(localMessage.getMessage().equals(NEW_MOVIE_TEXT_REQUEST)) {
            if(!filePathTextField.getText().isEmpty()){
                try {
                    File file = new File(filePathTextField.getText());
                    Image image = new Image(file.toURI().toString());
                    movieImageView.setImage(image);
                } catch (Exception e) {
                    e.printStackTrace();
                    SimpleClient.showAlert(Alert.AlertType.ERROR,"File path","File location is incorrect");
                }
            }
            else{
                SimpleClient.showAlert(Alert.AlertType.ERROR,"File path","The file path is empty.");
            }

        }
        else{
            if (movie.getImage() != null) {
                InputStream imageStream = new ByteArrayInputStream(movie.getImage());
                Image image = new Image(imageStream);
                movieImageView.setImage(image);
            }
        }
    }


    @FXML
    void submitMovie(ActionEvent event) throws IOException {
        resetFieldsColor();

        //Checking if the requested is a New movie and checking the fields accordingly.
       if(localMessage.getMessage().equals(NEW_MOVIE_TEXT_REQUEST)) {
            if(!checkFields()){
                copyMovieDetails(true);
                Message message = new Message();
                message.setMessage("Content Change");
                message.setData("New Movie");
                message.setSpecificMovie(movie);
                client.sendMessage(message);
                System.out.println("Movie ID: " + movie.getId() +
                        ", Name: " + movie.getMovieName() + "Hebrew: " + movie.getHebrewMovieName() +
                        ", Main Cast: " + movie.getMainCast() +
                        ", Producer: " + movie.getProducer() +
                        ", Description: " + movie.getMovieDescription() +
                        ", Duration: " + movie.getMovieDuration());
            }
           //If its a new movie setup the fields.
       }
       else{
           copyMovieDetails(false);
           Message message = new Message();
           message.setMessage("Content Change");
           message.setData("Update Movie");
           message.setSpecificMovie(movie);
           client.sendMessage(message);
       }
    }

    private void copyMovieDetails(boolean newMovie) throws IOException {
        movie.setHebrewMovieName(hebrewTitleTextField.getText());
        movie.setMovieName(englishTitleTextField.getText());
        movie.setMovieDescription(descriptionTextArea.getText());
        movie.setMainCast(castTextField.getText());
        movie.setProducer(producerTextField.getText());
        movie.setMovieGenre(chooseGenreComboBox.getValue());
        movie.getMovieType().setUpcoming(soonCheckBox.isSelected());
        movie.getMovieType().setCurrentlyRunning(inTheatersCheckBox.isSelected());
        movie.getMovieType().setPurchasable(packageCheckBox.isSelected());
        try {
            if(newMovie){
                movie.setImage(Files.readAllBytes(Paths.get(filePathTextField.getText())));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Checking the fields and setting them accordingly.
    private boolean checkFields(){
        boolean flag = false;
        if(hebrewTitleTextField.getText().isEmpty()){
            changeControlBorderColor(hebrewTitleTextField,errorColor);
            flag = true;
        }
        if(englishTitleTextField.getText().isEmpty()){
            changeControlBorderColor(englishTitleTextField,errorColor);
            flag = true;
        }
        if(producerTextField.getText().isEmpty()){
            changeControlBorderColor(producerTextField,errorColor);
            flag = true;
        }
        if(castTextField.getText().isEmpty()){
            changeControlBorderColor(castTextField,errorColor);
            flag = true;
        }
        if(filePathTextField.getText().isEmpty() && localMessage.getMessage().equals("new movie"))
        {
            changeControlBorderColor(filePathTextField,errorColor);
            flag = true;
        }
        if(chooseGenreComboBox.getValue() == null){
            changeControlBorderColor(chooseGenreComboBox,errorColor);
            flag = true;
        }
        if(!inTheatersCheckBox.isSelected() && !packageCheckBox.isSelected() && !soonCheckBox.isSelected()){
            setCheckBoxesColor(errorColor);
            flag = true;
        }
        return flag;
    }



    private void setCheckBoxesColor(String color){
        changeControlTextColor(soonCheckBox,color);
        changeControlTextColor(inTheatersCheckBox,color);
        changeControlTextColor(packageCheckBox,color);
    }

    //Resets the fields color to be null - the transparent border by default.
    private void resetFieldsColor(){
        changeControlBorderColor(descriptionTextArea,borderDefault);
        changeControlBorderColor(hebrewTitleTextField,borderDefault);
        changeControlBorderColor(englishTitleTextField,borderDefault);
        changeControlBorderColor(castTextField,borderDefault);
        changeControlBorderColor(producerTextField,borderDefault);
        changeControlBorderColor(chooseGenreComboBox,borderDefault);
        changeControlTextColor(soonCheckBox,normalColor);
        changeControlTextColor(inTheatersCheckBox,normalColor);
        changeControlTextColor(packageCheckBox,normalColor);
    }

    @FXML
    public void browseLocation(ActionEvent actionEvent) {
        changeControlBorderColor(filePathTextField,borderDefault);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            filePathTextField.setText(file.getAbsolutePath());
        }
    }


    @Override
    public void setClient(SimpleClient client) {
        this.client = client;
    }

    @Override
    public void setMessage(Message message) {
        this.localMessage = message;
    }


}
