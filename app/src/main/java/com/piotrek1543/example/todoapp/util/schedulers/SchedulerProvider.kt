package com.piotrek1543.example.todoapp.util.schedulers

import com.piotrek1543.example.todoapp.util.schedulers.BaseSchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Provides different types of schedulers.
 */
object SchedulerProvider : BaseSchedulerProvider {
  override fun computation(): Scheduler = Schedulers.computation()

  override fun io(): Scheduler = Schedulers.io()

  override fun ui(): Scheduler = AndroidSchedulers.mainThread()
}
