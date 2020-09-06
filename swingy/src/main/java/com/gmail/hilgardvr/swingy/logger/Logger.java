package com.gmail.hilgardvr.swingy.logger;

import java.io.*;
import java.nio.file.*;

public class Logger {
	public static Path file;

	public static void createFile() {
		file = Paths.get("gameData.txt");
		try {
			Files.delete(file);
		} catch (IOException ignore) {}

		try {
			Files.createFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeToFile(String str) {
		try {
            if (str != null) {
                String text = str;
                byte[] strToBytes = text.getBytes();
                Files.write(Logger.file, strToBytes, StandardOpenOption.APPEND);
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}