package claudiaburali.capstoneproject.controllers;

import claudiaburali.capstoneproject.entities.User;
import claudiaburali.capstoneproject.exceptions.BadRequestException;
import claudiaburali.capstoneproject.payloads.UserPasswordDTO;
import claudiaburali.capstoneproject.repositories.UsersRepository;
import claudiaburali.capstoneproject.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<User> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
        return this.usersService.getUsers(page, size, sortBy);
    }

    @GetMapping("/me")
    public User getProfile(@AuthenticationPrincipal User currentAuthenticatedUser){
        return currentAuthenticatedUser;
    }

    @PutMapping("/me")
    public User updateProfile(@AuthenticationPrincipal User currentAuthenticatedUser, @RequestBody User body){
        return this.usersService.findByIdAndUpdate(currentAuthenticatedUser.getId(), body);
    }

    @PatchMapping("/me/password")
    public String updatePassword(@AuthenticationPrincipal User currentAuthenticatedUser, @RequestBody @Validated UserPasswordDTO body, BindingResult bindingResult){
    if (bindingResult.hasErrors()) {
        throw new BadRequestException(bindingResult.getAllErrors());
    }
    return usersService.updatePassword(currentAuthenticatedUser.getId(), body.password());
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal User currentAuthenticatedUser){
        this.usersService.findByIdAndDelete(currentAuthenticatedUser.getId());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User findById(@PathVariable UUID userId) {
        return this.usersService.findById(userId);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User findByIdAndUpdate(@PathVariable UUID userId, @RequestBody User body) {
        return this.usersService.findByIdAndUpdate(userId, body);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID userId) {
        this.usersService.findByIdAndDelete(userId);
    }

    /*@PostMapping("/{userId}/avatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile image) throws IOException {
        return this.usersService.uploadImage(image);
    }*/

   @PatchMapping("/avatar")
   public String uploadAvatar(@AuthenticationPrincipal User currentAuthenticatedUser, @RequestParam("avatar") MultipartFile image) throws IOException {
       return usersService.uploadImage(currentAuthenticatedUser.getId(), image);
   }

}
