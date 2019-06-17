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

import java.io.Serializable;
import java.util.ArrayList;

public class Asteroid implements Serializable {

	public ArrayList<double[]> loc;
	public ArrayList<double[]> current;
	public ArrayList<int[]> data;
	public long startTime;
	public long loopTime;
	public AsteroidTrait trait;
	public String world;
	
	public Asteroid(ArrayList<double[]> roid, ArrayList<int[]> data, long loopTime, AsteroidTrait trait, String world) {
		loc=roid;
		for(double[] zone : roid)
			current.add(new double[]{zone[0], zone[1], zone[2]});
		this.data=data;
		startTime=System.currentTimeMillis();
		this.loopTime=loopTime;
		this.trait=trait;
		this.world=world;
	}
}
