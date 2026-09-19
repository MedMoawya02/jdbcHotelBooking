package service;

import exception.EmailAlreadyExistsException;
import exception.InvalidCredentialsException;
import model.User;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthService {
    private UserRepository userRepository;
    public AuthService(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    public boolean inscription(UUID id,String firstname,String lastName,String email,String phone,String password)throws EmailAlreadyExistsException,InvalidCredentialsException {
        if(firstname==null||firstname.isBlank()){
            throw new InvalidCredentialsException("Invalid name");
        }
        if(lastName==null||lastName.isBlank()){
            throw new InvalidCredentialsException("Invalid name");
        }
        if(email==null||!email.contains("@")){
            throw new EmailAlreadyExistsException("Email invalid");
        }
        if(password==null||password.length()<3){
            throw new InvalidCredentialsException("Invalid password");
        }
        if(userRepository.existsByEmail(email)){
            return false;
        }

        User newUser=new User(id,firstname,lastName,email,phone,password);
        //users.add(newUser);
        userRepository.save(newUser);
        return true;
    }
    //connexion
    public User connexion(String email,String password){
        Optional<User> user=userRepository.findByEmail(email);
        /*
        if(user.isEmpty()){
            return false;
        }
        return user.get().getPassword().equals(password);*/
        if(user.isPresent()&&user.get().getPassword().equals(password)){
            return user.get();
        }
        return null;
    }

    //edit profil
    public boolean editProfile(UUID id,String newFirstName,String newLastName,String newEmail,String newNum,String newPassword){
        Optional<User> userById=userRepository.findById(id);
        if(userById.isEmpty()){
            return false;
        }
        User user=userById.get();
        user.setFirstName(newFirstName);
        user.setLastName(newLastName);
        user.setEmail(newEmail);
        user.setPhone(newNum);
        user.setPassword(newPassword);
        userRepository.update(user);
        return true;
    }
    //findall
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User finById(UUID id){
        return  userRepository.findById(id).orElse(null);
    }
}
