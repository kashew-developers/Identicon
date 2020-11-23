package `in`.kashewdevelopers.identicon

import `in`.kashewdevelopers.identicon.databinding.ActivityControlsBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.ActionBar

class ControlsActivity : AppCompatActivity() {

    lateinit var binding: ActivityControlsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()
        addBackButton()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    // initialization
    private fun initialize() {
        setValuesToWidgets()

        setBorderChangeListener()
        setImageChangeListener()
        setBlockChangeListener()
    }

    private fun setValuesToWidgets() {
        // border size 10 - 50 px
        var temp: Int = SharedPrefManager.getBorderSize(this)
        binding.borderSizeController.progress = temp - 10
        binding.borderSizeValue.text = resources.getString(R.string.sizePx, temp)

        // image size 150 - 500 px
        temp = SharedPrefManager.getImageSize(this)
        binding.imageSizeController.progress = temp - 150
        binding.imageSizeValue.text = resources.getString(R.string.sizePx, temp)

        // no of blocks 3, 5, 7 - 21
        temp = SharedPrefManager.getNoOfBlocks(this)
        binding.noOfBlockController.progress = (temp - 3) / 2
        binding.noOfBlockValue.text = "$temp"
    }

    private fun addBackButton() {
        val actionBar: ActionBar? = supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    fun resetToDefaultClicked(view: View) {
        SharedPrefManager.restoreToDefault(this)
        setValuesToWidgets()
    }

    private fun setBorderChangeListener() {
        binding.borderSizeController.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val borderSize = progress + 10
                binding.borderSizeValue.text = resources.getString(R.string.sizePx, borderSize)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val borderSize: Int
                seekBar?.let {
                    borderSize = it.progress + 10
                    SharedPrefManager.setBorderSize(this@ControlsActivity, borderSize)
                }
            }
        })
    }

    private fun setImageChangeListener() {
        binding.imageSizeController.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val imageSize = progress + 150
                binding.imageSizeValue.text = resources.getString(R.string.sizePx, imageSize)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val imageSize: Int
                seekBar?.let {
                    imageSize = it.progress + 150
                    SharedPrefManager.setImageSize(this@ControlsActivity, imageSize)
                }
            }
        })
    }

    private fun setBlockChangeListener() {
        binding.noOfBlockController.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val blocks = 2 * progress + 3
                binding.noOfBlockValue.text = "$blocks"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val blocks: Int
                seekBar?.let {
                    blocks = 2 * it.progress + 3
                    SharedPrefManager.setNoOfBlocks(this@ControlsActivity, blocks)
                }
            }
        })
    }

}