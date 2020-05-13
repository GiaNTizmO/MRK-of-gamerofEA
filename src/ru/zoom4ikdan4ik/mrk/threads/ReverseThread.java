package ru.zoom4ikdan4ik.mrk.threads;

import mrk.utils.Reverse;

public class ReverseThread extends Thread {
    private final String mcp;
    private final String filePath;

    public ReverseThread(String mcp, String filePath) {
        this.mcp = mcp;
        this.filePath = filePath;

        this.setDaemon(true);
    }

    @Override
    public final void run() {
        Reverse.startReverse(this.mcp, this.filePath);
    }
}
