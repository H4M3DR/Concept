package hmd.example.concept;


import android.os.Bundle;

import java.math.BigInteger;

import androidx.appcompat.app.AppCompatActivity;
import hmd.example.concept.classes.Constants;
import hmd.example.concept.fragments.MainViewFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inflating the main activity layout
        setContentView(R.layout.activity_main);
        //showing first fragment if it is not already shown
        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.container,new MainViewFragment(),Constants.MAIN_FRAGMENT_TAG).commit();
        }
    }
}
