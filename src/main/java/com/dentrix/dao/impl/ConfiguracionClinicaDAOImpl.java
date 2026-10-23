package com.dentrix.dao.impl;

import com.dentrix.dao.ConfiguracionClinicaDAO;
import com.dentrix.model.ConfiguracionClinica;
import com.dentrix.util.ConexionDB;
import java.sql.*;

public class ConfiguracionClinicaDAOImpl implements ConfiguracionClinicaDAO {

    @Override
    public ConfiguracionClinica obtener() {
        String sql = "SELECT * FROM configuracion_clinica LIMIT 1";
        try (Connection conn = ConexionDB.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                ConfiguracionClinica c = new ConfiguracionClinica();
                c.setId(rs.getInt("id")); c.setRuc(rs.getString("ruc")); c.setRazonSocial(rs.getString("razon_social"));
                c.setNombreComercial(rs.getString("nombre_comercial")); c.setDireccionFiscal(rs.getString("direccion_fiscal"));
                c.setSerieBoleta(rs.getString("serie_boleta")); c.setCelularYape(rs.getString("celular_yape"));
                c.setTitularYape(rs.getString("titular_yape")); c.setDirectorMedico(rs.getString("director_medico"));
                c.setCop(rs.getString("cop")); c.setRne(rs.getString("rne")); c.setLicenciaMunicipal(rs.getString("licencia_municipal"));
                return c;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public boolean actualizar(ConfiguracionClinica c) {
        String sql = "UPDATE configuracion_clinica SET ruc=?, razon_social=?, nombre_comercial=?, direccion_fiscal=?, serie_boleta=?, celular_yape=?, titular_yape=?, director_medico=?, cop=?, rne=?, licencia_municipal=? WHERE id=?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getRuc()); ps.setString(2, c.getRazonSocial()); ps.setString(3, c.getNombreComercial());
            ps.setString(4, c.getDireccionFiscal()); ps.setString(5, c.getSerieBoleta()); ps.setString(6, c.getCelularYape());
            ps.setString(7, c.getTitularYape()); ps.setString(8, c.getDirectorMedico()); ps.setString(9, c.getCop());
            ps.setString(10, c.getRne()); ps.setString(11, c.getLicenciaMunicipal()); ps.setInt(12, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}