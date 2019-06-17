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

package com.spacestuffmc.orbit.main;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Asteroid implements Serializable {

	ArrayList<double[]> loc;
	ArrayList<double[]> current;
	ArrayList<int[]> data;
	long startTime;
	long loopTime;
	String world;
	
	public Asteroid(ArrayList<double[]> roid, ArrayList<int[]> data, long loopTime, String world) {
		loc=roid;
		for(double[] zone : roid) {
		}
		this.data=data;
		startTime=System.currentTimeMillis();
		this.loopTime=loopTime;
		this.world=world;
	}
}
