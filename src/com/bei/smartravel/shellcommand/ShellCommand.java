package com.bei.smartravel.shellcommand;

import java.io.IOException;
/**
 * ����ִ��Linux shell ����
 * @author YongKun
 */
public class ShellCommand {
	private Process exeEcho;
	private String myCommand;
	public ShellCommand() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * ִ��Linux shell ����
	 * @param command ������
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
