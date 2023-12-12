package pbl.tnt;

import android.annotation.SuppressLint;
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

public class LoginActivity extends Activity {

    private EditText editNumber;
    private EditText editPassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editNumber = findViewById(R.id.editNumber);
        editPassword = findViewById(R.id.editPassword);
        Button sendButton = findViewById(R.id.login_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editNumber.getText().toString();
                String password = editPassword.getText().toString();
                connection(name, password);
            }
        });
    }

    private void connection(String name, String password) {
        // 送信するデータを作成
        JSONObject data = new JSONObject();
        try {
            data.put("name", name);
            data.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // サーバに接続してデータを送信
        try {
            URL url = new URL("https://pbl-gairon-test.calloc134personal.workers.dev/students/login");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // データをサーバに送信
            OutputStream os = connection.getOutputStream();
            os.write(data.toString().getBytes());
            os.flush();
            os.close();

            // レスポンスコードを取得
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // レスポンスが成功の場合の処理
                InputStream is = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String response = br.readLine();

                // 応答に応じて処理を分岐
                if (response.equals("0")) {
                    // サーバからの応答が0の場合の処理
                    // Toastメッセージを表示する
                    Toast.makeText(this, "サーバからの応答: 0", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else if (response.equals("1")) {
                    // サーバからの応答が1の場合の処理
                    // Toastメッセージを表示する
                    Toast.makeText(this, "サーバからの応答: 1", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, BluetoothActivity.class);
                    startActivity(intent);
                } else if (response.equals("2")) {
                    // サーバからの応答が2の場合の処理
                    // Toastメッセージを表示する
                    Toast.makeText(this, "サーバからの応答: 2", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    // サーバからの応答が予期しない値の場合の処理
                    // TODO: 予期しない応答を処理する必要がある場合の処理を追加する
                    // 例えば、エラーメッセージを表示するなど
                    Toast.makeText(this, "予期しないサーバ応答: " + response, Toast.LENGTH_SHORT).show();
                }

                br.close();
                is.close();
            } else {
                // レスポンスがエラーの場合の処理
                // TODO: エラーハンドリングを行う
                Toast.makeText(this, "HTTPエラーレスポンスコード: " + responseCode, Toast.LENGTH_SHORT).show();
            }

            // 接続を閉じる
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}