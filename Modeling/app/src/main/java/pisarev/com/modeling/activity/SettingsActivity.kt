package pisarev.com.modeling.activity

import android.support.v7.app.AppCompatActivity
import android.content.SharedPreferences.Editor
import android.os.Bundle
import pisarev.com.modeling.R
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.RadioButton

class SettingsActivity : AppCompatActivity(), View.OnClickListener {
    private var myEdit: Editor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        myEdit = myPreferences.edit()
        val radioButtonHorizontal = findViewById<RadioButton>(R.id.radioButtonHorizontal)
        val radioButtonVertical = findViewById<RadioButton>(R.id.radioButtonVertical)
        radioButtonHorizontal.setOnClickListener(this)
        radioButtonVertical.setOnClickListener(this)
        if (myPreferences.getBoolean("RADIOBUTTON", false)) {
            radioButtonHorizontal.isChecked = true
        } else {
            radioButtonVertical.isChecked = true
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.radioButtonHorizontal -> {
                myEdit!!.putBoolean("RADIOBUTTON", true)
                myEdit!!.commit()
            }
            R.id.radioButtonVertical -> {
                myEdit!!.putBoolean("RADIOBUTTON", false)
                myEdit!!.commit()
            }
        }
    }
}