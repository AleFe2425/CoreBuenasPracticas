package com.example.BackendCoreMuebles.Controlador;

import com.example.BackendCoreMuebles.Modelos.Admin;
import com.example.BackendCoreMuebles.Servicio.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administrador")
public class AdminController {
    @Autowired
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(AdminService adminService, PasswordEncoder passwordEncoder) {
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/info")
    public String index(){
        return "Conectado a la tabla de Admin";
    }

    @GetMapping("")
    public List<Admin> getAllAdmins(){
        return adminService.getAllAdmins();
    }

    @GetMapping("/{idAdmin}")
    public ResponseEntity<Admin> getAdminById(@PathVariable int idAdmin) throws Exception {
        Admin admin = adminService.findAdminById(idAdmin);
        return admin != null ? ResponseEntity.ok(admin) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<Admin> getAdminByUsername(@PathVariable String username) throws Exception {
        Admin admin = adminService.findAdminByUsername(username);
        return admin != null ? ResponseEntity.ok(admin) : ResponseEntity.notFound().build();
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> createAdmin(@RequestBody Admin admin) throws Exception {
        String passwordEncriptado = passwordEncoder.encode(admin.getPassword());
        adminService.saveAdmin(admin, passwordEncriptado);
        return ResponseEntity.ok("Exito en el registro");
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteAdmin (@PathVariable String username) throws Exception {
        return adminService.deleteAdmin(username) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginAdmin(@RequestBody Admin admin) throws Exception {
        Admin encontrado = adminService.findAdminByUsername(admin.getUsername());
        if (encontrado != null && passwordEncoder.matches(admin.getPassword(), encontrado.getPassword() )){
            return ResponseEntity.ok("Inicio Exitoso");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales invalidas");
    }
}
