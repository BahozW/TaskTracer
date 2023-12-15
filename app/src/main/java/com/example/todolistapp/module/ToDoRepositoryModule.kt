package com.example.todolistapp.module

import com.example.todolistapp.data.ToDoRepository
import com.example.todolistapp.data.ToDoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ToDoRepositoryModule {

    @Provides
    @Singleton
    fun provideToDoRepository(): ToDoRepository {
        return ToDoRepositoryImpl()
    }
}
