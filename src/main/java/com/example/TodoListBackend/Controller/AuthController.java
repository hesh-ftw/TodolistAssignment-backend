package com.example.TodoListBackend.Controller;

import com.example.TodoListBackend.Config.JwtProvider;
import com.example.TodoListBackend.Model.User;
import com.example.TodoListBackend.Repository.UserRepository;
import com.example.TodoListBackend.RequestDtos.LoginRequest;
import com.example.TodoListBackend.ResponseDtos.AuthResponse;
import com.example.TodoListBackend.ResponseDtos.MessageResponse;
import com.example.TodoListBackend.Service.CustomUserDetailsService;
import com.example.TodoListBackend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@Controller
@RequestMapping("/api/public")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<?> userSignUp(@RequestBody User user) throws Exception{

        User isEmailExist= userRepository.findByEmail(user.getEmail());

        if(isEmailExist != null){
            throw new Exception("Email is already exist with another account");
        }
        User newUser= new User();
        newUser.setFullName(user.getFullName());
        newUser.setEmail(user.getEmail());
        newUser.setRole(user.getRole());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(newUser);


        // if the credentials are valid, authenticate the user and assign a jwt
        Authentication authentication= new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt= jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Register success! ");
        authResponse.setRole(String.valueOf(savedUser.getRole()));

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }


    @PostMapping("/signin")
    public ResponseEntity<?> userSignIn(@RequestBody LoginRequest req){

        String username = req.getEmail();
        String password = req.getPassword();

        Authentication authentication = authenticate(username, password);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role= authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Login success! ");
        authResponse.setRole(role);


        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }



    private Authentication authenticate(String username, String password) {

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if(userDetails== null){
            throw new BadCredentialsException("Invalid username..");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
    }


    //forgot password endpoint
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request){
            String email= request.get("email");
        try{
            userService.generatePasswordResetToken(email);
              return ResponseEntity.ok(new MessageResponse("password reset email sent!"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error sending password reset email"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword){
        try{
            userService.resetPassword(token,newPassword);
            return ResponseEntity.ok(new MessageResponse("password reset successful "));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse(e.getMessage()));
        }
    }
}
