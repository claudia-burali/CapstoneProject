package claudiaburali.capstoneproject.services;

import claudiaburali.capstoneproject.entities.User;
import claudiaburali.capstoneproject.entities.Wallet;
import claudiaburali.capstoneproject.exceptions.BadRequestException;
import claudiaburali.capstoneproject.exceptions.NotFoundException;
import claudiaburali.capstoneproject.payloads.NewUserDTO;
import claudiaburali.capstoneproject.repositories.UsersRepository;
import claudiaburali.capstoneproject.tools.MailgunSender;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private MailgunSender mailgunSender;

    @Autowired
    private Cloudinary cloudinaryUploader;

    @Autowired
    private WalletService walletService;

    public Page<User> getUsers(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 100) pageSize = 100;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return usersRepository.findAll(pageable);
    }

    public User save(NewUserDTO body) {
        this.usersRepository.findByEmail(body.email()).ifPresent(
                user -> {
                    throw new BadRequestException("L'email " + body.email() + " è già in uso!");
                }
        );
        User newUser = new User(body.name(), body.surname(), body.username(), body.email(), bcrypt.encode(body.password()), body.birthDate());
        newUser.setAvatarURL("https://ui-avatars.com/api/?name=" + body.name() + "+" + body.surname());
        newUser.setSingUpDate(LocalDate.now());
        User saved = usersRepository.save(newUser);
        mailgunSender.sendRegistrationEmail(saved);
        return saved;
    }

    public User findById(UUID userId) {
        return this.usersRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
    }

    public User findByIdAndUpdate(UUID userId, User modifiedUser) {
        User found = this.findById(userId);
        if (modifiedUser.getName() != null) {
            found.setName(modifiedUser.getName());
        }
        if (modifiedUser.getSurname() != null) {
            found.setSurname(modifiedUser.getSurname());
        }
        if (modifiedUser.getUsername() != null) {
            found.setUsername(modifiedUser.getUsername());
        }
        if (modifiedUser.getEmail() != null) {
            found.setEmail(modifiedUser.getEmail());
        }
        if (modifiedUser.getBirthDate() != null) {
            found.setBirthDate(modifiedUser.getBirthDate());
        }
        return this.usersRepository.save(found);
    }

    @Transactional
    public void findByIdAndDelete(UUID userId) {
        User found = this.findById(userId);
        for (Wallet wallet: found.getWallets()) {
            walletService.findByIdAndDelete(wallet.getId());
        }
        this.usersRepository.delete(found);
    }

    public User findByEmail(String email){
        return usersRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente con email " + email + " non trovato!"));
    }

    public String updatePassword(UUID userId, String password){
        User found = this.findById(userId);
        found.setPassword(password);
        usersRepository.save(found);
        return "Password modificata!";
    }

    public String uploadImage(UUID userId, MultipartFile file) throws IOException {
        User user = findById(userId);
        String imgURL = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        user.setAvatarURL(imgURL);
        usersRepository.save(user);
        return "Immagine aggiunta!";
    }

}
