package be.ucll.electroman.repository;

import android.app.Application;

import androidx.room.Room;

import java.util.List;

import be.ucll.electroman.database.ElectromanDatabase;
import be.ucll.electroman.database.UserDao;
import be.ucll.electroman.models.User;
import be.ucll.electroman.models.UserWithWorkOrders;

public class UserRepository {
    private final UserDao mUserDao;

    public UserRepository(Application application) {
        ElectromanDatabase db = ElectromanDatabase.getDatabase(application);
        mUserDao = db.userDao();
    }

    public void insert(User user) {
            mUserDao.insert(user);
    }

    public User findByUserName(String userName) {

            return mUserDao.findByUserName(userName);
    }

    public UserWithWorkOrders getUserWithWorkOrders(String userName) {
        return mUserDao.getUserWithWorkOrders(userName);
    }
}
