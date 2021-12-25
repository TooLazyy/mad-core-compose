package ru.wearemad.mad_core_compose.utils

fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}