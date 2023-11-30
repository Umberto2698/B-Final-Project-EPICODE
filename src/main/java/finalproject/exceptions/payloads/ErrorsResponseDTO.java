package finalproject.exceptions.payloads;

import java.util.Date;

public record ErrorsResponseDTO(String message, Date timestamp) {
}
