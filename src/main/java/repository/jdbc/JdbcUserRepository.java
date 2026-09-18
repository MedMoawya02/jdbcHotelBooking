package repository.jdbc;

import db.DatabaseConnection;
import model.Role;
import model.User;
import repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JdbcUserRepository implements UserRepository {
    private final Connection connection;
    public  JdbcUserRepository(){
        this.connection= DatabaseConnection.getInstance().getConnection();
    }
    @Override
    public void save(User user){
        String sql = """
                INSERT INTO users
                (id, first_name, last_name, email, phone, password, role)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try(PreparedStatement statement=connection.prepareStatement(sql)) {
            statement.setObject(1,user.getId());
            statement.setString(2,user.getFirstName());
            statement.setString(3,user.getLastName());
            statement.setString(4,user.getEmail());
            statement.setString(5,user.getPhone());
            statement.setString(6,user.getPassword());
            statement.setString(7,user.getRole().name());
            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erreur lors de l'enregistrement de l'utilisateur : "
                            + e.getMessage(),
                    e
            );
        }
    }
    @Override
    public Optional<User> findById(UUID id){
        return Optional.empty();
    }
    @Override
    public Optional<User> findByEmail(String email){
        String sql= """
                SELECT id,firstName,LastName,email,phone,password,role From users where email = ?
                """;
        try (PreparedStatement statement=connection.prepareStatement(sql)){
             statement.setString(1,email);
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()){
                UUID id=resultSet.getObject(1,UUID.class);
                String firstNAme=resultSet.getString("firstNAme");
                String lastNAme=resultSet.getString("lastName");
                String userEmail= resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String password = resultSet.getString("password");
                Role role= Role.valueOf(resultSet.getString("role"));
                User user=new User(id,firstNAme,lastNAme,email,phone,password);
                user.setRole(role);
                return Optional.of(user);
            }
        }catch (SQLException e){
            throw new RuntimeException(
                    "Erreur lors de la recherche par email : "
                            + e.getMessage(),
                    e
            );
        }
        return Optional.empty();
    }
    @Override
    public boolean existsByEmail(String email){
        return false;
    }
    @Override
    public List<User> findAll(){
        return new ArrayList<>();
    }
    @Override
    public void update(User user){}
}
