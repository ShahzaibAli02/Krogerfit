package com.krger.krgerfit.Activities.Admin
import android.app.Activity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.krger.krgerfit.Adapters.AppointmentAdapterAdmin
import com.krger.krgerfit.Adapters.UserAdapterAdmin
import com.krger.krgerfit.DataBaseOperations
import com.krger.krgerfit.Enums.AppointmentState
import com.krger.krgerfit.Interfaces.onDataBaseResult
import com.krger.krgerfit.Interfaces.onItemSelectedListener
import com.krger.krgerfit.Model.Appointment
import com.krger.krgerfit.Model.User
import com.krger.krgerfit.Model.mResult
import com.krger.krgerfit.R
import com.krger.krgerfit.Utils.Util
import com.krger.krgerfit.databinding.ActivityAdminDashboardBinding

class AdminDashboardViewModel (private  val activity: Activity, private  val binding: ActivityAdminDashboardBinding) : ViewModel()
{


    var appointmentsList:MutableList<Appointment> = ArrayList()
    lateinit var appointmentAdapterAdmin:AppointmentAdapterAdmin


    var usersList:MutableList<User> = ArrayList()
    lateinit var userAdapterAdmin:UserAdapterAdmin

    init {

        val values=AppointmentState.values()
        binding.spinnerState.adapter=ArrayAdapter(activity, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,values)



        initRecyclerView()
        initRecyclerView2()
        loadUsers()
        binding.spinnerState.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
              loadAppointments()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }
    fun loadAppointments()
    {

        binding.recyclerView.visibility=View.GONE
        binding.txtErrorMessage.visibility=View.GONE
        binding.progressBar.visibility=View.VISIBLE
        DataBaseOperations.getAppointmentsAtState(binding.spinnerState.selectedItem.toString(),object :onDataBaseResult<Task<QuerySnapshot>>
        {
            override fun onResult(task: mResult<Task<QuerySnapshot>>)
            {
                appointmentsList.clear()
                binding.progressBar.visibility=View.GONE
                val result=task.getResult()
                val snapShots=result!!.result!!
                if(snapShots.isEmpty)
                {
                    binding.txtErrorMessage.visibility=View.VISIBLE
                }
                else
                {
                    for(doc in snapShots.documents)
                    {
                        appointmentsList.add(doc.toObject(Appointment::class.java)!!)
                    }

                    binding.recyclerView.visibility=View.VISIBLE
                    appointmentAdapterAdmin.notifyDataSetChanged()


                }
            }
        })

    }

    fun loadUsers()
    {

        binding.recyclerView2.visibility=View.GONE
        binding.txtErrorMessage2.visibility=View.GONE
        binding.progressBar2.visibility=View.VISIBLE
        DataBaseOperations.getAllUsers(object :onDataBaseResult<Task<QuerySnapshot>>
        {
            override fun onResult(task: mResult<Task<QuerySnapshot>>)
            {
                usersList.clear()
                binding.progressBar2.visibility=View.GONE
                val result=task.getResult()
                val snapShots=result!!.result!!
                if(snapShots.isEmpty)
                {
                    binding.txtErrorMessage2.visibility=View.VISIBLE
                }
                else
                {
                    for(doc in snapShots.documents)
                    {
                        usersList.add(doc.toObject(User::class.java)!!)
                    }

                    binding.recyclerView2.visibility=View.VISIBLE
                    userAdapterAdmin.notifyDataSetChanged()

                }
            }
        })

    }
    fun  setvalsonHeader()
    {
        Util.putValsOnHeader(activity,
            "DashBoard",
            binding.lytHeader.txtHeaderTitle,
            binding.lytHeader.imgUser,
            binding.lytHeader.imgMenu
        )
    }
    fun initRecyclerView() {

        appointmentAdapterAdmin=AppointmentAdapterAdmin(appointmentsList,activity,object :onItemSelectedListener{

            override fun onItemSelected(view: View, position: Int)
            {
                val appointment=appointmentsList[position]
                val progressDialog=Util.getProgressDialog(activity)
                var appointmentState=AppointmentState.CANCELED
                if(view.id==R.id.btnAdd)
                    appointmentState=AppointmentState.APPROVED
                DataBaseOperations.updateAppointmentState(appointment.id,appointmentState,object :onDataBaseResult<Task<Void>>
                {
                    override fun onResult(task: mResult<Task<Void>>)
                    {
                        progressDialog!!.dismiss()

                        if(task.isSuccess)
                        {
                            Util.showMessage(activity,"Appointment Update")
                            appointment.appointmentState=appointmentState
                            appointmentAdapterAdmin.notifyDataSetChanged()
                        }
                        else
                        {
                            Util.showMessage(activity,"Failed To Update")
                        }

                    }
                })
            }
        })
        binding.recyclerView.adapter=appointmentAdapterAdmin

    }

    fun initRecyclerView2() {
        userAdapterAdmin=UserAdapterAdmin(usersList,activity,object :onItemSelectedListener{

            override fun onItemSelected(view: View, position: Int)
            {
                val user=usersList[position]
                val progressDialog=Util.getProgressDialog(activity)


                if(view.id==R.id.btnMakeAdmin)
                {
                    DataBaseOperations.updateUserAdminState(user.uid,!user.admin,object :onDataBaseResult<Task<Void>>
                    {
                        override fun onResult(task: mResult<Task<Void>>)
                        {
                            progressDialog!!.dismiss()

                            if(task.isSuccess)
                            {
                                Util.showMessage(activity,"User Admin State Changed")
                                user.admin=!user.admin
                                userAdapterAdmin.notifyDataSetChanged()
                            }
                            else
                            {
                                Util.showMessage(activity,"Failed To Update")
                            }

                        }
                    })
                }
                if(view.id==R.id.btnRemove)
                {
                    DataBaseOperations.removeUser(user.uid,object :onDataBaseResult<Task<Void>>
                    {
                        override fun onResult(task: mResult<Task<Void>>)
                        {
                            progressDialog!!.dismiss()

                            if(task.isSuccess)
                            {
                                Util.showMessage(activity,"User Removed")
                                usersList.removeAt(position)
                                userAdapterAdmin.notifyDataSetChanged()
                            }
                            else
                            {
                                Util.showMessage(activity,"Failed To Update")
                            }

                        }
                    })
                }


            }
        })
        binding.recyclerView2.adapter=userAdapterAdmin
    }


    class AdminDashboardViewModelFactory(private val activity: Activity, private  val  binding: ActivityAdminDashboardBinding) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AdminDashboardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AdminDashboardViewModel(
                    activity,binding
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


}