/*
 * Copyright 2016, The Android Open Source Project
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

package com.piotrek1543.example.todoapp.ui.tasks

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.piotrek1543.example.todoapp.EventObserver
import com.piotrek1543.example.todoapp.R
import com.piotrek1543.example.todoapp.databinding.FragmentTasksBinding
import com.piotrek1543.example.todoapp.presentation.tasks.TasksFilterType
import com.piotrek1543.example.todoapp.presentation.tasks.TasksViewModel
import com.piotrek1543.example.todoapp.ui.util.obtainViewModel
import com.piotrek1543.example.todoapp.ui.util.setupSnackbar
import timber.log.Timber
import java.util.*

/**
 * Display a grid of [Task]s. User can choose to view all, active or completed tasks.
 */
class TasksFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentTasksBinding
    private lateinit var listAdapter: TasksAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewDataBinding = FragmentTasksBinding.inflate(inflater, container, false).apply {
            viewmodel = obtainViewModel(TasksViewModel::class.java)
        }
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.menu_clear -> {
                    viewDataBinding.viewmodel?.clearCompletedTasks()
                    true
                }
                R.id.menu_filter -> {
                    showFilteringPopUpMenu()
                    true
                }
                R.id.menu_refresh -> {
                    viewDataBinding.viewmodel?.loadTasks(true)
                    true
                }
                else -> false
            }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tasks_fragment_menu, menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupSnackbar()
        setupListAdapter()
        setupRefreshLayout()
        setupNavigation()
        setupFab()
        viewDataBinding.viewmodel?.loadTasks(true)
    }

    private fun setupNavigation() {
        viewDataBinding.viewmodel?.openTaskEvent?.observe(this, EventObserver {
            openTaskDetails(it)
        })
        viewDataBinding.viewmodel?.newTaskEvent?.observe(this, EventObserver {
            navigateToAddNewTask()
        })
    }

    private fun setupSnackbar() {
        viewDataBinding.viewmodel?.let {
            view?.setupSnackbar(this, it.snackbarMessage, Snackbar.LENGTH_SHORT)
        }
        arguments?.let {
            val message = TasksFragmentArgs.fromBundle(it).userMessage
            viewDataBinding.viewmodel?.showEditResultMessage(message)
        }
    }

    private fun showFilteringPopUpMenu() {
        val view = activity?.findViewById<View>(R.id.menu_filter) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.filter_tasks, menu)

            setOnMenuItemClickListener {
                viewDataBinding.viewmodel?.run {
                    setFiltering(
                            when (it.itemId) {
                                R.id.active -> TasksFilterType.ACTIVE_TASKS
                                R.id.completed -> TasksFilterType.COMPLETED_TASKS
                                else -> TasksFilterType.ALL_TASKS
                            }
                    )
                    loadTasks(false)
                }
                true
            }
            show()
        }
    }

    private fun setupFab() {
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.let {
            it.setImageDrawable(resources.getDrawable(R.drawable.ic_add_white_24dp, it.context!!.theme))
            it.setOnClickListener {
                navigateToAddNewTask()
            }
        }
    }

    private fun navigateToAddNewTask() {
        val action = TasksFragmentDirections
                .actionTasksFragmentToAddEditTaskFragment(null,
                        resources.getString(R.string.add_task))
        findNavController().navigate(action)
    }

    private fun openTaskDetails(taskId: String) {
        val action = TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment(taskId)
        findNavController().navigate(action)
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter = TasksAdapter(ArrayList(0), viewModel)
            viewDataBinding.listTasks.adapter = listAdapter
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun setupRefreshLayout() {
        viewDataBinding.swipeRefresh.run {
            // Set the scrolling view in the custom SwipeRefreshLayout.
            scrollUpChild = viewDataBinding.listTasks
        }
    }
}
