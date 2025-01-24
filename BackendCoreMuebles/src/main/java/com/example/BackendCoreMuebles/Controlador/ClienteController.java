package com.example.BackendCoreMuebles.Controlador;

import com.example.BackendCoreMuebles.Modelos.Cliente;
import com.example.BackendCoreMuebles.Servicio.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
    @Autowired
    private final ClienteService clienteService;
    private final PasswordEncoder passwordEncoder;

    public ClienteController(ClienteService clienteService, PasswordEncoder passwordEncoder) {
        this.clienteService = clienteService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/info")
    public String index(){
        return "Conectado a la tabla de Cliente";
    }

    @GetMapping("")
    public List<Cliente> getAllClientes(){
        return clienteService.getAllClientes();
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable int idCliente) throws Exception {
        Cliente cliente = clienteService.findClienteById(idCliente);
        return cliente != null ? ResponseEntity.ok(cliente) : ResponseEntity.notFound().build();
    }


    @PostMapping("/registrar")
    public ResponseEntity<String> createCliente(@RequestBody Cliente cliente) throws Exception {
        String passwordEncriptado = passwordEncoder.encode(cliente.getPassword());
        clienteService.saveCliente(cliente, passwordEncriptado);
        return ResponseEntity.ok("Exito en el registro");
    }

    @PutMapping("/{idCliente}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable int idCliente, @RequestBody Cliente cliente) throws Exception{
        Cliente actualizar = clienteService.updateCliente(idCliente, cliente);
        return actualizar != null ? ResponseEntity.ok(actualizar) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteCliente (@PathVariable String email) throws Exception {
        return clienteService.deleteCliente(email) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginCliente(@RequestBody Cliente cliente) throws Exception {
        Cliente encontrado = clienteService.findClienteByEmail(cliente.getEmail());
        if (encontrado != null && passwordEncoder.matches(cliente.getPassword(), encontrado.getPassword() )){
            Cliente clienteResponse = new Cliente();
            clienteResponse.setIdCliente(encontrado.getIdCliente());
            clienteResponse.setNombre(encontrado.getNombre());
            clienteResponse.setEmail(encontrado.getEmail());
            clienteResponse.setTelefono(encontrado.getTelefono());
            return ResponseEntity.ok(clienteResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales invalidas");
    }
}

