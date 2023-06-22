/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;

/**
 *
 * @author rango
 */
public class Fraction {
    public int num;
    public int den;

    /**
     * TO know wether the fraction is strict positive or not
     * @return 
     */
    public boolean isPositive(){
        if(this.value() > 0) return true;
        return false;
    }
    
    public boolean  isNegative(){
        if(this.value() < 0) return true;
        return false;
    }
       
    /**
     * Get the max index from a list of fractions
     * @param fractions
     * @return 
     */
    public static int maxIndex(List<Fraction> fractions){
        int index = 0;
        
        for(int i = 1; i < fractions.size(); i++){
            if(fractions.get(index).value() <  fractions.get(i).value()){
                index = i;
            }
        }
        return index;
    }
    
    
    /**
     * VAlue of the fractions
     * @return 
     */
    public double value(){
        return (double) num/den;
    }
    
    /**
     * Error on dividing two fractions
     * @param fr1
     * @param fr2
     * @return
     * @throws Exception 
     */
    public static Fraction divide(Fraction fr1, Fraction fr2) throws Exception{
         try {
            if(fr1.num == 0 || fr2.num == 0) return new Fraction(0);
            return new Fraction((fr1.num * fr2.den), fr1.den * fr2.num);
        } catch (Exception e) {
            throw new Exception("Error on dividing :"+ e.getMessage());
        }
    }
    
    /**
     * MUltiplication of two fractions
     * @param fr1
     * @param fr2
     * @return
     * @throws Exception 
     */
    public static Fraction mul(Fraction fr1, Fraction fr2) throws Exception{
        try {
            return new Fraction((fr1.num * fr2.num), fr1.den * fr2.den);
        } catch (Exception e) {
            throw new Exception("Error on mul :"+ e.getMessage());
        }
    }
    
    /**
     * MInus of two fractions
     * @param fr1
     * @param fr2
     * @return
     * @throws Exception 
     */
    public static Fraction minus(Fraction fr1, Fraction fr2) throws Exception{
        try {
            return new Fraction((fr1.num*fr2.den - fr2.num*fr1.den), fr1.den*fr2.den);
        } catch (Exception e) {
            throw new Exception("Error on minus :"+ e.getMessage());
        }
    }
    
    /**
     * Sum of two fractions
     * @param fr1
     * @param fr2
     * @return
     * @throws Exception 
     */
    public static Fraction sum(Fraction fr1, Fraction fr2) throws Exception{
        try {
            return new Fraction((fr1.num*fr2.den + fr2.num*fr1.den), fr1.den*fr2.den);
        } catch (Exception e) {
            throw new Exception("Error on sum :"+ e.getMessage());
        }
    }
    
    /**
     * Reduce the fraction
     */
    public void reduce(){
        int gcd = Fraction.pgcd(num, den);
        num = num / gcd;
        den = den / gcd;
        if(num < 0 && den < 0){
            num *= -1;
            den *= -1;
        }
        if(den == -1){
            num *= -1;
            den = 1;
        }
        
        if(den < 0 && num > 0){
            num *= -1;
            den *= -1;
        }
    }
    
    
    /**
     * Get the Great Common Denominator for reducing the fraction
     * @param a
     * @param b
     * @return 
     */
    public static int pgcd(int a, int b){
        int min;
        
        if(Math.abs(a) < Math.abs(b)) min = Math.abs(a);
        else min = Math.abs(b);
        
        for(min = min ; min > 1; min--){
            if(a % min == 0 && b % min == 0) return min;
        }
        return 1;
    }
    
    /**
     * Not a fraction but a simple number
     * @param num 
     */
    public Fraction(int num){
        this.num = num;
        this.den = 1;
    }
    
    /**
     * Constructors with all numerator and denominator given
     * @param num
     * @param den
     * @throws Exception 
     */
    public Fraction(int num, int den) throws Exception{
        if(den == 0) throw  new Exception("Denominator must not 0");
        this.num = num;
        if(num == 0) this.den = 1;
        else this.den = den;
        this.reduce();
    }
    
    @Override
    public String toString(){       
        if(this.den == 1) return String.valueOf(this.num);
        return num+"/"+den;
    }
    
}
