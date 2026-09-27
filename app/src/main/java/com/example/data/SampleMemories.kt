package com.example.data

object SampleMemories {
    val items = listOf(
        MemoryEntity(
            id = "sample-1",
            title = "Modern CSS Grid & Subgrid Deep Dive",
            content = "https://ishadeed.com/article/learn-css-subgrid/",
            why = "Need this for redesigning the dashboard layout next sprint; subgrid solves the nested card alignment issue.",
            category = "Learn",
            createdAt = System.currentTimeMillis() - 1000 * 60 * 60 * 3, // 3 hours ago
            updatedAt = System.currentTimeMillis() - 1000 * 60 * 60 * 3
        ),
        MemoryEntity(
            id = "sample-2",
            title = "Ergonomic Split Mechanical Keyboard",
            content = "https://ergodox-ez.com",
            why = "Wrist strain during long coding sessions; compare with Moonlander before the Black Friday discount.",
            category = "Buy",
            createdAt = System.currentTimeMillis() - 1000 * 60 * 60 * 24, // 1 day ago
            updatedAt = System.currentTimeMillis() - 1000 * 60 * 60 * 24
        ),
        MemoryEntity(
            id = "sample-3",
            title = "Local First Software Architecture Principles",
            content = "https://www.inkandswitch.com/local-first/",
            why = "Inspiration for our hackathon project offline-first storage and sync strategy.",
            category = "Reference",
            createdAt = System.currentTimeMillis() - 1000 * 60 * 60 * 48, // 2 days ago
            updatedAt = System.currentTimeMillis() - 1000 * 60 * 60 * 48
        )
    )
}
