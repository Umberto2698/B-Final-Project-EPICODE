package finalproject.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(UUID id) {
        super("There is no item with this id: " + id);
    }

    public NotFoundException(String message) {
        super(message);
    }
}
