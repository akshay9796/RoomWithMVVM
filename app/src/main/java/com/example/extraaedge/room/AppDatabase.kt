package com.example.extraaedge.room

import android.content.Context
import androidx.room.*
import com.example.extraaedge.domain.model.RocketDataList
import dagger.Module

@Module
@Database(entities = [RocketDataList::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun rocketDao(): RocketDao

    companion object {

        @Volatile
        private var databseInstance: AppDatabase? = null

        fun getDatabasenIstance(mContext: Context): AppDatabase =
            databseInstance ?: synchronized(this) {
                databseInstance ?: buildDatabaseInstance(mContext).also {
                    databseInstance = it
                }
            }

        private fun buildDatabaseInstance(mContext: Context) =
            Room.databaseBuilder(mContext, AppDatabase::class.java, "ExtraaEdge_DATABASE")
                .fallbackToDestructiveMigration()
                .build()
    }
}