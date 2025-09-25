package com.example.practica_academica01

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: AlumnoAdapter
    private lateinit var data: ArrayList<Alumno>
    private lateinit var fabAgregarAlumno: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Configurar ActionBar para mostrar el menú
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = "Lista de Alumnos"

        initializeViews()
        setupRecyclerView()
        setupFloatingActionButton()
    }

    private fun initializeViews() {
        recyclerview = findViewById(R.id.recyclerPersonas)
        fabAgregarAlumno = findViewById(R.id.faButton)
    }

    private fun setupRecyclerView() {
        // Crear layout manager vertical
        recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList de clase Alumno
        data = ArrayList()

        // Listas predefinidas de estudiantes
        val nombres = listOf(
            "Ana María Pérez",
            "Juan Carlos Silva",
            "Laura Martínez",
            "Diego Herrera",

        )

        val cuentas = listOf(
            "20207001",
            "20207002",
            "20207003",
            "20207004",

        )

        val correos = listOf(
            "ana.perez@unah.hn",
            "juan.silva@unah.hn",
            "laura.martinez@unah.hn",
            "diego.herrera@unah.hn",

        )

        // Diferentes imágenes de perfil (opcionales)
        val imagenes = listOf(
            "https://i.pinimg.com/236x/e0/b8/3e/e0b83e84afe193922892917ddea28109.jpg",
            "https://i.pinimg.com/236x/a3/7e/99/a37e9956b8b8d1d8c6b1a2d3e4f5a6b7.jpg",
            "https://i.pinimg.com/236x/b4/8f/a1/b48fa1e7c9d9e2e9d7c2b3e4f5a6b8c9.jpg",
            "https://i.pinimg.com/236x/c5/9a/b2/c59ab2f8dad0f3fae8d3c4e5f6a7b9ca.jpg",

        )

        // Agregar todos los estudiantes usando las listas
        for (i in nombres.indices) {
            data.add(Alumno(
                nombres[i],
                cuentas[i],
                correos[i],
                imagenes[i]
            ))
        }

        // Configurar adapter
        adapter = AlumnoAdapter(this, data)
        recyclerview.adapter = adapter
    }

    private fun setupFloatingActionButton() {
        fabAgregarAlumno.setOnClickListener {
            mostrarDialogoAgregarAlumno()
        }
    }

    private fun mostrarDialogoAgregarAlumno() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_agregar_alumno, null)

        val etNombre = dialogView.findViewById<EditText>(R.id.etNombre)
        val etCuenta = dialogView.findViewById<EditText>(R.id.etCuenta)
        val etCorreo = dialogView.findViewById<EditText>(R.id.etCorreo)
        val etImagen = dialogView.findViewById<EditText>(R.id.etImagen)

        builder.setView(dialogView)
            .setTitle("Agregar Nuevo Alumno")
            .setPositiveButton("Agregar") { dialog, _ ->
                val nombre = etNombre.text.toString().trim()
                val cuenta = etCuenta.text.toString().trim()
                val correo = etCorreo.text.toString().trim()
                val imagen = etImagen.text.toString().trim()

                if (validarCampos(nombre, cuenta, correo)) {
                    val imagenUrl = if (imagen.isEmpty()) {
                        "https://i.pinimg.com/236x/e0/b8/3e/e0b83e84afe193922892917ddea28109.jpg"
                    } else {
                        imagen
                    }

                    agregarNuevoAlumno(nombre, cuenta, correo, imagenUrl)
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Por favor complete todos los campos obligatorios",
                        Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

        builder.create().show()
    }

    private fun validarCampos(nombre: String, cuenta: String, correo: String): Boolean {
        return nombre.isNotEmpty() && cuenta.isNotEmpty() && correo.isNotEmpty()
    }

    private fun agregarNuevoAlumno(nombre: String, cuenta: String, correo: String, imagen: String) {
        val nuevoAlumno = Alumno(nombre, cuenta, correo, imagen)
        data.add(0, nuevoAlumno) // Agregar al inicio de la lista
        adapter.notifyItemInserted(0)
        recyclerview.scrollToPosition(0)

        Toast.makeText(this, "Alumno agregado exitosamente", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_student -> {
                mostrarDialogoAgregarAlumno()
                true
            }
            R.id.action_settings -> {
                Toast.makeText(this, "Configuración seleccionada", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_about -> {
                mostrarAcercaDe()
                true
            }
            R.id.action_clear_all -> {
                mostrarDialogoLimpiarLista()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun mostrarAcercaDe() {
        AlertDialog.Builder(this)
            .setTitle("Acerca de")
            .setMessage("Práctica Académica 01\nSistema de Gestión de Alumnos\n\nDesarrollado por: Tu Nombre\nVersión: 1.0")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun mostrarDialogoLimpiarLista() {
        AlertDialog.Builder(this)
            .setTitle("Limpiar Lista")
            .setMessage("¿Está seguro de que desea eliminar todos los alumnos de la lista?")
            .setPositiveButton("Sí") { dialog, _ ->
                data.clear()
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Lista limpiada", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}