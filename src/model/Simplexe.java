/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rango
 */
public class Simplexe {
    public List<String> unity = new ArrayList<>();
    public List<String> order_unity = new ArrayList<>();
    public List<Inequation> contraintes;
    public List<String> base = new ArrayList<>();
    public Inequation target;
    
    /**
     * Resultat du simplexe en HAshmap
     * @return
     * @throws Exception 
     */
    public HashMap<String, Fraction> resultat_simplexe() throws Exception{
        try {
        
            HashMap<String, Fraction> result = new HashMap<>();
            result.put("z", target.result_algebra.coeff);
            
            // Tous les hors bases == 0
            for(int i = 0; i < order_unity.size(); i++){
                if(base.contains(order_unity.get(i)) == false){
                    result.put(order_unity.get(i), new Fraction(0));
                }
            }

            // Les autres, prendre des valeurs resultats de chaque contraintes
            for(int j = 0; j < base.size(); j++){
                result.put(base.get(j), contraintes.get(j).result_algebra.coeff);
            }
                
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting simplexe result. "+e.getMessage());
        }
    }
    
    
    
    public HashMap<String, Fraction> main_gauss_jordan(int trigger) throws Exception{
        try {
            standardize();
            HashMap<String, Integer> index_map = new HashMap<>();
            Inequation trig_phase = initialize_base(index_map);     // New target function if there is artificial variable
            
            if(trig_phase == null){
                
                 HashMap<String, Fraction> result = simple_gauss_jordan(this, trigger);
            
//                System.out.println("------------- Contraintes  ------------------------");
//                 show_contraintes();
//
//                 System.out.println("-------------- TARGET ---------------------------");
//                 System.out.println("Target: "+target.toString());
//
//                 System.out.println("------------- BASE --------------------- ");
//                 for(int i = 0; i < base.size(); i++){
//                     System.out.print(base.get(i) + " - ");
//                 }
//                 System.out.println("");
//                 System.out.println("------------------------------------------");
                return result;
            } else{
                for (Map.Entry<String, Integer> entry : index_map.entrySet()) {
                    trig_phase = Inequation.identification(entry.getKey(), trig_phase, this.contraintes.get(entry.getValue()));
                }
            }
            return two_phase(this, trig_phase, trigger);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error in main Gauss jordan. "+ e.getMessage() );
        }
    }
    /**
     * Simple gauss without 2 phases
     * @param simplexe
     * @param trigger
     * @return
     * @throws Exception 
     */
    public static HashMap<String, Fraction> simple_gauss_jordan(Simplexe simplexe, int trigger) throws Exception{
        try {
            int nature;
                Algebra trig_algebra = null;
                while(simplexe.can_continue_simplexe(trigger) == true){
                    if(trigger == 0) trig_algebra = simplexe.target.max_variable();           
                    else if(trigger == 1) trig_algebra = simplexe.target.min_variable();                     // Get min/max in the objective inequation
                    nature = simplexe.pivot_nature(trig_algebra);                                       // Prendre le pivot parmi les inequations
                    simplexe.base.set(nature, trig_algebra.variable);                            // Echange de base

                    Inequation main_pivot = simplexe.contraintes.get(nature);                         // Inequation pivot
                    main_pivot.pivot_one(trig_algebra.variable);                                // Remise de l'inequation pivot a 1

                    simplexe.contraintes_jordan(trig_algebra, main_pivot, nature);
                    simplexe.target_jordan(trig_algebra, main_pivot);
                }
                return simplexe.resultat_simplexe();
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error in simple gauss_jordan. Error:  "+e.getMessage());
        }
    }   
    
