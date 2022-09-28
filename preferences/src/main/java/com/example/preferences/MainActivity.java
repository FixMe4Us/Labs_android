package com.example.preferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Random;


// implements - реализация, используется для интрфейса
public class MainActivity extends AppCompatActivity
             implements View.OnClickListener {


    private ConstraintLayout layout;

    EditText etText;
    Button b_Save, b_Load;

    // Объект sPref служит для работы с данными, которые мы
    // хотим сохранить.
    SharedPreferences sPref;

    final String SAVED_TEXT = "Напоминане сохранено!";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etText = (EditText) findViewById(R.id.etText);

        // Создание слушателей двух кнопок
        b_Save = (Button)findViewById(R.id.b_Save);
        b_Save.setOnClickListener(this);

        b_Load = (Button) findViewById(R.id.b_Load);
        b_Load.setOnClickListener(this);

        // Создание макета
        layout = findViewById(R.id.layout);

        // загружаем данные, если они есть, вначале работы приложения.
        loadText();

    }


    // Обработчики нажатия двух кнопок
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.b_Save:   // сохранение текста
                saveText();
                break;
            case R.id.b_Load:   // загрузка текста
                loadText();
                break;
            default:
                break;
        }


        // Генерируем цвета при нажатии на кнопки
        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256),
                random.nextInt(256));
        layout.setBackgroundColor(color);
    }

    // сохранение текста
    void saveText() {
        // Для работы с данными получаем объект sPref
        sPref = getPreferences(MODE_PRIVATE);
        // Создаем объект ed в котором будем хранить данные, необходимые
        // для обеспечения их сохранения.
        SharedPreferences.Editor ed = sPref.edit();
        // Метод putString служит для указания имени переменной - SAVED_TEXT
        ed.putString(SAVED_TEXT, etText.getText().toString());
        // Сохраняем данные
        ed.commit();
        // Вывод сообщения о том, что данные сохранены
        Toast.makeText(this, "Напоминане сохранено!", Toast.LENGTH_SHORT).show();
    }

    // Загрузка текста
    void loadText() {
        // Для работы с данными получаем объект sPref
        sPref = getPreferences(MODE_PRIVATE);
        // Читаем данные подлежащие сохранению
        String savedText = sPref.getString(SAVED_TEXT, "");
        // Запись данных
        etText.setText(savedText);
        // Вывод сообщения о том, что данные загружены
        Toast.makeText(this, "Напоминане загружено!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected  void onDestroy() {
        // Сохраняем текст при завершении работы приложения
        saveText();
        super.onDestroy();
    }

}