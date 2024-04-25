package com.soupapp.soup.services;


import com.soupapp.soup.dtos.user.LoginMessage;
import com.soupapp.soup.dtos.user.UserLoginDto;
import com.soupapp.soup.dtos.user.UserRegisterDto;
import com.soupapp.soup.dtos.user.UserResponseDto;
import com.soupapp.soup.repositories.UserRepository;
import com.soupapp.soup.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final static String USER_NOT_FOUND = "User with  email %s not found";
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Optional<User> getUserById(Integer userId) {
        return userRepository.findById(userId);
    }
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    public UserResponseDto create(UserRegisterDto request) {
        var users = userRepository.findAll();
        System.out.println("Existing users: " + users.toString());
        System.out.println("Email to create: " + request.getEmail());

        if (users.stream().noneMatch(x -> Objects.equals(Objects.toString(x.getEmail(), null), request.getEmail()))) {
            User user = request.toUser(new User());
            var createdUser = userRepository.save(user);
            return new UserResponseDto().fromUser(createdUser);
        }
        return null;
    }


    public LoginMessage login(UserLoginDto request){
        String msg = "";
        User user = userRepository.findByEmail(request.getEmail());
        if (user != null) {
            String password = request.getPassword();
            String existingPassword = user.getPassword();
            boolean isPwdRight = Objects.equals(password, existingPassword);
            if (isPwdRight) {
                Optional<User> existingUser = userRepository.findOneByEmailAndPassword(request.getEmail(), existingPassword);
                if (existingUser.isPresent()) {
                    return new LoginMessage("Login Success", true);
                } else {
                    return new LoginMessage("Login Failed", false);
                }
            } else {
                return new LoginMessage("password Not Match", false);
            }
        }else {
            return new LoginMessage("Email not exits", false);
        }
    }

    public List<UserResponseDto> getAll(){
        var users = userRepository.findAll();
        return users.stream().map(x -> new UserResponseDto().fromUser(x))
                .collect(Collectors.toList());
    }

//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        return userRepository.findByEmail(email)
//                .orElseThrow(() ->  new UsernameNotFoundException(String.format(USER_NOT_FOUND,email)));
//    }
}
