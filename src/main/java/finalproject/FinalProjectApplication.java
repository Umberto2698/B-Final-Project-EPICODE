package finalproject;

import finalproject.json.ReadJSON;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinalProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalProjectApplication.class, args);

        ReadJSON.readJson("C:\\Users\\Arcangelo_xxl\\Desktop\\final-project\\src\\main\\java\\finalproject\\json\\donation_center.json");
    }

}
