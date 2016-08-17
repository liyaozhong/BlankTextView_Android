package code.joshuali.widgettest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BlankTextView blank = (BlankTextView) this.findViewById(R.id.blank);
        blank.setup();
    }
}
