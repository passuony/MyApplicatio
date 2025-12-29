package com.example.myapplication.data

import androidx.annotation.DrawableRes // للاستخدام مع معرّف الموارد
import com.example.myapplication.R // لتضمين الموارد


data class BookListItem(
    val id: Int,
    val title: String,
    val author: String,
    val year: Int,
    @DrawableRes val imageResId: Int,
)

data class BookDetail(
    val id: Int,
    val title: String,
    val author: String,
    val year: Int,
    val description: String,
    val genre: String,
    val pages: Int,
    @DrawableRes val imageResId: Int
)


object MockDataSource {
    val booksList = listOf(
        BookListItem(
            id = 1,
            title = "Rich Dad Poor Dad",
            author = "Robert Kiyosaki",
            year = 1997,
            imageResId = R.drawable.rich_dad
        ),
        BookListItem(
            id = 2,
            title = "Atomic Habits",
            author = "James Clear",
            year = 2018,
            imageResId = R.drawable.atomic_habits
        ),
        BookListItem(
            id = 3,
            title = "Think and Grow Rich",
            author = "Napoleon Hill",
            year = 1937,
            imageResId = R.drawable.think_rich
        ),
        BookListItem(
            id = 4,
            title = "The Power of Habit",
            author = "Charles Duhigg",
            year = 2012,
            imageResId = R.drawable.power_habit
        )
    )

    fun getBookDetail(id: String): BookDetail? {
        val item = booksList.find { it.id.toString() == id }
            ?: return null

        val detailData = when (item.id) {
            1 -> Triple(
                "This book advocates the importance of financial literacy, financial independence, and building wealth through investing, real estate, and owning businesses. It challenges conventional views on money.",
                "Finance/Self-help",
                207
            )
            2 -> Triple(
                "A practical guide to breaking bad habits, forming good ones, and mastering the tiny behaviors that lead to remarkable results. It focuses on the system rather than the goals, introducing the 4 Laws of Behavior Change.",
                "Self-help/Productivity",
                320
            )
            3 -> Triple(
                "One of the first and most enduring books on personal success. It details a 'Thirteen Steps' philosophy that is said to be the key to achieving wealth and success by harnessing the power of the mind.",
                "Self-help/Motivation",
                238
            )
            4 -> Triple(
                "Explores the science behind why habits exist and how they can be changed. It introduces the 'Habit Loop' (Cue, Routine, Reward) and discusses its impact on individuals, companies, and society.",
                "Psychology/Self-help",
                371
            )
            else -> Triple("No detailed description available.", "Unknown", 0)
        }

        return BookDetail(
            id = item.id,
            title = item.title,
            author = item.author,
            year = item.year,
            description = detailData.first,
            genre = detailData.second,
            pages = detailData.third,
            imageResId = item.imageResId
        )
    }
}