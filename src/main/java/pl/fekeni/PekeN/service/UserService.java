package pl.fekeni.PekeN.service;

import pl.fekeni.PekeN.entity.User;

public interface UserService {

    public void saveUser(User user);

    public void addStat(User user, String stat) throws Exception;

    public boolean isUserAlreadyPresent(User user);

    public Iterable<User> getAllUsers();

    public User getCurrentUser(String email);

    public User getUserById(Long id) throws Exception;

    public User updateUser(User user) throws Exception;
}
