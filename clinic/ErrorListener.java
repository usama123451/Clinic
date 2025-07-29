package clinic;

/**
 * An interface for a listener that is notified of errors during data loading.
 */
public interface ErrorListener {
    /**
     * Called when a malformed line is encountered in the input file.
     *
     * @param lineNumber the line number (starting from 1)
     * @param line       the content of the offending line
     */
    void offending(int lineNumber, String line);
}
