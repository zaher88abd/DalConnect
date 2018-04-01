package ca.connect.dal.dalconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void listV(View view) {
        Intent intent=new Intent(this,MapActivity.class);
        startActivity(intent);
    }

    public void chatBot(View view) {
        Intent intent=new Intent(this,ChatActivity.class);
        startActivity(intent);
    }
}
