package cc.cuichen.feihualing;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        EditText input = (EditText)findViewById(R.id.editText);
        showResult(input.getText().toString());
    }

    public void showResult(String str) {
        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView.setHorizontallyScrolling(true);
        InputStream context = getClass().getClassLoader().getResourceAsStream("assets/shi300");
        BufferedReader br = new BufferedReader(new InputStreamReader(context));
        String ans = "", title = "", line;
        try {
            int count = 0;
            while ((line = br.readLine()) != null) {
                if (line.length() == 0) continue;
                if ('0'<= line.charAt(0) && line.charAt(0) <= '9') {
                    title = line.substring(3, line.length());
                    continue;
                }
                if (line.contains(str))
                    ans += "" + (++count) + "." + line + ' ' + title + '\n';
            }

            SpannableStringBuilder builder = new SpannableStringBuilder(ans);
            for (int i = 0; i < ans.length() - str.length(); i++) {
                if (ans.substring(i, i + str.length()).equals(str)) {
                    ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
                    builder.setSpan(redSpan, i, i + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    i += str.length() - 1;
                } else if (ans.charAt(i) == ' ') {
                    int j = i + 1;
                    while (ans.charAt(i) != '\n') i++;
                    ForegroundColorSpan graySpan = new ForegroundColorSpan(Color.GRAY);
                    builder.setSpan(graySpan, j, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.BLACK);
                    builder.setSpan(blackSpan, i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            Toast.makeText(MainActivity.this, "一共找到" + count + "句", Toast.LENGTH_SHORT).show();
            textView.setText(builder);

        } catch (IOException e) {
            textView.setText("出错了！");
        }
    }
}
