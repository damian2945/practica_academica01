package com.example.practica_academica01

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop

class AlumnoAdapter(
    private val context: Context,
    private val listAlumnos: ArrayList<Alumno>
) : RecyclerView.Adapter<AlumnoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_personas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alumno = listAlumnos[position]

        // Cargar imagen con Glide y aplicar transformación circular
        Glide.with(context)
            .load(alumno.Imagen)
            .transform(CircleCrop())
            .into(holder.imgFoto)

        holder.nombres.text = alumno.nombre
        holder.numeroDoc.text = alumno.cuenta

        // Agregar click listener para mostrar detalles
        holder.itemView.setOnClickListener {
            mostrarDetallesAlumno(alumno)
        }
    }

    override fun getItemCount(): Int {
        return listAlumnos.size
    }

    // Función para agregar un nuevo alumno
    fun agregarAlumno(nuevoAlumno: Alumno) {
        listAlumnos.add(0, nuevoAlumno) // Agregar al inicio
        notifyItemInserted(0)
    }

    // Función para eliminar un alumno
    fun eliminarAlumno(position: Int) {
        if (position >= 0 && position < listAlumnos.size) {
            listAlumnos.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    // Función para limpiar toda la lista
    fun limpiarLista() {
        val size = listAlumnos.size
        listAlumnos.clear()
        notifyItemRangeRemoved(0, size)
    }

    private fun mostrarDetallesAlumno(alumno: Alumno) {
        // Crear un AlertDialog para mostrar los detalles del alumno
        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("Detalles del Alumno")
        builder.setMessage("""
            Nombre: ${alumno.nombre}
            Cuenta: ${alumno.cuenta}
            Correo: ${alumno.correo}
        """.trimIndent())
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFoto: ImageView = itemView.findViewById(R.id.imgPersona)
        val nombres: TextView = itemView.findViewById(R.id.tvNombre)
        val numeroDoc: TextView = itemView.findViewById(R.id.tvCuenta)
    }
}