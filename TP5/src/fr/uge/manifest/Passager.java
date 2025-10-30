package fr.uge.manifest;

import java.util.Objects;

public record Passager(String name, String destination) implements ItemManifest {
	public Passager {
		Objects.requireNonNull(name, "Le nom ne peut pas être Null\n");
		Objects.requireNonNull(destination, "La destination ne peut pas être Null\n");
	}
	
	@Override
	  public int price() {
		  return 10;
	  }
	
	@Override
	public String toString() {
	  return name + " to " + destination;
	}
	
	@Override
	public String getDestination() {
	  return destination;
	}
	
	@Override
	  public String getId() {return name;}
}
