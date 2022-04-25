package general;

import general.entities.*;
import general.reposes.SchoolClassRepos;
import general.reposes.SchoolRepos;
import general.reposes.UserRepos;
import general.services.SchoolClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class Application {
    @Autowired
    private UserRepos userRepos;
    @Autowired
    private SchoolRepos schoolRepos;
    @Autowired
    SchoolClassService schoolClassService;
    @Autowired
    private SchoolClassRepos schoolClassRepos;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        String generalDirector = "GeneralDirector";
        if (userRepos.findUserByName(generalDirector) == null) {
            User admin = new User(generalDirector, bCryptPasswordEncoder.encode("144"),
                    null, Role.Admin);
            userRepos.save(admin);
        }
        String schoolName = "Омская БОУ СОШ №1";
        School school = schoolRepos.findSchoolByName(schoolName);
        if (school == null) {
            school = new School(schoolName);
            schoolRepos.save(school);
        }
        String className = "Класс №1";
        SchoolClass schoolClass = schoolClassRepos.findSchoolClassByName(className);
        if (schoolClass == null) {
            schoolClass = new SchoolClass(className,school);
            schoolClassRepos.save(schoolClass);
            schoolClassRepos.addClassToSchool(schoolClass.getId(),school.getId());
        }
        String teacherName = "Фролов Константин Романович (Учитель Физики)";
        if (userRepos.findUserByName(teacherName) == null) {
            Teacher teacher = new Teacher(teacherName, bCryptPasswordEncoder.encode("12345"),
                    school, Role.Teacher);
            teacher.addSchoolClass(schoolClass);
            userRepos.save(teacher);
            userRepos.setSchoolForTeacher(teacher.getId(), school.getId());
        }
        String studentName = "Сидоров";
        if (userRepos.findUserByName(studentName) == null) {
            Student student = new Student(studentName, bCryptPasswordEncoder.encode("12345"),
                    Role.Student, school, schoolClass);
            userRepos.save(student);
            schoolClassRepos.addStudentToClass(schoolClass.getId(), student.getId());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
