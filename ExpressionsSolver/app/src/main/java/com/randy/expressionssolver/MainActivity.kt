package com.randy.expressionssolver

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.widget.*
import android.os.Handler
import android.widget.Toast
import android.content.res.Resources
import android.view.*

class MainActivity : AppCompatActivity() {

    // declare all widgets
    private lateinit var btnNewGame: Button
    private lateinit var btnAbout: Button
    private lateinit var btnHistory: Button
    private lateinit var btnInstruction: Button
    private lateinit var popupWindow:PopupWindow
    private lateinit var view:View
    private lateinit var alertDialog:AlertDialog

    // initialize class variables
    private var isOpened:Boolean = false
    private var isAlert:Boolean = false
    private var width:Int = 0
    private var height:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        width = getScreenWidth()
        height = getScreenHeight()

        // calling method to initialize android widgets
        initViews()
        // configure popup settings
        setPopUpWindow()

        // onclick btn new game
        btnNewGame.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java) //intent to GameActivity
            startActivity(intent)
        }

        // onclick btn instructions
        btnInstruction.setOnClickListener {
            showGameAlert()
            isAlert = true
        }

        // onclick btn about
        btnAbout.setOnClickListener {
            showPopUp() // show popup on btn click
            isOpened = true
        }

        // onclick btn History
        btnHistory.setOnClickListener {
            val intent =
                Intent(this, ShowHistoryActivity::class.java) //intent to ShowHistoryActivity
            startActivity(intent)
        }
    }

    // method to initialize all widgets
    private fun initViews(){
        btnNewGame = findViewById(R.id.btnNewGame)
        btnAbout = findViewById(R.id.btnAbout)
        btnHistory = findViewById(R.id.btnHistory)
        btnInstruction = findViewById(R.id.btnInstruction)
    }

    // method to setup popup window (About btn)
    private fun setPopUpWindow(){
        // Initialize new layout inflater
        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        // Inflate view
        view = inflater.inflate(R.layout.popup_window,null)

        // Initialize popup window
          popupWindow = PopupWindow(
            view, // Custom view to show in popup window
            RelativeLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
            RelativeLayout.LayoutParams.WRAP_CONTENT // Window height
        )

        // set an elevation for the popup window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.elevation = 10.0F
        }

        //API level should be above 22
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Create slide in animation
            val slideIn = Slide()
            slideIn.slideEdge = Gravity.BOTTOM
            popupWindow.enterTransition = slideIn

            // set slide out animation
            val slideOut = Slide()
            slideOut.slideEdge = Gravity.END
            popupWindow.exitTransition = slideOut
        }

        // Get the widgets reference from custom view
        val popUpTxt = view.findViewById<TextView>(R.id.txtPopUp)
        val btnPopUp = view.findViewById<Button>(R.id.btnPopUp)

        // Set click listener for popup window's text view
        popUpTxt?.setOnClickListener{
            // Change the text color of popup window's text view
            popUpTxt.setTextColor(Color.RED)
        }

        // Set a click listener for popup's button widget
        btnPopUp?.setOnClickListener{
            // Dismiss the popup window
            popupWindow.dismiss()
            isOpened = false
        }

        // Set a dismiss listener for popup window
        popupWindow.setOnDismissListener {
            Toast.makeText(applicationContext,"Popup closed",Toast.LENGTH_SHORT).show()
        }

//        popupWindow?.showAtLocation(
//            view, // Location to display popup window
//            Gravity.CENTER, // Exact position of layout to display popup
//            0, // X offset
//            0 // Y offset
//        )
    }


    // method to show popup
    private fun showPopUp(){
                popupWindow.showAtLocation(
            view, // Location to display popup window
            Gravity.CENTER, // Exact position of layout to display popup
            0, // X offset
            100 // Y offset
        )
    }

    // override method to bundle current state of data
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("isOpened", isOpened)
        outState.putBoolean("isAlert", isAlert)
        super.onSaveInstanceState(outState)
    }

    // override back button functionality
    override fun onBackPressed() {
        if(isOpened) {
            popupWindow.dismiss()
            isOpened=false
        }
        else {
            super.onBackPressed()
        }
    }

    // override method to handle data
    // on orientation change
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        isOpened = savedInstanceState.getBoolean("isOpened")
        isAlert = savedInstanceState.getBoolean("isAlert")
        handleOrientation()
        super.onRestoreInstanceState(savedInstanceState)
    }

    //method to handle showing popup window on orientation
    private fun handleOrientation(){
        val orientation = this.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (isOpened){
                Handler().post { btnAbout.performClick() }
            }
            if (isAlert){
                Handler().post { btnInstruction.performClick() }
            }
        } else {
            if (isOpened){
                Handler().post { btnAbout.performClick() }
            }
            if (isAlert){
                Handler().post { btnInstruction.performClick() }
            }
        }
    }

    // method to show game instruction alert
    private fun showGameAlert(){
         alertDialog = AlertDialog.Builder(this, R.style.AlertDialogStyle) //set icon
            .setIcon(R.drawable.alert_ic) //set title
            .setTitle("GAME INSTRUCTIONS") //set message
            .setCancelable(false)
            .setMessage(R.string.alert_txt)
            // positive btn functionality
            .setPositiveButton(
                "START"
            ) { _, _ ->
                val intent = Intent(this, GameActivity::class.java) //intent to GameActivity
                startActivity(intent)
                isAlert = false
            } //set negative button
            .setNegativeButton(
                "BACK"
            ) { _, _ ->
                Toast.makeText(applicationContext, "Cancelled!", Toast.LENGTH_SHORT).show()
                isAlert = false
            }
             .show()

        // set layout attributes for alert box
        // for landscape mode
//        width = getScreenWidth()
//        height = getScreenHeight()
        val orientation = this.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            val layoutSettings = WindowManager.LayoutParams()
            layoutSettings.copyFrom(alertDialog.window!!.attributes)
            // get the correct width & height with percentage
            layoutSettings.width = height
            layoutSettings.height = width
            layoutSettings.x = 0
            layoutSettings.y = 60
            alertDialog.window!!.attributes = layoutSettings
        }
    }

    // get appropriate width for alert dialog
    // based on device width
    private fun getScreenWidth(): Int {
        val orientation = this.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            return (Resources.getSystem().displayMetrics.widthPixels * 0.41).toInt()
        }
        return (Resources.getSystem().displayMetrics.widthPixels * 1.49).toInt()
    }

    // get appropriate height for alert dialog
    // based on device height
    private fun getScreenHeight(): Int {
        val orientation = this.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            return (Resources.getSystem().displayMetrics.heightPixels * 1.49).toInt()
        }
        return (Resources.getSystem().displayMetrics.heightPixels * 0.41).toInt()
    }
}