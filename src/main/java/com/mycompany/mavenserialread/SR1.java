package com.mycompany.mavenserialread;
/*
 *  RXTX binary builds provided as a courtesy of Fizzed, Inc. (http://fizzed.com/).
 *  Please see http://fizzed.com/oss/rxtx-for-java for more information.
 */
import gnu.io.CommPortIdentifier;        
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
 
public class SR1 implements Runnable, SerialPortEventListener 
{
   static CommPortIdentifier    portId;
   static CommPortIdentifier    saveportId;
   static Enumeration<?>        portList;
   static InputStream           inputStream;
   static SerialPort            serialPort;
   Thread                       readThread;
   static OutputStream          outputStream;
   static boolean               outputBufferEmptyFlag = false;
 
   public static void launchSR1(String sPort, int[] sr1params) {		

	  boolean           portFound = false;
      System.out.println("Connecting to "+sPort);
       
        // parse ports and if the default port is found, initialized the reader
      portList = CommPortIdentifier.getPortIdentifiers();
      while (portList.hasMoreElements()) {
         portId = (CommPortIdentifier) portList.nextElement();
         if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            if (portId.getName().equals(sPort)) {
               System.out.println("Found port: "+sPort);
               portFound = true;
               // init reader thread
               @SuppressWarnings("unused")
               SR1 reader = new SR1(sr1params);
               setConnected = true;
            } 
         } 
          
      } 
      if (!portFound) {
         System.out.println("port " + sPort + " not found.");
      } 
       
   } 
 
   public void initWriteToPort() {
      // initwritetoport() assumes that the port has already been opened and
      //    initialized by "public SR1()"
 
      try {
         // get the outputstream
         outputStream = serialPort.getOutputStream();
      } catch (IOException e) {}
 
      try {
         // activate the OUTPUT_BUFFER_EMPTY notifier
         serialPort.notifyOnOutputEmpty(true);
      } catch (Exception e) {
         System.out.println("Error setting event notification");
         System.out.println(e.toString());
         System.exit(-1);
      }
       
   }
 
   public static void writeToPort(String msg) {
 //     System.out.println("Writing \""+msg+"\" to "+serialPort.getName());
      try {
         // write string to serial port
         outputStream.write(msg.getBytes());
      } catch (IOException e) {}
   }
 static public Boolean setConnected = false;
 
   public static void disconnect()
   {
       //close the serial port
       try
       {
    	   if(setConnected) {
           serialPort.removeEventListener();
           serialPort.close();
           inputStream.close();
           outputStream.close();
           setConnected = false;

           System.out.println("Disconnected");
    	   }
       }
       catch (Exception e)
       {
    	   System.out.println("Failed to close " + serialPort.getName());
       }
   }
   
   
   public SR1(int[] args) {

      // initialize serial port
      try {
         serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
      } catch (PortInUseException e) {}
    
      try {
         inputStream = serialPort.getInputStream();
      } catch (IOException e) {}
    
      try {
         serialPort.addEventListener(this);
      } catch (TooManyListenersException e) {}
       
      // activate the DATA_AVAILABLE notifier
      serialPort.notifyOnDataAvailable(true);
    
      try {
         // set port parameters
         serialPort.setSerialPortParams(args[0], args[1], args[2], args[3]);
      } catch (UnsupportedCommOperationException e) {System.out.println("UnsupportedCommOperationException");}
       
      // start the read thread
      readThread = new Thread(this);
      readThread.start();
      setConnected = true;
       
   }
 
   public void run() {
      // first thing in the thread, we initialize the write operation
      initWriteToPort();
      try {
         while (true) {
            // write string to port, the serialEvent will read it
//            writetoport("Ping");
            Thread.sleep(1000);
         }
      } catch (InterruptedException e) {}
   } 
 
   public void serialEvent(SerialPortEvent event) {
//       System.out.println(".............."+event.getEventType());
      switch (event.getEventType()) {
      case SerialPortEvent.BI:
          System.out.println("BI");
      case SerialPortEvent.OE:
          System.out.println("OE");
      case SerialPortEvent.FE:
          System.out.println("FE");
      case SerialPortEvent.PE:
          System.out.println("PE");
      case SerialPortEvent.CD:
          System.out.println("CD");
      case SerialPortEvent.CTS:
          System.out.println("CTS");
      case SerialPortEvent.DSR:
          System.out.println("DSR");
      case SerialPortEvent.RI:
          System.out.println("RI");
      case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
  //        System.out.println("OUTPUT_BUFFER_EMPTY");
          break;
      case SerialPortEvent.DATA_AVAILABLE:
         // Data waiting
         byte[] readBuffer = new byte[1000];
         try {
            // read data
            if (inputStream.available() > 0) {
               @SuppressWarnings("unused")
			int numBytes = inputStream.read(readBuffer);
            } 
            // print data
            String result  = new String(readBuffer);
            System.out.println(result.trim());
         } catch (IOException e) {System.out.println("IO exception");}
    
         break;
      }
   } 
 
    public static void updatePorts(boolean firstRun)
    {
        //Ports Check
        @SuppressWarnings("unchecked")
	java.util.Enumeration<CommPortIdentifier> portIdentifiers = CommPortIdentifier.getPortIdentifiers();
        
        int p = 0;
        CommPortIdentifier portId = null; // set if port is found
        while (portIdentifiers.hasMoreElements()) {
        	CommPortIdentifier pid = (CommPortIdentifier) portIdentifiers.nextElement();
        	if(pid.getPortType() == CommPortIdentifier.PORT_SERIAL) {
        		portId = pid;
        		SR1gui.availablePorts[p] = pid.getName();
        		SR1gui.selectedPort = pid.getName();
        		if(!firstRun) {
                            if(!(((String) pid.getName()).equals((String) SR1gui.PortBox1.getSelectedItem())))
        			SR1gui.PortBoxModel.addElement((String) pid.getName());
        			SR1gui.Panel.revalidate();
        			SR1gui.Panel.repaint();
        		}
        		p++;
        		SR1gui.isPortAvailable = true;
        	}
        }
        if(portId == null) {
        	if(firstRun) {
        		System.err.println("Could not find serial ports ");
        		SR1gui.isPortAvailable = false;
        	}
                else {System.err.println("Could not find serial ports ");}
        }
    }

}