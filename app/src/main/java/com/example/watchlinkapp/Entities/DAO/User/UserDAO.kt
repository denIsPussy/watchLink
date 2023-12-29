package com.example.watchlinkapp.Entities.DAO.User

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.watchlinkapp.Entities.Model.User.User

@Dao
interface UserDAO {
    @Query("select * from users")
    fun getAll(): List<User>

    @Query("select * from users where user_name = :userName")
    fun getUserByName(userName: String): User

    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)
}