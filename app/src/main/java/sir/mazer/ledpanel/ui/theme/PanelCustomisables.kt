package sir.mazer.ledpanel.ui.theme

import androidx.compose.ui.text.font.FontFamily

enum class LEDFonts(val font: FontFamily){
    DEFAULT(FontFamily.Default),
    CURSIVE(FontFamily.Cursive),
    MONOSPACE(FontFamily.Monospace),
    SERIF(FontFamily.Serif),
    SANSSERIF(FontFamily.SansSerif)
}

enum class LEDBackgrounds(val color: Long){
    BLUE(0xFF337AFA),
    GREEN(0xFF4BBA33),
    YELLOW(0xFFF9E539),
    ORANGE(0xFFE27531),
    RED(0xFFFC3D3B),
    PINK(0xFFFFB6B4),
    PURPLE(0xFF101010),
    BROWN(0xFF9F4811)
}