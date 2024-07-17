package claudiaburali.capstoneproject.services;

import claudiaburali.capstoneproject.entities.User;
import claudiaburali.capstoneproject.exceptions.BadRequestException;
import claudiaburali.capstoneproject.exceptions.NotFoundException;
import claudiaburali.capstoneproject.payloads.NewUserDTO;
import claudiaburali.capstoneproject.repositories.UsersRepository;
import claudiaburali.capstoneproject.tools.MailgunSender;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

        User newUser = new User(body.name(), body.surname(), body.email(), bcrypt.encode(body.password()));
        newUser.setAvatarURL("https://ui-avatars.com/api/?name=" + body.name() + "+" + body.surname());
        User saved = usersRepository.save(newUser);
        mailgunSender.sendRegistrationEmail(saved);
        return saved;
    }

    public User findById(UUID userId) {
        return this.usersRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
    }

    public User findByIdAndUpdate(UUID userId, User modifiedUser) {
        User found = this.findById(userId);
        found.setName(modifiedUser.getName());
        found.setSurname(modifiedUser.getSurname());
        found.setEmail(modifiedUser.getEmail());
        found.setPassword(modifiedUser.getPassword());
        found.setAvatarURL("https://ui-avatars.com/api/?name=" + modifiedUser.getName() + "+" + modifiedUser.getSurname());
        return this.usersRepository.save(found);
    }

    public void findByIdAndDelete(UUID userId) {
        User found = this.findById(userId);
        this.usersRepository.delete(found);
    }

    public User findByEmail(String email){
        return usersRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente con email " + email + " non trovato!"));
    }

    public String uploadImage(MultipartFile file) throws IOException {
        return (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
    }

}
