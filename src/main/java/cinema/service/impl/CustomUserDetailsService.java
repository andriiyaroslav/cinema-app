package cinema.service.impl;

import static org.springframework.security.core.userdetails.User.withUsername;

import cinema.model.User;
import cinema.service.UserService;
import java.util.Optional;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<User> user = userService.findByEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Couldn't find user with username: "
                    + username);
        }
        UserBuilder builder = withUsername(username);
        builder.password(user.get().getPassword());
        builder.roles(user.get()
                .getRoles()
                .stream()
                .map(role -> role.getRoleName().name())
                .toArray(String[]::new));
        return builder.build();
    }
}
