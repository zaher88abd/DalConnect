package ca.connect.dal.dalconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnUserA, btnUserB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnUserA = findViewById(R.id.buttonUserA);

        btnUserA.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this, UserListActivity.class);
                intent.putExtra("email","user0@dalconnect.com");
                intent.putExtra("password","123456");

                MainActivity.this.finish();
                startActivity(intent);

            }
        });

        btnUserB = findViewById(R.id.buttonUserB);

        btnUserB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, UserListActivity.class);
                /*intent.putExtra("user_name","UserB");
                intent.putExtra("email","22@qq.com");
                intent.putExtra("password","123456");
                intent.putExtra("room_id","1");*/
                intent.putExtra("email","user3@dalconnect.com");
                intent.putExtra("password","123456");

                MainActivity.this.finish();
                startActivity(intent);

            }
        });





    }
}
