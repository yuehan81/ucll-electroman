package be.ucll.electroman.database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import be.ucll.electroman.models.User;
import be.ucll.electroman.models.UserWithWorkOrders;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User")
    List<User> getAll();

    @Query("SELECT * FROM User WHERE uid IN (:userIds)")
    List<User> loadAllByIds(long[] userIds);

    @Query("SELECT * FROM User WHERE uid LIKE :uid")
    User findById(long uid);

    @Query("SELECT * FROM User WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);

    @Query("SELECT * FROM User WHERE user_name LIKE :userName")
    User findByUserName(String userName);

    @Transaction
    @Query("SELECT * FROM User WHERE user_name LIKE :userName")
    UserWithWorkOrders getUserWithWorkOrders(String userName);


    @Insert
    void insertAll(User... users);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(User user);

    @Delete
    void delete(User user);


}
