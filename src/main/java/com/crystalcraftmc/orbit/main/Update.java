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

package com.crystalcraftmc.orbit.main;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Update implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		for(int i = 0; i < Orbit.asteroids.size(); i++) {
			//step 1; remove blocks that have changed since last asteroid move
			for(int ii = 0; ii < Orbit.asteroids.get(i).current.size(); ii++) {
				Block test = new Location(Bukkit.getWorld(Orbit.asteroids.get(i).world),
						Orbit.asteroids.get(i).current.get(ii)[0], Orbit.asteroids.get(i).current.get(ii)[1],
						Orbit.asteroids.get(i).current.get(ii)[2]).getBlock();
				boolean removeIt = false;
				if(test.getType() == Material.AIR) {
					removeIt = true;
				}
				else {
					if(test.getTypeId() != Orbit.asteroids.get(i).data.get(ii)[0] ||
							test.getData() != (byte)Orbit.asteroids.get(i).data.get(ii)[1]) {
						removeIt = true;
					}
				}
				
				if(removeIt) {
					Orbit.asteroids.get(i).current.remove(ii);
					Orbit.asteroids.get(i).loc.remove(ii);
					Orbit.asteroids.get(i).data.remove(ii);
					ii--;
				}
			}
			
			//step 2; delete all current asteroid blocks
			for(int ii = 0; ii < Orbit.asteroids.get(i).current.size(); ii++) {
				Block changew = new Location(Bukkit.getWorld(Orbit.asteroids.get(i).world),
						Orbit.asteroids.get(i).current.get(ii)[0], Orbit.asteroids.get(i).current.get(ii)[1], 
						Orbit.asteroids.get(i).current.get(ii)[2]).getBlock();
				changew.setType(Material.AIR);
			}
			
			//step 3; delete the current arraylist
			Orbit.asteroids.get(i).current.clear();
			
			//step 4; place new blocks based off the time
			long dif = System.currentTimeMillis()-Orbit.asteroids.get(i).startTime;
			dif %= Orbit.asteroids.get(i).loopTime;
			dif /= Orbit.asteroids.get(i).loopTime;
			//dif now holds the percentage of of full circle it should be at
			
		}
		
	}
}
