package general;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import general.entities.Admin;
import general.entities.Role;
import general.entities.User;
import general.reposes.UserRepos;

@SpringBootApplication
public class Application {
    @Autowired
    private UserRepos userRepos;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    public void init() {
        if (userRepos.findUserByName("GeneralDirector") == null) {
            User admin = new Admin("GeneralDirector", bCryptPasswordEncoder.encode("144"),
                    Arrays.asList(new Role("ROLE_ADMIN"), new Role("ROLE_OPERATOR")));
            userRepos.save(admin);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
