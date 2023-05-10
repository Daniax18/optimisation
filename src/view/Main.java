/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.util.ArrayList;
import model.Fraction;
import model.Simplexe;
import java.util.List;

/**
 *
 * @author rango
 */
public class Main {
    public static void main(String[] args) {
        try {
            Simplexe simplexe = new Simplexe();
            
            // Unity of the simplexe
            List<String> unit = new ArrayList<>();
            unit.add("x1"); unit.add("x2"); unit.add("x3");
            simplexe.unity = unit;
            
            // Target
            List<Fraction> targ = new ArrayList<>();
            targ.add(new Fraction(5));
            targ.add(new Fraction(4));
            targ.add(new Fraction(3));
            simplexe.target = targ;
            
            // Contraintes
            List<Fraction> c1 = new ArrayList<>();
            c1.add(new Fraction(2));
            c1.add(new Fraction(3));
            c1.add(new Fraction(1));
            
            List<Fraction> c2 = new ArrayList<>();
            c2.add(new Fraction(4));
            c2.add(new Fraction(1));
            c2.add(new Fraction(2));
            
            List<Fraction> c3 = new ArrayList<>();
            c3.add(new Fraction(3));
            c3.add(new Fraction(4));
            c3.add(new Fraction(2));
            
//            List<Fraction> c4 = new ArrayList<>();
//            c4.add(new Fraction(1));
//            c4.add(new Fraction(3));
//            c4.add(new Fraction(-1));
            
            List<List<Fraction>> ctes = new ArrayList<>();
            ctes.add(c1); ctes.add(c2); ctes.add(c3); 
//            ctes.add(c4);
            simplexe.contraintes = ctes;
            
            // Vector result
            List<Fraction> vect = new ArrayList<>();
            vect.add(new Fraction(5));
            vect.add(new Fraction(11));
            vect.add(new Fraction(8));
            //vect.add(new Fraction(2));
            simplexe.vector = vect;
            
            // ---------------------------------- ////
//            simplexe. standardize();
            simplexe.gaussJordan();
            
            
            for(int a = 0; a < simplexe.base.size(); a++){
              //  System.out.print(simplexe.base.get(a) + " | " );
            }
            
            simplexe.finalResult();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
         
         
    }
    
}
