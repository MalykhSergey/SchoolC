package general.services;

import general.entities.User;
import general.reposes.UserRepos;
import general.utils.UserDetailsExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeService implements UserDetailsService {
    private final UserRepos userRepos;

    @Autowired
    public AuthorizeService(UserRepos userRepos) {
        this.userRepos = userRepos;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepos.findUserByName(name);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new UserDetailsExtended(user);
    }
}
