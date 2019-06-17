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

import com.spacestuffmc.orbit.main.Asteroid;
import com.spacestuffmc.orbit.OrbitMain;

import java.io.*;
import java.util.ArrayList;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class AsteroidFile {
	
	
	/**Initializes the asteroids file by reading in
	 * @return ArrayList of Strings that holds asteroid data
	 */
	public static ArrayList<Asteroid> initializeAsteroids() {
		File file = new File("OrbitFiles\\asteroid.txt");
		if(file.exists()) {
			ArrayList<Asteroid> asteroids;
			try{
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				asteroids = (ArrayList<Asteroid>)ois.readObject();
				ois.close();
				fis.close();
				return asteroids;
			}catch(IOException e) { e.printStackTrace(); return new ArrayList<Asteroid>(); 
			}catch(ClassNotFoundException e) { e.printStackTrace(); return new ArrayList<Asteroid>();  }
		}
		else
			return new ArrayList<Asteroid>();
	}
	
	/**Updates the asteroid file to the according ArrayList
	 */
	public static void updateAsteroid() {
		File file = new File("OrbitFiles\\asteroid.txt");
		if(file.exists())
			file.delete();
		if(!new File("OrbitFiles").exists())
			new File("OrbitFiles").mkdir();
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(OrbitMain.asteroids);
			oos.close();
			fos.close();
		}catch(IOException e) { e.printStackTrace(); }
	}
	
	
}
