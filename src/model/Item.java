package model;

public interface Item {
	abstract String toString();
	abstract void use(Hero hero, Object target);
}
