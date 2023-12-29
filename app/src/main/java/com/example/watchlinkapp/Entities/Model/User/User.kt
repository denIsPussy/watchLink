package com.example.watchlinkapp.Entities.Model.User

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.mindrot.jbcrypt.BCrypt

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int?,
    @ColumnInfo(name = "user_name")
    val userName: String,
    @ColumnInfo(name = "user_date_of_birth")
    val dateOfBirth: String,
    @ColumnInfo(name = "user_phone_number")
    val phoneNumber: String,
    @ColumnInfo(name = "user_password")
    val password: String
) {
    @Ignore
    constructor(
        userName: String,
        dateOfBirth: String,
        phoneNumber: String,
        password: String
    ) : this(null, userName, dateOfBirth, phoneNumber, password)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as User
        return userId == other.userId
    }

    override fun hashCode(): Int {
        return userId ?: -1
    }

    companion object {
        fun hashPassword(password: String): String {
            return BCrypt.hashpw(password, BCrypt.gensalt())
        }

        fun checkPassword(inputPassword: String, hashedPassword: String): Boolean {
            return BCrypt.checkpw(inputPassword, hashedPassword)
        }
    }
}