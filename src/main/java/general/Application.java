package general;
import general.entities.*;
import general.reposes.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;

@SpringBootApplication
public class Application {
    @Autowired
    private UserRepos  userRepos;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @PostConstruct
    public void initAdmin(){
        if (userRepos.findUserByName("GeneralDirector") == null){
            User admin = new Admin("GeneralDirector",
                    bCryptPasswordEncoder.encode("AmudeFx"),
                    Arrays.asList(
                            new Role("ROLE_ADMIN"),
                            new Role("ROLE_OPERATOR")
                    ));
            userRepos.save(admin);
        }
    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
