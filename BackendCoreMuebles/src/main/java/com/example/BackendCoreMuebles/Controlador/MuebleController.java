package com.example.BackendCoreMuebles.Controlador;

import com.example.BackendCoreMuebles.Modelos.Mueble;
import com.example.BackendCoreMuebles.Servicio.MuebleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mueble")
public class MuebleController {
    @Autowired
    private final MuebleService muebleService;

    public MuebleController(MuebleService muebleService) {
        this.muebleService = muebleService;
    }

    @GetMapping("/info")
    public  String index(){
        return "Conectado a la tabla Mueble";
    }

    @GetMapping()
    public List<Mueble> getAllMateriales(){
        return muebleService.getAllMuebles();
    }

    @GetMapping("/{idMueble}")
    public ResponseEntity<Mueble> getMuebleById(@PathVariable int idMueble) throws Exception {
        Mueble mueble = muebleService.findMuebleById(idMueble);
        return mueble != null ? ResponseEntity.ok(mueble) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Mueble> createMueble(@RequestBody Mueble mueble) throws Exception{
        return new ResponseEntity<>(muebleService.saveMueble(mueble), HttpStatus.CREATED);
    }

    @PutMapping("/{idMueble}")
    public ResponseEntity<Mueble> updateMaterial(@PathVariable int idMueble, @RequestBody Mueble mueble) throws Exception{
        Mueble actualizar = muebleService.updateMueble(idMueble,mueble);
        return actualizar != null ? ResponseEntity.ok(actualizar) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{idMueble}")
    public ResponseEntity<Void> deleteMueble(@PathVariable int idMueble) throws Exception{
        return muebleService.deleteMueble(idMueble) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
