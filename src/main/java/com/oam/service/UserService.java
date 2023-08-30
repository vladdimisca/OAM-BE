package com.oam.service;

import com.oam.exception.ErrorMessage;
import com.oam.exception.model.BadRequestException;
import com.oam.exception.model.ConflictException;
import com.oam.exception.model.ForbiddenException;
import com.oam.exception.model.NotFoundException;
import com.oam.model.Role;
import com.oam.model.User;
import com.oam.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SecurityService securityService;
    private final SecureRandomService secureRandomService;
    private final FirebaseStorageService firebaseStorageService;
    private final EmailSenderService emailSenderService;

    public User create(User user) {
        checkEmailNotUsed(user.getEmail());
        checkPhoneNumberNotUsed(user.getUserDetails().getCallingCode(), user.getUserDetails().getPhoneNumber());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setIsBanned(false);

        return userRepository.save(user);
    }

    public User update(User user, UUID id) {
        User existingUser = getById(id);
        securityService.authorize(id);

        if (!user.getEmail().equals(existingUser.getEmail())) {
            checkEmailNotUsed(user.getEmail());
        }
        if (!user.getUserDetails().getCallingCode().equals(existingUser.getUserDetails().getCallingCode())
            || !user.getUserDetails().getPhoneNumber().equals(existingUser.getUserDetails().getPhoneNumber())) {
            checkPhoneNumberNotUsed(user.getUserDetails().getCallingCode(), user.getUserDetails().getPhoneNumber());
        }
        existingUser.setEmail(user.getEmail());
        existingUser.getUserDetails().setCallingCode(user.getUserDetails().getCallingCode());
        existingUser.getUserDetails().setPhoneNumber(user.getUserDetails().getPhoneNumber());
        existingUser.getUserDetails().setDescription(user.getUserDetails().getDescription());
        existingUser.getUserDetails().setFirstName(user.getUserDetails().getFirstName());
        existingUser.getUserDetails().setLastName(user.getUserDetails().getLastName());

        return userRepository.save(existingUser);
    }

    public User updateProfilePictureById(UUID userId, MultipartFile image) throws IOException {
        User user = getById(userId);
        securityService.authorize(userId);

        URL profilePictureURL = firebaseStorageService.uploadFile(userId.toString(), image, "image");
        user.getUserDetails().setProfilePictureURL(profilePictureURL.toString());
        return userRepository.save(user);
    }

    public User updatePasswordById(UUID userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        securityService.authorize(userId);

        if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException(ErrorMessage.PASSWORDS_NOT_MATCHING);
        }
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public void deleteById(UUID userId, String password) {
        User user = getById(userId);
        securityService.authorize(userId);

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException(ErrorMessage.PASSWORDS_NOT_MATCHING);
        }
        userRepository.delete(user);
    }

    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND, "user", id));
    }

    @Transactional
    public void sendNewPasswordByEmail(String email) {
        User user = getByEmail(email);
        if (user == null) {
            throw new BadRequestException(ErrorMessage.NOT_ALLOWED_TO_CREATE_APARTMENTS);
        }
        String newPassword = secureRandomService.generateRandomPassword();
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));

        userRepository.save(user);
        sendNewPasswordViaEmail(user, newPassword);
    }

    public void banById(UUID id) {
        User userToBeBanned = getById(id);
        if (!securityService.hasRole(Role.ADMIN) || userToBeBanned.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN);
        }
        userToBeBanned.setIsBanned(true);
        userRepository.save(userToBeBanned);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private void sendNewPasswordViaEmail(User user, String newPassword) {
        String text = String.format(
                """
                Hello %s,
                
                This is your new password for OAM: %s
                
                For security reasons, we recommend you to change it as soon as possible!
                
                Kind regards,
                Support Team
                """,
                user.getUserDetails().getFirstName(), newPassword);

        emailSenderService.sendSimpleMessage(user, "Your password has been reset", text);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    private void checkEmailNotUsed(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException(ErrorMessage.ALREADY_EXISTS, "A user with this email");
        }
    }

    private void checkPhoneNumberNotUsed(String callingCode, String phoneNumber) {
        if (userRepository.existsByPhoneNumber(callingCode, phoneNumber)) {
            throw new ConflictException(ErrorMessage.ALREADY_EXISTS, "A user with this phone number");
        }
    }
}
