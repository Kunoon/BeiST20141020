package com.bei.smartravel.shellcommand;

import java.io.IOException;
/**
 * ÓÃÓÚÖ´ĞĞLinux shell ÃüÁî
 * @author YongKun
 */
public class ShellCommand {
	private Process exeEcho;
	private String myCommand;
	public ShellCommand() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Ö´ĞĞLinux shell ÃüÁî
	 * @param command ÃüÁî×Ö
	 */
	public void ExecuteCommand(String command) {
		myCommand = String.format(command + "\n");
        try {
            exeEcho = Runtime.getRuntime().exec("su");
            exeEcho.getOutputStream().write(myCommand.getBytes());
            exeEcho.getOutputStream().flush();
        } catch (IOException e) {
            System.out.println("Excute exception: " + e.getMessage());
        }
	}
}
