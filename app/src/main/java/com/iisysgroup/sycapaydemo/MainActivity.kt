package com.iisysgroup.sycapaydemo

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.iisysgroup.sycapaylibrary.tams.CustomDataMessage
import com.iisysgroup.sycapaylibrary.tams.host.entities.TransactionResult
import com.iisysgroup.sycapaylibrary.tams.utils.InputData
import com.iisysgroup.tams.TAMS.TamService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var inputData = InputData(5000, 0)
        var tamService = TamService(this)

        coonect.setOnClickListener {
            tamService.connectBluetoothh()
        }

        prep.setOnClickListener {
            //Supply your details
            tamService.configureTerminal("22330745", "196.216.144.142", 9091)
            //tamService.configureTerminal("2014A003", "41.203.114.250", 9080)
        }

        txn.setOnClickListener {
            tamService.perFormEftt(inputData, 0)
            Log.e("okkkkk", inputData.amount.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 2){
            var message = data!!.getStringExtra("message")
            var result = Gson().fromJson(message, CustomDataMessage::class.java)
            if (result.status.equals(true) && result.message.equals("Success")){
                Log.e("okPin", result.data.toString())
                //Toast.makeText(this, result.data.toString(), Toast.LENGTH_SHORT).show()
            }else{
                Log.e("okERRRR", Gson().toJson(result))
                //Prepping Failed
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            val message = data!!.getStringExtra("message")
            val result =
                    Gson().fromJson(message, CustomDataMessage::class.java)
            Log.d("okkkResult", Gson().toJson(result))
            if (result.status === true) {
                var transactionResult =
                        Gson().fromJson(Gson().toJson(result.data), TransactionResult::class.java)
                Log.d("okkkTraResult", Gson().toJson(transactionResult))
                Toast.makeText(this, transactionResult.cardHolderName, Toast.LENGTH_SHORT).show()
            }else{
                //Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}