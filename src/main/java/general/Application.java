package general;

import general.entities.*;
import general.reposes.SchoolClassRepos;
import general.reposes.SchoolRepos;
import general.reposes.UserRepos;
import general.services.SchoolClassService;
import general.services.SchoolService;
import general.services.TaskService;
import general.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Application {
    @Autowired
    private UserRepos userRepos;
    @Autowired
    UserService userService;
    @Autowired
    private SchoolRepos schoolRepos;
    @Autowired
    private TaskService taskService;
    @Autowired
    SchoolService schoolService;
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
            userService.saveUser(admin);
            String schoolName = "Омская БОУ СОШ №1";
            School school = new School(schoolName);
            schoolRepos.save(school);
            User operator = new User("Operator", bCryptPasswordEncoder.encode("12345"),
                    school, Role.Operator);
            userService.saveUser(operator);
            for (int i = 1; i <= 11; i++)
                for (int j = 1; j <= 4; j++)
                    schoolClassService.createNewSchoolClass(new SchoolClass(Integer.toString(j), i, school));
            List<Teacher> teachers = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                Teacher teacher = new Teacher("Учитель " + i, bCryptPasswordEncoder.encode("12345"), school);
                teachers.add(teacher);
            }
            String taskExampleBody = "Используя рычажные весы с разновесом, мензурку, стакан с водой," +
                    " цилиндр, соберите экспериментальную установку для измерения плотности материала, из которого изготовлен цилиндр. \n" +
                    "\n" +
                    "В бланке ответов:\n" +
                    "\n" +
                    "1) сделайте рисунок экспериментальной установки для определения объема тела;\n" +
                    "\n" +
                    "2) запишите формулу для расчета плотности;\n" +
                    "\n" +
                    "3) укажите результаты измерения массы цилиндра и его объема;\n" +
                    "\n" +
                    "4) запишите числовое значение плотности материала цилиндра.";
            for (SchoolClass currentClass : schoolClassService.getAllClassesBySchool(school)) {
                for (Teacher teacher : teachers) {
                    teacher.addSchoolClass(currentClass);
                    userService.saveUser(teacher);
                    for (int j = 1; j < 4; j++) {
                        taskService.createTask("Задача №" + j + "." + j * j + "("+teacher.getName()+")", taskExampleBody, "2022-10-28T19:55",
                                currentClass, teacher);
                    }
                }
                String studentName = "Ученик " + currentClass.getNameWithNumber();
                Student student = new Student(studentName, bCryptPasswordEncoder.encode("12345"),
                        school, currentClass);
                userService.saveUser(student);
            }
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
