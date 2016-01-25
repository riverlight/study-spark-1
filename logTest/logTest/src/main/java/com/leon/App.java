package com.leon;

import org.apache.log4j.*;

/**
 * Hello world!
 *
 */
public class App 
{
	
    public static void main( String[] args ) throws InterruptedException
    {
        System.out.println( "Hello World!" );
        
        //PropertyConfigurator.configure("D:\\workroom\\test\\java_workspace\\logTest\\logTest\\log4j.properties");
        //PropertyConfigurator.configure("/mnt/hgfs/share/log4j.properties");
        PropertyConfigurator.configure("/home/hadoop/spark/flume-spark-demo/log4j.properties");
        Logger logger  =  Logger.getLogger(App.class );
        
        int dura, interval, i;
        dura = 6000*100;
        interval = 10;
        i = 0;
        while (i<dura)
        {
        	String str = new String();
        	switch ((int)(Math.random()*5))
        	{
        	case 0:
        		str = "hello";
        		break;
        	case 1:
        		str = "world";
        		break;
        	case 2:
        		str = "spark";
        		break;
        	case 3:
        		str = "hadoop";
        		break;
        	case 4:
        		str = "flume";
        		break;
        	}
        	logger.info(str);
        	Thread.sleep(interval);
        	i+=interval;
        }
        
    }
}
