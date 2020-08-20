package cl.desafiolatam.desafiodos

import android.content.DialogInterface
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cl.desafiolatam.desafiodos.orm.DAO
import cl.desafiolatam.desafiodos.orm.Task
import cl.desafiolatam.desafiodos.orm.UserRoomDatabase
import cl.desafiolatam.desafiodos.task.OnItemClickListener
import cl.desafiolatam.desafiodos.task.TaskListAdapter
import cl.desafiolatam.desafiodos.task.TaskUIDataHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_task.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnItemClickListener {
    override fun onItemClick(taskItem: TaskUIDataHolder) {
        val dialogView = layoutInflater.inflate(R.layout.add_task, null)
        val taskText = dialogView.task_input
        taskText.setText(taskItem.text)
        val dialogBuilder = AlertDialog
            .Builder(this)
            .setTitle("Editar una Tarea")
            .setView(dialogView)
            .setNegativeButton("Cerrar") {
                    dialog: DialogInterface, _: Int -> dialog.dismiss()}
            .setPositiveButton("Editar") {
                    _: DialogInterface, _: Int ->
                //generar código para editar/actualizar la tarea
                updateEntity(taskItem, taskText.text.toString())

            }
        dialogBuilder.create().show()
    }

    private lateinit var list: RecyclerView
    private lateinit var adapter: TaskListAdapter
    // crear las variables para utilizar la base de datos
    private lateinit var dao: DAO
    private lateinit var listadesderoom: MutableList<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        setUpViews()
        dao = UserRoomDatabase.getDatabase(application).idDao()
        iniciarLista()
        //inicializar lo necesario para usar la base de datos
    }

        fun iniciarLista(){
            CoroutineScope(Dispatchers.Main).launch{
                listadesderoom = dao.getAll() as MutableList<Task>
                when (listadesderoom.isEmpty()) {
                    true -> {
                        Toast.makeText(applicationContext, "No hay datos", Toast.LENGTH_LONG)
                        Log.d("AAA", "No hay datos")
                    }
                    false -> {
                        Log.d("AAA", "Lista desde room $listadesderoom")
                    }
                }
                adapter.updateData(traductor(listadesderoom))

            }
        }
    //esta funcion transforma desde lista tTask (base dae datos) hasta lista (UIDAtaHooder)
    fun traductor(list: MutableList<Task>): MutableList<TaskUIDataHolder>{
        var listareturn = mutableListOf<TaskUIDataHolder>()
        for (elemento in list){
            listareturn.add(TaskUIDataHolder(elemento.uid!!.toLong(), elemento.text))
        }
        return listareturn
    }
    override fun onResume() {
        super.onResume()
        AsyncTask.execute {
          iniciarLista()
            }
        }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return  true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when {
            item.itemId == R.id.add -> addTask()
            item.itemId == R.id.remove_all -> removeAll()
        }
        return true
    }

    private fun setUpViews() {
        list = task_list
        list.layoutManager = LinearLayoutManager(this)
        adapter = TaskListAdapter( mutableListOf(), this, this)
        list.adapter = adapter
    }

    private fun updateEntity(taskItem: TaskUIDataHolder, newText: String) {
        //completar método para actualizar una tarea en la base de datos
        dao.update(newText,taskItem.id)
        iniciarLista()

    }

    private fun addTask() {
        val dialogView = layoutInflater.inflate(R.layout.add_task, null)
        val taskText = dialogView.task_input
        val dialogBuilder = AlertDialog
            .Builder(this)
            .setTitle("Agrega una Tarea")
            .setView(dialogView)
            .setNegativeButton("Cerrar") {
                    dialog: DialogInterface, _: Int -> dialog.dismiss()}
            .setPositiveButton("Agregar") {
                    dialog: DialogInterface, _: Int ->
                if (taskText.text?.isNotEmpty()!!) {
                    //Completar para agregar una tarea a la base de datos
                    AsyncTask.execute{
                        dao.insert(Task(taskText.text.toString()))
                        iniciarLista()
                    }
                }
            }
        dialogBuilder.create().show()
    }

    private fun removeAll() {
        val dialog = AlertDialog
            .Builder(this)
            .setTitle("Borrar Todo")
            .setMessage("¿Desea Borrar todas las tareas?")
            .setNegativeButton("Cerrar") {
                    dialog: DialogInterface, _: Int -> dialog.dismiss()}
            .setPositiveButton("Aceptar") { dialog: DialogInterface, _: Int ->
                //Código para eliminar las tareas de la base de datos
                AsyncTask.execute{
                    dao.delete()
                    iniciarLista()
                }
            }
        dialog.show()
    }
    private fun createEntity(text:String) {
        //completar este método para retornar un Entity
    }

    private fun createEntityListFromDatabase(/* párametro de entrada*/): MutableList<TaskUIDataHolder> {
        val dataList = mutableListOf<TaskUIDataHolder>()
        //completar método para crear una lista de datos compatibles con el adaptador, mire lo que
        //retorna el método. Este método debe recibir un parámetro también.
        return dataList
    }
}
