package com.mysite.sbb.user;

import com.mysite.sbb.question.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String name, String password, String email){
        SiteUser siteUser = new SiteUser();
        siteUser.setUsername(name);
        siteUser.setPassword(passwordEncoder.encode(password));
        siteUser.setEmail(email);
        this.userRepository.save(siteUser);
        return siteUser;
    }

    public SiteUser getUser(String username){
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if(siteUser.isPresent()){
            return siteUser.get();
        }else{
            throw new DataNotFoundException("siteuser not found");
        }
    }

}
