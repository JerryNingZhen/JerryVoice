package com.soniq.jerryvoice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private JerryVoiceApplication mJerryVoiceApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // 设置全屏
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        mJerryVoiceApplication = new JerryVoiceApplication();
    }

    public void voice(View v) {
        RecognizerDialog mRecognizerDialog = new RecognizerDialog(this, null);
        mRecognizerDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mRecognizerDialog.setParameter(SpeechConstant.ACCENT, "mandrarin");
        mRecognizerDialog.setListener(listener);
        mRecognizerDialog.show();
    }

    RecognizerDialogListener listener = new RecognizerDialogListener() {
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            //返回成功
            if (b) {
                return;
            }

            Gson gson = new Gson();
            String result = recognizerResult.getResultString();
            ResultBean resultBean = gson.fromJson(result, ResultBean.class);
            String w = "";
            for (int i = 0; i < resultBean.getWs().size(); i++) {
                ResultBean.WsBean wsBean = resultBean.getWs().get(i);
                List<ResultBean.WsBean.CwBean> cw = wsBean.getCw();
                for (int j = 0; j < cw.size(); j++) {
                    w += cw.get(j).getW();
                }
            }
            Toast.makeText(MainActivity.this, w, Toast.LENGTH_LONG).show();
            mJerryVoiceApplication.startVoice(MainActivity.this,w);
        }

        @Override
        public void onError(SpeechError error) {
            //直接告诉用户失败
            String errorErrorDescription = error.getErrorDescription();
            mJerryVoiceApplication.startVoice(MainActivity.this,errorErrorDescription);
        }
    };


}
