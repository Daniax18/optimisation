/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Algebra;
import model.Fraction;
import model.Inequation;
import model.Simplexe;

/**
 *
 * @author rango
 */
public class Main {
    public static void main(String[] args) throws Exception{
        try {
            
            // First inequation
            Algebra a1 = new Algebra(new Fraction(6), "a");
            Algebra a2 = new Algebra(new Fraction(4), "b");
            Algebra re1 = new Algebra(new Fraction(13));
            List<Algebra> alg1 = new ArrayList<>();
            alg1.add(a1); alg1.add(a2); 
            Inequation inequation1 = new Inequation(alg1, ">=", re1);
            
            // Second inequation
            Algebra b1 = new Algebra(new Fraction(2), "a");
            Algebra b2 = new Algebra(new Fraction(-3), "b");
            Algebra re2 = new Algebra(new Fraction(0));
            List<Algebra> alg2 = new ArrayList<>();
            alg2.add(b1); alg2.add(b2); 
            Inequation inequation2 = new Inequation(alg2, "<=", re2);
            
            // trhird inequation
            Algebra c1 = new Algebra(new Fraction(2), "a");
            Algebra c2 = new Algebra(new Fraction(2), "b");
            Algebra re3 = new Algebra(new Fraction(16));
            List<Algebra> alg3 = new ArrayList<>();
            alg3.add(c1); alg3.add(c2); 
            Inequation inequation3 = new Inequation(alg3, "<=", re3);
            
            
//            // fourth inequation
            Algebra e1 = new Algebra(new Fraction(0), "a");
            Algebra e2 = new Algebra(new Fraction(1), "b");
            Algebra re5 = new Algebra(new Fraction(4));
            List<Algebra> alg5 = new ArrayList<>();
            alg5.add(e1); alg5.add(e2); 
            Inequation inequation5 = new Inequation(alg5, "<=", re5);
            
            
            //  Fonction objective
            Algebra d1 = new Algebra(new Fraction(3), "a");
            Algebra d2 = new Algebra(new Fraction(2), "b");
            Algebra re4 = new Algebra(new Fraction(0));
            List<Algebra> alg4 = new ArrayList<>();
            alg4.add(d1); alg4.add(d2); 
            Inequation obj = new Inequation(alg4, "=", re4);
            
            List<Inequation> sc = new ArrayList<>();
            sc.add(inequation1); 
            sc.add(inequation2); 
            sc.add(inequation3);
            sc.add(inequation5);
            
            // Simplexe
            Simplexe simplexe = new Simplexe();
            
            // unit
            List<String> unit = new ArrayList<>();
            
            unit.add("a"); unit.add("b");
            simplexe.unity = unit;
            
            // order_unit
            List<String> order_unit = new ArrayList<>();
            order_unit.add("a"); order_unit.add("b"); 
            simplexe.order_unity = order_unit;
            
            // contrsintes et objective
            simplexe.contraintes = sc;
            simplexe.target = obj;
            
            // RESULTAT
            HashMap<String, Fraction> res = simplexe.main_gauss_jordan(1);

             
           //  System.out.println("---------------- Result ----------------------------");
            for (Map.Entry<String, Fraction> entry : res.entrySet()) {
                String key = entry.getKey();
                Fraction value = entry.getValue();
                System.out.println(key + " = " + value.toString());
            }
            
//            simplexe.standardize();
//            
//            HashMap<String, Integer> index_map = new HashMap<>();
//            Inequation target_temp = simplexe.initialize_base(index_map);
//            System.out.println("New target: "+ target_temp.toString());
//            
//            simplexe.show_contraintes();
//            
//            for (Map.Entry<String, Integer> entry : index_map.entrySet()) {
//                target_temp = Inequation.identification(entry.getKey(), target_temp, sc.get(entry.getValue()));
//            }
// 
//            System.out.println("Then the target -> "+target_temp.toString());
//            
//            Simplexe phase1 = new Simplexe();
//            phase1.contraintes = simplexe.contraintes;
//            phase1.base = simplexe.base;
//            phase1.unity = simplexe.unity;
//            phase1.order_unity = simplexe.order_unity;
//            phase1.target = target_temp;
//            phase1.target.algebras = Inequation.orderAlgebraList(phase1.order_unity, phase1.target.algebras);
//            
//            int nature;
//            Algebra trig_algebra = null;
//            System.out.println("-----------------------------------------------------------------");
//             phase1.show_contraintes();
//            
//            while(phase1.can_continue_simplexe(1) == true){         
//                trig_algebra = phase1.target.min_variable();                     // Get min/max in the objective inequation
//                System.out.println("MIn here is "+trig_algebra.toString());
//                nature = phase1.pivot_nature(trig_algebra);                                       // Prendre le pivot parmi les inequations
//                System.out.println("NAture is "+nature);
//                phase1.base.set(nature, trig_algebra.variable);                            // Echange de base
//
//                Inequation main_pivot = phase1.contraintes.get(nature);                         // Inequation pivot
//                main_pivot.pivot_one(trig_algebra.variable);                                // Remise de l'inequation pivot a 1
//
//                phase1.contraintes_jordan(trig_algebra, main_pivot, nature);
//                phase1.target_jordan(trig_algebra, main_pivot);
//            }
//            HashMap<String, Fraction> res = phase1.resultat_simplexe();
//             for (Map.Entry<String, Fraction> entry : res.entrySet()) {
//                String key = entry.getKey();
//                Fraction value = entry.getValue();
//                System.out.println(key + " = " + value.toString());
//            }
//             System.out.println("--------------------------------------");
//             for(int i = 0; i < phase1.unity.size(); i++){
//                 System.out.println(phase1.unity.get(i));
//             }
//
            
            
      //      phase1.show_contraintes();

//// First inequation
//            Algebra a1 = new Algebra(new Fraction(-1), "a");
//            Algebra a2 = new Algebra(new Fraction(-1), "b");
//            Algebra a3 = new Algebra(new Fraction(-1), "c");
//            Algebra re1 = new Algebra(new Fraction(3));
//            List<Algebra> alg1 = new ArrayList<>();
//            alg1.add(a1); alg1.add(a2); alg1.add(a3); 
//            Inequation inequation1 = new Inequation(alg1, "<=", re1);
//            
//            // Seconde inequation
//            Algebra b1 = new Algebra(new Fraction(1), "a");
//            Algebra b2 = new Algebra(new Fraction(-1), "b");
//            Algebra b3 = new Algebra(new Fraction(1), "c");
//            Algebra res2 = new Algebra(new Fraction(4));
//            List<Algebra> alg2 = new ArrayList<>();
//            alg2.add(b1); alg2.add(b2);  alg2.add(b3); 
//            Inequation inequation2 = new Inequation(alg2, "<=", res2);
//            
//            // third inequation
//            Algebra c1 = new Algebra(new Fraction(-1), "a");
//            Algebra c2 = new Algebra(new Fraction(1), "b");
//            Algebra c3 = new Algebra(new Fraction(2), "c");
//            Algebra res3 = new Algebra(new Fraction(1));
//            List<Algebra> alg3 = new ArrayList<>();
//            alg3.add(c1); alg3.add(c2); alg3.add(c3);
//            Inequation inequation3 = new Inequation(alg3, "<=", res3);
//            
////            // fourth inequation
////            Algebra d1 = new Algebra(new Fraction(-1), "a");
////            Algebra d2 = new Algebra(new Fraction(4), "b");
////            Algebra res4 = new Algebra(new Fraction(13));
////            List<Algebra> alg4 = new ArrayList<>();
////            alg4.add(d1); alg4.add(d2);
////            Inequation inequation4 = new Inequation(alg4, "<=", res4);
////            
////            // fifth inequation
////            Algebra e1 = new Algebra(new Fraction(4), "a");
////            Algebra e2 = new Algebra(new Fraction(-1), "b");
////            Algebra res5 = new Algebra(new Fraction(23));
////            List<Algebra> alg5 = new ArrayList<>();
////            alg5.add(e1); alg5.add(e2);
////            Inequation inequation5 = new Inequation(alg5, "<=", res5);
////            
//            // TArget inequation
//            Algebra t1 = new Algebra(new Fraction(-2), "a");
//            Algebra t2 = new Algebra(new Fraction(-3), "b");
//            Algebra t3 = new Algebra(new Fraction(1), "c");
//            Algebra res_target = new Algebra(new Fraction(0), "z");
//            List<Algebra> alg_target = new ArrayList<>();
//            alg_target.add(t1); alg_target.add(t2); alg_target.add(t3);
//            Inequation in_target = new Inequation(alg_target, "=", res_target);
//            
//            List<Inequation> ctes = new ArrayList<>();
//            ctes.add(inequation1);  ctes.add(inequation2); 
//           // ctes.add(inequation3); ctes.add(inequation4); ctes.add(inequation5);
//            
//            
//            Simplexe simplexe = new Simplexe();
//            List<String> unit = new ArrayList<>();
//            unit.add("a"); unit.add("b"); 
//            //unit.add("c");
//            
//            simplexe.contraintes = ctes;
//            simplexe.unity = unit;
//            simplexe.target = in_target;
//         //   simplexe.standardize();
////            simplexe.gaussJordan();
////            
////            for(String s : simplexe.base){
////                System.out.println(s);
////            }
////            
//            HashMap<String, Fraction> res = simplexe.resultat_simplexe(1);
//             for (Map.Entry<String, Fraction> entry : res.entrySet()) {
//                String key = entry.getKey();
//                Fraction value = entry.getValue();
//                System.out.println(key + " = " + value.toString());
//            }
//
//            simplexe.show_contraintes();
           
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
