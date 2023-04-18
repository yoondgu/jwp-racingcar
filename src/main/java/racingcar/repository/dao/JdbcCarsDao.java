package racingcar.repository.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import racingcar.repository.dao.entity.CarEntity;

@Component
public class JdbcCarsDao implements CarsDao {

    private final RowMapper<CarEntity> actorRowMapper = (resultSet, rowNum) -> {
        CarEntity car = new CarEntity(
                resultSet.getLong("play_id"),
                resultSet.getString("name"),
                resultSet.getInt("position")
        );
        return car;
    };
    private final ResultSetExtractor<Map<Long, List<CarEntity>>> actorResultSetExtractor = resultSet -> {
        final Map<Long, List<CarEntity>> result = new LinkedHashMap<>();
        while (resultSet.next()) {
            final long id = resultSet.getLong("play_id");
            final List<CarEntity> found = result.getOrDefault(id, new ArrayList<>());
            found.add(actorRowMapper.mapRow(resultSet, resultSet.getRow()));
            result.put(id, found);
        }
        return result;
    };

    private final JdbcTemplate jdbcTemplate;

    public JdbcCarsDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insert(final long id, final List<CarEntity> cars) {
        jdbcTemplate.batchUpdate("INSERT INTO cars (play_id, name, position) VALUES (?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                        ps.setLong(1, id);
                        ps.setString(2, cars.get(i).getName());
                        ps.setInt(3, cars.get(i).getPosition());
                    }

                    @Override
                    public int getBatchSize() {
                        return cars.size();
                    }
                });
    }

    @Override
    public List<CarEntity> find(final long id) {
        return jdbcTemplate.query(
                "SELECT play_id, name, position FROM cars WHERE play_id = ?",
                actorRowMapper, id
        );
    }

    // TODO 다른 테이블과 조인하는 쿼리를 해당 Dao에서 쓰는 게 맞을까?
    @Override
    public Map<Long, List<CarEntity>> findAllByPlayerId() {
        return jdbcTemplate.query(
                "SELECT play_id, name, position FROM cars, play_records "
                        + "WHERE cars.play_id = play_records.id "
                        + "ORDER BY play_records.created_at DESC",
                actorResultSetExtractor);
    }
}
