/*
 * Copyright (c) 2017 anno Domini.
 *
 * Code below is a part of
 * https://github.com/halfiuuu/Notepad_x
 * available for use under the
 * GNU Affero General Public License v3.0
 * as stated in
 * https://github.com/halfiuuu/Notepad_x/blob/master/LICENSE
 *
 * Created by Dawid Halfar
 * contact possible via halfardawid@gmail.com
 *
 * I'm not sure what else this thing should state... Whatever.
 */

package halfardawid.notepadx.activity.sketch_editor.finger_movement;

import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import halfardawid.notepadx.activity.generic.layouts.Updatable;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_POINTER_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_UP;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by Dawid on 2017-10-27.
 */

final class MoveThread extends Thread{
    private static final String TAG="MOVE_THREAD";

    private Fingers fingers;
    private BlockingQueue<FingerMovement> queue;
    private AtomicInteger progression=new AtomicInteger(0);
    private AtomicInteger tasks=new AtomicInteger(0);

    private void logThread(){
        Log.d(TAG,Thread.currentThread()+" thread...");
    }

    public MoveThread(Fingers fingers) {
        this.fingers = fingers;
        queue=new LinkedBlockingQueue<>();
    }

    public void add(MotionEvent me){
        try {
            if(queue.size()!=0)tasks.addAndGet(2);
            updateProgress();
            queue.put(getFingerMovement(me));
        } catch (InterruptedException e) {
            Log.d(TAG,"interrupted adding");
        }
    }

    private void updateProgress() {
        setProgress((tasks.get()!=0)?(float)progression.get()/(float)tasks.get():0);
    }

    @Override
    public void run() {
        try {
            for(;;){
                if(queue.size()==0){
                    tasks.set(0);
                    progression.set(0);
                }
                progressUp();
                queue.take().run();
                progressUp();
                fingers.refreshView();
            }
        }
        catch (InterruptedException e1) {
            Log.d(TAG,"interrupted");
        }
    }

    private void progressUp() {
        progression.incrementAndGet();
        updateProgress();
    }

    private void setProgress(float i) {
        Updatable u=fingers.getProgressBar();
        if(u==null)return;
        u.updateWithValue(i);
    }

    private FingerMovement getFingerMovement(MotionEvent me) {
        switch (me.getActionMasked()) {
            case ACTION_UP:
                return new LastFingerUp(fingers, me);
            case ACTION_POINTER_UP:
                return new FingerUp(fingers, me);
            case ACTION_MOVE:
                return new FingersMoved(fingers, me);
            case ACTION_DOWN:
                return new FirstFingerDown(fingers, me);
            case ACTION_POINTER_DOWN:
                return new FingerDown(fingers, me);
            default:
                return new WtfDidJustMove(me);
        }
    }
}
