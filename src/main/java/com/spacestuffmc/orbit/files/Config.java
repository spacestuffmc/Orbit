/*
 * Copyright 2019 SpaceStuffMC
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

package com.spacestuffmc.orbit.files;

import com.spacestuffmc.orbit.OrbitMain;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**saves all configurations; reads in from file onEnable(), and
 * updates the config file when any pertanent data is changed in game*/
@SuppressWarnings({"ResultOfMethodCallIgnored", "unused"})
public class Config {
	
	/**Initializes the config settings
	 */
	public static void initializeConfig(OrbitMain o) {
		File file = new File("OrbitFiles\\config.txt");
		if(!file.exists()) {
			if(!new File("OrbitFiles").exists())
				new File("OrbitFiles").mkdir();
			try {
				o.maxRad = 50;
				o.maxSize = 30;
				o.maxAsteroids = 2;
				o.destroy = false;
				o.delay = 3000;
				
				PrintWriter pw = new PrintWriter(file);
				pw.println("Max Radius: 50");
				pw.flush();
				pw.println("Max Size In Blocks: 30");
				pw.flush();
				pw.println("Max Asteroids per Person: 2");
				pw.flush();
				pw.println("Asteroids Destroy What They Hit: false");
				pw.flush();
				pw.println("Time between all asteroids update positions: 3000");
				pw.close();
				
			}catch(IOException e) { e.printStackTrace(); }
		}
		else {
			try{
				Scanner in = new Scanner(file);
				String maxRadStr = in.nextLine();
				String maxSizeStr = in.nextLine();
				String maxAsteroidsStr = in.nextLine();
				String destroyStr = in.nextLine();
				String delayStr = in.nextLine();
				
				in.close();
				
				o.maxRad=Integer.parseInt(maxRadStr.substring(maxRadStr.indexOf(":")+1).trim());
				o.maxSize=Integer.parseInt(maxSizeStr.substring(maxSizeStr.indexOf(":")+1).trim());
				o.maxAsteroids=Integer.parseInt(maxAsteroidsStr.substring(maxAsteroidsStr.indexOf(":")+1).trim());
				o.destroy=Boolean.parseBoolean(destroyStr.substring(destroyStr.indexOf(":")+1).trim());
				o.delay=Integer.parseInt(delayStr.substring(delayStr.indexOf(":")+1).trim());
				
			}catch(IOException e) { e.printStackTrace(); }
		}
	}

	/**Updates the config file
	 */
	public static void updateConfig(OrbitMain o) {
		File file = new File("OrbitFiles\\config.txt");
		if(file.exists())
			file.delete();
		if(!new File("OrbitFiles").exists())
			new File("OrbitFiles").mkdir();
		
		try{
			PrintWriter pw = new PrintWriter(file);
			pw.println("Max Radius: " + o.maxRad);
			pw.flush();
			pw.println("Max Size In Blocks: " + o.maxSize);
			pw.flush();
			pw.println("Max Asteroids per Person: " + o.maxAsteroids);
			pw.flush();
			pw.println("Asteroids Destroy What They Hit: " + o.destroy);
			pw.flush();
			pw.println("Time between all asteroids update positions: " + o.delay);
			pw.flush();
			pw.close();
		}catch(IOException e) { e.printStackTrace(); }
		
	}
	
}
