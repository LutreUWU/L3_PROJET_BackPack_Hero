package fr.uge.manifest;
import java.util.Objects;

public record Container(String bic, int weight, String destination) implements ItemManifest {
  public Container {
	  Objects.requireNonNull(bic, "Le BIC ne doit pas être NULL\n");
	  Objects.requireNonNull(destination, "La destination ne doit pas être NULL\n");
	  if (weight <= 0) throw new IllegalArgumentException("Le poids doit être positif !\n");
  }
  
  @Override
  public int price() {
	  return weight * 2;
  }
  
  @Override
  public String toString() {
	  return bic + " " + weight + "kg to " + destination;
  }
  
  @Override
  public String getDestination() {
	  return destination;
  }
  
  @Override
  public String getId() {return bic;}
  
}
