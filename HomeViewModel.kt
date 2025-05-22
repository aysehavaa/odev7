package com.example.todoapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.local.TodoEntity
import com.example.todoapp.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    private val _todos = MutableStateFlow<List<TodoEntity>>(emptyList())
    val todos: StateFlow<List<TodoEntity>> = _todos

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        loadTodos()
    }

    fun loadTodos() {
        viewModelScope.launch {
            repository.getAllTodos()
                .catch { e -> e.printStackTrace() }
                .collect { todoList ->
                    _todos.value = todoList
                }
        }
    }

    fun searchTodos(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            repository.searchTodos(query)
                .catch { e -> e.printStackTrace() }
                .collect { todoList ->
                    _todos.value = todoList
                }
        }
    }

    fun deleteTodo(todo: TodoEntity) {
        viewModelScope.launch {
            repository.deleteTodo(todo)
        }
    }
} 