    /**
     * If there are artificialle variabales, then we have to make two phase
     * @param main_simplexe
     * @param debut_target
     * @param trigger
     * @return
     * @throws Exception 
     */
    public HashMap<String, Fraction> two_phase(Simplexe main_simplexe, Inequation debut_target, int trigger) throws Exception{
        try {
            Simplexe phase1 = new Simplexe();
            phase1 = Simplexe.clone_simplexe(main_simplexe);
            phase1.target = debut_target;
            phase1.target.algebras = Inequation.orderAlgebraList(main_simplexe.order_unity, phase1.target.algebras);
            
            
            HashMap<String, Fraction> temp_result = simple_gauss_jordan(phase1, 1);
            if(temp_result.get("z").value() != 0){
                throw new Exception("None solution because the sum of artificial variables is differente of 0");
            } else{
                phase1.delete_artificial_variable();
                main_simplexe.delete_artificial_variable();
            }
            String decision = phase1.decision_variable_in_base();       // Get decision variable in the base
            List<Integer> index_not_in = new ArrayList<>();
            for(int i = 0; i < phase1.base.size(); i++){
                if(phase1.base.get(i).equals(decision) == false){       // Loop phase1 base
                    for(int k = 0; k < main_simplexe.contraintes.size(); k++){      // Loop all main_simplexe contraintes to see all inequation that has phase1 base variable not null
                        Algebra test = main_simplexe.contraintes.get(k).variable_in(phase1.base.get(i));
                        if(test != null && test.coeff.value() != 0){
                            index_not_in.add(k);            // Get all inequation that contains variable , autre que decision, in base
                            break;
                        } 
                    }
                }
            }

            
            Integer index_main = null;      // Get the indx of the LIst<INequation> that have to be in identification with the decision variable
            for(int i = 0; i < main_simplexe.contraintes.size(); i++){
                if(index_not_in.contains(i) == false){
                    Inequation inequation = main_simplexe.contraintes.get(i);
                    Algebra algebra = inequation.variable_in(decision);
                    if(algebra != null && algebra.coeff != null){
                        index_main = i;
                    }
                }
            }
       //     System.out.println("TArget is "+phase1.target.toString());
        //    System.out.println(main_simplexe.contraintes.get(index_main).toString());
            phase1.target = Inequation.identification(decision, main_simplexe.target, main_simplexe.contraintes.get(index_main));
            phase1.target.algebras = Inequation.orderAlgebraList(main_simplexe.order_unity, phase1.target.algebras);
       //     System.out.println("----------------------------------------------------------------");
        //     System.out.println("TArget is "+phase1.target.toString());
            HashMap<String, Fraction> result = simple_gauss_jordan(phase1, trigger);
            
//            System.out.println("------------- Contraintes  ------------------------");
//             phase1.show_contraintes();
//             
//             System.out.println("-------------- TARGET ---------------------------");
//             System.out.println("Target: "+phase1.target.toString());
//             
//             System.out.println("------------- BASE --------------------- ");
//             for(int i = 0; i < phase1.base.size(); i++){
//                 System.out.print(phase1.base.get(i) + " - ");
//             }
//             System.out.println("");
//             System.out.println("------------------------------------------");
            return result;
            
            
           // return temp_result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on making the phase II. Error: "+e.getMessage());
        }
    }
    
    /**
     * Get the decision variable in the base
     * @return 
     */
    public String decision_variable_in_base(){
        for(int i = 0; i < base.size(); i++){
            if(unity.contains(base.get(i)) == true){
                return base.get(i);
            }
        }
        return null;
    }
    
