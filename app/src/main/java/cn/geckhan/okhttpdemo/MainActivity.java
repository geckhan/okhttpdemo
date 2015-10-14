package cn.geckhan.okhttpdemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();

    private final int BYTESIZE= 2048;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTask.execute();
            }
        });


    }
    AsyncTask<Void,Void,Void> mTask = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... params) {
            Request mRquest = new Request.Builder().url("http://121.41.119.107:81/test/1.doc").build();
            try {
                downloading(client, mRquest, new IProgressListener() {
                    @Override
                    public void onStart(long contentLength) {
                        MtLog.LogV("需要下载的文件长度"+contentLength);
                    }

                    @Override
                    public void onProgress(long currentLength) {
                        MtLog.LogV("当前进度"+currentLength);
                    }

                    @Override
                    public void onSuccess(String filePath) {
                        MtLog.LogV("下载成功，文件地址"+filePath);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    };

    private void downloading(final OkHttpClient client, final Request request, IProgressListener listener) throws IOException {
        Response response = client.newCall(request).execute();
        InputStream inputStream=null;
        int count ;
        byte[] bytes =  new byte[BYTESIZE];

        if (response.isSuccessful()) {
            inputStream = response.body().byteStream();
            listener.onStart(response.body().contentLength());
        }

        if (inputStream!=null){
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            int sumCount =0;
            while ((count=bis.read(bytes))!=-1){
                sumCount+=count;
                listener.onProgress(sumCount);
            }
        }


        /**
         * todo 校验文件 是否完整等问题
         */

        listener.onSuccess("ok");


    }


}
