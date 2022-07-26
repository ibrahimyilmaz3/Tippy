package com.example.tippy

import android.animation.ArgbEvaluator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val seekBarTip = findViewById<SeekBar>(R.id.seekBarTip)
        val tvTipPercent = findViewById<TextView>(R.id.tvTipPercent)
        val etBase: EditText = findViewById(R.id.etBase)
        val tvTipAmount: TextView = findViewById(R.id.tvTipAmount)
        val tvTotalAmount: TextView = findViewById(R.id.tvTotalAmount)
        val tvTipDescription = findViewById<TextView>(R.id.tvTipDescription)

        fun updateTipDescription(tipPercent: Int) {
            val tipDescription: String

            when (tipPercent) {
                in 0..9 -> tipDescription = "Poor"
                in 10..15 -> tipDescription = "Acceptable"
                in 15..19 -> tipDescription = "Good"
                in 20..24 -> tipDescription = "Great"
                else -> tipDescription = "Amazing"
            }
            tvTipDescription.text = tipDescription
            val color = ArgbEvaluator().evaluate(tipPercent.toFloat() / seekBarTip.max,
                ContextCompat.getColor(this, R.color.colorWorstTip),
                ContextCompat.getColor(this,R.color.colorBestTip)) as Int
            tvTipDescription.setTextColor(color)
        }

        updateTipDescription(INITIAL_TIP_PERCENT)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercent.text = "$INITIAL_TIP_PERCENT%"

        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG, "onProgressChanged $p1")
                tvTipPercent.text = "$p1%"
                updateTipDescription(p1)
                fun computeTipAndTotal() {
                    if (etBase.text.isEmpty()) {
                        tvTipAmount.text = ""
                        tvTotalAmount.text = ""
                        return
                    }
                    val baseAmount = etBase.text.toString().toDouble()
                    val tipPercent = seekBarTip.progress
                    val tipAmount = baseAmount * tipPercent / 100
                    val totalAmount = baseAmount + tipAmount
                    tvTipAmount.text = "%.2f".format(tipAmount)
                    tvTotalAmount.text = "%.2f".format(totalAmount)
                }
                computeTipAndTotal()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })


        etBase.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "afterTextChanged $p0")
                computeTipAndTotal()
            }

            private fun computeTipAndTotal() {
                if (etBase.text.isEmpty()) {
                    tvTipAmount.text = ""
                    tvTotalAmount.text = ""
                    return
                }
                val baseAmount = etBase.text.toString().toDouble()
                val tipPercent = seekBarTip.progress
                val tipAmount = baseAmount * tipPercent / 100
                val totalAmount = baseAmount + tipAmount
                tvTipAmount.text = "%.2f".format(tipAmount)
                tvTotalAmount.text = "%.2f".format(totalAmount)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription: String
        val tvTipDescription = findViewById<TextView>(R.id.tvTipDescription)
        when (tipPercent) {
            in 0..9 -> tipDescription = "Poor"
            in 10..15 -> tipDescription = "Acceptable"
            in 15..19 -> tipDescription = "Good"
            in 20..24 -> tipDescription = "Great"
            else -> tipDescription = "Amazing"
        }
        tvTipDescription.text = tipDescription
    }
}