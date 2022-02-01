package com.krger.krgerfit.Interfaces

import com.krger.krgerfit.Model.mResult

interface  onDataBaseResult<T> {
    fun onResult(task: mResult<T>)
}