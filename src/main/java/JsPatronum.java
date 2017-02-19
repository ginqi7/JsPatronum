package main.java;

import jargs.gnu.CmdLineParser;
import java.io.*;

public class JsPatronum {
    public static void main(String args[]) {
        // 命令行输入处理
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option helpOpt = parser.addBooleanOption('h', "help");

        CmdLineParser.Option outputFilenameOpt = parser.addStringOption('o', "output");

        Reader in = null;
        Writer out = null;


        try {
            parser.parse(args);
            Boolean help = (Boolean) parser.getOptionValue(helpOpt);
            if (help != null && help.booleanValue()) {
                usage();
                System.exit(0);
            }

            // 输入
            String[] fileArgs = parser.getRemainingArgs();
            java.util.List<String> files = java.util.Arrays.asList(fileArgs);
            // 如果命令行没有文件输入，则从终端读取
            if (files.isEmpty()) {
                files = new java.util.ArrayList<String>();
                files.add("-");
            }

            // 输出
            String output = (String) parser.getOptionValue(outputFilenameOpt);
            String pattern[];
            if(output == null) {
                pattern = new String[0];
            } else if (output.matches("(?i)^[a-z]\\:\\\\.*")){ // if output is with something like c:\ dont split it
                pattern = new String[]{output};
            } else {
                pattern = output.split(":");
            }

            // 保存输入文件内容
            java.util.Iterator<String> filenames = files.iterator();
            while (filenames.hasNext()) {
                String inputFilename = (String)filenames.next();
                if (inputFilename.equals("-")) {
                    in = new InputStreamReader(System.in);
                } else {
                    in = new InputStreamReader(new FileInputStream(inputFilename));
                }

                //
                String outputFilename = output;
                if (pattern.length > 1 && files.size() > 0) {
                    outputFilename = inputFilename.replaceFirst(pattern[0], pattern[1]);
                }
                
                Obfuscator obfuscator = new Obfuscator(in);

                // 关闭输入流，打开输出流，防止输入文件被覆盖
                in.close();
                in = null;

                if (outputFilename == null) {
                    out = new OutputStreamWriter(System.out);
                } else {
                    out = new OutputStreamWriter(new FileOutputStream(outputFilename));
                }
                obfuscator.obfuscate();
                obfuscator.compress(out);
            }

        } catch (CmdLineParser.IllegalOptionValueException e) {
            e.printStackTrace();
        } catch (CmdLineParser.UnknownOptionException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * 打印帮助信息
     */
    private static void usage() {
        System.err.println(
                "JsPatronum Version: @VERSION@\n"

                        + "\nUsage: java -jar JsPatronum-@VERSION@.jar [options] [input file]\n"
                        + "\n"
                        + "Global Options\n"
                        + "  -V, --version             Print version information\n"
                        + "  -h, --help                Displays this information\n"
                        + "  -o <file>                 Place the output into <file>. Defaults to stdout.\n"
                        + "                            Multiple files can be processed using the following syntax:\n"
                        + "                            java -jar JsPatronum.jar -o '.js$:-new.js' *.js\n\n");
 }
}
