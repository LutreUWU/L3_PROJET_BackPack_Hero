package model.map;

import java.util.List;

import model.XY;

public interface Room {
	public abstract List<XY> get_accessible();
	public abstract void addAccessible(XY coord);
}
