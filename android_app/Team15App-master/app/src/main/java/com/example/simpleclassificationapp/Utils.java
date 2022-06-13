package com.example.simpleclassificationapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;

import androidx.camera.core.ImageProxy;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static String writeResults(Map<String, Float> mapResults){
        Map.Entry<String, Float> entryMax = null;
        Map.Entry<String, Float> entryMax1 = null;
        Map.Entry<String, Float> entryMax2 = null;
        for(Map.Entry<String, Float> entry: mapResults.entrySet()){
            if (entryMax == null || entry.getValue().compareTo(entryMax.getValue()) > 0){
                entryMax = entry;
            } else if (entryMax1 == null || entry.getValue().compareTo(entryMax1.getValue()) > 0){
                entryMax1 = entry;
            } else if (entryMax2 == null || entry.getValue().compareTo(entryMax2.getValue()) > 0){
                entryMax2 = entry;
            }
        }
        String result = entryMax.getKey().trim() + " " + entryMax.getValue().toString() + "\n" +
                entryMax1.getKey().trim() + " " + entryMax1.getValue().toString() + "\n" +
                entryMax2.getKey().trim() + " " + entryMax2.getValue().toString() + "\n";
        return result;
    }

    public static int getImageRotation(ImageProxy image){
        int rotation = image.getImageInfo().getRotationDegrees();
        return rotation/90;
    }

    public static Bitmap toBitmap(Image image) {
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];
        //U and V are swapped
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 100, out);

        byte[] imageBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    public static HashMap<String, Integer> initLookupTable() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("Sunday_roast_with_all_the_trimmings", 727);
        map.put("Black_pudding",379);
        map.put("Fish_and_chips", 533);
        map.put("Full_English_breakfast",618);
        map.put("Chicken_tikka_masala",139);
        map.put("Roast_potatoes",164);
        map.put("Roast_chicken_meat",177);
        map.put("UK_chips",294);
        map.put("Pizza",275);
        map.put("roast_turkey_carved",135);
        map.put("Bacon",541);
        map.put("Lasagne",602);
        map.put("Scones_with_cream_and_jam",108);
        map.put("Roast_beef_meat",257);
        map.put("Sausage_and_mash",423);
        map.put("Spaghetti_Bolognese",297);
        map.put("Stir_fry",115);
        map.put("Shepherds_pie",693);
        map.put("Cottage_pie",470);
        map.put("Beef_burger_and_chips",440);
        map.put("Pigs_in_blankets_(sausages_wrapped_in_bacon)",120);
        map.put("Scrambled_egg",170);
        map.put("Ham_egg_and_chips",857);
        map.put("Chilli_con_carne",551);
        map.put("Salad",20);
        map.put("Beans_on_toast",364);
        map.put("Sausage_rolls",356);
        map.put("Omelette",221);
        map.put("Jacket_potato_with_beans_and_cheese",350);
        map.put("Ham_and_cheese_toastie",307);
        map.put("Fajita_wraps",505);
        map.put("Toad_in_the_hole",520);
        map.put("Pasta bake",384);
        map.put("Cornish_pasties",282);
        map.put("Beef_stew",94);
        map.put("Steak_and_kidney_pie",307);
        map.put("Macaroni_cheese",183);
        map.put("Sweet_and_sour_chicken", 152);
        map.put("Chicken_and_mushroom_pie", 198);
        map.put("Fish_finger_sandwiches",514);
        map.put("Tomato_soup",53);
        map.put("Chicken_burger_and_chips",266);
        map.put("Thai_curry",124);
        map.put("Paella",442);
        map.put("Meatballs",123);
        map.put("Fishcakes",247);

        return map;
    }
}
