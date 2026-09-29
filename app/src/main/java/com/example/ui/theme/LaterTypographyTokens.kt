package com.example.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

/**
 * Typography Token System for LATER Editorial Archive.
 *
 * Core Principles:
 * 1. Elegant high-contrast serif (NewsreaderFamily) for saved "WHY" quotes and taglines.
 * 2. Clean modern sans-serif (PlusJakartaSansFamily) for navigation, labels, metadata, and actions.
 * 3. Readability & accessible contrast: Small text is bold, clear, and never washed out.
 */
object LaterTypographyTokens {

    // =========================================================================
    // 1. EDITORIAL SERIF TOKENS: "WHY" & LITERARY QUOTES
    // =========================================================================

    /**
     * Saved "WHY" Reason in Memory Cards:
     * Beautiful, high-contrast serif quote (18sp) with generous line height (25sp).
     */
    val whyCard = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 25.sp,
        letterSpacing = 0.2.sp,
        color = LaterInkPrimary
    )

    /**
     * Primary WHY Hero Content (Detail View):
     */
    val whyHero = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = 21.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 29.sp,
        letterSpacing = 0.2.sp,
        color = LaterInkPrimary
    )

    /**
     * WHY Editor Input (Capture & Edit screen):
     */
    val whyEditor = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = 18.5.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 26.sp,
        letterSpacing = 0.2.sp,
        color = LaterInkPrimary
    )

    /**
     * Opening Quotation Mark for WHY callouts:
     */
    val whyQuoteMark = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 1.2.em,
        color = LaterTerracotta
    )

    val whyQuoteMarkHero = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 1.2.em,
        color = LaterTerracotta
    )

    /**
     * Editorial Tagline ("Never ask “Why did I save this?” again."):
     */
    val editorialTagline = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = 19.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 26.sp,
        color = LaterInkPrimary
    )

    val editorialDynamicPrompt = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 13.5.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 19.sp,
        color = LaterTerracotta
    )

    val editorialBody = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp,
        color = LaterInkPrimary
    )

    // =========================================================================
    // 2. SANS-SERIF TOKENS: LABELS, TITLES, METADATA & ACTIONS (BOLD & READABLE)
    // =========================================================================

    /**
     * "WHY YOU SAVED THIS" Label:
     * Small all-caps label, bold and readable with crisp letter spacing.
     */
    val whyHeaderLabel = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 12.5.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.1.sp,
        color = LaterTerracotta
    )

    /**
     * Resource Title in Memory Cards:
     * Clean modern sans-serif, bold, scannable and prominent.
     */
    val titlePrimary = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 23.sp,
        color = LaterInkPrimary
    )

    /**
     * Title in Memory Detail view.
     */
    val titleDetail = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 27.sp,
        color = LaterInkPrimary
    )

    /**
     * Metadata: Category and relative timestamp (e.g. "LEARN · 3 HOURS AGO").
     * Bold, high-contrast, uppercase styling.
     */
    val metaCategoryTime = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 12.5.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 0.9.sp,
        color = LaterSecondaryText
    )

    /**
     * Source domain breadcrumb (e.g. "ishadeed.com" or "product research").
     */
    val sourceBreadcrumb = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = LaterInkPrimary
    )

    /**
     * Card Primary Action ("START: REDESIGN DASHBOARD →").
     */
    val cardAction = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 13.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 0.8.sp,
        color = LaterTerracotta
    )

    /**
     * Brand Primary Wordmark ("LATER").
     */
    val brandWordmark = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 22.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 2.sp,
        color = LaterInkPrimary
    )

    /**
     * Brand Subtitle ("WHY FIRST").
     */
    val brandSecondary = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.4.sp,
        color = LaterTerracotta
    )

    /**
     * Archive Status ("3 ACTIVE MEMORIES").
     */
    val archiveCounter = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 12.5.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 0.7.sp,
        color = LaterInkPrimary
    )

    /**
     * Category Navigation Tab:
     */
    fun categoryTab(isSelected: Boolean) = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 13.5.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp,
        color = if (isSelected) LaterTerracotta else LaterSecondaryText
    )

    /**
     * Search Input and Placeholder:
     */
    val searchInput = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = LaterInkPrimary
    )

    val searchPlaceholder = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 14.5.sp,
        fontWeight = FontWeight.Medium,
        color = LaterWarmGray
    )

    val fieldLabel = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 13.5.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.1.sp,
        color = LaterInkPrimary
    )

    val fieldLabelActive = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 13.5.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.1.sp,
        color = LaterTerracotta
    )

    val actionButton = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
    )

    val emptyHeadline = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontSize = 15.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.1.sp,
        color = LaterInkPrimary
    )

    val emptySubtitle = TextStyle(
        fontFamily = NewsreaderFamily,
        fontSize = 16.5.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp,
        color = LaterInkPrimary
    )
}
