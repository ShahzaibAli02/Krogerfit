package com.krger.krgerfit.Model

import javax.annotation.Nullable

public  class mResult<T>
{

    var  isSuccess:Boolean=false
    lateinit var  message:String
    var  data: T?=null;


    public fun  setResult(data:T)
    {
        this.data=data
    }
    @Nullable
    public  fun getResult():T?
    {
        return data;
    }

}