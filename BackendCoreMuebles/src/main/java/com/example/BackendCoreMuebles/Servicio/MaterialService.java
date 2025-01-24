package com.example.BackendCoreMuebles.Servicio;

import com.example.BackendCoreMuebles.Modelos.Material;
import com.example.BackendCoreMuebles.Repositorio.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {
    @Autowired
    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    //Metodo para Obtener todos los materiales
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    //Metodo para Obtener Material por ID
    public Material findMaterialById(int id) throws Exception {
        Material material = materialRepository.findById(id);
        if (material == null) {
            throw new Exception("No se encontró material con el ID: " + id);
        }
        return material;
    }

    //Metodo para Crear Material
    public Material saveMaterial(Material material) throws Exception{
        if(!material.getNombreMaterial().matches("^[a-zA-Z\\s]*$")){
            throw new Exception("El nombre debe contar solo con letras y comenzar con una mayúscula");
        }
        if (material.getFactorTiempo()<0){
            throw new Exception("No se puede asignar un factor tiempo menor a 0 al material");
        }
        if (material.getPrecioMaterial()<=0){
            throw new Exception("El precio del material no puede ser menor o igula a 0");
        }
        return materialRepository.save(material);
    }

    //Metodo para Actualizar Empleado
    public Material updateMaterial(int idMaterial, Material materialActualizado) throws Exception{
        Material materialEncontrado = materialRepository.findById(idMaterial);
        if (materialEncontrado != null){
            if(!materialActualizado.getNombreMaterial().matches("^[a-zA-Z\\s]*$")){
                throw new Exception("El nombre debe contar solo con letras y comenzar con una mayúscula");
            }
            if (materialActualizado.getFactorTiempo()<0){
                throw new Exception("No se puede asignar un factor tiempo menor a 0 al material");
            }
            if (materialActualizado.getPrecioMaterial()<=0){
                throw new Exception("El precio del material no puede ser menor o igula a 0");
            }
            materialEncontrado.setNombreMaterial(materialActualizado.getNombreMaterial());
            materialEncontrado.setFactorTiempo(materialActualizado.getFactorTiempo());
            materialEncontrado.setPrecioMaterial(materialActualizado.getPrecioMaterial());
            return materialRepository.save(materialEncontrado);
        } else {
            throw new Exception("El material con ID: " + idMaterial + " no existe");
        }
    }

    //Metodo para Eliminar Material
    public boolean deleteMaterial(int idMaterial) throws Exception {
        Material materialEncontrado = materialRepository.findById(idMaterial);
        if (materialEncontrado != null) {
            materialRepository.deleteById(idMaterial);
            return true;
        } else {
            throw new Exception("El material con ID: " + idMaterial + " no existe");
        }
    }
}