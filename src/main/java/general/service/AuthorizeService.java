package general.service;

import general.entity.User;
import general.repository.UserRepository;
import general.util.UserDetailsExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public AuthorizeService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findUserByName(name);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new UserDetailsExtended(user);
    }
}
