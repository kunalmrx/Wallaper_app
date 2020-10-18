package database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PhotoEntity::class], version = 1)
abstract class Photodatabase (): RoomDatabase()
{
    abstract fun orderedMenuDao():PhotoDao
}