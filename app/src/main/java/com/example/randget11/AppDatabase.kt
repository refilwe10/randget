package com.example.randget11
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Category::class, Expense::class, Budget::class],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun budgetDao(): BudgetDao
}
