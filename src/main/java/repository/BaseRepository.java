package repository;

import models.mappers.IMapper;
import services.DatabaseService;

import java.sql.*;
import java.util.StringJoiner;

public abstract class BaseRepository<T> implements IRepository<T> {

    protected abstract String tableName();
    protected abstract String idColumnName();
    protected abstract String[] insertColumns();
    protected abstract String[] updateColumns();
    protected abstract IMapper<T> getMapper();

    protected abstract void setPstmCreate(PreparedStatement pstm, T obj) throws SQLException;
    protected abstract void setPstmUpdate(PreparedStatement pstm, T obj) throws SQLException;

    protected String buildInsertQuery() {
        StringJoiner columns = new StringJoiner(", ");
        StringJoiner placeholders = new StringJoiner(", ");

        for (int i = 0; i < insertColumns().length; i++) {
            columns.add(insertColumns()[i]);
            placeholders.add("?");
        }

        return "INSERT INTO " + tableName() +
                " (" + columns + ") VALUES (" + placeholders + ")";
    }

    protected String buildUpdateQuery() {
        StringJoiner setClause = new StringJoiner(", ");

        for (String column : updateColumns()) {
            setClause.add(column + " = ?");
        }

        return "UPDATE " + tableName() +
                " SET " + setClause +
                " WHERE " + idColumnName() + " = ?";
    }

    protected String buildSelectByIdQuery() {
        return "SELECT * FROM " + tableName() +
                " WHERE " + idColumnName() + " = ?";
    }

    protected String buildDeleteByIdQuery() {
        return "DELETE FROM " + tableName() +
                " WHERE " + idColumnName() + " = ?";
    }

    @Override
    public T create(T obj) {
        String query = buildInsertQuery();

        try (
                Connection connection = DatabaseService.getConnection();
                PreparedStatement pstm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            setPstmCreate(pstm, obj);
            pstm.executeUpdate();

            try (ResultSet res = pstm.getGeneratedKeys()) {
                if (res.next()) {
                    int id = res.getInt(1);
                    return getById(id);
                }
            }

            return obj;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create record in table: " + tableName(), e);
        }
    }

    @Override
    public T update(T obj) {
        String query = buildUpdateQuery();

        try (
                Connection connection = DatabaseService.getConnection();
                PreparedStatement pstm = connection.prepareStatement(query)
        ) {
            setPstmUpdate(pstm, obj);
            pstm.executeUpdate();
            return obj;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update record in table: " + tableName(), e);
        }
    }

    @Override
    public T getById(int id) {
        String query = buildSelectByIdQuery();

        try (
                Connection connection = DatabaseService.getConnection();
                PreparedStatement pstm = connection.prepareStatement(query)
        ) {
            pstm.setInt(1, id);

            try (ResultSet res = pstm.executeQuery()) {
                if (res.next()) {
                    return getMapper().getFromResultSet(res);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch record from table: " + tableName() + " by id: " + id, e);
        }

        return null;
    }

    @Override
    public boolean delete(int id) {
        String query = buildDeleteByIdQuery();

        try (
                Connection connection = DatabaseService.getConnection();
                PreparedStatement pstm = connection.prepareStatement(query)
        ) {
            pstm.setInt(1, id);
            return pstm.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete record from table: " + tableName() + " by id: " + id, e);
        }
    }

    @Override
    public abstract boolean delete(T obj);
}