package com.example.randget11

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kotlinx.coroutines.*

class CategoryReportActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_report)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "budget_db"
        ).build()

        val start = findViewById<EditText>(R.id.etStartDate)
        val end = findViewById<EditText>(R.id.etEndDate)
        val btn = findViewById<Button>(R.id.btnGenerate)
        val recycler = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerReport)

        recycler.layoutManager = LinearLayoutManager(this)

        btn.setOnClickListener {
            val s = start.text.toString()
            val e = end.text.toString()

            if (s.isEmpty() || e.isEmpty()) {
                Toast.makeText(this, "Enter dates", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                val results = db.expenseDao().getTotalSpentPerCategory(s, e)

                withContext(Dispatchers.Main) {
                    recycler.adapter = CategoryReportAdapter(results)
                }
            }
        }
    }
}