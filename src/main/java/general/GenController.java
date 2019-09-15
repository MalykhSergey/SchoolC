package general;

import general.entities.*;
import general.reposes.SchoolClassRepos;
import general.reposes.SchoolRepos;
import general.reposes.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Collection;

@Controller
public class GenController {
    @Autowired
    UserRepos userRepos;
    @Autowired
    SchoolRepos schoolRepos;
    @Autowired
    SchoolClassRepos schoolClassRepos;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
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
            School school = schoolRepos.findSchoolByName(nameOfSchool);
            switch (type){
                case "student":
                    Collection<Role> roles = Arrays.asList(
                            new Role("ROLE_Student"));
                    Student student = new Student(name, passwordEncoder.encode(password), roles, schoolRepos.findSchoolByName(nameOfSchool));
                    userRepos.save(student);
                    break;
                case "teacher":
                    roles = Arrays.asList(
                            new Role("ROLE_Teacher"));
                    Teacher teacher = new Teacher(name, passwordEncoder.encode(password), roles, schoolRepos.findSchoolByName(nameOfSchool));
                    userRepos.save(teacher);
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
        @RequestMapping(value = "/addclass", method = RequestMethod.GET)
        public String addClassGet(){
            return "addclass";
        }
        @RequestMapping(value = "/addclass", method = RequestMethod.POST)
        public @ResponseBody String addClassPost(
                @RequestParam(name = "name") String name,
                @RequestParam(name = "school")String school
        ){
            SchoolClass schoolClass = new SchoolClass();
            schoolClass.setName(name);
            schoolClass.setSchool(schoolRepos.findSchoolByName(school));
            schoolClassRepos.save(schoolClass);
            return "Class created";
        }
        @RequestMapping(value = "/adft", method = RequestMethod.GET)
        public String addClassForTeacher(){
            return "addclassforteacher";
        }
        @RequestMapping(value = "/adft", method = RequestMethod.POST)
        public @ResponseBody String addClassForTeacherPost(
                @RequestParam(name = "className") String className,
                @RequestParam(name = "teachername") String teachername,
                @RequestParam(name = "schoolName") String schoolName){
            School school = schoolRepos.findSchoolByName(schoolName);
            Teacher teacher = (Teacher) userRepos.findUserByNameAndSchool(teachername, school);
            SchoolClass schoolClass = schoolClassRepos.findSchoolClassByName(className);
            teacher.addSchoolClassSet(schoolClass);
            userRepos.save(teacher);
            return "class added for teacher";
        }
    }