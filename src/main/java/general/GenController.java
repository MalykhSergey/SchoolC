package general;

import general.entities.School;
import general.entities.Student;
import general.entities.Teacher;
import general.reposes.SchoolRepos;
import general.reposes.StudentRepos;
import general.reposes.TeacherRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GenController {
    @Autowired
    StudentRepos studentRepos;
    @Autowired
    SchoolRepos schoolRepos;
    @Autowired
    TeacherRepos teacherRepos;
        @RequestMapping(value = "/adduser", method = RequestMethod.GET)
        public String addUserGet(){
            return "adduser";
        }
        @RequestMapping(value = "/adduser", method = RequestMethod.POST)
        public @ResponseBody String addUserPost(
                @RequestParam(name = "name") String name,
                @RequestParam(name = "typeOfUser") String type,
                @RequestParam(name = "school") String nameOfSchool,
                @RequestParam(name = "password") String password){
            switch (type){
                case "student":
                    Student student = new Student();
                    student.setName(name);
                    student.setPassword(password);
                    student.setSchool(nameOfSchool);
                    studentRepos.save(student);
                    break;
                case "teacher":
                    Teacher teacher = new Teacher();
                    teacher.setName(name);
                    teacher.setPassword(password);
                    teacher.setSchool(schoolRepos.findSchoolByName(nameOfSchool));
                    teacherRepos.save(teacher);
            }
            return "User succesfully created";
        }
        @RequestMapping(value = "/addschool", method = RequestMethod.GET)
        public String addSchoolGet(){
            return "addschool";
        }
        @RequestMapping(value = "/addschool", method = RequestMethod.POST)
        public @ResponseBody String addSchoolPost(
                @RequestParam(name = "name") String name
        ){
            School school = new School();
            school.setName(name);
            schoolRepos.save(school);
            return "school created";
        }
    }