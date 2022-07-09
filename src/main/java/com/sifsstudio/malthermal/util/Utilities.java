package com.sifsstudio.malthermal.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.stream.Collector;

public class Utilities {
    @SuppressWarnings("ConstantConditions")
    @Nonnull
    public static <T> T nonnull() {
        return null;
    }

    public static boolean isInRect(int x, int y, int minX, int minY, int maxX, int maxY) {
        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }

    public static boolean neverSpawn(BlockState ignoredState, IBlockReader ignoredWorld, BlockPos ignoredPos, EntityType<?> ignoredEntity) {
        return false;
    }

    public static boolean neverDo(BlockState ignoredState, IBlockReader ignoredWorld, BlockPos ignoredPos) {
        return false;
    }

    public static class Stats<T> {
        private int count;

        private final Comparator<? super T> comparator;
        private T min;
        private T max;

        public Stats(Comparator<? super T> comparator) {
            this.comparator = comparator;
        }

        public int getCount() {
            return count;
        }
        public T getMax() {
            return max;
        }
        public T getMin() {
            return min;
        }

        public void accept(T val) {
            if (this.count == 0) {
                this.min = this.max = val;
            } else if (this.comparator.compare(val, this.min) < 0) {
                this.min = val;
            } else if (this.comparator.compare(val, this.max) > 0) {
                this.max = val;
            }
            this.count++;
        }

        public Stats<T> combine(Stats<T> that) {
            if (this.count == 0) { return that; }
            if (that.count == 0) { return this; }

            this.count += that.count;
            if(this.comparator.compare(that.min, this.min) < 0) {
                this.min = that.min;
            }
            if(this.comparator.compare(that.max, this.max) > 0) {
                this.max = that.max;
            }

            return this;
        }

        public static <T> Collector<T, Stats<T>, Stats<T>> collector(Comparator<? super T> comparator)
        {
            return Collector.of(
                    () -> new Stats<>(comparator),
                    Stats::accept,
                    Stats::combine,
                    Collector.Characteristics.UNORDERED, Collector.Characteristics.IDENTITY_FINISH
            );
        }

        public static <T extends Comparable<? super T>> Collector<T, Stats<T>, Stats<T>> collector()
        {
            return collector(Comparator.naturalOrder());
        }
    }
}
