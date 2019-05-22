package com.piotrek1543.example.todoapp.util.schedulers

import io.reactivex.Scheduler

/**
 * Allow providing different types of [Scheduler]s.
 */
interface BaseSchedulerProvider {
  fun computation(): Scheduler

  fun io(): Scheduler

  fun ui(): Scheduler
}
