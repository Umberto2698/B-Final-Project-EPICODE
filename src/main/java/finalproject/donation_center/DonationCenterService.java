package finalproject.donation_center;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import finalproject.exceptions.NotFoundException;
import finalproject.user.enums.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class DonationCenterService {
    @Autowired
    private DonationCenterRepository donationCenterRepository;

    public void save(DonationCenter donationCenter) {
        donationCenterRepository.save(donationCenter);
    }

    public DonationCenter getById(UUID id) {
        return donationCenterRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public List<DonationCenter> getCenterFromRegion(Region region) {
        return donationCenterRepository.findByRegion(region);
    }

    public void readDonationCenterFileCsv(String path) {
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

    private DonationCenter createCenter(String[] line) {
        try {
            DonationCenter donationCenter = new DonationCenter();
            donationCenter.setAddress(line[1].trim());
            donationCenter.setDenomination(line[5].trim());
            donationCenter.setProvinceAbbreviation(line[4].trim());
            donationCenter.setCap(line[2]);
            donationCenter.setMunicipality(line[3].trim());
            switch (line[0]) {
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
                    donationCenter.setRegion(Region.valueOf(line[0].trim()));
                }
            }
            return donationCenter;
        } catch (Exception e) {
            throw new RuntimeException("Error creating province", e);
        }
    }
}
