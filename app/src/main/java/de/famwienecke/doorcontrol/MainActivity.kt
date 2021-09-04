package de.famwienecke.doorcontrol

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import de.xembalo.doorcontrol.Constants
import de.xembalo.doorcontrol.R
import de.xembalo.doorcontrol.SettingsActivity
import de.xembalo.doorcontrol.WSUtils

import kotlinx.coroutines.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.channels.ticker

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        up.setOnClickListener { view ->
            GlobalScope.launch {
                val resp = WSUtils.callWS(
                    Constants.GARAGE,
                    Constants.UP,
                    this@MainActivity,
                    Constants.PATH_MOVE
                )
                if (!(resp.isNullOrEmpty())) {
                    Snackbar.make(view, resp, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                } else {
                    //animate UP

                    val tickerChannel = ticker(delayMillis = 1_000, initialDelayMillis = 0)
                    var curNum: Int = 5
                    for (event in tickerChannel) {
                        if (curNum <= 0) {
                            imageView_Garage.setImageResource(R.drawable.ic_garage_opened)
                            break
                        } else {
                            var resId = view.context.resources.getIdentifier(
                                "ic_garage_" + curNum.toString(),
                                "drawable",
                                getPackageName()
                            )
                            imageView_Garage.setImageResource(resId)
                            curNum--
                        }
                    }
                    tickerChannel.cancel()
                }
            }
        }

        down.setOnClickListener { view ->
            GlobalScope.launch {
                val resp = WSUtils.callWS(
                    Constants.GARAGE,
                    Constants.DOWN,
                    this@MainActivity,
                    Constants.PATH_MOVE
                )
                if (!(resp.isNullOrEmpty())) {
                    Snackbar.make(view, resp, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                } else {
                    //animate DOWN

                    val tickerChannel = ticker(delayMillis = 1_000, initialDelayMillis = 0)
                    var curNum: Int = 1
                    for (event in tickerChannel) {
                        if (curNum >= 6) {
                            imageView_Garage.setImageResource(R.drawable.ic_garage_closed)
                            break
                        } else {
                            var resId = view.context.resources.getIdentifier(
                                "ic_garage_" + curNum.toString(),
                                "drawable",
                                getPackageName()
                            )
                            imageView_Garage.setImageResource(resId)
                            curNum++
                        }
                    }
                    tickerChannel.cancel()
                }
            }
        }

        impulse.setOnClickListener { view ->
            GlobalScope.launch {
                val resp = WSUtils.callWS(
                    Constants.GARAGE,
                    Constants.IMPULSE,
                    this@MainActivity,
                    Constants.PATH_MOVE
                )
                if (!(resp.isNullOrEmpty()))
                    Snackbar.make(view, resp, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        }

        climate.setOnClickListener { view ->
            imageView_Garage.setImageResource(R.drawable.ic_garage_climate)


            GlobalScope.launch {
                val resp = WSUtils.callWS(
                    Constants.GARAGE,
                    Constants.CLIMATE,
                    this@MainActivity,
                    Constants.PATH_MOVE
                )
                if (!(resp.isNullOrEmpty()))
                    Snackbar.make(view, resp, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {
                val intent : Intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }
}

