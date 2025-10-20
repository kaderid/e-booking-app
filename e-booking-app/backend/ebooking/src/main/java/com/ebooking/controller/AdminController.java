package com.ebooking.controller;

import com.ebooking.dto.StatistiquesDTO;
import com.ebooking.dto.StatistiquesPrestataireDTO;
import com.ebooking.dto.UserResponseDTO;
import com.ebooking.services.AdminService;
import com.ebooking.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    @GetMapping("/users")
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/users/{id}/activer")
    public UserResponseDTO activerCompte(@PathVariable Long id) {
        return adminService.activerCompte(id);
    }

    @PutMapping("/users/{id}/bloquer")
    public UserResponseDTO bloquerCompte(@PathVariable Long id) {
        return adminService.bloquerCompte(id);
    }

    @GetMapping("/statistiques")
    public StatistiquesDTO getStatistiques() {
        return adminService.getStatistiques();
    }

    @GetMapping("/statistiques/prestataires")
    public List<StatistiquesPrestataireDTO> getStatistiquesPrestataires() {
        return adminService.getStatistiquesPrestataires();
    }
}
