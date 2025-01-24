package com.example.BackendCoreMuebles.Servicio;

import com.example.BackendCoreMuebles.Modelos.Admin;
import com.example.BackendCoreMuebles.Repositorio.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    //Metodo para Obtener de todos los admins
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    //Metodo para Obtener Admin por medio de su Id
    public Admin findAdminById(int id) throws Exception{
        Admin admin = adminRepository.findById(id);
        if (admin == null){
            throw new Exception("No se encontro admin con el ID: "+id);
        }
        return admin;
    }

    //Metodo para Obtener Admin por medio de su Username
    public Admin findAdminByUsername(String username) throws Exception{
        Admin admin = adminRepository.findByUsername(username);
        if (admin == null){
            throw new Exception("No se encontro admin con el username: "+username);
        }
        return admin;
    }

    //Metodo para Crear Admin
    public Admin saveAdmin(Admin admin, String encodedPassword) throws Exception {
        Admin adminEncontrado = adminRepository.findByUsername(admin.getUsername());
        if (adminEncontrado != null) {
            throw new Exception("El Admin con username " + admin.getUsername() + " ya existe");
        }

        admin.setPassword(encodedPassword);
        return adminRepository.save(admin);
    }

    //Metodo para Eliminar Admin
    public boolean deleteAdmin(String username)  throws Exception{
        Admin adminEncontrado = adminRepository.findByUsername(username);
        if (adminEncontrado != null){
            adminRepository.deleteById(adminEncontrado.getIdAdmin());
            return true;
        }else {
            throw new Exception("El admin con username: "+username+" no existe");
        }
    }
}
