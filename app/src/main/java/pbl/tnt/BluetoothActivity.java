package pbl.tnt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BluetoothActivity extends Activity {

    private EditText editTextText;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        editTextText = findViewById(R.id.editTextText);
        button2 = findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataToSend = editTextText.getText().toString();
                sendToServer(dataToSend);
            }
        });
    }

    private void sendToServer(String data) {
        // 送信するデータを作成
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // サーバのURLを指定
        String serverUrl = "https://pbl-gairon-test.calloc134personal.workers.dev/students/login";

        // HTTP POSTリクエストを作成してデータを送信
        try {
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // データをサーバに送信
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(jsonData.toString().getBytes());
            outputStream.flush();
            outputStream.close();

            // レスポンスコードを取得
            int responseCode = connection.getResponseCode();

            // レスポンスを受け取る
            InputStream inputStream;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // 応答に応じて処理を分岐
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // サーバからの応答が成功の場合の処理
                // Toastメッセージを表示する
                Toast.makeText(this, "データの送信に成功しました", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                // サーバからの応答がエラーの場合の処理
                // Toastメッセージを表示する
                Toast.makeText(this, "データの送信に失敗しました", Toast.LENGTH_SHORT).show();
                // TODO: 必要なエラーハンドリングを行う
            }

            // 接続を閉じる
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}