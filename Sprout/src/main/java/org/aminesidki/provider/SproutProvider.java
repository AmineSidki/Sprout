package org.aminesidki.provider;

/**
 * Provides a class with a specific needed configuration
 *
 * @param <T> configured class
 */
public interface SproutProvider<T> {
  T provide();
}
