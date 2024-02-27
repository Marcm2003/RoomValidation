package com.mp08.a07roomValidation.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "friendValidation")
data class Friend (
    @PrimaryKey(autoGenerate = true) var id: Long,
    var name: String,
    @ColumnInfo(name = "last_name") var lastName: String?,
    @ColumnInfo(name = "phone") var phone: String? = "",
    @ColumnInfo(name = "email") var email: String? = "",
    @ColumnInfo(name = "age") var age: Int = 0
): Serializable