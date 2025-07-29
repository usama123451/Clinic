package clinic;

/**
 * A base class representing a person with a name and SSN.
 * This class serves as the foundation for both patients and doctors.
 */
public class Person {
    private final String firstName;
    private final String lastName;
    private final String ssn;

    public Person(String firstName, String lastName, String ssn) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSsn() {
        return ssn;
    }

    @Override
    public String toString() {
        return lastName + " " + firstName + " (" + ssn + ")";
    }
}
