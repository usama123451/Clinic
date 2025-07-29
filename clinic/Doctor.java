package clinic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * Represents a doctor in the clinic. A doctor is a person with a badge ID and specialization.
 */
public class Doctor extends Patient {

    private final int badgeId;
    private final String specialization;
    private final Collection<Patient> assignedPatients = new ArrayList<>();

    public Doctor(String firstName, String lastName, String ssn, int badgeId, String specialization) {
        super(firstName, lastName, ssn);
        this.badgeId = badgeId;
        this.specialization = specialization;
    }

    public int getBadgeId() {
        return badgeId;
    }

    public String getSpecialization() {
        return specialization;
    }

    /**
     * Gets the patients assigned to this doctor.
     * @return A collection of assigned patients.
     */
    public Collection<Patient> getAssignedPatients() {
        return assignedPatients;
    }

    /**
     * Internal method to add a patient to this doctor's list.
     * @param patient the patient to add.
     */
    void addPatient(Patient patient) {
        this.assignedPatients.add(patient);
    }

    /**
     * Internal method to remove a patient from this doctor's list.
     * @param patient the patient to remove.
     */
    void removePatient(Patient patient) {
        this.assignedPatients.remove(patient);
    }

    /**
     * A comparator for sorting doctors by last name, then first name.
     */
    public static final Comparator<Doctor> ALPHABETICAL_ORDER =
            Comparator.comparing(Doctor::getLastName).thenComparing(Doctor::getFirstName);

    @Override
    public String toString() {
        return super.toString() + " [" + badgeId + "]: " + specialization;
    }
}
