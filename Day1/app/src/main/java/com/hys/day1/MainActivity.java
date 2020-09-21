package com.hys.day1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button button;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // R: res.파일.내용 ex) R.color.~~~

        textView = findViewById(R.id.textview); // 레이아웃의 요소를 객체에 바인딩( findViewById : 레이아웃의 요소를 View로 반환 ) setContentView로 지정한 xml 파일 안에서 불러옴
        button = findViewById(R.id.button);
        editText = findViewById(R.id.edittext);

        button.setOnClickListener(listener); // 이벤트 리스너 추가
    }

    View.OnClickListener listener = new View.OnClickListener() { // OnClickListener 만들기
        @Override
        public void onClick(View view) {
            String input = editText.getText().toString(); // 입력창의 내용 얻기(getText: Editable 반환)
            textView.setText(input); // 텍스트 뷰의 내용 지정
        }
    };
}