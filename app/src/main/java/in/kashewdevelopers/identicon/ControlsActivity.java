package in.kashewdevelopers.identicon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import in.kashewdevelopers.identicon.databinding.ActivityControlsBinding;

public class ControlsActivity extends AppCompatActivity {

    private ActivityControlsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityControlsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        addBackButton();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // initialization
    public void initialize() {
        setValuesToWidgets();

        setBorderChangeListener();
        setImageChangeListener();
        setBlockChangeListener();
    }

    public void setValuesToWidgets() {
        // border size 10 - 50 px
        int temp = SharedPrefManager.getBorderSize(this);
        binding.borderSizeController.setProgress(temp - 10);
        binding.borderSizeValue.setText(getResources().getString(R.string.sizePx, temp));

        // image size 150 - 500 px
        temp = SharedPrefManager.getImageSize(this);
        binding.imageSizeController.setProgress(temp - 150);
        binding.imageSizeValue.setText(getResources().getString(R.string.sizePx, temp));

        // no of blocks 3, 5, 7 - 21
        temp = SharedPrefManager.getNoOfBlocks(this);
        binding.noOfBlockController.setProgress((temp - 3) / 2);
        binding.noOfBlockValue.setText(String.valueOf(temp));
    }

    public void addBackButton() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }


    // handle widget clicks
    public void resetToDefaultClicked(View v) {
        binding.borderSizeController.setProgress(SharedPrefManager.DEFAULT_BORDER_SIZE - 10);
        SharedPrefManager.setBorderSize(this, SharedPrefManager.DEFAULT_BORDER_SIZE);

        binding.imageSizeController.setProgress(SharedPrefManager.DEFAULT_IMAGE_SIZE - 150);
        SharedPrefManager.setImageSize(this, SharedPrefManager.DEFAULT_IMAGE_SIZE);

        binding.noOfBlockController.setProgress((SharedPrefManager.DEFAULT_NO_OF_BLOCKS - 3) / 2);
        SharedPrefManager.setNoOfBlocks(this, SharedPrefManager.DEFAULT_NO_OF_BLOCKS);
    }

    public void setBorderChangeListener() {
        binding.borderSizeController.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int borderSize = progress + 10;
                binding.borderSizeValue.setText(getResources().getString(R.string.sizePx, borderSize));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int borderSize = seekBar.getProgress() + 10;
                SharedPrefManager.setBorderSize(ControlsActivity.this, borderSize);
            }
        });
    }

    public void setImageChangeListener() {
        binding.imageSizeController.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int imageSize = progress + 150;
                binding.imageSizeValue.setText(getResources().getString(R.string.sizePx, imageSize));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int imageSize = seekBar.getProgress() + 150;
                SharedPrefManager.setImageSize(ControlsActivity.this, imageSize);
            }
        });
    }

    public void setBlockChangeListener() {
        binding.noOfBlockController.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int blocks = 2 * progress + 3;
                binding.noOfBlockValue.setText(String.valueOf(blocks));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int blocks = 2 * seekBar.getProgress() + 3;
                SharedPrefManager.setNoOfBlocks(ControlsActivity.this, blocks);
            }
        });
    }

}