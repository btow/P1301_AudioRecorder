package com.example.samsung.p1301_audiorecorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "myLogs";

    int mBufferSize = 8192;
    AudioRecord audioRecord;
    boolean isReading = false;

    @BindView(R.id.btnStartRecord)
    Button btnStartRecord;
    @BindView(R.id.btnStopRecord)
    Button btnStopRecord;
    @BindView(R.id.btnStartRead)
    Button btnStartRead;
    @BindView(R.id.btnStopRead)
    Button btnStopRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        createAudioRecorder();

        Messager.sendToAllRecipients(getBaseContext(), "init state = " + audioRecord.getState());
    }

    private void createAudioRecorder() {
        int sampleRate = 8000;
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

        int minInternalBufferSize = AudioRecord.getMinBufferSize(
                sampleRate,
                channelConfig,
                audioFormat
        );
        int internalBufferSize = minInternalBufferSize * 4;
        Messager.sendToAllRecipients(
                getBaseContext(),
                "minInternalBufferSize = " + minInternalBufferSize +
                ", internalBufferSize = " + internalBufferSize +
                ", mBufferSize = " + mBufferSize);
        audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                channelConfig,
                audioFormat,
                internalBufferSize
        );
    }

    public void recordStart(View view) {
        Messager.sendToAllRecipients(getBaseContext(), "record start");
        audioRecord.startRecording();
        int recordingState = audioRecord.getRecordingState();
        Messager.sendToAllRecipients(getBaseContext(), "record state = " + recordingState);
    }

    public void recordStop(View view) {
        Messager.sendToAllRecipients(getBaseContext(), "record stop");
        audioRecord.stop();
        int recordingState = audioRecord.getRecordingState();
        Messager.sendToAllRecipients(getBaseContext(), "record state = " + recordingState);
    }

    public void readStart(View view) {
        Messager.sendToAllRecipients(getBaseContext(), "read start");
        isReading = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (audioRecord == null) {
                    return;
                }
                byte[] mBuffer = new byte[mBufferSize];
                int readCount = 0;
                int totalCount = 0;
                while (isReading) {
                    readCount = audioRecord.read(mBuffer, 0, mBufferSize);
                    totalCount += readCount;
                    Messager.sendToAllRecipients(
                            getBaseContext(),
                            "readCount = " + readCount +
                            "totalCount" + totalCount
                    );
                }
            }
        }).start();
    }

    public void readStop(View view) {
        Messager.sendToAllRecipients(getBaseContext(), "read stop");
        isReading = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        isReading = false;
        if (audioRecord != null) {
            audioRecord.release();
        }
    }
}
