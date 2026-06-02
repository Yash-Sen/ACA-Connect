package com.example.acaconnect

data class Department(
    val name: String,
    val code: String,
    val icon: Int? = null // For tile icons
)

data class Batch(
    val number: Int,
    val academicYear: String
)

data class Mentor(
    val id: String,
    val name: String,
    val department: String,
    val batch: Int,
    val bio: String,
    val email: String,
    val whatsapp: String,
    val profileImage: Any, // URL or R.drawable.id
    var rating: Float = 0f
)

data class AcademicResource(
    val title: String,
    val url: String,
    val type: String // e.g., "Slide", "Recording", "Notes"
)

data class SubTask(
    val title: String,
    var isCompleted: Boolean = false
)

data class Task(
    val id: String,
    val title: String,
    var isCompleted: Boolean = false,
    val dueDate: Long, // Use timestamp for calendar compatibility
    val subTasks: List<SubTask> = emptyList()
)

data class UserProfile(
    val uid: String = "",
    val email: String = "",
    val fullName: String = "",
    val username: String = "",
    val profileImageUrl: String = "",
    val mobileNumber: String = "",
    val batch: Int = 0,
    val department: String = ""
)
