package com.jcgroup.common.framework.task;

import android.os.Message;

import com.jcgroup.common.framework.logic.LogicCallback;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

/**
 * 任务父类, 子类继承并实现doInBackground()方法, 在其中执行耗时操作
 *
 * @author hiphonezhu@gmail.com
 * @version [DX-AndroidLibrary, 2018-3-6]
 */
public abstract class Task implements ITask {
    int taskId;
    WeakReference<LogicCallback> callbackReference;

    public Task(int taskId, Object subscriber) {
        this.taskId = taskId;
        if (subscriber instanceof LogicCallback) {
            this.callbackReference = new WeakReference<>((LogicCallback) subscriber);
        } else if (subscriber != null) {
            throw new IllegalArgumentException("subscriber must implements LogicCallback interface");
        }
    }

    /**
     * 封装结果,将doInBackground()与taskId合并成Message
     *
     * @return
     */
    public Message execute() {
        Message msg = new Message();
        msg.what = taskId;
        msg.obj = doInBackground();
        return msg;
    }
    
    public void callback(Message message) {
        if (callbackReference != null && callbackReference.get() != null) {
            callbackReference.get().call(message);
        }
    }

    public void unregister() {
        if (callbackReference != null) {
            callbackReference.clear();
            callbackReference = null;
        }
    }
}
