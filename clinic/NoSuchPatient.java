package clinic;

/**
 * Exception thrown when a patient with a given SSN is not found.
 */
@SuppressWarnings("serial")
public class NoSuchPatient extends Exception {
    public NoSuchPatient() {
        super();
    }

    public NoSuchPatient(String message) {
        super(message);
    }
}
