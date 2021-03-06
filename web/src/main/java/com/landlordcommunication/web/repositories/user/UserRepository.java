package com.landlordcommunication.web.repositories.user;

import com.landlordcommunication.web.models.LoginInfo;
import com.landlordcommunication.web.models.Rating;
import com.landlordcommunication.web.models.User;
import com.landlordcommunication.web.models.UserRating;

import java.util.List;

public interface UserRepository {
    void createUser(User user);
    void deleteUser(int userId);
    void modifyUser(int userId, User user);
    List<User> getAllUsers();
    List<User> getAllLandlords();
    List<User> getAllTenants();
    List<Rating> getUserRatings(int userId);
    UserRating getRatingById(int userId);
    List<User> getUsersByResidence(int residenceId);
    void payRentFromTenantToLandlord(int tenantId, int landlordId, int residenceId);
    User getUserByEmail(LoginInfo loginInfo);
    User getUserById(int userId);
}
