package com.example.TodoListBackend.Service;

import com.example.TodoListBackend.Config.JwtProvider;
import com.example.TodoListBackend.Model.PasswordResetToken;
import com.example.TodoListBackend.Model.User;
import com.example.TodoListBackend.Repository.PasswordResetTokenRepository;
import com.example.TodoListBackend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${frontend.url}")
    String frontendUrl;

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String email=jwtProvider.getEmailFromJwtToken(jwt);
        User user=findUserByEmail(email);

        return user;
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if(user==null){
            throw new Exception("user does not exist!");
        }
        return user;
    }


    //validate user email and generate a token to reset the password
    @Override
    public void generatePasswordResetToken(String email) {
        //check user already exist
        User user = userRepository.findByEmail(email);

        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plus(Duration.ofHours(24));

        PasswordResetToken resetToken = new PasswordResetToken(token,expiryDate,user);
        passwordResetTokenRepository.save(resetToken);

        //include the generated token in the reset url
        String resetUrl = frontendUrl +"/reset-password?token=" + token;

        //send the password reset email with reset url to the user
        emailService.sendPasswordResetEmail(user.getEmail(), resetUrl);
    }


    //validate the token and reset with the new password
//    @Override
//    public void resetPassword(String token, String newPassword) {
//
//        PasswordResetToken resetToken= passwordResetTokenRepository.findByToken(token)
//                .orElseThrow(()-> new RuntimeException("invalid password reset token"));
//
//        if(resetToken.isUsed()){
//            throw new RuntimeException("password reset token has already been used");
//        }
//
//        if(resetToken.getExpiryDate().isBefore(Instant.now())){
//            throw new RuntimeException("password reset token is expired");
//        }
//
//        System.out.println("request cameeeee");
//
//        User user = resetToken.getUser();
//
//        System.out.println("user founddd "+ user.getId());
//        //set new password to user
//        user.setPassword(passwordEncoder.encode(newPassword));
//        userRepository.save(user);
//
//        //finally make the token as a used token
//        resetToken.setUsed(true);
//        passwordResetTokenRepository.save(resetToken);
//
//    }

    @Override
    public void resetPassword(String token, String newPassword) {
        System.out.println("Checking token: " + token);
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
               .orElseThrow(() -> new RuntimeException("invalid password reset token"));

        System.out.println("Token found: " + resetToken.getToken());

        if (resetToken.isUsed()) {
            throw new RuntimeException("password reset token has already been used");
        }

        if (resetToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("password reset token is expired");
        }

        User user = resetToken.getUser();
        if (user == null) {
            throw new RuntimeException("User not found for this token");
        }

        System.out.println("Resetting password for user: " + user.getEmail());

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);

        System.out.println("Password reset successful!");
    }



}
