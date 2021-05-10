/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package me.shedaniel.architectury.utils;

import com.google.common.math.LongMath;

import java.text.DecimalFormat;

public final class Fraction extends Number implements Comparable<Fraction> {
    private static final Fraction[] SIMPLE_CACHE = new Fraction[2048];
    private static final Fraction ZERO = ofWhole(0);
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###.###");
    private final long numerator;
    private final long denominator;
    private boolean simplified;
    
    static {
        for (int i = 0; i < 2048; i++) {
            SIMPLE_CACHE[i] = new Fraction(i - 1024, 1);
        }
    }
    
    private Fraction(long numerator, long denominator) {
        if (denominator > 0) {
            this.numerator = numerator;
            this.denominator = denominator;
        } else if (denominator < 0) {
            this.numerator = -numerator;
            this.denominator = -denominator;
        } else {
            throw new ArithmeticException("/ by zero");
        }
        this.simplified = (this.numerator >= -1 && this.numerator <= 1) || this.denominator == 1;
    }
    
    public static Fraction zero() {
        return ZERO;
    }
    
    public static Fraction ofWhole(long whole) {
        if (whole >= -1024 && whole < 1024) {
            Fraction cached = SIMPLE_CACHE[(int) whole + 1024];
            if (cached != null) {
                return cached;
            }
        }
        return new Fraction(whole, 1);
    }
    
    public static Fraction of(long numerator, long denominator) {
        if (denominator == 1)
            return ofWhole(numerator);
        if (denominator == -1)
            return ofWhole(-numerator);
        return new Fraction(numerator, denominator);
    }
    
    public static Fraction of(long whole, long numerator, long denominator) {
        return of(numerator + whole * denominator, denominator);
    }
    
    public static Fraction from(double value) {
        int whole = (int) value;
        double part = value - whole;
        int i = 1;
        
        while (true) {
            double tem = part / (1D / i);
            long numerator = Math.round(tem);
            if (Math.abs(tem - numerator) < 0.00001) {
                return of(whole, numerator, i);
            }
            i++;
        }
    }
    
    public long getNumerator() {
        return numerator;
    }
    
    public long getDenominator() {
        return denominator;
    }
    
    public Fraction add(Fraction other) {
        if (other.numerator == 0) return this;
        return of(numerator * other.denominator + other.numerator * denominator, denominator * other.denominator);
    }
    
    public Fraction minus(Fraction other) {
        if (other.numerator == 0) return this;
        return of(numerator * other.denominator - other.numerator * denominator, denominator * other.denominator);
    }
    
    public Fraction multiply(Fraction other) {
        if (other.numerator == other.denominator) return this;
        return of(numerator * other.numerator, denominator * other.denominator);
    }
    
    public Fraction divide(Fraction other) {
        if (other.numerator == other.denominator) return this;
        return of(numerator * other.denominator, denominator * other.numerator);
    }
    
    public Fraction inverse() {
        if (numerator == denominator)
            return this;
        Fraction fraction = of(denominator, numerator);
        fraction.simplified = fraction.simplified && this.simplified;
        return fraction;
    }
    
    public Fraction simplify() {
        if (simplified)
            return this;
        if (numerator == 0)
            return ofWhole(0);
        long gcd = LongMath.gcd(Math.abs(numerator), denominator);
        Fraction fraction = of(numerator / gcd, denominator / gcd);
        fraction.simplified = true;
        return fraction;
    }
    
    public boolean isGreaterThan(Fraction fraction) {
        return compareTo(fraction) > 0;
    }
    
    public boolean isLessThan(Fraction fraction) {
        return compareTo(fraction) < 0;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fraction fraction = (Fraction) o;
        return numerator * fraction.denominator == denominator * fraction.numerator;
    }
    
    @Override
    public int hashCode() {
        return Double.hashCode(doubleValue());
    }
    
    @Override
    public int compareTo(Fraction fraction) {
        return Long.compare(numerator * fraction.denominator, denominator * fraction.numerator);
    }
    
    @Override
    public int intValue() {
        return (int) longValue();
    }
    
    @Override
    public long longValue() {
        return numerator / denominator;
    }
    
    @Override
    public float floatValue() {
        return (float) numerator / denominator;
    }
    
    @Override
    public double doubleValue() {
        return (double) numerator / denominator;
    }
    
    public String toDecimalString() {
        return DECIMAL_FORMAT.format(doubleValue());
    }
    
    @Override
    public String toString() {
        if (intValue() == doubleValue()) return toDecimalString();
        return String.format("%s (%d/%d)", toDecimalString(), numerator, denominator);
    }
}
