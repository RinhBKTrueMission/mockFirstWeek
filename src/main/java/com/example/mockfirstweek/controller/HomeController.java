package com.example.mockfirstweek.controller;

import com.example.mockfirstweek.JWT.JwtUtils;
import com.example.mockfirstweek.JWT.request.LoginRequest;
import com.example.mockfirstweek.JWT.request.SignupRequest;
import com.example.mockfirstweek.JWT.response.JwtResponse;
import com.example.mockfirstweek.JWT.response.MessageResponse;
import com.example.mockfirstweek.model.Account;
import com.example.mockfirstweek.model.ERole;
import com.example.mockfirstweek.model.Product;
import com.example.mockfirstweek.model.Role;
import com.example.mockfirstweek.reponsitory.AccountRepository;
import com.example.mockfirstweek.reponsitory.RoleRepository;
import com.example.mockfirstweek.service.AccountDetailService;
import com.example.mockfirstweek.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/home")
public class HomeController {
    final
    AuthenticationManager authenticationManager;

    final
    AccountRepository accountRepository;

    final
    RoleRepository roleRepository;

    final
    PasswordEncoder encoder;

    final
    JwtUtils jwtUtils;

    public HomeController(AuthenticationManager authenticationManager, AccountRepository accountRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAccount(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication=authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt=jwtUtils.generateJwtToken(authentication);
        AccountService account=(AccountService) authentication.getPrincipal();
        List<String> roles = account.getAuthorities().stream().
                map(
                        item->item.getAuthority()).collect(Collectors.toList()
                );
        return  ResponseEntity.ok(
                new JwtResponse(jwt,
                        account.getId(),
                        account.getUsername(),
                        account.getPassword(),
                        roles));
    }
    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/gettk")
    public ResponseEntity<Iterable<Account>> getAll(){
        return new ResponseEntity<>(accountRepository.findAll(), HttpStatus.OK);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (accountRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (accountRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Account account = new Account(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        String emailTerm=signUpRequest.getEmail();
        String regexAdmin="vnpt-technology";
        //String regexMod="^[a-zA-Z0-9_+&*-] + (?:\\\\.[a-zA-Z0-9_+&*-] + )*@(vnshop)\\.+ [a-zA-Z]{2, 7}";
        String regexStaff="vnshop";


        if (strRoles == null) {
            if(emailTerm.contains(regexAdmin)){
                Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            }else if(emailTerm.contains(regexStaff)){
                Role userRole = roleRepository.findByName(ERole.ROLE_STAFF)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            }else{
                Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMERS)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            }

        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    case "staff":
                        Role staffRole = roleRepository.findByName(ERole.ROLE_STAFF)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(staffRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMERS)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        account.setRoles(roles);
        accountRepository.save(account);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}

