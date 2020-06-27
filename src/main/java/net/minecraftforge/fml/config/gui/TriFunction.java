package net.minecraftforge.fml.config.gui;

public interface TriFunction<A, B, C, T> {
    T apply(A a, B b, C c);
}
