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

package com.spacestuffmc.orbit.api;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;

public class Utility {
	
	/**Tests whether a String is a valid int value
	 * @return boolean, true if the String is a valid int value
	 */
	public static boolean isInt(String... args) {
		try{
			for(String test : args)
				Integer.parseInt(test);
			return true;
		}catch(NumberFormatException e) { return false; }
	}
	
	/**Tests whether a location is an air block or not
	 * @param loc the location we're testing
	 * @return boolean, true if it's air
	 */
	public static boolean isAir(Location loc) {
		return loc.getBlock().getType() != Material.AIR;
	}
	
	/**Tests whether a double array is contained inside an arraylist (assumes same length array)
	 */
	public static boolean contains(ArrayList<double[]> zone, double[] test, int precision) {
		String dots = "1";
		for(int i = 0; i < precision; i++)
			dots = dots.concat("0");
		double round = Double.parseDouble(dots);
		for(double[] check : zone) {
			boolean isSimilar = true;
			for(int i = 0; i < test.length; i++) {
				if(((double)Math.round(check[i]*round)/round) != ((double)Math.round(test[i]*round)/round)) {
					isSimilar = false;
				}
			}
			if(isSimilar)
				return true;
		}
		return false;
	}
}
