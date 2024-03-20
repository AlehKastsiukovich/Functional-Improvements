package com.general.projectimprovements.view

data class SubtitleViewState(
    val body: String?,
    val links: List<LinkViewState>?
)

data class LinkViewState(
    val order: Int,
    val label: String?,
    val link: String?
)
