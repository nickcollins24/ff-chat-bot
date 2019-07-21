package ncollins.controllers;

import ncollins.data.PinCollection;
import ncollins.model.chat.Pin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("bot")
public class BotController {
    // these only get set when testing locally, dont set these if running in GCP
    String GCP_PROJECT_ID =         System.getenv("GCP_PROJECT_ID");
    String GCP_KEY =                System.getenv("GCP_KEY");

    PinCollection pinCollection = new PinCollection(GCP_PROJECT_ID, GCP_KEY);

    @GetMapping("pin")
    public List<Pin> getPins() {
        return pinCollection.getPins();
    }
}
