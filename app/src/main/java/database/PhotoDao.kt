package database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

interface PhotoDao {
    @Insert
    fun insertmenu(orderedMenuEntity: PhotoEntity)

    @Delete
    fun deletemenu(orderedMenuEntity: PhotoEntity)


    fun deleteAll()
}