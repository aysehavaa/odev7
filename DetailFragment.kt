package com.example.todoapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todoapp.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeTodo()
    }

    private fun setupViews() {
        binding.buttonUpdate.setOnClickListener {
            val todoName = binding.editTextTodoName.text.toString()
            if (todoName.isNotBlank()) {
                viewModel.updateTodo(todoName)
                findNavController().navigateUp()
            }
        }

        binding.buttonDelete.setOnClickListener {
            viewModel.deleteTodo()
            findNavController().navigateUp()
        }
    }

    private fun observeTodo() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.todo.collectLatest { todo ->
                todo?.let {
                    binding.editTextTodoName.setText(it.name)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 
