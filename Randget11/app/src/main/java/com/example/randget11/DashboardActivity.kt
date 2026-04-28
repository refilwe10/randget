package com.example.randget11


import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        db = DatabaseProvider.getDatabase(this)

        val monthInput = findViewById<EditText>(R.id.etMonth)
        val btnLoad = findViewById<Button>(R.id.btnLoad)

        val tvTotal = findViewById<TextView>(R.id.tvTotalSpent)
        val tvStatus = findViewById<TextView>(R.id.tvBudgetStatus)
        val tvRemaining = findViewById<TextView>(R.id.tvRemaining)

        btnLoad.setOnClickListener {

            val month = monthInput.text.toString()

            if (month.isBlank()) {
                Toast.makeText(this, "Enter month", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {

                val totalSpent = db.expenseDao().getTotalForMonth(month) ?: 0.0
                val budget = db.budgetDao().getBudgetForMonth(month)

                withContext(Dispatchers.Main) {

                    tvTotal.text = "Total Spent: R %.2f".format(totalSpent)

                    if (budget == null) {
                        tvStatus.text = "No budget set for this month"
                        tvRemaining.text = ""
                        return@withContext
                    }

                    val remaining = budget.maxAmount - totalSpent

                    tvRemaining.text = "Remaining: R %.2f".format(remaining)

                    when {
                        totalSpent > budget.maxAmount -> {
                            tvStatus.text = "⚠ OVER BUDGET"
                            tvStatus.setTextColor(Color.RED)
                        }

                        totalSpent >= budget.maxAmount * 0.8 -> {
                            tvStatus.text = "⚠ Near Limit"
                            tvStatus.setTextColor(Color.YELLOW)
                        }

                        else -> {
                            tvStatus.text = "✓ Within Budget"
                            tvStatus.setTextColor(Color.GREEN)
                        }
                    }
                }
            }
        }
    }
}