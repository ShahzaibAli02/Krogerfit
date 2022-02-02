package com.krger.krgerfit.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.krger.krgerfit.Enums.AppointmentState
import com.krger.krgerfit.Interfaces.onItemSelectedListener
import com.krger.krgerfit.Model.Appointment
import com.krger.krgerfit.Model.User
import com.krger.krgerfit.R
import com.krger.krgerfit.Utils.Util
import com.krger.krgerfit.databinding.LytAppointment2Binding
import com.krger.krgerfit.databinding.LytUserBinding
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target


class UserAdapterAdmin(val listData: MutableList<User>, val context: Context, val onItemSelectedListener: onItemSelectedListener) : RecyclerView.Adapter<UserAdapterAdmin.ViewHolder>()
{



    val mUid:String=FirebaseAuth.getInstance().currentUser!!.uid

    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): ViewHolder {
        val binding: LytUserBinding=LytUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, i: Int)
    {


        val user=listData[holder.adapterPosition]
        val binding=holder.binding

        if(user.imageLink.isNotEmpty())
        {
            Picasso.get().load(user.imageLink).placeholder(R.drawable.ic_loading).error(R.drawable.ic_avatar).into(binding.imgUser);
        }
        else
            Picasso.get().load(R.drawable.ic_avatar).into(binding.imgUser);


        binding.txtDate.text=Util.getFormattedDate(user.date)
        binding.txtName.text=user.userName

        var adminMessage="Are you sure to make this user admin  ?"
        if(user.admin)
        {
            binding.btnMakeAdmin.text="Remove Admin"
            adminMessage="Are you sure to remove this user as admin  ?"
        }
        else
            binding.btnMakeAdmin.text="Make Admin"






        binding.btnRemove.setOnClickListener {


            AlertDialog.Builder(context)
                .setMessage("Are you sure to remove this user ?")
                .setPositiveButton("Yes",object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        onItemSelectedListener.onItemSelected( binding.btnRemove,holder.adapterPosition)
                    }
                })
                .setNegativeButton("No",null).show()

        }

        binding.btnMakeAdmin.setOnClickListener {


            AlertDialog.Builder(context)
                .setMessage(adminMessage)
                .setPositiveButton("Yes",object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        onItemSelectedListener.onItemSelected( binding.btnMakeAdmin,holder.adapterPosition)
                    }
                })
                .setNegativeButton("No",null).show()

        }




    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class ViewHolder(itemView: LytUserBinding) : RecyclerView.ViewHolder(itemView.root)
    {
        var binding: LytUserBinding=itemView
    }

}
