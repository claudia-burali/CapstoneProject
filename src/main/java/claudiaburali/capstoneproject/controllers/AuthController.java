package claudiaburali.capstoneproject.controllers;

import claudiaburali.capstoneproject.exceptions.BadRequestException;
import claudiaburali.capstoneproject.payloads.NewUserDTO;
import claudiaburali.capstoneproject.payloads.NewUserResponseDTO;
import claudiaburali.capstoneproject.payloads.UserLoginDTO;
import claudiaburali.capstoneproject.payloads.UserLoginResponseDTO;
import claudiaburali.capstoneproject.services.AuthService;
import claudiaburali.capstoneproject.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UsersService usersService;

    @PostMapping("/login")
    public UserLoginResponseDTO login(@RequestBody @Validated UserLoginDTO payload, BindingResult validationResult){
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors());
        }
        return new UserLoginResponseDTO(authService.authenticateUserAndGenerateToken(payload));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUserResponseDTO saveUser(@RequestBody @Validated NewUserDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors());
        }
        return new NewUserResponseDTO(this.usersService.save(body).getId());
    }
}
