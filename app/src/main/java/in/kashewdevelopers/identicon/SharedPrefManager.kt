package `in`.kashewdevelopers.identicon

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager {

    companion object {
        private const val DEFAULT_BORDER_SIZE = 20
        private const val DEFAULT_IMAGE_SIZE = 420
        private const val DEFAULT_NO_OF_BLOCKS = 5

        private const val PREF_NAME = "controlValues"
        private const val BORDER_SIZE = "borderSize"
        private const val IMAGE_SIZE = "imageSize"
        private const val NO_OF_BLOCKS = "noOfBlocks"

        fun getBorderSize(context: Context): Int {
            val sharedPreference =
                    context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreference.getInt(BORDER_SIZE, DEFAULT_BORDER_SIZE)
        }

        fun setBorderSize(context: Context, borderSize: Int) {
            val sharedPreference =
                    context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            sharedPreference.edit().putInt(BORDER_SIZE, borderSize).apply()
        }


        fun getImageSize(context: Context): Int {
            val sharedPreferences = context
                    .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getInt(IMAGE_SIZE, DEFAULT_IMAGE_SIZE)
        }

        fun setImageSize(context: Context, imageSize: Int) {
            val sharedPreferences = context
                    .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            sharedPreferences.edit().putInt(IMAGE_SIZE, imageSize).apply()
        }


        fun getNoOfBlocks(context: Context): Int {
            val sharedPreferences = context
                    .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getInt(NO_OF_BLOCKS, DEFAULT_NO_OF_BLOCKS)
        }

        fun setNoOfBlocks(context: Context, noOfBlocks: Int) {
            val sharedPreferences = context
                    .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            sharedPreferences.edit().putInt(NO_OF_BLOCKS, noOfBlocks).apply()
        }

        fun restoreToDefault(context: Context) {
            val sharedPreference =
                    context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreference.edit()

            editor.putInt(BORDER_SIZE, DEFAULT_BORDER_SIZE)
            editor.putInt(IMAGE_SIZE, DEFAULT_IMAGE_SIZE)
            editor.putInt(NO_OF_BLOCKS, DEFAULT_NO_OF_BLOCKS)

            editor.apply()
        }
    }

}