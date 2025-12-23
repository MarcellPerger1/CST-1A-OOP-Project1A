package uk.ac.cam.mp2226.part1a.oop.proj1a;

@FunctionalInterface
public interface ThrowingSupplier<R, E extends Throwable> {
    R get() throws E;
}
