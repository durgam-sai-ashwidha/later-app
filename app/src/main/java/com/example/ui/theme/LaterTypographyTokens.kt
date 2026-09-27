package com.example.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

/**
 * Unified Typography Token System for LATER.
 *
 * Strictly enforces:
 * 1. Editorial Serif (NewsreaderFamily) for 'WHY' text and literary quotations,
 *    scaled up 10-15% (24.5sp - 26sp) with a uniform 1.2x proportional line-height.
 * 2. Sans-Serif (PlusJakartaSansFamily) for titles, metadata, actions, and secondary labels.
 *
 * Establishes the 10/10 archival visual reading hierarchy:
 * WHY (Dominant) → TITLE (Secondary) → CATEGORY / TIME → SOURCE
 */
object LaterTypographyTokens {

    // =========================================================================
    // 1. EDITORIAL SERIF TOKENS: "WHY" & LITERARY QUOTES
    // =========================================================================

    /**
     * Primary WHY Card Content:
     * Scaled up 10–15% (24.5sp) over standard card text, with uniform 1.2x line-height
     * and subtle letter-spacing for open, comfortable reading in archive feeds.
     */
    val whyCard = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = 24.5.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 1.2.em,
        letterSpacing = 0.4.sp,
        color = LaterInkPrimary
    )

    /**
     * Primary WHY Hero Content (Detail View):
     * Prominent editorial serif callout (26sp) for dedicated entry inspection.
     */
    val whyHero = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = 26.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 1.2.em,
        letterSpacing = 0.4.sp,
        color = LaterInkPrimary
    )

    /**
     * WHY Editor Input (Capture & Edit screen):
     * Editorial serif styling during the moment of reflection and composition.
     */
    val whyEditor = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 1.2.em,
        letterSpacing = 0.35.sp,
        color = LaterInkPrimary
    )

    /**
     * Opening Quotation Mark for WHY callouts:
     * Terracotta accent in Newsreader serif matching the proportional line-height.
     */
    val whyQuoteMark = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 1.2.em,
        color = LaterTerracotta
    )

    val whyQuoteMarkHero = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 1.2.em,
        color = LaterTerracotta
    )

    /**
     * Editorial Tagline ("Never ask “Why did I save this?” again"):
     * Literary serif presentation in Newsreader.
     */
    val editorialTagline = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 25.sp,
        color = LaterInkPrimary
    )

    /**
     * Editorial Quote / Brand Story text:
     */
    val editorialBody = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
        color = LaterWarmGray
    )

    // =========================================================================
    // 2. SANS-SERIF TOKENS: LABELS, TITLES, METADATA & ACTIONS
    // =========================================================================

    /**
     * "WHY YOU SAVED THIS" Section Header:
     * High-contrast, tracked uppercase label in terracotta.
     */
    val whyHeaderLabel = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 11.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.5.sp,
        color = LaterTerracotta
    )

    /**
     * Secondary Title in Memory Cards:
     * Clear, legible title that deliberately does not compete with the dominant WHY.
     */
    val titlePrimary = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 21.sp,
        color = LaterInkPrimary
    )

    /**
     * Title in Memory Detail view.
     */
    val titleDetail = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp,
        color = LaterInkPrimary
    )

    /**
     * Metadata: Category and relative timestamp (e.g. "LEARN · 3H AGO").
     */
    val metaCategoryTime = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = LaterWarmGray
    )

    /**
     * Source URL / Domain breadcrumb (e.g. "shadcn.com/...").
     */
    val sourceBreadcrumb = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp,
        color = LaterTextMuted
    )

    /**
     * Brand Primary Wordmark ("LATER").
     */
    val brandWordmark = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 2.4.sp,
        color = LaterInkPrimary
    )

    /**
     * Brand Secondary Tagline ("WHY FIRST").
     */
    val brandSecondary = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 10.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.6.sp,
        color = LaterTerracotta
    )

    /**
     * Archive Counter ("3 / 50 MEMORIES").
     */
    val archiveCounter = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.8.sp,
        color = LaterWarmGray
    )

    /**
     * Category Navigation Tab:
     */
    fun categoryTab(isSelected: Boolean) = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 12.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
        letterSpacing = 1.2.sp,
        color = if (isSelected) LaterTerracotta else LaterWarmGray
    )

    /**
     * Search Input and Placeholder:
     */
    val searchInput = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 14.5.sp,
        fontWeight = FontWeight.Medium,
        color = LaterInkPrimary
    )

    val searchPlaceholder = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = LaterTextMuted
    )

    /**
     * Field / Section Label (e.g. "1. WHAT ARE YOU SAVING?"):
     */
    val fieldLabel = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 11.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.4.sp,
        color = LaterWarmGray
    )

    val fieldLabelActive = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 11.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.4.sp,
        color = LaterTerracotta
    )

    /**
     * Primary Action Button (e.g. "SAVE MEMORY", "CONTINUE WITH PRO").
     */
    val actionButton = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp
    )

    /**
     * Empty State Headline and Subtitle:
     */
    val emptyHeadline = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.6.sp,
        color = LaterInkPrimary
    )

    val emptySubtitle = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = 17.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
        color = LaterWarmGray
    )
}
