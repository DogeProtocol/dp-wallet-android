package com.dpwallet.app.asynctask.download;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.dpwallet.app.R;
import com.dpwallet.app.utils.CheckForSDCard;
import com.dpwallet.app.utils.GlobalMethods;

import java.io.File;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadingTask extends AsyncTask<String, Void, Void> {

    private Context context;
    private boolean downloadStatus = false;
    private TaskListener taskListener;
    private Exception exception;

    public DownloadingTask(Context context, TaskListener listener) {
        this.context = context;
        this.taskListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params) {
        String downloadUrl = params[0];
        String jsonDocument = params[1];

        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.connect();

            if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e("DownloadingTask ", "Server returned HTTP " + c.getResponseCode()
                        + " " + c.getResponseMessage());
            }

            if (new CheckForSDCard().isSDCardPresent() == false) {
                GlobalMethods.ShowToast(context, "Oops!! There is no SD Card.");
            }

            File apkStorage = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"
                         + GlobalMethods.downloadDirectory);

            if (!apkStorage.exists()) {
                apkStorage.mkdir();
                Log.e("DownloadingTask", "Directory Created.");
            }

            String downloadFileName = downloadUrl.replace(GlobalMethods.mainUrl, "");
            File outputFile = new File(apkStorage, downloadFileName);

            if (!outputFile.exists()) {
                outputFile.createNewFile();
                Log.e("DownloadingTask", "File Created");
            }

            FileWriter writer = new FileWriter(outputFile,true);
            writer.write(jsonDocument);
            writer.flush();
            writer.close();

        } catch (Exception e) {
            exception = e;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if(this.taskListener != null) {
            if (exception == null) {
                this.taskListener.onFinished(downloadStatus);
            } else {
                this.taskListener.onFailure(exception);
            }
        }
    }

    public interface TaskListener {
        public void onFinished(boolean downloadStatus);
        public void onFailure(Exception exception);
    }
}

/*
public class DownloadTask1 {

    private static final String TAG = "Download Task";
    private Context context;
    private Button buttonText;
    private String downloadUrl = "";
    private String downloadFileName = "";

    public DownloadTask(Context context, Button buttonText, String downloadUrl) {
        this.context = context;
        this.buttonText = buttonText;
        this.downloadUrl = downloadUrl;

        downloadFileName = downloadUrl.replace(GlobalMethods.mainUrl, "");

        Log.e(TAG, downloadFileName);

        //Start Downloading Task
        new DownloadingTask().execute();
    }

}
 */
