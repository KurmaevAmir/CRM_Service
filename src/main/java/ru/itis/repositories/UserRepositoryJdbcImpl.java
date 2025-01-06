package ru.itis.repositories;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.dto.HumanDto;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepositoryJdbcImpl implements UserRepository {
    private final Connection connection;
    PasswordEncoder passwordEncoder;

    private static final String SLQ_FIND_BY_EMAIL_AND_PASSWORD = "SELECT id, password, CASE WHEN EXISTS (SELECT 1 FROM employee e WHERE e.id = h.id)" +
            "THEN 'employee'" +
            "WHEN EXISTS (SELECT 1 FROM client c WHERE c.id = h.id)" +
            "THEN 'client' ELSE NULL END AS role FROM human h WHERE email = ?";
    private static final String SQL_SELECT_COUNT_EMAIL = "select count(*) from human where email = ?";

    public UserRepositoryJdbcImpl(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public Optional<HumanDto> findByEmailAndPassword(String email, String password) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SLQ_FIND_BY_EMAIL_AND_PASSWORD);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String passwordHash = resultSet.getString("password");
            if (passwordEncoder.matches(password, passwordHash)) {
                HumanDto human = new HumanDto();
                human.setId(resultSet.getLong("id"));
                human.setEmail(email);
                human.setRole(resultSet.getString("role"));
                return Optional.of(human);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByEmail(String email) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_COUNT_EMAIL);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;
        }
        return false;
    }
}
