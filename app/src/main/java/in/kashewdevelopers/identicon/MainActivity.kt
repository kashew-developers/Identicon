package `in`.kashewdevelopers.identicon

import `in`.kashewdevelopers.identicon.databinding.ActivityMainBinding
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.DateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bitmap: Bitmap
    private lateinit var random: Random

    private var imageSize = 0
    private var borderSize = 0
    private var numberOfBlocks = 0
    private var blockSize = 0
    private var red = 0
    private var green = 0
    private var blue = 0

    val WRITE_PERMISSION: Int = 1234
    val SHARE_PERMISSION: Int = 4321


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialization()
        drawIdenticon()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refreshOption -> {
                drawIdenticon()
                return true
            }

            R.id.controlsOption -> {
                startActivityForResult(Intent(this, ControlsActivity::class.java), 123)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            WRITE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    noPermission()
                else
                    binding.saveSection.performClick()
            }

            SHARE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    noPermission()
                else
                    binding.shareSection?.performClick()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123) {
            initializeImageElements()
            binding.refreshSection.performClick()
        }
    }


    // initialization
    private fun initialization() {
        random = Random()

        initializeImageElements()
    }

    private fun initializeImageElements() {
        borderSize = SharedPrefManager.getBorderSize(this)
        imageSize = SharedPrefManager.getImageSize(this)
        numberOfBlocks = SharedPrefManager.getNoOfBlocks(this)
        blockSize = (imageSize - borderSize - borderSize) / numberOfBlocks
        bitmap = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.RGB_565)
    }


    // permissions
    private fun hasWritePermission(): Boolean {
        return (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    private fun askWritePermission(permissionCode: Int) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            with(AlertDialog.Builder(this)) {
                setTitle(R.string.need_storage_permission)

                setPositiveButton(R.string.give_permission) { _, _ ->
                    startActivity(Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:${BuildConfig.APPLICATION_ID}")))
                }

                create().show()
            }

        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    permissionCode)
        }
    }

    private fun noPermission() {
        Toast.makeText(this, R.string.give_permission_toast, Toast.LENGTH_LONG).show()
    }


    // saving identicon
    private fun saveIdenticon(): File? {
        val path = Environment.getExternalStorageDirectory()

        val date = Date()
        val calendar = Calendar.getInstance()
        val format = DateFormat.getDateTimeInstance()
        format.timeZone = calendar.timeZone
        val fileName = format.format(date)

        var file = File(path, getString(R.string.app_name))
        var counter = 1
        if (file.exists()) {
            while (!file.isDirectory) {
                file = File(path, "${getString(R.string.app_name)} - $counter")
                counter++
            }
        } else if (!file.mkdir()) {
            return null
        }

        file = File(path, "${getString(R.string.app_name)}/${fileName}.jpg")
        counter = 1
        while (file.exists()) {
            file = File(path, "${getString(R.string.app_name)}/${fileName} - ${counter}.jpg")
            counter++
        }

        try {
            val fOut = FileOutputStream(file)

            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            fOut.write(bytes.toByteArray())
            fOut.flush()
            fOut.close()

            val contentValues = ContentValues()
            contentValues.put(MediaStore.Images.Media.DATA, file.absolutePath)
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        } catch (e: Exception) {
            return null
        }

        return file
    }

    private fun getImageContentUri(imageFile: File): Uri? {
        val filePath = imageFile.absolutePath

        val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.Media._ID),
                "${MediaStore.Images.Media.DATA}=?",
                arrayOf(filePath), null)

        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
            cursor.close()
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "$id")
        }
        return null
    }


    // drawing
    private fun paint(xCount: Int, yCount: Int) {
        val x = borderSize + (xCount * blockSize)
        val y = borderSize + (yCount * blockSize)

        for (i in 0 until blockSize) {
            for (j in 0 until blockSize) {
                bitmap.setPixel(x + i, y + j, Color.rgb(red, green, blue))
                bitmap.setPixel(imageSize - x - i, y + j, Color.rgb(red, green, blue))
            }
        }
    }

    private fun drawIdenticon() {
        bitmap.eraseColor(Color.rgb(240, 240, 240))

        red = random.nextInt(256)
        green = random.nextInt(256)
        blue = random.nextInt(256)

        binding.fileLocation.text = ""

        random.setSeed(System.currentTimeMillis())

        for (i in 0..(numberOfBlocks / 2)) {
            for (j in 0 until numberOfBlocks) {
                if ((random.nextInt() and 1) == 1) {
                    paint(i, j)
                }
            }
        }

        binding.identicon.setImageBitmap(bitmap)
    }


    // handle widget clicks
    fun refreshClicked(view: View) {
        drawIdenticon()
    }

    fun saveClicked(view: View) {
        if (hasWritePermission()) {
            val imageFile = saveIdenticon()
            if (imageFile != null) {
                binding.fileLocation.text =
                        applicationContext.getString(R.string.fileLocation, imageFile.absolutePath)
            }
        } else {
            askWritePermission(WRITE_PERMISSION)
        }
    }

    fun shareClicked(view: View) {
        if (hasWritePermission()) {
            val imageFile = saveIdenticon()
            val imageUri = imageFile?.let { getImageContentUri(it) }

            if (imageUri != null) {
                val intent = Intent(Intent.ACTION_SEND)
                with(intent) {
                    putExtra(Intent.EXTRA_STREAM, imageUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    setType("image/png")
                }
                startActivity(Intent.createChooser(intent, "Share With"))
            } else {
                Toast.makeText(this, getString(R.string.error_sharing),
                        Toast.LENGTH_LONG).show()
            }

        } else {
            askWritePermission(SHARE_PERMISSION)
        }
    }

}