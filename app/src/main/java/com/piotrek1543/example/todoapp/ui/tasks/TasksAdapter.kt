package com.piotrek1543.example.todoapp.ui.tasks

import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.piotrek1543.example.todoapp.R
import com.piotrek1543.example.todoapp.data.model.Task
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

//TODO: Use databinding library
class TasksAdapter(tasks: List<Task>) : BaseAdapter() {
    private val taskClickSubject = PublishSubject.create<Task>()
    private val taskToggleSubject = PublishSubject.create<Task>()
    private lateinit var tasks: List<Task>

    val taskClickObservable: Observable<Task>
        get() = taskClickSubject

    val taskToggleObservable: Observable<Task>
        get() = taskToggleSubject

    init {
        setList(tasks)
    }

    fun replaceData(tasks: List<Task>) {
        setList(tasks)
        notifyDataSetChanged()
    }

    private fun setList(tasks: List<Task>) {
        this.tasks = tasks
    }

    override fun getCount(): Int = tasks.size

    override fun getItem(position: Int): Task = tasks[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        val rowView: View = view
                ?: LayoutInflater.from(viewGroup.context).inflate(R.layout.task_item, viewGroup, false)

        val task = getItem(position)

        rowView.findViewById<TextView>(R.id.title).text = task.titleForList

        val completeCB = rowView.findViewById<CheckBox>(R.id.completeCheckBox)

        // Active/completed task UI
        completeCB.isChecked = task.completed
        if (task.completed) {
            ViewCompat.setBackground(
                    rowView,
                    ContextCompat.getDrawable(viewGroup.context, R.drawable.list_completed_touch_feedback))
        } else {
            ViewCompat.setBackground(
                    rowView,
                    ContextCompat.getDrawable(viewGroup.context, R.drawable.touch_feedback))
        }

        completeCB.setOnClickListener { taskToggleSubject.onNext(task) }

        rowView.setOnClickListener { taskClickSubject.onNext(task) }

        return rowView
    }
}