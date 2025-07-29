package clinic;

/**
 * Exception thrown when a doctor with a given badge ID is not found.
 */
@SuppressWarnings("serial")
public class NoSuchDoctor extends Exception {
    public NoSuchDoctor() {
        super();
    }

    public NoSuchDoctor(String message) {
        super(message);
    }
}
