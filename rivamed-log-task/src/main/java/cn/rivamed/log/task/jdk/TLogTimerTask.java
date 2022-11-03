package cn.rivamed.log.task.jdk;


import java.util.TimerTask;

/**
 * TLog用于jdk TimerTask的替换类
 *
 * @author Bryan.Zhang
 * @since 1.3.0
 */
public abstract class TLogTimerTask extends TimerTask {


    public void run() {

    }

    public abstract void runTask();

}
