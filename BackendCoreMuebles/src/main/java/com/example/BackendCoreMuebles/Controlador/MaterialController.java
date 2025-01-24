package com.example.BackendCoreMuebles.Controlador;

import com.example.BackendCoreMuebles.Modelos.Material;
import com.example.BackendCoreMuebles.Servicio.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/material")
public class MaterialController {
    @Autowired
    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping("/info")
    public  String index(){
        return "Conectado a la tabla Materiales";
    }

    @GetMapping()
    public List<Material> getAllMateriales(){
        return materialService.getAllMaterials();
    }

    @GetMapping("/{idMaterial}")
    public ResponseEntity<Material> getMaterialById(@PathVariable int idMaterial) throws Exception {
        Material material = materialService.findMaterialById(idMaterial);
        return material != null ? ResponseEntity.ok(material) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Material> createMaterial(@RequestBody Material material) throws Exception{
        return new ResponseEntity<>(materialService.saveMaterial(material), HttpStatus.CREATED);
    }

    @PutMapping("/{idMaterial}")
    public ResponseEntity<Material> updateMaterial(@PathVariable int idMaterial, @RequestBody Material material) throws Exception{
        Material actualizar = materialService.updateMaterial(idMaterial,material);
        return actualizar != null ? ResponseEntity.ok(actualizar) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{idMaterial}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable int idMaterial) throws Exception{
        return materialService.deleteMaterial(idMaterial) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
