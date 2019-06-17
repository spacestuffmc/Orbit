/*
 * Copyright 2015 CrystalCraftMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.crystalcraftmc.orbit.files;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**Reads in list of players with perms upon onEnable(),
 * and updates file when a player is added/removed from list*/
public class Perms {
	
	/**Initializes the Perms file by reading in
	 * @return ArrayList of Strings that holds permsList
	 */
	public static ArrayList<String> initializePerms() {
		File file = new File("OrbitFiles\\perms.txt");
		if(file.exists()) {
			ArrayList<String> permsList = new ArrayList<String>();
			try{
				Scanner in = new Scanner(file);
				while(in.hasNext()) {
					permsList.add(in.nextLine());
				}
				in.close();
				return permsList;
			}catch(IOException e) { e.printStackTrace(); return new ArrayList<String>(); }
		}
		else
			return new ArrayList<String>();
	}
	
	/**Updates the Perms file to the according ArrayList
	 * @param ArrayList of String that we're overwritting the permsfile with
	 */
	public static void updatePerms(ArrayList<String> str) {
		File file = new File("OrbitFiles\\perms.txt");
		if(!new File("OrbitFiles").exists())
			new File("OrbitFiles").mkdir();
		try {
			PrintWriter pw = new PrintWriter(file);
			for(String r : str) {
				pw.println(r);
				pw.flush();
			}
			pw.close();
		}catch(IOException e) { e.printStackTrace(); }
	}
}
