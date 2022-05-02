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
            School school = schoolRepos.findSchoolByName(schoolName);
            if (school == null) {
                school = new School(schoolName);
                schoolRepos.save(school);
            }
            String className = "Класс №1";
            SchoolClass schoolClass = schoolClassRepos.findSchoolClassByName(className);
            if (schoolClass == null) {
                schoolClass = new SchoolClass(className, school);
                schoolClassRepos.save(schoolClass);
            }
            String teacherName = "Фролов Константин Романович (Учитель Физики)";
            Teacher teacher = new Teacher(teacherName, bCryptPasswordEncoder.encode("12345"),
                    school, Role.Teacher);
            teacher.addSchoolClass(schoolClass);
            userService.saveUser(teacher);
            String studentName = "Сидоров";
            if (userRepos.findUserByName(studentName) == null) {
                Student student = new Student(studentName, bCryptPasswordEncoder.encode("12345"),
                        Role.Student, school, schoolClass);
                userService.saveUser(student);
            }
            String taskExampleBody = "Происходит от глагола задать, из за- + дать, дал" +
                    "ее от праслав. *dā́tī; *dājā́tī; *dāvā́tī, от кот. в числе прочего произошли: ст.-слав. дати " +
                    "(греч. διδόναι), русск. дать, давать, укр. дати, белор. даць, сербохорв. да̏ти, словенск. dáti" +
                    ", чешск. dát, польск., в.-луж. dać, н.-луж. daś. Восходит к праиндоевр. *do-. Родственно лит. dúot" +
                    "i, 1 л. ед. dúomi, dúodu «даю», греч. δίδωμι, др.-инд. dádāti «даёт», авест. dadāiti «даёт», алб. аор.";
            taskService.createTask("Задача №1", taskExampleBody,
                    "2022-10-28T19:55", schoolClass, teacher);
            taskService.createTask("Задача №2", taskExampleBody,
                    "2022-10-28T19:56", schoolClass, teacher);
            taskService.createTask("Задача №3", taskExampleBody,
                    "2022-10-28T19:56", schoolClass, teacher);
            taskService.createTask("Задача №4", taskExampleBody,
                    "2022-10-28T19:56", schoolClass, teacher);
            taskService.createTask("Задача №5", taskExampleBody,
                    "2022-10-28T19:56", schoolClass, teacher);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
