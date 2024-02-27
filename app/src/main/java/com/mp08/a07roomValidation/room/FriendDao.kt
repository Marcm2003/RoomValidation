package com.mp08.a07roomValidation.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FriendDao {

    // The flow always holds/caches latest version of data. Notifies its observers when the
    // data has changed.
    @Query("SELECT * FROM friendValidation ORDER BY id ASC")
    suspend fun getFriends(): MutableList<Friend>

    @Query("SELECT * FROM friendValidation WHERE id = :id")
    suspend fun getFriend(id: Int): Friend

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(friend: Friend): Long

    @Update
    suspend fun updateFriend(friend: Friend)

    @Query("DELETE FROM friendValidation WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM friendValidation WHERE age = :age")
    suspend fun deleteByAge(age: Int)

    @Query("SELECT SUM(age) FROM friendValidation")
    suspend fun getSumOfAges(): Int

    @Query("DELETE FROM friendValidation")
    suspend fun deleteAll()

    @Query("select coalesce(max(id),0) from friendValidation")
    suspend fun getLastId(): Int
}

