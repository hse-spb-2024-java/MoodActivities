package org.hse.moodactivities.models

import org.hse.moodactivities.R

val EMOTIONS: ArrayList<Item> = arrayListOf(
    Item("joy", R.drawable.icon_joy, R.color.pastel_creamy),
    Item("sadness", R.drawable.icon_sadness, R.color.pastel_light_blue),
    Item("satisfaction", R.drawable.icon_relax, R.color.pastel_green),
    Item("inspiration", R.drawable.icon_inspiration, R.color.pastel_yellow),
    Item("anxiety", R.drawable.icon_anxiety, R.color.pastel_blue),
    Item("love", R.drawable.icon_love, R.color.pastel_pink),
    Item("optimism", R.drawable.icon_optimism, R.color.pastel_yellow),
    Item("anger", R.drawable.icon_anger, R.color.pastel_coral),
    Item("disappointment", R.drawable.icon_disappointment, R.color.pastel_lavender),
    Item("fear", R.drawable.icon_fear, R.color.pastel_light_blue),
    Item("drowsiness", R.drawable.icon_drowsiness, R.color.pastel_lavender),
    Item("awareness", R.drawable.icon_meditation, R.color.pastel_mint),
    Item("thoughtfulness", R.drawable.icon_thoughtfulness, R.color.neutral),
    Item("gratitude", R.drawable.icon_gratitude, R.color.pastel_beige),
    Item("lonely", R.drawable.icon_lonely, R.color.pastel_light_blue),
    Item("irritation", R.drawable.icon_irritation, R.color.pastel_coral),
    Item("trust", R.drawable.icon_trust, R.color.pastel_green),
    Item("enthusiasm", R.drawable.icon_party, R.color.pastel_orange),
    Item("astonishment", R.drawable.icon_astonishment, R.color.pastel_turkis),
    Item("irony", R.drawable.icon_irony, R.color.pastel_purple),
    Item("shock", R.drawable.icon_shock, R.color.pastel_red),
    Item("indifference", R.drawable.icon_indifference, R.color.pastel_gray),
    Item("determination", R.drawable.icon_determination, R.color.pastel_blue),
    Item("energy", R.drawable.icon_energy, R.color.pastel_lemon),
    Item("admiration", R.drawable.icon_admiration, R.color.pastel_raspberry),
)

val ACTIVITIES: ArrayList<Item> = arrayListOf(
    Item("read", R.drawable.icon_books, R.color.pastel_creamy),
    Item("family", R.drawable.icon_family, R.color.pastel_light_blue),
    Item("work", R.drawable.icon_work, R.color.pastel_green),
    Item("walk", R.drawable.icon_walking, R.color.pastel_yellow),
    Item("sport", R.drawable.icon_sport, R.color.pastel_blue),
    Item("study", R.drawable.icon_study, R.color.pastel_pink),
    Item("watch movie", R.drawable.icon_movie, R.color.pastel_yellow),
    Item("friends", R.drawable.icon_friends, R.color.pastel_coral),
    Item("cook", R.drawable.icon_cook, R.color.pastel_lavender),
    Item("meditation", R.drawable.icon_meditation, R.color.pastel_light_blue),
    Item("listen music", R.drawable.icon_music, R.color.pastel_lavender),
    Item("art", R.drawable.icon_art, R.color.pastel_mint),
    Item("photo", R.drawable.icon_photo, R.color.neutral),
    Item("dance", R.drawable.icon_dance, R.color.pastel_beige),
    Item("play with pet", R.drawable.icon_pet, R.color.pastel_light_blue),
    Item("museum", R.drawable.icon_museum, R.color.pastel_coral),
    Item("learn new language", R.drawable.icon_language, R.color.pastel_green),
    Item("board games", R.drawable.icon_board_games, R.color.pastel_orange),
    Item("video games", R.drawable.icon_video_games, R.color.pastel_turkis),
    Item("play music", R.drawable.icon_instrument, R.color.pastel_purple),
    Item("date", R.drawable.icon_date, R.color.pastel_red),
    Item("shopping", R.drawable.icon_shopping, R.color.pastel_gray),
    Item("attractions", R.drawable.icon_attractions, R.color.pastel_blue),
    Item("holiday", R.drawable.icon_holiday, R.color.pastel_lemon),
    Item("concert", R.drawable.icon_concert, R.color.pastel_raspberry),
)

open class Item(
    private var name: String,
    private var iconIndex: Int,
    private var iconColor: Int,
) {
    fun getName(): String {
        return name
    }

    fun getIconColor(): Int {
        return iconColor
    }

    fun getIconIndex(): Int {
        return iconIndex
    }

    companion object {
        fun getEmotionByName(name: String): Item? {
            return EMOTIONS.find { it.name == name }
        }

        fun getEmotionIconIdByName(name: String): Int? {
            return EMOTIONS.find { it.name == name }?.iconIndex
        }


        fun getActivityByName(name: String): Item? {
            return ACTIVITIES.find { it.name == name }
        }

        fun getActivityIconIdByName(name: String): Int? {
            return ACTIVITIES.find { it.name == name }?.iconIndex
        }
    }
}
