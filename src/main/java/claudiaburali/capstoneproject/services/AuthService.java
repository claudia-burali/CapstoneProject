package claudiaburali.capstoneproject.services;

import claudiaburali.capstoneproject.entities.User;
import claudiaburali.capstoneproject.exceptions.UnauthorizedException;
import claudiaburali.capstoneproject.payloads.UserLoginDTO;
import claudiaburali.capstoneproject.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UsersService usersService;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private JWTTools jwtTools;

    public String authenticateUserAndGenerateToken(UserLoginDTO payload){

        User user = this.usersService.findByEmail(payload.email());
        if(bcrypt.matches(payload.password(), user.getPassword())){
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Credenziali non corrette!");
        }
    }
}
