package sg.edu.rp.c346.id20004969.myscheduler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    ListView lv;
    ArrayList<Song> songList;
    //ArrayAdapter<Song> adapter;
    String moduleCode;
    Button btn5Stars;
    Button insertIsland;

    ArrayList<String> years;
    Spinner spinner;
    ArrayAdapter<String> spinnerAdapter;
    CustomAdapter adapter;
    @Override
    protected void onResume() {
        super.onResume();
        DBHelper dbh = new DBHelper(this);
        songList.clear();
        songList.addAll(dbh.getAllSongs());
        adapter.notifyDataSetChanged();

        years.clear();
        years.addAll(dbh.getYears());
        spinnerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        setTitle("My Scheduler" + " ~ " + "Show Schedule");

        lv = (ListView) this.findViewById(R.id.lv);
        btn5Stars = (Button) this.findViewById(R.id.btnShow5Stars);
        spinner = (Spinner) this.findViewById(R.id.spinnerYear);
        insertIsland = findViewById(R.id.buttonAdd);

        DBHelper dbh = new DBHelper(this);
        songList = dbh.getAllSongs();
        years = dbh.getYears();
        dbh.close();

        adapter = new CustomAdapter(this, R.layout.row, songList);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SecondActivity.this, ThirdActivity.class);
                i.putExtra("song", songList.get(position));
                startActivity(i);
            }
        });

        btn5Stars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(SecondActivity.this);
                songList.clear();
                songList.addAll(dbh.getAllSongsByStars(5));
                adapter.notifyDataSetChanged();
            }
        });

        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, years);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DBHelper dbh = new DBHelper(SecondActivity.this);
                songList.clear();
                songList.addAll(dbh.getAllSongsByYear(Integer.valueOf(years.get(position))));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        insertIsland.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater =
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewDialog = inflater.inflate(R.layout.input1, null);

                final EditText etTitle = viewDialog.findViewById(R.id.etTitle);
                final EditText etSinger = viewDialog.findViewById(R.id.etSingers);
                final EditText etYear = viewDialog.findViewById(R.id.etYear);
                final RatingBar ratingBar = viewDialog.findViewById(R.id.ratingBarStars);
                AlertDialog.Builder myBuilder = new AlertDialog.Builder(SecondActivity.this);
                myBuilder.setView(viewDialog);  // Set the view of the dialog
                myBuilder.setTitle("Insert Schedule");
                myBuilder.setPositiveButton("Insert", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = etTitle.getText().toString().trim();
                        String singers = etSinger.getText().toString().trim();
                        if (title.length() == 0 || singers.length() == 0){
                            Toast.makeText(SecondActivity.this, "Incomplete data", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        String year_str = etYear.getText().toString().trim();
                        int year = 0;
                        try {
                            year = Integer.valueOf(year_str);
                        } catch (Exception e){
                            Toast.makeText(SecondActivity.this, "Invalid Square Kilometer", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        DBHelper dbh = new DBHelper(SecondActivity.this);

                        //int stars = getStars();
                        int rating = (int) ratingBar.getRating();
                        dbh.insertSong(title, singers, year, rating);
                        dbh.close();
                        Toast.makeText(SecondActivity.this, "Island Inserted", Toast.LENGTH_LONG).show();

                        etTitle.setText("");
                        etSinger.setText("");
                        etYear.setText("");
                    }
                });

                myBuilder.setNegativeButton("CANCEL", null);
                AlertDialog myDialog = myBuilder.create();
                myDialog.show();
            }
        });
    }
}
