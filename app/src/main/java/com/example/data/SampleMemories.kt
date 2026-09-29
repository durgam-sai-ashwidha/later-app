package com.example.data

object SampleMemories {
    val items = listOf(
        MemoryEntity(
            id = "sample-1",
            title = "Modern CSS Grid & Subgrid Deep Dive",
            content = "https://ishadeed.com/article/learn-css-subgrid/",
            why = "Need this for next sprint’s dashboard redesign—subgrid may solve the card-alignment issue.",
            category = "Learn",
            status = "READY FOR ACTION",
            action = "START FOLLOW-THROUGH →",
            relevanceLabel = "CONTEXT MATCH",
            reminderContext = "Your dashboard sprint starts today",
            trigger = "Your dashboard sprint starts today",
            tinyAction = "20-MIN LAYOUT RESEARCH",
            projectTag = "#dashboard",
            state = "Needs action",
            estimatedMinutes = 20,
            isCompleted = false,
            createdAt = System.currentTimeMillis() - (1000 * 60 * 60 * 3), // 3 hours ago ("3h ago")
            updatedAt = System.currentTimeMillis() - (1000 * 60 * 60 * 3)
        ),
        MemoryEntity(
            id = "sample-2",
            title = "Ergonomic Split Mechanical Keyboard",
            content = "product research · zsa.io",
            why = "Wrist strain during long coding sessions; compare with Moonlander before the Black Friday discount.",
            category = "Buy",
            status = "NEEDS REVIEW",
            action = "COMPARE OPTIONS →",
            relevanceLabel = "SALE WINDOW APPROACHING",
            reminderContext = "Before Black Friday sale window ends",
            trigger = "Before Black Friday sale window ends",
            tinyAction = "Compare with Moonlander",
            projectTag = "#hardware",
            state = "Needs action",
            estimatedMinutes = 15,
            isCompleted = false,
            createdAt = System.currentTimeMillis() - (1000 * 60 * 60 * 24), // Yesterday
            updatedAt = System.currentTimeMillis() - (1000 * 60 * 60 * 24)
        ),
        MemoryEntity(
            id = "sample-3",
            title = "15-Minute Morning Spine & Hip Mobility Routine",
            content = "mobility/desk-stretch-protocol",
            why = "Stiffness from sitting during deep work sprints; do this before coffee on coding mornings.",
            category = "Try",
            status = "COMPLETED",
            action = "VIEW MOBILITY PROTOCOL →",
            relevanceLabel = "COMPLETED THIS MORNING",
            reminderContext = "During a morning routine",
            trigger = "During a morning routine",
            tinyAction = "Follow 15-min mobility flow",
            projectTag = "#health",
            state = "Completed",
            estimatedMinutes = 15,
            isCompleted = true, // One completion preloaded for demo mode
            createdAt = System.currentTimeMillis() - (1000L * 60 * 60 * 18),
            updatedAt = System.currentTimeMillis() - (1000L * 60 * 60 * 2)
        ),
        MemoryEntity(
            id = "sample-4",
            title = "Calm Information Density & Design System Tokens",
            content = "https://component.gallery/design-systems/",
            why = "Design reference for clean, non-cluttered editorial hierarchy in the new product release.",
            category = "Reference",
            status = "IN PROGRESS",
            action = "EXPLORE ARCHITECTURE →",
            relevanceLabel = "RELATED TO CURRENT PROJECT",
            reminderContext = "When I start a project: Design Refresh",
            trigger = "When I start a project: Design Refresh",
            tinyAction = "Review typography tokens",
            projectTag = "#architecture",
            state = "In progress",
            estimatedMinutes = 25,
            isCompleted = false,
            createdAt = System.currentTimeMillis() - (1000 * 60 * 60 * 48), // 2 days ago
            updatedAt = System.currentTimeMillis() - (1000 * 60 * 60 * 48)
        )
    )
}
