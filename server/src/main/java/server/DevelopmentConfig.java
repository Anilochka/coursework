package server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import server.repository.DiagnosisRepository;
import server.repository.PatientRepository;
import server.repository.RefreshTokenRepository;
import server.repository.UserRepository;
import server.repository.WardRepository;
import server.entity.Diagnosis;
import server.entity.Patient;
import server.entity.User;
import server.entity.Ward;

import java.util.List;
import java.util.Random;

@Configuration
@Profile("!prod")
public class DevelopmentConfig {
    public static final String[] FIRST_NAMES = {"Дмитрий", "Василий", "Иван"};
    public static final String[] LAST_NAMES = {"Дмитриев", "Васильав", "Иванов"};
    public static final String[] FATHER_NAMES = {"Дмитриевич", "Васильевич", "Иванович"};

    private static final Random random = new Random();

    @Bean
    public CommandLineRunner dataLoader(DiagnosisRepository diagnosisRepo,
                                        WardRepository wardRepo,
                                        PatientRepository patientRepo,
                                        UserRepository userRepo,
                                        PasswordEncoder passwordEncoder,
                                        RefreshTokenRepository refreshTokenRepo) {
        return args -> {
            patientRepo.deleteAll();
            diagnosisRepo.deleteAll();
            wardRepo.deleteAll();
            refreshTokenRepo.deleteAll();
            userRepo.deleteAll();

            final int n = 20;

            Diagnosis[] diagnosis = new Diagnosis[n];
            Ward[] wards = new Ward[n];
            Patient[] patients = new Patient[n];

            for (int i = 0; i < n; i++) {
                final String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
                final String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
                final String fatherName = FATHER_NAMES[random.nextInt(FATHER_NAMES.length)];

                diagnosis[i] = new Diagnosis("Диагноз №" + (i + 1));
                diagnosisRepo.save(diagnosis[i]);

                wards[i] = new Ward("Палата №" + (i + 1), 10);
                wardRepo.save(wards[i]);

                patients[i] = new Patient(firstName, lastName, fatherName, diagnosis[i], wards[i]);
                patientRepo.save(patients[i]);
            }

            userRepo.save(new User("user", passwordEncoder.encode("user"), List.of("ROLE_USER")));
            userRepo.save(new User("admin", passwordEncoder.encode("admin"), List.of("ROLE_ADMIN")));
        };
    }
}
