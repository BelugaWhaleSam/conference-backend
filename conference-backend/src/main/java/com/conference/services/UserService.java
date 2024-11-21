package com.conference.services;

import com.conference.exceptions.EmailAlreadyTakenException;
import com.conference.exceptions.EmailFailedToSendException;
import com.conference.exceptions.UserDoesNotExistException;
import com.conference.models.ApplicationUser;
import com.conference.models.RegistrationObject;
import com.conference.models.Role;
import com.conference.repositories.RoleRepository;
import com.conference.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final MailService mailService;

    @Autowired
    public UserService(UserRepository userRepo, RoleRepository roleRepo, MailService mailService) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.mailService = mailService;
    }

    public ApplicationUser getUserByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
    }

    public ApplicationUser updateUser(ApplicationUser user) {
        try{
            return userRepo.save(user);
        } catch (Exception e) {
            throw new EmailAlreadyTakenException();
        }
    }

    public ApplicationUser registerUser(RegistrationObject ro) {

        ApplicationUser user = new ApplicationUser();
        user.setFirstName(ro.getFirstName());
        user.setLastName(ro.getLastName());
        user.setEmail(ro.getEmail());
        user.setDateOfBirth(ro.getDob());

        String name = user.getFirstName()+user.getLastName();
        boolean nameTaken = true;
        String tempName="";
        while(nameTaken) {
            tempName = generateUsername(name);
            if(userRepo.findByUsername(tempName).isEmpty()) {
                nameTaken = false;
            }
        }
        user.setUsername(tempName);

        Set<Role> roles = user.getAuthorities();
        roles.add(roleRepo.findByAuthority("USER").get());
        user.setAuthorities(roles);

        try {
            return userRepo.save(user);
        } catch (Exception e) {
            throw new EmailAlreadyTakenException();
        }
    }

    public void generateEmailVerification(String username) {
        ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
        user.setVerification(generateVerificationNumber());
        try {
            mailService.sendEmail(user.getEmail(),"Your verification code","Here is your verification code: "+user.getVerification());
            userRepo.save(user);
        } catch (Exception e) {
            throw new EmailFailedToSendException();
        }
        userRepo.save(user);
    }

    private String generateUsername(String name) {
        long generatedNumber = (long) Math.floor(Math.random()*1_000_000_000);
        return name+generatedNumber;
    }

    private Long generateVerificationNumber() {
        return (long) Math.floor(Math.random()*1_000_000_000);
    }

}
