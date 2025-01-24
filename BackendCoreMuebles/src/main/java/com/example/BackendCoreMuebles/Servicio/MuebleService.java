package com.example.BackendCoreMuebles.Servicio;

import com.example.BackendCoreMuebles.Modelos.Mueble;
import com.example.BackendCoreMuebles.Repositorio.MuebleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MuebleService {
    @Autowired
    private final MuebleRepository muebleRepository;

    public MuebleService(MuebleRepository muebleRepository) {
        this.muebleRepository = muebleRepository;
    }

    //Metodo para Obtener todos los muebles
    public List<Mueble> getAllMuebles() {
        return muebleRepository.findAll();
    }

    //Metodo para Obtener Material por ID
    public Mueble findMuebleById(int id) throws Exception {
        Mueble mueble = muebleRepository.findById(id);
        if (mueble == null) {
            throw new Exception("No se encontró mueble con el ID: " + id);
        }
        return mueble;
    }

    //Metodo para Crear Material
    public Mueble saveMueble(Mueble mueble) throws Exception{
        if(!mueble.getNombreMueble().matches("^[a-zA-Z\\s]*$")){
            throw new Exception("El nombre debe contar solo con letras y comenzar con una mayúscula");
        }
        if (mueble.getTiempoBaseProduccion()<0){
            throw new Exception("No se puede asignar un tiempo de produccion menor a 0 al mueble");
        }
        if (mueble.getPrecioMueble()<=0){
            throw new Exception("El precio del mueble no puede ser menor o igula a 0");
        }
        return muebleRepository.save(mueble);
    }

    //Metodo para Actualizar Empleado
    public Mueble updateMueble(int idMueble, Mueble muebleActualizado) throws Exception{
        Mueble muebleEncontrado = muebleRepository.findById(idMueble);
        if (muebleEncontrado != null){
            if(!muebleActualizado.getNombreMueble().matches("^[a-zA-Z\\s]*$")){
                throw new Exception("El nombre debe contar solo con letras y comenzar con una mayúscula");
            }
            if (muebleActualizado.getTiempoBaseProduccion()<0){
                throw new Exception("No se puede asignar un tiempo de produccion menor a 0 al mueble");
            }
            if (muebleActualizado.getPrecioMueble()<=0){
                throw new Exception("El precio del mueble no puede ser menor o igula a 0");
            }
            muebleEncontrado.setNombreMueble(muebleActualizado.getNombreMueble());
            muebleEncontrado.setTiempoBaseProduccion(muebleActualizado.getTiempoBaseProduccion());
            muebleEncontrado.setPrecioMueble(muebleActualizado.getPrecioMueble());
            return muebleRepository.save(muebleEncontrado);
        } else {
            throw new Exception("El mueble con ID: " + idMueble + " no existe");
        }
    }

    //Metodo para Eliminar Mueble
    public boolean deleteMueble(int idMueble) throws Exception {
        Mueble muebleEncontrado = muebleRepository.findById(idMueble);
        if (muebleEncontrado != null) {
            muebleRepository.deleteById(idMueble);
            return true;
        } else {
            throw new Exception("El mueble con ID: " + idMueble + " no existe");
        }
    }
}
