package com.example.todoapp.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.local.TodoEntity
import com.example.todoapp.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: TodoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val todoId: Int = checkNotNull(savedStateHandle["todoId"])

    private val _todo = MutableStateFlow<TodoEntity?>(null)
    val todo: StateFlow<TodoEntity?> = _todo

    init {
        loadTodo()
    }

    private fun loadTodo() {
        viewModelScope.launch {
            repository.getAllTodos().collect { todos ->
                _todo.value = todos.find { it.id == todoId }
            }
        }
    }

    fun updateTodo(name: String) {
        _todo.value?.let { todo ->
            viewModelScope.launch {
                repository.updateTodo(todo.copy(name = name))
            }
        }
    }

    fun deleteTodo() {
        _todo.value?.let { todo ->
            viewModelScope.launch {
                repository.deleteTodo(todo)
            }
        }
    }
} 
