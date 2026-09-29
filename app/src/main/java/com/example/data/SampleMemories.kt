package com.example.data

object SampleMemories {
    val items = listOf(
        MemoryEntity(
            id = "sample-1",
            title = "Modern CSS Grid & Subgrid Deep Dive",
            content = "https://ishadeed.com/article/learn-css-subgrid/",
            why = "Need this for next sprint’s dashboard redesign—subgrid may solve the card-alignment issue.",
            category = "Learn",
            status = "NEEDS ACTION",
            action = "START: REDESIGN DASHBOARD →",
            createdAt = System.currentTimeMillis() - (1000 * 60 * 60 * 3), // 3 hours ago ("3h ago")
            updatedAt = System.currentTimeMillis() - (1000 * 60 * 60 * 3)
        ),
        MemoryEntity(
            id = "sample-2",
            title = "Ergonomic Split Mechanical Keyboard",
            content = "product research",
            why = "Wrist strain during long coding sessions; compare with Moonlander before the Black Friday discount.",
            category = "Buy",
            status = "NEEDS REVIEW",
            action = "COMPARE OPTIONS →",
            createdAt = System.currentTimeMillis() - (1000 * 60 * 60 * 24), // Yesterday (24 hours ago)
            updatedAt = System.currentTimeMillis() - (1000 * 60 * 60 * 24)
        ),
        MemoryEntity(
            id = "sample-3",
            title = "Local First Software Architecture Principles",
            content = "https://www.inkandswitch.com/local-first/",
            why = "Inspiration for our hackathon project offline-first storage and sync strategy.",
            category = "Reference",
            status = "INSPIRATION",
            action = "EXPLORE ARCHITECTURE →",
            createdAt = System.currentTimeMillis() - (1000 * 60 * 60 * 48), // 2 days ago ("2d ago")
            updatedAt = System.currentTimeMillis() - (1000 * 60 * 60 * 48)
        )
    )
}
