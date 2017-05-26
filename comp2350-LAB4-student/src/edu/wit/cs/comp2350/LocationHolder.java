package edu.wit.cs.comp2350;

/*
 * An abstract class to insert and retrieve some info about disk locations
 */
public abstract class LocationHolder {

	public abstract DiskLocation find(DiskLocation d);
	public abstract DiskLocation next(DiskLocation d);
	public abstract DiskLocation prev(DiskLocation d);
	public abstract void insert(DiskLocation d);
	public abstract int height();
	
}
