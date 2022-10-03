package com.test.diff.services.utils;

import com.test.diff.services.exceptions.BizException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author wl
 * @date 2022/8/17
 */
@Slf4j
public class CMDUtils {

    public static CmdResult exec (String cmdCommand) {
        cmdCommand = fixWinCmd(cmdCommand);
        StringBuilder stringBuilder = new StringBuilder();
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(getOsCmd() + cmdCommand );
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "GBK"));
            String line = null;
            while((line=bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line+"\n");
            }
            return CmdResult.builder()
                    .isSuccess(process.exitValue() == 0 ? true : false )
                    .status(process.exitValue())
                    .context(stringBuilder.toString())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String execShell(String scriptPath, String... params) {
        String[] cmd = new String[]{scriptPath};
        cmd = ArrayUtils.addAll(cmd, params);
        //解决脚本没有执行权限
        ProcessBuilder builder = new ProcessBuilder("/bin/chmod", "777", scriptPath);
        Process process = null;
        int runningStatus = 0;
        try {
            process = builder.start();
            process.waitFor();
            Process ps = Runtime.getRuntime().exec(cmd);
            runningStatus = ps.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            BufferedReader err = new BufferedReader(new InputStreamReader(ps.getErrorStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            while ((line = err.readLine()) != null) {
                log.error("shell log error...." + line);
                sb.append(line).append("\n");
            }
            if (runningStatus != 0){
                throw new BizException("exec shell script failed, result: " + sb.toString());
            }
            //执行结果
            return sb.toString();
        } catch (IOException e) {
            log.error("Failed to exec {}, sandbox load failed", scriptPath);
            e.printStackTrace();
            throw new RuntimeException("shell script exec failed, sandbox load failed");
        } catch (InterruptedException e) {
            log.error("Failed to exec {}, sandbox load failed", scriptPath);
            e.printStackTrace();
            throw new RuntimeException("shell script exec failed, sandbox load failed");
        }
    }

    /**
     * windows系统,如果指令是cd, 需要先D: ,然后再执行cd D:\home 才能正确进入到home目录下
     * 所以这里特殊处理下
     * @param cmdCommand
     * @return
     */
    private static String fixWinCmd(String cmdCommand){
        if (OsUtils.isWindows() && cmdCommand.contains("cd")){
            String driveLetter = "";
            // 1.先把多条指令拆分
            String[] commands = cmdCommand.split("&&");
            List<String> commandList = new ArrayList<>();
            for (int i = 0; i < commands.length; i++) {
                // 2.找出cd操作的指令
                if (commands[i].trim().startsWith("cd")){
                    driveLetter = getDriveLetter(commands[i]);
                    if (StringUtils.isNotEmpty(driveLetter)){
                        commandList.add( driveLetter + ":"); // D:
                    }

                }
                commandList.add(commands[i]);
            }
            return String.join(" && ", commandList.toArray(new String[0]));
        }
        return cmdCommand;
    }

    /**
     * 获取windows系统下,cd命令中的盘符
     * @param command 指令
     * @return
     */
    private static String getDriveLetter(String command){
        String driveLetter = "";
        String[] arr = command.split(" ");
        for (String s: arr){
            if (s.trim().contains(":\\")){
                driveLetter = s.split(":")[0];
            }
        }
        return driveLetter.toUpperCase(Locale.ROOT).trim();
    }

    /**
     * 获取根指令信息
     * @return
     */
    private static String getOsCmd(){
        if (OsUtils.isLinux()){
            return "/bin/sh -c ";
        }
        else if(OsUtils.isWindows()){
            return "cmd /c ";
        }
        throw new BizException("系统不支持");
    }

    /**
     * 执行结果
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CmdResult{

        /**
         * 是否成功
         */
        private Boolean isSuccess;

        /**
         * 执行返回状态码
         */
        private Integer status;

        /**
         * 执行返回内容
         */
        private String context;

    }

    class inputStreamThread implements Runnable{
        private InputStream ins = null;
        private BufferedReader bfr = null;
        public inputStreamThread(InputStream ins){
            this.ins = ins;
            this.bfr = new BufferedReader(new InputStreamReader(ins));
        }
        @Override
        public void run() {
            String line = null;
            byte[] b = new byte[100];
            int num = 0;
            try {
                while((num=ins.read(b))!=-1){
                    System.out.println(new String(b,"gb2312"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
