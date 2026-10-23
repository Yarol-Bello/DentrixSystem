package com.dentrix.dao;

import com.dentrix.model.Cita;
import com.dentrix.model.Usuario;
import java.sql.Date;
import java.util.List;

public interface CitaDAO {
    List<Cita> listarPorFecha(Date fecha);
    boolean guardar(Cita c);
    boolean actualizar(Cita c);
    boolean actualizarEstadoCita(int citaId, String nuevoEstado);
    List<Usuario> listarOdontologos();
}