package in.kashewdevelopers.identicon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Bitmap bitmap;
    Random random;
    ImageView identicon;
    TextView fileLocation;
    Button save;
    int imageSize, borderSize, numberOfBlocks, blockSize;
    int red, green, blue;
    int WRITE_PERMISSION = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        borderSize = 20;
        imageSize = 420;
        numberOfBlocks = 5;
        blockSize = (imageSize - borderSize - borderSize) / numberOfBlocks;
        random = new Random();
        bitmap = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.RGB_565);
        identicon = findViewById(R.id.identicon);
        save = findViewById(R.id.save);
        fileLocation = findViewById(R.id.fileLocation);

        drawIdenticon();

    }


    public void checkPermission(View v) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                noPermission();

                new AlertDialog.Builder(this)
                        .setTitle("Need Storage Permission")
                        .setPositiveButton("Give Permission", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
            }
        } else {
            save();
        }

    }


    public void noPermission() {
        Toast.makeText(this, "Give permission to save file", Toast.LENGTH_LONG).show();
    }


    public void save() {

        String path = Environment.getExternalStorageDirectory().toString();
        OutputStream fOut;

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        DateFormat format = DateFormat.getDateTimeInstance();
        format.setTimeZone(calendar.getTimeZone());
        String fileName = format.format(date);

        File file = new File(path, "Identicon");
        if (file.exists()) {
            int counter = 1;
            while (!file.isDirectory()) {
                file = new File(path, "Identicon - " + counter);
                counter++;
            }
        } else {
            file.mkdir();
        }

        file = new File(path, "Identicon/" + fileName + ".png");
        int counter = 1;
        while (file.exists()) {
            file = new File(path, "Identicon/" + fileName + " - " + counter + ".png");
            counter++;
        }

        try {

            fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            fileLocation.setText(getApplicationContext().getString(R.string.fileLocation, file.getAbsolutePath()));

        } catch (Exception e) {
            Toast.makeText(this, "Error! Could'nt save file", Toast.LENGTH_LONG).show();
            Log.d("KashewDevelopers", "Error : " + e.getMessage());
        }


    }


    public void paint(int x, int y) {

        x = borderSize + (x * blockSize);
        y = borderSize + (y * blockSize);

        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
                bitmap.setPixel(x + i, y + j, Color.rgb(red, green, blue));
                bitmap.setPixel(imageSize - x - i, y + j, Color.rgb(red, green, blue));

            }
        }

    }


    public void drawIdenticon() {

        bitmap.eraseColor(Color.rgb(240, 240, 240));

        red = random.nextInt(256);
        green = random.nextInt(256);
        blue = random.nextInt(256);

        fileLocation.setText("");

        random.setSeed(System.currentTimeMillis());

        for (int i = 0; i <= numberOfBlocks / 2; i++) {
            for (int j = 0; j < numberOfBlocks; j++) {
                if ((random.nextInt() & 1) == 1)
                    paint(i, j);
            }
        }

        identicon.setImageBitmap(bitmap);

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.refreshOption) {
            drawIdenticon();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == WRITE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                save();
            else
                noPermission();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


}
