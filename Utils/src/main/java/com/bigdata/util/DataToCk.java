package com.bigdata.util;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataToCk {

    public static void main(String[] args) throws ParseException {
        //定义
        Options options = new Options();
        options.addOption("h", false, "list help");//false代表不强制有
        options.addOption("src", false, "mysql源表格式: db_name.table_name ");//false代表不强制有
        options.addOption("target", false, "clickhouse目标表格式: db_name.table_name");//false代表不强制有
        options.addOption("t", true, "set time on system");
        //解析
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        //查询交互
        if (cmd.hasOption("h")) {
            String formatstr = "CLI  cli  test";
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp(formatstr, "", options, "");
            return;
        }

        if (cmd.hasOption("t")) {
            System.out.printf("system time has setted  %s \n", cmd.getOptionValue("t"));
            return;
        }

        System.out.println("error");
    }

}