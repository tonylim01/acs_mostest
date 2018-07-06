package common;

import java.lang.reflect.Field;

public class ShellUtil {

    private static final String[] AMR_MODE_SET = new String[] {
            "6.60k", "8.85k", "12.65k", "14.25k", "15.85k", "18.25k", "19.85k", "23.05k", "23.85k"
    };


    public static Process runShell(String cmd) {
        Process p = null;
        try {
            String[] cmds = { "/bin/sh", "-c", cmd };
            p = Runtime.getRuntime().exec(cmds);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

    public static void waitShell(Process p) {
        if (p == null) {
            return;
        }

        try {
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void killShell(Process p) {
        if (p == null) {
            return;
        }

        try {
            int pid;
            Field f = p.getClass().getDeclaredField("pid");
            f.setAccessible(true);
            pid = (Integer)f.get(p);
            f.setAccessible(false);

            if (pid > 0) {
                String killCmd = String.format("kill -9 %d", pid);
                runShell(killCmd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Process convertPcmToAmr(String sample_rate, String inputName, String outputName,int modeset) {
        if (inputName == null || outputName == null) {
            return null;
        }

        String ffmpegCmd = String.format("exec ./ffmpeg -y -f s16le -ar %s -ac 1 -i %s -acodec amr_wb -ar 16000 -b:a %s -ac 1  -y %s",
                                         sample_rate,
                                         inputName,
                                         AMR_MODE_SET[modeset],
                                         outputName);

        //System.out.println("FFMPEG Command : [" + ffmpegCmd +"]");


        return runShell(ffmpegCmd);
    }

    public static Process convertAmrToWav(String sample_rate, String inputName, String outputName) {
        if (inputName == null || outputName == null) {
            return null;
        }

        String ffmpegCmd = String.format("exec ./ffmpeg -i %s -f s16le -ar %s -ac 1  -y %s",
                                         inputName,
                                         sample_rate,
                                         outputName);

        //System.out.println("FFMPEG Command : [" + ffmpegCmd +"]");


        return runShell(ffmpegCmd);
    }

    public static Process wavmostest(String inputName, String outputName,String sample_rate) {
        if (inputName == null || outputName == null) {
            return null;
        }

        String ffmpegCmd = String.format("exec ./pesq %s %s +%s",
                                         inputName,
                                         outputName,
                                         sample_rate
        );

        //System.out.println("FFMPEG Command : [" + ffmpegCmd +"]");


        return runShell(ffmpegCmd);
    }

}
