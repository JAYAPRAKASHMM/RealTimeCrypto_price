package Application;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Controller implements Initializable {

    @FXML
    private TextField nameInput; // TextField for entering the name

    @FXML
    private TextField priceDisplay; // TextField for displaying the price

    ObservableList<Cryp> finalData = FXCollections.observableArrayList();

    @FXML
    private TableView<Cryp> table;
    
    @FXML
    private TableColumn<Cryp,String> nameCol;

    @FXML
    private TableColumn<Cryp,String> noCol;

    @FXML
    private Button start;

    @FXML
    private Button stop;

    private final String API = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?start=1&limit=5&convert=INR&CMC_PRO_API_KEY=9304d88b-59a6-4802-8219-fbca969f58d0";

    private Timer timer;

    @FXML
    void startClicked(ActionEvent event) {
        String cryptoName = nameInput.getText();
        if (cryptoName.isEmpty()) {
            return;
        }

        String price = fetchCryptoPrice(cryptoName);

        priceDisplay.setText(price);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String updatedPrice = fetchCryptoPrice(cryptoName);
                Platform.runLater(() -> priceDisplay.setText(updatedPrice));
            }
        }, 5000, 5000); // Update every 5 seconds
    }

    @FXML
    void stopClicked(ActionEvent event) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void table() {
        table.setItems(finalData);
        nameCol.setCellValueFactory(data -> data.getValue().getNameProperty());
        noCol.setCellValueFactory(data -> data.getValue().getIdProperty());
    }

    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Cryp> cryptos = FXCollections.observableArrayList();
        Api o = null;
        try {
            o = new Api(API);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        JSONObject response = o.getJSONObject();

        // Parse the JSON response
        try {
            JSONArray dataArray = (JSONArray) response.get("data");
            for (Object obj : dataArray) {
                JSONObject jsonObject = (JSONObject) obj;
                long id = (long) jsonObject.get("id");
                String name = (String) jsonObject.get("name");
                JSONObject quote = (JSONObject) jsonObject.get("quote");
                JSONObject inr = (JSONObject) quote.get("INR");
                String price = inr.get("price").toString();
                cryptos.add(new Cryp(id, name, price));
                System.out.println(new Cryp(id, name, price));
            }
            this.finalData=cryptos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        table();
    }

    private String fetchCryptoPrice(String cryptoName) {
      

        try {
            URL url = new URL(API);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(response.toString());
            JSONArray dataArray = (JSONArray) jsonObject.get("data");
            for (Object obj : dataArray) {
                JSONObject crypto = (JSONObject) obj;
                String name = (String) crypto.get("name");
                if (name.equalsIgnoreCase(cryptoName)) {
                    JSONObject quote = (JSONObject) crypto.get("quote");
                    JSONObject inr = (JSONObject) quote.get("INR");
                    String price = inr.get("price").toString();
                    return price;
                }
            }
            return "Not Found";
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Error";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        } catch (ParseException e) {
            e.printStackTrace();
            return "Error";
        }
    }
}
