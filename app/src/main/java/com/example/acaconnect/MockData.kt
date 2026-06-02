package com.example.acaconnect

val departments = listOf(
    Department("Computer Engineering", "CE"),
    Department("Electrical and Electronic Engineering", "EE"),
    Department("Mechanical Engineering", "ME"),
    Department("Civil Engineering", "CV")
)

val batches = listOf(
    Batch(6, "2020/21"),
    Batch(7, "2021/22"),
    Batch(8, "2022/23"),
    Batch(9, "2023/24"),
    Batch(10, "2024/25")
)

val semesters = listOf("Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6", "Semester 7", "Semester 8")

val APP_LOGO = R.drawable.logo

val sampleMentors = listOf(
    Mentor(
        id = "1", 
        name = "Yashmika Senadheera",
        department = "EE",
        batch = 6, 
        bio = "Can help with anything",
        email = "yashmika1100@gmail.com",
        whatsapp = "94770452829",
        profileImage = R.drawable.yashmika,
        rating = 4.9f
    ),
    Mentor(
        id = "2", 
        name = "Nipuna jayarathna",
        department = "EE", 
        batch = 6,
        bio = "Passionate and Contact for help.",
        email = "nipunajayarathna2000@gmail.com",
        whatsapp = "94717116277",
        profileImage = R.drawable.nipuna,
        rating = 4.7f
    ),
    Mentor(
        id = "3", 
        name = "Sithara Hennayaka",
        department = "EE",
        batch = 6, 
        bio = "Electrical expert",
        email = "sitharush123@gmail.com",
        whatsapp = "94711733590",
        profileImage = R.drawable.sithara,
        rating = 4.5f
    )
)

val sampleTasks = listOf(
    Task("1", "EE4354 Assignment", false, 1778025600000L, // May 6, 2026
        listOf(SubTask("App Development", true), SubTask("Report Writing", false))),
    Task("2", "CE Lecture 5 Review", true, 1777507200000L, // April 30, 2026
        listOf(SubTask("Read Slides", true))),
    Task("3", "Mobile Application Development Assignment", false, 1780684740000L, // June 5, 2026, 11:59 PM
        listOf(SubTask("Complete UI", true), SubTask("Connect Firebase", true), SubTask("Final Submission", false)))
)

val sampleResources = mapOf(
    "EE" to mapOf(
        "Semester 3" to listOf(AcademicResource("EE Resource Folder", "https://drive.google.com/drive/folders/19DRoyQa0LQzp3jMbrM009ICc75y5mL6p", "Drive")),
        "Semester 5" to listOf(AcademicResource("EE Resource Folder", "https://drive.google.com/drive/folders/1puUR52RS8eVw-sHtpsbVy7Q5L44QU69o", "Drive")),
        "Semester 6" to listOf(AcademicResource("EE Resource Folder", "https://drive.google.com/drive/folders/1-I8BqYGigiSIPEFkiGsqEzynU7Lrm870", "Drive")),
        "Semester 7" to listOf(AcademicResource("EE Resource Folder", "https://drive.google.com/drive/folders/16zpF1xz1mssc5QoOFQ80TY5KF3r8Ird9", "Drive")),
        "Semester 8" to listOf(AcademicResource("EE Resource Folder", "https://drive.google.com/drive/folders/1cKfDUCuxX_4frBRgD8hOLIX53v0-jJYA", "Drive"))
    ),
    "CE" to mapOf(
        "Semester 5" to listOf(
            AcademicResource("EE4354: Jetpack Compose Basics", "https://drive.google.com/sample", "Slide"),
            AcademicResource("EE4354: Assignment Rubric", "https://drive.google.com/sample", "Notes")
        )
    )
)