    /**
     * Delete all artificial variable
     */
    public void delete_artificial_variable(){
        // DElete in all contraintes
        for(int i = 0; i < contraintes.size(); i++){
            Inequation inequation = contraintes.get(i);
            for(int k = 0; k < inequation.algebras.size(); k++){
                if(inequation.algebras.get(k).variable.contains("A") == true){
                    inequation.algebras.remove(inequation.algebras.get(k));
                }
            }
        }
         // Idem for target inequation
         for(int i = 0; i < target.algebras.size(); i++){
            if(target.algebras.get(i).variable.contains("A") == true){
                  target.algebras.remove(target.algebras.get(i));
              }
         }
         // idem for base
         for(int i = 0; i < base.size(); i++){
             if(base.get(i).contains("A") == true){
                 base.remove(base.get(i));
             }
         }
         
         // idem for order
         for(int i = 0; i < order_unity.size(); i++){
             if(order_unity.get(i).contains("A") == true){
                 order_unity.remove(order_unity.get(i));
             }
         }
    }
    
    
    /**
     * Gauss jordan des contraintes par raport a:
     * @param coefficient_target : la maximum parmi les coefficients de la fonction ovbjective
     * @param pivot_inequation : LA nouvelle inequation de la fonction pivot(efa misy 1)
     * @param index_pivot_nature : L'indice de pivot_nature pour eviter de traiter cette inequation
     * @throws Exception 
     */
    public void contraintes_jordan(Algebra coefficient_target, Inequation pivot_inequation, int index_pivot_nature) throws Exception{
        try {
            for(int i = 0; i < contraintes.size(); i++){                                    // Loop toutes les inequations
                if(i != index_pivot_nature){                                                // Sauf le pivot
                    Inequation main_cte = contraintes.get(i);                           // TRaitement de chaque contraintes
                    Algebra alg_multiplication = contraintes.get(i).variable_in(coefficient_target.variable);    // Prendre la valeur qui a meme variable que le pivot dans chaque inequation
                    Fraction coeff_multiplication = alg_multiplication.coeff;               // Prendre le coefficient multiplication -> [2]
                    if(alg_multiplication != null){                                         // Si null -> On laisse l'inequation car deja 0
                        for(int k = 0; k < main_cte.algebras.size(); k++){
                            Fraction from_main = pivot_inequation.algebras.get(k).coeff;  // fraction from inequation pivot -> [L2]
                            Fraction main_alg = main_cte.algebras.get(k).coeff;         // fraction de l'actuel contraintes  -> [L1]

                            // L2 = L2 - 2*L1
                            Fraction new_value = Fraction.minus(main_alg, Fraction.mul(coeff_multiplication, from_main));
                            main_cte.algebras.get(k).coeff = new_value;                 // Set la nouvelle fraction
                        }
                        Algebra resultat_cte = contraintes.get(i).result_algebra;                // Idem pour le resultat
                        Fraction resultat_cte_coeff = resultat_cte.coeff;
                        Fraction new_resultat_cte = Fraction.minus(resultat_cte_coeff, Fraction.mul(coeff_multiplication, pivot_inequation.result_algebra.coeff));
                        resultat_cte.coeff = new_resultat_cte;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on contraintes jordan." +e.getMessage());
        }
    }
    
    /**
     * Idem que contraintes jordan mais avec la fonction objective
     * @param coefficient_target
     * @param pivot_inequation
     * @throws Exception 
     */
    public void target_jordan(Algebra coefficient_target, Inequation pivot_inequation) throws Exception{
        try {
            Algebra alg_multiplication = target.variable_in(coefficient_target.variable);     // Traiter de meme pour la fonction objective
            if(alg_multiplication != null){
                Fraction multiplication = alg_multiplication.coeff;
                for(int k = 0; k < target.algebras.size(); k++){
                    Fraction from_main = pivot_inequation.algebras.get(k).coeff;
                    Fraction cte_target = target.algebras.get(k).coeff;
                    
                    // L2 = L2 - 3*L1
                    Fraction new_value = Fraction.minus(cte_target, Fraction.mul(multiplication, from_main));
                    target.algebras.get(k).coeff = new_value;
                }
                Algebra resultat_cte = target.result_algebra;
                Fraction resultat_cte_coeff = resultat_cte.coeff;
                Fraction new_resultat_cte = Fraction.minus(resultat_cte_coeff, Fraction.mul(multiplication, pivot_inequation.result_algebra.coeff));
                resultat_cte.coeff = new_resultat_cte;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error in targe jordan. "+e.getMessage());
        }
    }
    
    /**
     * Checker si on peut encore maximiser//minimiser en fonction des coefficients de la fonction objective
     * @return 
     */
    public boolean can_continue_simplexe(int trig){ // 0 -> Maximise // 1 -> Minimise
        if(trig == 0){
            for(int i = 0; i < target.algebras.size(); i++){
                if(target.algebras.get(i).is_positive() == true) return true;
            }
            return false;
        } else if(trig == 1){
            for(int i = 0; i < target.algebras.size(); i++){
                if(target.algebras.get(i).is_negative()== true) return true;
            }
            return false;
        }
        return false;
    }
    
    
    /**
     * Prendre l'index du pivot (Column) en fonction de la maximum/minimum coefficient de la fonction objectif
     * @return
     * @throws Exception 
     */
    public int pivot_nature(Algebra focus) throws Exception{
        try {
            Fraction[] same_variable = Inequation.variable_values(focus.variable, contraintes);    // Prendre toutes les valeurs qui ont les memes variables

            double[] toCompare = new double[same_variable.length];
            for(int i = 0; i < same_variable.length; i++){
                toCompare[i] = Fraction.divide(contraintes.get(i).result_algebra.coeff, same_variable[i]).value();   // A diviser par les resultats d'equation
            }
            int min = 0;
            for(int i = 0; i < toCompare.length; i++){
                if(toCompare[i] > 0){                                             // Get the firs algebre that is > 0
                    min = i;                                                     // use it for seaking the pivot_nature
                    break;
                }
            }
            for(int i = 1; i < toCompare.length; i++){
                if(toCompare[i] > 0 &&  toCompare[min] > toCompare[i]){                                             // Comparaison, prendre le minimum
                    min = i;
                }
            }
            if(toCompare[min] <= 0) throw new Exception("We can not solve this linear");                    // Si le minimum est <= 0, on ne peut pas resoudre
            return min;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on searching the min Index for NATURE method");
        }
            
    }
    
    /**
     * Inserer les premieres bases de simplexe
     */
    public Inequation initialize_base(HashMap<String, Integer> base_index_contraintes){
        Inequation target_temp = new Inequation();
        int trig_nul = 0;                                   // Verify if there is artificial vartiable of not
        for(int i = 0; i < contraintes.size(); i++){
            Inequation in = contraintes.get(i);
            Algebra base_of_inequation = in.first_base();   // get the algebra for base
            base.add(base_of_inequation.variable);
            if(base_of_inequation.isArtificial == true){
                trig_nul += 1;
                base_index_contraintes.put(base_of_inequation.variable, i); // If artificial, we have to save the index amd the string
                target_temp.algebras.add(new Algebra(base_of_inequation.variable)); // add it to the target_temp inequation algebras
            }
        }
        if(trig_nul == 0) return null;
        target_temp.operator = "=";
        target_temp.result_algebra = new Algebra(new Fraction(0));
        return target_temp;
    }
    
    /**
     * Standartisation de la forme canonique
     */
    public void standardize(){
        for(int i = 0; i < contraintes.size(); i++){
            String op = contraintes.get(i).operator;
            String s = "s";
            String a = "A";
            if(op.equals("<=") == true || op.equals(">=") == true){     // Ajouter les variables d'ecart
                s += i;
                Algebra ecart = new Algebra(new Fraction(1), s);            // ecart + pour les inf
                ecart.isEcart = true;
                
                if(op.equals(">=") == true){        
                    ecart.coeff = new Fraction(-1);                               // ecart - pour les sup
                }
                contraintes.get(i).algebras.add(ecart);
                add_specific_unity(s, i, true);           // Ajout de la variable d'ecart pour les autres contraintes
          //      unity.add(s);                                                       // AJout dans unity
                order_unity.add(s);
            } 
            
            if(op.equals(">=") == true || op.equals("=") == true) {     // AJouter les variables artificielles
                a += i;
                Algebra artificial = new Algebra(new Fraction(1), a);       // Toujours + pour les contraintes
                artificial.isArtificial = true;
                
                contraintes.get(i).algebras.add(artificial);
                add_specific_unity(a, i, false);                    // Ajout de la variable artificielle pour les autres contraintes
             //   unity.add(a);                                                      // AJouter dans unity
                order_unity.add(a);
            }
        }
    }
    
    /**
     * Ajout d;une variable d'ecart ou artificielle a valeur null pour toutes les contraintes
     * @param new_unity
     * @param not_null_value_index 
     */
    public void add_specific_unity(String new_unity, int not_null_value_index, boolean for_target){
        for(int k = 0; k < contraintes.size(); k++){
            if(k != not_null_value_index){
                contraintes.get(k).algebras.add(new Algebra(new Fraction(0), new_unity));
            }
        }
        if(for_target == true){
            target.algebras.add(new Algebra(new Fraction(0), new_unity));
        }
            
    }
     
    /**
     * Displaying all contraintes
     */
    public void show_contraintes(){
        System.out.println("-------------------d-----------------");
        System.out.println("----Contraintes ------------");
        for(Inequation in : contraintes){
            System.out.println(in.toString());
        }
        System.out.println("------ Objectif -----------");
        System.out.println(target.toString());
        System.out.println("-------------------e-----------------");
    }
    
    public static Simplexe clone_simplexe(Simplexe main_simplexe) throws Exception{
        try {
             Simplexe simplexe = new Simplexe();
            List<String> for_unity = new ArrayList<>();
            // unity
            for(int i = 0; i < main_simplexe.unity.size(); i++){
                for_unity.add(main_simplexe.unity.get(i));
            }

            // order unity
            List<String> for_order = new ArrayList<>();
            for(int i = 0; i < main_simplexe.order_unity.size(); i++){
                for_order.add(main_simplexe.order_unity.get(i));
            }

            // INequation
            List<Inequation> for_inequation = new ArrayList<>();
            for(int i = 0; i < main_simplexe.contraintes.size(); i++){
                Inequation temp = Inequation.clone_inequation(main_simplexe.contraintes.get(i));
                for_inequation.add(temp);
            }

            // base
            List<String> for_base = new ArrayList<>();
            for(int i = 0; i < main_simplexe.base.size(); i++){
                for_base.add(main_simplexe.base.get(i));
            }

            // target
            Inequation for_target = Inequation.clone_inequation(main_simplexe.target);

            simplexe.base = for_base;
            simplexe.contraintes = for_inequation;
            simplexe.order_unity = for_order;
            simplexe.unity = for_unity;
            simplexe.target = for_target;
            return simplexe;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on clonning simplexe");
        }
    }
}
