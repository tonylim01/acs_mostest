import common.ShellUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) throws IOException {
        System.out.println("ACS MOS Test Program !!!");


        if (args.length != 4) {
            System.out.println("java -jar acs_mostest.jar sample_rate input.wav output.wav amr_modeset");
            return;
        }

        int mode_set = Integer.valueOf(args[3]);

        String inputFile = args[1];
        String sampleRate = args[0];
        String outputFile = args[2];
        String tempfile = "temp.amr";


        Process p = ShellUtil.convertPcmToAmr(sampleRate,inputFile,tempfile,mode_set);
        ShellUtil.waitShell( p );

        Process p2 = ShellUtil.convertAmrToWav(sampleRate,tempfile,outputFile);
        ShellUtil.waitShell( p2 );


        System.out.println("Start MOS Test");

        Process p3 = ShellUtil.wavmostest(inputFile,outputFile,sampleRate);

        ShellUtil.waitShell( p3 );

        BufferedReader input = new BufferedReader( new InputStreamReader(
                p3.getInputStream()));

        String line = null;

        while ((line = input.readLine()) != null)
        {
            System.out.println(line);
        }


    }
}
