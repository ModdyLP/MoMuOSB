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

    public long usedMem() {
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
        sb.append("Free memory: ");
        sb.append(format.format(freeMemory / 1024));
        sb.append("\n");
        sb.append("Allocated memory: ");
        sb.append(format.format(allocatedMemory / 1024));
        sb.append("\n");
        sb.append("Max memory: ");
        sb.append(format.format(maxMemory / 1024));
        sb.append("\n");
        sb.append("Total free memory: ");
        sb.append(format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024));
        sb.append("\n");
        return sb.toString();

    }

    private String OsInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("OS: ");
        sb.append(this.OSname());
        sb.append("\n");
        sb.append("Version: ");
        sb.append(this.OSversion());
        sb.append("\n");
        sb.append(": ");
        sb.append(this.OsArch());
        sb.append("\n");
        sb.append("Available processors (cores): ");
        sb.append(runtime.availableProcessors());
        sb.append("\n");
        return sb.toString();
    }
}
