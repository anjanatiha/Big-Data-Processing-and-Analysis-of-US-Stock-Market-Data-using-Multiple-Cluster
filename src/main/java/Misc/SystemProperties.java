package Misc;

import java.lang.management.ManagementFactory;

public class SystemProperties {
    public static int getMaxMemorySize() {
        long memMax = ((com.sun.management.OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
        java.lang.System.out.print(memMax / (1024 * 1024 * 1024));
        return (int) memMax;
    }

    public static int setMemorySize(int num) {
        int max = getMaxMemorySize();
        return max - num;
    }
}
