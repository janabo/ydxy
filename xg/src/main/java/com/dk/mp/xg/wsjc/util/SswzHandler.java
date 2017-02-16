package com.dk.mp.xg.wsjc.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.ui.Sswz.SswzMainActivity;
import com.google.zxing.Result;

/**
 * 作者：janabo on 2017/2/15 14:35
 */
public class SswzHandler extends Handler {

    private final SswzMainActivity activity;
    private final SswzDecodeThread decodeThread;
    private final CameraManager cameraManager;
    private State state;

    private enum State {
        PREVIEW, SUCCESS, DONE
    }

    public SswzHandler(SswzMainActivity activity, CameraManager cameraManager, int decodeMode) {
        this.activity = activity;
        decodeThread = new SswzDecodeThread(activity, decodeMode);
        decodeThread.start();
        state = State.SUCCESS;

        // Start ourselves capturing previews and decoding.
        this.cameraManager = cameraManager;
        cameraManager.startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        if(message.what == R.id.restart_preview){
            restartPreviewAndDecode();
        }else if(message.what == R.id.decode_succeeded){
            state = State.SUCCESS;
            Bundle bundle = message.getData();
            activity.handleDecode((Result) message.obj, bundle);
        }else if(message.what == R.id.decode_failed){
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
        }else if(message.what == R.id.return_scan_result){
            activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
            activity.finish();
        }

    }

    public void quitSynchronously() {
        state = State.DONE;
        cameraManager.stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
        quit.sendToTarget();
        try {
            decodeThread.join(500L);
        } catch (InterruptedException e) {
        }

        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);
    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
        }
    }
}
