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

import com.crystalcraftmc.orbit.api.Utility;
import com.crystalcraftmc.orbit.files.AsteroidFile;
import com.crystalcraftmc.orbit.files.Config;
import com.crystalcraftmc.orbit.files.Perms;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Orbit extends JavaPlugin implements Listener {
	
	//#################config variables###################
	/**max asteroid radius*/
	public double maxRad;
	/**max asteroid size*/
	public int maxSize;
	/**max asteroid per person*/
	public int maxAsteroids;
	/**whether asteroid destroys other objects upon impact*/
	public boolean destroy;
	/**How often asteroids are updated*/
	public int delay;
	//#################config variables###################
	
	/**Holds existing asteroids*/
	public static ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
	
	/**List of non op people with perms to modify aspects of Orbit & overcome limitations*/
	public ArrayList<String> permsList = new ArrayList<String>();
	
	/**A way to nicely format floating point numbers*/
	public DecimalFormat df = new DecimalFormat("#0.00");
	
	/**Thread to start global update clock*/
	public Thread t1;
	
	public void onEnable() {
		Config.initializeConfig(this);
		permsList = Perms.initializePerms();
		asteroids = AsteroidFile.initializeAsteroids();
		getServer().getPluginManager().registerEvents(this, this);
		t1 = new Thread(new UpdateThread(delay));
		t1.start();
	}
	public void onDisable() {
		try{
			t1.join();
		}catch(InterruptedException e) { e.printStackTrace(); }
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
		
		
		return true;
	}
	
	
	@EventHandler
	public void startOrbit(SignChangeEvent e) {
		if(e.getLine(0) != null && e.getLine(1) != null) {
			if(e.getLine(0).length() >= 1 && e.getLine(1).length() >= 1) {
				if(e.getLine(0).startsWith("Orbit") || e.getLine(0).startsWith("orbit")) {
					Player p = e.getPlayer();
					String line = e.getLine(0);
					int firstP = line.indexOf("(");
					int secondP = line.indexOf(")");
					if(firstP < secondP && firstP != -1) {
						StringTokenizer st = new StringTokenizer(line.substring(firstP+1, secondP),
								", ", false);
						if(st.countTokens() == 3) {
							String t1 = st.nextToken();
							String t2 = st.nextToken();
							String t3 = st.nextToken();
							String t4 = e.getLine(1);
							String adminTrait = e.getLine(2);
							boolean isInvincible = false;
							boolean isPhase = false;
							if(adminTrait == null) {
							}
							else if(adminTrait.equalsIgnoreCase("-i") && hasPerms(p)) {
								isInvincible = true;
							}
							else if(adminTrait.equalsIgnoreCase("-p") && hasPerms(p)) {
								isPhase = true;
							}
							if(t4 == null) {
								p.sendMessage(ChatColor.YELLOW + "Error; the second line must list how long " +
										"a full orbit takes in minutes.  Use /orbithelp for more information.");
								return;
							}
							if(!Utility.isInt(t4)) {
								p.sendMessage(ChatColor.YELLOW + "Error; the second line must list how long " +
										"a full orbit takes in minutes (no decimals).  Use /orbithelp for more information.");
								return;
							}
							if(Integer.parseInt(t4) < 1) {
								p.sendMessage(ChatColor.YELLOW + "Error; the second line that specifies time (minutes) " +
										"for a full orbit must be greater than 0.");
								return;
							}
							if(Utility.isInt(t1, t2, t3)) {
								int x = Integer.parseInt(t1);
								int y = Integer.parseInt(t2);
								int z = Integer.parseInt(t3);
								int time = Integer.parseInt(t4);
								double rad = Math.sqrt((x*x)+(y*y)+(z*z));
								if(rad < maxRad || hasPerms(p)) {
									Block startBlock = null;
									if(e.getBlock().getLocation().add(0, -1, 0).getBlock().getType() !=
											Material.AIR) {
										startBlock = e.getBlock().getLocation().add(0, -1, 0).getBlock();
									}
									else {
										e.getPlayer().sendMessage(ChatColor.YELLOW + "Error; there must be a block " +
												"under the sign.");
										return;
									}
									
									ArrayList<int[]> data = new ArrayList<int[]>();
									ArrayList<double[]> paths = new ArrayList<double[]>();
									paths.add(new double[]{startBlock.getX(), startBlock.getY(), startBlock.getZ()});
									ArrayList<double[]> newPaths = new ArrayList<double[]>();
									newPaths.add(new double[]{startBlock.getX(), startBlock.getY(), startBlock.getZ()});
									ArrayList<double[]> newPaths2 = new ArrayList<double[]>();
									while(newPaths.size() > 0) {
										ArrayList<double[]> toDel = new ArrayList<double[]>();
										for(double[] zone : newPaths) {
											toDel.add(new double[]{zone[0], zone[1], zone[2]});
										}
										/*  ArrayList<double[]> al1 = new ArrayList<double[]>();
											al1.add(new double[]{2, 3, 4});
											(al1.contains(new double[]{2, 3, 4}))
											above boolean returns false; hence the need for a deep check
										*/
										for(double[] zone : newPaths) {
											if(!Utility.isAir(new Location(p.getWorld(), zone[0], zone[1]+1, zone[2])) &&
													!Utility.contains(paths, new double[]{zone[0], zone[1]+1, zone[2]}, 1)) {
												newPaths2.add(new double[]{zone[0], zone[1]+1, zone[2]});
												paths.add(new double[]{zone[0], zone[1]+1, zone[2]});
												data.add(new int[]{new Location(p.getWorld(), zone[0], zone[1]+1, 
														zone[2]).getBlock().getTypeId(), 
														new Location(p.getWorld(), zone[0], zone[1]+1, 
																zone[2]).getBlock().getData()});
											}
											if(!Utility.isAir(new Location(p.getWorld(), zone[0], zone[1]-1, zone[2])) &&
													!Utility.contains(paths, new double[]{zone[0], zone[1]-1, zone[2]}, 1)) {
												newPaths2.add(new double[]{zone[0], zone[1]-1, zone[2]});
												paths.add(new double[]{zone[0], zone[1]-1, zone[2]});
												data.add(new int[]{new Location(p.getWorld(), zone[0], zone[1]-1, 
														zone[2]).getBlock().getTypeId(), 
														new Location(p.getWorld(), zone[0], zone[1]-1, 
																zone[2]).getBlock().getData()});
											}
											if(!Utility.isAir(new Location(p.getWorld(), zone[0]+1, zone[1], zone[2])) &&
													!Utility.contains(paths, new double[]{zone[0]+1, zone[1], zone[2]}, 1)) {
												newPaths2.add(new double[]{zone[0]+1, zone[1], zone[2]});
												paths.add(new double[]{zone[0]+1, zone[1], zone[2]});
												data.add(new int[]{new Location(p.getWorld(), zone[0]+1, zone[1], 
														zone[2]).getBlock().getTypeId(), 
														new Location(p.getWorld(), zone[0]+1, zone[1], 
																zone[2]).getBlock().getData()});
											}
											if(!Utility.isAir(new Location(p.getWorld(), zone[0]-1, zone[1], zone[2])) &&
													!Utility.contains(paths, new double[]{zone[0]-1, zone[1], zone[2]}, 1)) {
												newPaths2.add(new double[]{zone[0]-1, zone[1], zone[2]});
												paths.add(new double[]{zone[0]-1, zone[1], zone[2]});
												data.add(new int[]{new Location(p.getWorld(), zone[0]-1, zone[1], 
														zone[2]).getBlock().getTypeId(), 
														new Location(p.getWorld(), zone[0]-1, zone[1], 
																zone[2]).getBlock().getData()});
											}
											if(!Utility.isAir(new Location(p.getWorld(), zone[0], zone[1], zone[2]+1)) &&
													!Utility.contains(paths, new double[]{zone[0], zone[1], zone[2]+1}, 1)) {
												newPaths2.add(new double[]{zone[0], zone[1], zone[2]+1});
												paths.add(new double[]{zone[0], zone[1], zone[2]+1});
												data.add(new int[]{new Location(p.getWorld(), zone[0], zone[1], 
														zone[2]+1).getBlock().getTypeId(), 
														new Location(p.getWorld(), zone[0], zone[1], 
																zone[2]+1).getBlock().getData()});
											}
											if(!Utility.isAir(new Location(p.getWorld(), zone[0], zone[1], zone[2]-1)) &&
													!Utility.contains(paths, new double[]{zone[0], zone[1], zone[2]-1}, 1)) {
												newPaths2.add(new double[]{zone[0], zone[1], zone[2]-1});
												paths.add(new double[]{zone[0], zone[1], zone[2]-1});
												data.add(new int[]{new Location(p.getWorld(), zone[0], zone[1], 
														zone[2]-1).getBlock().getTypeId(), 
														new Location(p.getWorld(), zone[0], zone[1], 
																zone[2]-1).getBlock().getData()});
											}
										}
										for(double[] zone : newPaths2)
											newPaths.add(zone);
										newPaths2.clear();
										ArrayList<double[]> toDel2 = new ArrayList<double[]>();
										for(double[] zone : newPaths) {
											if(Utility.contains(toDel, zone, 1)) {
												toDel2.add(zone);
											}
										}
										for(double[] zone : toDel2) {
											newPaths.remove(zone);
										}
										if(paths.size() > maxSize) {
											p.sendMessage(ChatColor.YELLOW + "Error; your asteroid is too big. Max size " +
													"is " + ChatColor.BLUE + String.valueOf(maxSize) + ChatColor.YELLOW + ".");
											return;
										}
									}
									p.sendMessage(ChatColor.BLUE + "Detected craft-size: " + ChatColor.GOLD +
											String.valueOf(paths.size()));
									//TODO
									AsteroidTrait trait;
									if(isInvincible)
										trait=AsteroidTrait.INVINCIBLE;
									else if(isPhase)
										trait=AsteroidTrait.PHASE;
									else if(destroy)
										trait=AsteroidTrait.NORMAL;
									else
										trait=AsteroidTrait.BRITTLE;
									asteroids.add(new Asteroid(paths, data, time*60000, trait, p.getWorld().getName()));
									AsteroidFile.updateAsteroid();
									p.sendMessage(ChatColor.GOLD + "Your asteroid is now in orbit.");
								}
								else {
									e.getPlayer().sendMessage(ChatColor.YELLOW + "Error; radius of your asteroid is " +
											ChatColor.RED + df.format(rad) + ChatColor.YELLOW + ", must be below " +
											ChatColor.BLUE + String.valueOf(maxRad) + ChatColor.YELLOW + ".");
								}
							}
							else {
								e.getPlayer().sendMessage(ChatColor.YELLOW + "Error; illegal formating. Use /orbithelp " +
										"to learn more.");
							}
						}
						else {
							e.getPlayer().sendMessage(ChatColor.YELLOW + "Error; illegal formating. Use /orbithelp " +
									"to learn more.");
						}
					}
				}
			}
		}
	}
	
	/**Checks if someone has perms (either op or on permsList)
	 * @param p the player we're checking
	 * @return boolean true if they have permissions
	 */
	public boolean hasPerms(Player p) {
		if(p.isOp())
			return true;
		String name = p.getName();
		for(String str : permsList) {
			if(str.equals(name))
				return true;
		}
		return false;
	}
}
