package discord;

import java.io.File;
import java.text.NumberFormat;

/**
 * Created by ModdyLP on 06.07.2017. Website: https://moddylp.de/
 */
public class SystemInfo {
    private Runtime runtime = Runtime.getRuntime();

    public String Info() {
        return this.OsInfo() +
                this.MemInfo();
    }

    private String OSname() {
        return System.getProperty("os.name");
    }

    private String OSversion() {
        return System.getProperty("os.version");
    }

    private String OsArch() {
        return System.getProperty("os.arch");
    }

    public long totalMem() {
        return Runtime.getRuntime().totalMemory();
    }

    private long usedMem() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
    public String getUsedMem() {
        NumberFormat format = NumberFormat.getInstance();
        return format.format((usedMem() / 1024 / 1024))+" MB";
    }

    private String MemInfo() {
        NumberFormat format = NumberFormat.getInstance();
        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        sb.append("Used Memory:       |");
        sb.append(getUsedMem());
        sb.append("\n");
        return sb.toString();

    }

    private String OsInfo() {
        return "\nOS:                |" +
                this.OSname() +
                "\n" +
                "Version:           |" +
                this.OSversion() +
                " : " +
                this.OsArch() +
                "\n" +
                "CPU Cores:         |" +
                runtime.availableProcessors() +
                "\n";
    }
}
