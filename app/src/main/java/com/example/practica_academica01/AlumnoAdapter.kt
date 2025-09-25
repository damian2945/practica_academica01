package com.example.practica_academica01

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.PopupMenu
import android.widget.Toast
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

        // Configurar el menú de cada alumno
        holder.btnMenu.setOnClickListener {
            mostrarMenuAlumno(it, alumno, position)
        }

        // Click en el item completo para ver detalles
        holder.itemView.setOnClickListener {
            mostrarDetallesAlumno(alumno)
        }
    }

    override fun getItemCount(): Int {
        return listAlumnos.size
    }

    private fun mostrarMenuAlumno(view: View, alumno: Alumno, position: Int) {
        val popup = PopupMenu(context, view)
        popup.menuInflater.inflate(R.menu.menu_alumno, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_ver_detalles -> {
                    mostrarDetallesAlumno(alumno)
                    true
                }
                R.id.action_editar -> {
                    editarAlumno(alumno, position)
                    true
                }
                R.id.action_eliminar -> {
                    eliminarAlumno(alumno, position)
                    true
                }
                R.id.action_compartir -> {
                    compartirAlumno(alumno)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun mostrarDetallesAlumno(alumno: Alumno) {
        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("Detalles del Alumno")
        builder.setMessage("""
            📝 Nombre: ${alumno.nombre}
            🎓 Cuenta: ${alumno.cuenta}
            📧 Correo: ${alumno.correo}
        """.trimIndent())
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun editarAlumno(alumno: Alumno, position: Int) {
        Toast.makeText(context, "Editar: ${alumno.nombre}", Toast.LENGTH_SHORT).show()
        // Aquí puedes implementar la funcionalidad de edición
    }

    private fun eliminarAlumno(alumno: Alumno, position: Int) {
        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("Eliminar Alumno")
        builder.setMessage("¿Estás seguro de que deseas eliminar a ${alumno.nombre}?")
        builder.setPositiveButton("Eliminar") { dialog, _ ->
            listAlumnos.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, listAlumnos.size)
            Toast.makeText(context, "${alumno.nombre} eliminado", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun compartirAlumno(alumno: Alumno) {
        Toast.makeText(context, "Compartir: ${alumno.nombre}", Toast.LENGTH_SHORT).show()
        // Aquí puedes implementar la funcionalidad de compartir
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFoto: ImageView = itemView.findViewById(R.id.imgPersona)
        val nombres: TextView = itemView.findViewById(R.id.tvNombre)
        val numeroDoc: TextView = itemView.findViewById(R.id.tvCuenta)
        val btnMenu: ImageButton = itemView.findViewById(R.id.btnMenuAlumno)
    }
}