package sg.edu.rp.c346.id20004969.myscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText etTitle, etSingers, etYear;
    Button btnInsert, btnShowList;
    RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("My Scheduler" + " ~ " + "Insert Schedule");

        etTitle = (EditText) findViewById(R.id.etTitle);
        etSingers = (EditText) findViewById(R.id.etSingers);
        etYear = (EditText) findViewById(R.id.etYear);
        btnInsert = (Button) findViewById(R.id.btnInsertSong);
        btnShowList = (Button) findViewById(R.id.btnShowList);
        ratingBar = findViewById(R.id.ratingBarStars);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = etTitle.getText().toString().trim();
                String singers = etSingers.getText().toString().trim();
                if (title.length() == 0 || singers.length() == 0){
                    Toast.makeText(MainActivity.this, "Incomplete data", Toast.LENGTH_SHORT).show();
                    return;
                }


                String year_str = etYear.getText().toString().trim();
                int year = 0;
                try {
                    year = Integer.valueOf(year_str);
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, "Invalid Date", Toast.LENGTH_SHORT).show();
                    return;
                }


                DBHelper dbh = new DBHelper(MainActivity.this);

                //int stars = getStars();
                int rating = (int) ratingBar.getRating();
                dbh.insertSong(title, singers, year, rating);
                dbh.close();
                Toast.makeText(MainActivity.this, "Schedule Inserted", Toast.LENGTH_LONG).show();

                etTitle.setText("");
                etSingers.setText("");
                etYear.setText("");

            }
        });

        btnShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(i);
            }
        });

    }
}
