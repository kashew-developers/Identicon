package in.kashewdevelopers.identicon;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    static public int DEFAULT_BORDER_SIZE = 20;
    static public int DEFAULT_IMAGE_SIZE = 420;
    static public int DEFAULT_NO_OF_BLOCKS = 5;

    static public String PREF_NAME = "controlValues";
    static public String BORDER_SIZE = "borderSize";
    static public String IMAGE_SIZE = "imageSize";
    static public String NO_OF_BLOCKS = "noOfBlocks";


    static public int getBorderSize(Context context) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(BORDER_SIZE, DEFAULT_BORDER_SIZE);
    }

    static public void setBorderSize(Context context, int borderSize) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(BORDER_SIZE, borderSize).apply();
    }


    static public int getImageSize(Context context) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(IMAGE_SIZE, DEFAULT_IMAGE_SIZE);
    }

    static public void setImageSize(Context context, int imageSize) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(IMAGE_SIZE, imageSize).apply();
    }


    static public int getNoOfBlocks(Context context) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(NO_OF_BLOCKS, DEFAULT_NO_OF_BLOCKS);
    }

    static public void setNoOfBlocks(Context context, int noOfBlocks) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(NO_OF_BLOCKS, noOfBlocks).apply();
    }

}
