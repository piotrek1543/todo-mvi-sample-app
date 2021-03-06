/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.piotrek1543.example.todoapp.ui.addedittask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.piotrek1543.example.todoapp.EventObserver
import com.piotrek1543.example.todoapp.R
import com.piotrek1543.example.todoapp.databinding.FragmentAddtaskBinding
import com.piotrek1543.example.todoapp.presentation.addedittask.AddEditTaskViewModel
import com.piotrek1543.example.todoapp.ui.util.ADD_EDIT_RESULT_OK
import com.piotrek1543.example.todoapp.ui.util.obtainViewModel
import com.piotrek1543.example.todoapp.ui.util.setupSnackbar

/**
 * Main UI for the add task screen. Users can enter a task title and description.
 */
class AddEditTaskFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentAddtaskBinding

    private lateinit var viewModel: AddEditTaskViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_addtask, container, false)
        viewModel = obtainViewModel(AddEditTaskViewModel::class.java)
        viewDataBinding = FragmentAddtaskBinding.bind(root).apply {
            this.viewmodel = viewModel
        }
        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupFab()
        setupSnackbar()
        setupNavigation()
        loadData()
    }

    private fun setupFab() {
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.let {
            it.setImageDrawable(resources.getDrawable(R.drawable.ic_done_white_24dp, it.context!!.theme))
            it.setOnClickListener {
                viewDataBinding.viewmodel?.saveTask()
            }
        }
    }

    private fun setupSnackbar() {
        viewDataBinding.viewmodel?.let {
            view?.setupSnackbar(this, it.snackbarMessage, Snackbar.LENGTH_SHORT)
        }
    }

    private fun setupNavigation() {
        viewModel.taskUpdatedEvent.observe(this, EventObserver {
            val action = AddEditTaskFragmentDirections
                    .actionAddEditTaskFragmentToTasksFragment(ADD_EDIT_RESULT_OK)
            findNavController().navigate(action)
        })
    }

    private fun loadData() {
        // Add or edit an existing task?
        viewDataBinding.viewmodel?.start(getTaskId())
    }

    private fun getTaskId(): String? {
        return arguments?.let {
            AddEditTaskFragmentArgs.fromBundle(it).TASKID
        }
    }
}
