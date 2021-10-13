package net.peanuuutz.intellibow.sound

import net.peanuuutz.intellibow.util.Constants
import net.peanuuutz.intellibow.util.sound

val SCOPE_OPEN_SOUND by lazy {
    sound(Constants.SCOPE_OPEN)
}
val SCOPE_CLOSE_SOUND by lazy {
    sound(Constants.SCOPE_CLOSE)
}

fun registerSounds() {
    SCOPE_OPEN_SOUND
    SCOPE_CLOSE_SOUND
}