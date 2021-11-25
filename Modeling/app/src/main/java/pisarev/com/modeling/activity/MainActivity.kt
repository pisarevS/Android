package pisarev.com.modeling.activity

import android.support.v7.app.AppCompatActivity
import pisarev.com.modeling.interfaces.MainMvp.ViewMvp
import pisarev.com.modeling.interfaces.MainMvp.PresenterMainMvp
import android.widget.EditText
import pisarev.com.modeling.mvp.model.SQLiteData
import android.annotation.SuppressLint
import android.os.Bundle
import pisarev.com.modeling.R
import pisarev.com.modeling.mvp.presenter.PresenterMainImpl
import android.support.design.widget.FloatingActionButton
import android.text.InputType
import com.obsez.android.lib.filechooser.ChooserDialog
import android.widget.Toast
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Environment
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import java.io.File
import java.util.HashMap

class MainActivity : AppCompatActivity(), View.OnClickListener, ViewMvp {
    private var presenter: PresenterMainMvp? = null
    private var editText: EditText? = null
    private var sqLiteData: SQLiteData? = null
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = PresenterMainImpl(this)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener(this)
        editText = findViewById(R.id.editText)
        editText!!.setBackgroundColor(Color.rgb(64, 64, 64))
        editText!!.setRawInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
        editText!!.requestFocus()
        sqLiteData = SQLiteData(this, SQLiteData.DATABASE_PROGRAM)
        editText!!.setText(sqLiteData!!.programText!![SQLiteData.KEY_PROGRAM]!!)

        //StyleText.setStyle(editText);
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val sdcard = Environment.getExternalStorageDirectory()
        when (id) {
            R.id.action_openProgram -> {
                ChooserDialog(this@MainActivity)
                    .withStartFile(sdcard.path)
                    .withChosenListener { path: String, pathFile: File? ->
                        Toast.makeText(this@MainActivity, "FILE: $path", Toast.LENGTH_SHORT).show()
                        presenter!!.openProgram(path)
                        val stringMap: MutableMap<String?, String?> = HashMap()
                        stringMap[SQLiteData.KEY_PROGRAM] = path
                        SQLiteData(application, SQLiteData.DATABASE_PATH).programText=stringMap
                    } // to handle the back key pressed or clicked outside the dialog:
                    .withOnCancelListener { dialog: DialogInterface ->
                        Log.d("CANCEL", "CANCEL")
                        dialog.cancel() // MUST have
                    }
                    .build()
                    .show()
                return true
            }
            R.id.action_exit -> {
                SQLiteData(this, SQLiteData.DATABASE_PROGRAM).deleteProgramText()
                SQLiteData(this, SQLiteData.DATABASE_PARAMETER).deleteProgramText()
                SQLiteData(this, SQLiteData.DATABASE_PATH).deleteProgramText()
                System.exit(0)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showProgram(program: String?) {
        editText!!.setText(program)
        //StyleText.setStyle(editText);
    }

    override fun onClick(v: View) {
        if (editText!!.text.toString() != "") {
            val intent = Intent(this@MainActivity, DrawActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "the field is empty", Toast.LENGTH_LONG).show()
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        val stringMap: MutableMap<String?, String?> = HashMap()
        stringMap[SQLiteData.KEY_PROGRAM] = editText!!.text.toString()
        sqLiteData!!.deleteProgramText()
        sqLiteData!!.programText=stringMap
    }

    public override fun onPause() {
        super.onPause()
        val stringMap: MutableMap<String?, String?> = HashMap()
        stringMap[SQLiteData.KEY_PROGRAM] = editText!!.text.toString()
        sqLiteData!!.deleteProgramText()
        sqLiteData!!.programText=stringMap
    }

    override fun onResume() {
        super.onResume()
        //StyleText.setStyle(editText);
        editText!!.requestFocus()
        //editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
    }
}