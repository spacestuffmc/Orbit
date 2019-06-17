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

package com.crystalcraftmc.orbit.api;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;

public class Utility {
	
	/**Tests whether a String is a valid int value
	 * @param String varargs, the string(s) we're testing
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
		if(loc.getBlock().getType() == Material.AIR)
			return true;
		else
			return false;
	}
	
	/**Tests whether a double array is contained inside an arraylist (assumes same length array)
	 * @param the arraylist we're looking in
	 * @param the double array we're checking
	 * @param the number of decimal points we're rounding to
	 * @return boolean, true if the arraylist contains the double array
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
