package finalproject.donation_center;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import finalproject.user.enums.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
public class DonationCenterService {
    @Autowired
    private DonationCenterRepository donationCenterRepository;

    public void save(DonationCenter donationCenter) {
        donationCenterRepository.save(donationCenter);
    }

    public void getCenterFromRegion(Region region) {
        donationCenterRepository.findByRegion(region);
    }

    public void readDonationCenterFileCsv(String path) {
        if (donationCenterRepository.findAll().isEmpty()) {
            try {
                CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
                CSVReader reader = new CSVReaderBuilder(new FileReader(path)).withCSVParser(parser).withSkipLines(1).build();
                List<String[]> donationCenterRows = reader.readAll();
                for (int i = 0; i < donationCenterRows.size(); i++) {
                    DonationCenter center = this.createCenter(donationCenterRows.get(i));
                    this.save(center);
                }
            } catch (CsvException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private DonationCenter createCenter(String[] line) {
        try {
            DonationCenter donationCenter = new DonationCenter();
            donationCenter.setAddress(line[5]);
            donationCenter.setCap(line[6]);
            donationCenter.setMunicipality(line[7]);
            switch (line[2]) {
                case "VALLE DAOSTA" -> {
                    donationCenter.setRegion(Region.VALLE_DAOSTA);
                }
                case "PROV. AUTON. BOLZANO", "PROV. AUTON. TRENTO" -> {
                    donationCenter.setRegion(Region.TRENTINO_ALTO_ADIGE);
                }
                case "FRIULI VENEZIA GIULIA" -> {
                    donationCenter.setRegion(Region.FRIULI_VENEZIA_GIULIA);
                }
                case "EMILIA ROMAGNA" -> {
                    donationCenter.setRegion(Region.EMILIA_ROMAGNA);
                }
                default -> {
                    donationCenter.setRegion(Region.valueOf(line[2]));
                }
            }
            return donationCenter;
        } catch (Exception e) {
            throw new RuntimeException("Error creating province", e);
        }
    }
}
