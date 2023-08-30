package com.oam.controller;

import com.oam.dto.ChangePasswordRequestDto;
import com.oam.dto.CreateUserRequestDto;
import com.oam.dto.UpdateUserRequestDto;
import com.oam.mapper.UserMapper;
import com.oam.dto.UserResponseDto;
import com.oam.model.User;
import com.oam.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody CreateUserRequestDto createUserRequestDto) {
        User user = userService.create(userMapper.mapToEntity(createUserRequestDto));
        return ResponseEntity
                .created(URI.create("/api/users/" + user.getId()))
                .body(userMapper.mapToDto(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable("id") UUID id,
                                                  @Valid @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        User user = userService.update(userMapper.mapToEntity(updateUserRequestDto), id);
        return ResponseEntity.ok(userMapper.mapToDto(user));
    }

    @PatchMapping(path = "/{id}/profilePicture", consumes = { MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<UserResponseDto> updateProfilePictureById(@PathVariable("id") UUID userId,
                                                                    @RequestParam("image") MultipartFile image) throws IOException {
        User user = userService.updateProfilePictureById(userId, image);
        return ResponseEntity.ok(userMapper.mapToDto(user));
    }

    @PatchMapping(path = "/{id}/password")
    public ResponseEntity<UserResponseDto> updatePasswordById(@PathVariable("id") UUID userId,
                                                              @Valid @RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
        User user = userService.updatePasswordById(userId, changePasswordRequestDto.oldPassword(), changePasswordRequestDto.newPassword());
        return ResponseEntity.ok(userMapper.mapToDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(userMapper.mapToDto(userService.getById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") UUID id, @RequestHeader("Password") String password) {
        userService.deleteById(id, password);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{email}/forgotPassword")
    public ResponseEntity<?> forgotPassword(@PathVariable("email") String email) {
        userService.sendNewPasswordByEmail(email);
        return ResponseEntity.ok("A new password has been sent to you by email.");
    }

    @PostMapping("/{id}/ban")
    public ResponseEntity<?> banById(@PathVariable("id") UUID id) {
        userService.banById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers().stream().map(userMapper::mapToDto).toList());
    }
}
