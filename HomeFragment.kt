package com.example.todoapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchView()
        setupFab()
        observeTodos()
    }

    private fun setupRecyclerView() {
        todoAdapter = TodoAdapter(
            onItemClick = { todo ->
                val action = HomeFragmentDirections
                    .actionHomeFragmentToDetailFragment(todo.id)
                findNavController().navigate(action)
            },
            onDeleteClick = { todo ->
                viewModel.deleteTodo(todo)
            }
        )

        binding.recyclerViewTodos.apply {
            adapter = todoAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupSearchView() {
        binding.searchView.addTextChangedListener { text ->
            viewModel.searchTodos(text.toString())
        }
    }

    private fun setupFab() {
        binding.fabAddTodo.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addTodoFragment)
        }
    }

    private fun observeTodos() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.todos.collectLatest { todos ->
                todoAdapter.submitList(todos)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 
