package com.example.todoapp.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.local.TodoEntity
import com.example.todoapp.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTodoViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    fun addTodo(name: String) {
        viewModelScope.launch {
            repository.insertTodo(TodoEntity(name = name))
        }
    }
} 
