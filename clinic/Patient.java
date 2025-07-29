package clinic;

import java.util.Optional;

/**
 * Represents a patient in the clinic. A patient is a person who can be assigned a doctor.
 */
public class Patient extends Person {

    private Doctor assignedDoctor;

    public Patient(String firstName, String lastName, String ssn) {
        super(firstName, lastName, ssn);
    }

    /**
     * Gets the doctor assigned to this patient.
     * @return an Optional containing the doctor, or an empty Optional if none is assigned.
     */
    public Optional<Doctor> getAssignedDoctor() {
        return Optional.ofNullable(assignedDoctor);
    }

    /**
     * Internal method to set the assigned doctor.
     * @param doctor the doctor to assign.
     */
    void setAssignedDoctor(Doctor doctor) {
        this.assignedDoctor = doctor;
    }
}
