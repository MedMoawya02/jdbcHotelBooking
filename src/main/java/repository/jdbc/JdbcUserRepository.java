package repository.jdbc;

import db.DatabaseConnection;
import model.Role;
import model.User;
import repository.UserRepository;

import java.sql.*;
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
                SELECT id,first_name,last_name,email,phone,password,role From users where email = ?
                """;
        try (PreparedStatement statement=connection.prepareStatement(sql)){
             statement.setString(1,email);
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()){
                UUID id=resultSet.getObject(1,UUID.class);
                String firstNAme=resultSet.getString("first_name");
                String lastNAme=resultSet.getString("last_name");
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
        String sql= """
                SELECT id,first_name,last_name,email,phone,password,role FROM users WHERE email=?
                """;
        try(PreparedStatement statement=connection.prepareStatement(sql)) {
            statement.setString(1,email);
            ResultSet resultSet=statement.executeQuery();
            return resultSet.next();
        }catch (SQLException e){
            throw new RuntimeException(
                    "Erreur lors de la vérification de l'email : "
                            + e.getMessage(),
                    e
            );
        }
    }
    @Override
    public List<User> findAll(){
        List<User> users=new ArrayList<>();
        String sql= """
                SELECT id,first_name,last_name,email,phone,password,role FROM users
                """;
        try (PreparedStatement statement= connection.prepareStatement(sql)){
            ResultSet resultSet=statement.executeQuery();
            while (resultSet.next()){
                UUID id=resultSet.getObject("id",UUID.class);
                String first_name=resultSet.getString("first_name");
                String last_name=resultSet.getString("lastst_name");
                String email=resultSet.getString("email");
                String phone=resultSet.getString("phone");
                String password=resultSet.getString("password");
                Role role=Role.valueOf(resultSet.getString("role"));
                User user=new User(id,first_name,last_name,email,phone,password);
                user.setRole(role);
                users.add(user);
            }
        }catch (SQLException e){
            throw new RuntimeException(
                    "Erreur lors de la récupération des utilisateurs : "
                            + e.getMessage(),
                    e
            );
        }
        return users;
    }
    @Override
    public void update(User user){}
}
