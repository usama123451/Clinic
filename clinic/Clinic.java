package clinic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Represents the main class for managing a medical clinic.
 * It handles patients, doctors, and their assignments.
 */
public class Clinic {

    private final Map<String, Patient> patients = new TreeMap<>();
    private final Map<Integer, Doctor> doctors = new TreeMap<>();

    public void addPatient(String first, String last, String ssn) {
        patients.putIfAbsent(ssn, new Patient(first, last, ssn));
    }

    public String getPatient(String ssn) throws NoSuchPatient {
        Patient patient = patients.get(ssn);
        if (patient == null) {
            throw new NoSuchPatient();
        }
        return patient.toString();
    }

    public void addDoctor(String first, String last, String ssn, int badgeID, String specialization) {
        Doctor doctor = new Doctor(first, last, ssn, badgeID, specialization);
        // A doctor is also a person and can be a patient
        patients.putIfAbsent(ssn, doctor);
        doctors.putIfAbsent(badgeID, doctor);
    }

    public String getDoctor(int badgeID) throws NoSuchDoctor {
        Doctor doctor = doctors.get(badgeID);
        if (doctor == null) {
            throw new NoSuchDoctor();
        }
        return doctor.toString();
    }

    public void assignPatientToDoctor(String ssn, int badgeID) throws NoSuchPatient, NoSuchDoctor {
        Patient patient = patients.get(ssn);
        if (patient == null) throw new NoSuchPatient();

        Doctor doctor = doctors.get(badgeID);
        if (doctor == null) throw new NoSuchDoctor();

        // If patient was previously assigned, remove from old doctor's list.
        patient.getAssignedDoctor().ifPresent(oldDoctor -> oldDoctor.removePatient(patient));
        
        // Make the new assignment.
        patient.setAssignedDoctor(doctor);
        doctor.addPatient(patient);
    }

    public int getAssignedDoctor(String ssn) throws NoSuchPatient, NoSuchDoctor {
        Patient patient = patients.get(ssn);
        if (patient == null) throw new NoSuchPatient();

        return patient.getAssignedDoctor()
                .map(Doctor::getBadgeId)
                .orElseThrow(NoSuchDoctor::new);
    }

    public List<String> getAssignedPatients(int badgeID) throws NoSuchDoctor {
        Doctor doctor = doctors.get(badgeID);
        if (doctor == null) throw new NoSuchDoctor();

        return doctor.getAssignedPatients().stream()
                .map(Patient::getSsn)
                .collect(Collectors.toList());
    }

    public int loadData(Reader reader) throws IOException {
        return loadData(reader, null);
    }

    public int loadData(Reader reader, ErrorListener listener) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        AtomicInteger lineCounter = new AtomicInteger(1);
        
        long loadedRecords = bufferedReader.lines().map(line -> {
            int currentLine = lineCounter.getAndIncrement();
            try {
                String[] parts = line.trim().split("\\s*;\\s*");
                if (parts.length > 0) {
                    if ("P".equals(parts[0]) && parts.length == 4) {
                        addPatient(parts[1], parts[2], parts[3]);
                        return true;
                    } else if ("M".equals(parts[0]) && parts.length == 6) {
                        addDoctor(parts[2], parts[3], parts[4], Integer.parseInt(parts[1]), parts[5]);
                        return true;
                    }
                }
                // If we reach here, the line is malformed
                if (listener != null) listener.offending(currentLine, line);
                return false;
            } catch (Exception e) {
                if (listener != null) listener.offending(currentLine, line);
                return false;
            }
        }).filter(Boolean::booleanValue).count();

        return (int) loadedRecords;
    }

    public Collection<String> idleDoctors() {
        return doctors.values().stream()
                .filter(d -> d.getAssignedPatients().isEmpty())
                .sorted(Doctor.ALPHABETICAL_ORDER)
                .map(Doctor::toString)
                .collect(Collectors.toList());
    }

    public Collection<Integer> busyDoctors() {
        double averageAssignments = doctors.values().stream()
                .mapToInt(d -> d.getAssignedPatients().size())
                .average()
                .orElse(0.0);

        return doctors.values().stream()
                .filter(d -> d.getAssignedPatients().size() > averageAssignments)
                .map(Doctor::getBadgeId)
                .collect(Collectors.toList());
    }

    public Collection<String> doctorsByNumPatients() {
        return doctors.values().stream()
                .sorted(Comparator.comparingInt((Doctor d) -> d.getAssignedPatients().size()).reversed()
                .thenComparing(Doctor::getLastName).thenComparing(Doctor::getFirstName))
                .map(d -> String.format("%3d : %d %s %s",
                        d.getAssignedPatients().size(),
                        d.getBadgeId(),
                        d.getLastName(),
                        d.getFirstName()))
                .collect(Collectors.toList());
    }

    public Collection<String> countPatientsPerSpecialization() {
        return doctors.values().stream()
                .filter(d -> !d.getAssignedPatients().isEmpty())
                .collect(Collectors.groupingBy(
                        Doctor::getSpecialization,
                        Collectors.summingInt(d -> d.getAssignedPatients().size())))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .map(e -> String.format("%3d - %s", e.getValue(), e.getKey()))
                .collect(Collectors.toList());
    }
}
