package models.mappers;

import models.dto.FluturimiTabelaDTO;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FluturimiMapper {

    public static FluturimiTabelaDTO toDTO(ResultSet rs) throws SQLException {
        return new FluturimiTabelaDTO(
                rs.getInt("id_fluturimit"),
                rs.getString("numri_fluturimit"),
                rs.getString("kompania_emri"),
                rs.getString("aeroporti_nisjes"),     // nga JOIN i parë i aeroporteve
                rs.getString("aeroporti_mberritjes"), // nga JOIN i dytë i aeroporteve
                rs.getTimestamp("ora_nisjes_planifikuar"),
                rs.getString("statusi")
        );
    }
}