package general;
import general.entities.Teacher;
import general.entities.User;
import general.reposes.TeacherRepos;
import general.reposes.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GenController {
    @Autowired
    UserRepos userRepos;
    @Autowired
    TeacherRepos teacherRepos;
    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
    @GetMapping("/adduser")
    public String addUserGET(){
        return "adduser";
    }
    @PostMapping
    public @ResponseBody String addUser(@RequestParam(name = "name") String name, @RequestParam(name = "school") String  school, @RequestParam(name = "typeOfUser") String type){
        switch (type){
            case "user":
                User user = new User();
                user.setName(name);
                user.setSchool(school);
                userRepos.save(user);
            case "teacher":
                Teacher teacher = new Teacher();
                teacher.setName(name);
                teacherRepos.save(teacher);
        }
        return "New user succesfully created";
    }
    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepos.findAll();
    }
    @GetMapping("/addteacher")
    public  String addTeacher(){
        return "adduser";
    }
}