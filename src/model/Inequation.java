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
public class Inequation {

    public List<Algebra> algebras = new ArrayList<>();
    public String operator;
    public Algebra result_algebra = new Algebra();
    
    /**
     * Order a list of algebra
     * @param stringList
     * @param algebraList
     * @return 
     */
    public static List<Algebra> orderAlgebraList(List<String> stringList, List<Algebra> algebraList) {
        Map<String, Algebra> algebraMap = new HashMap<>();
        List<Algebra> orderedList = new ArrayList<>();

        // Create a map of variable names to Algebra objects
        for (Algebra algebra : algebraList) {
            algebraMap.put(algebra.variable, algebra);
        }

        // Order the algebra objects based on the order of the strings
        for (String variable : stringList) {
            Algebra algebra = algebraMap.get(variable);
            if (algebra != null) {
                orderedList.add(algebra);
            } else{
                orderedList.add(new Algebra(new Fraction(0), variable));
            }
        }
        return orderedList;
    }
    
    
    
    /**
     * 3x +5c +2y = 5
     * y = 3x + 2c
     * 1er etap => 2*(3x + 2c)
     * 2eme etap => 3x +5c + 6x + 4c = 5
     * @param variable_trig
     * @param to_insert 
     */
    public static Inequation identification(String variable_trig, Inequation to_insert, Inequation main_inequation) throws Exception{
        try {
             Inequation clone = main_inequation.clone_inequation(main_inequation);
             Inequation result = null;
             Algebra algebre_main_eq = clone.variable_in(variable_trig);          // See if the variable exist in the inequation "main_equation"
            if(algebre_main_eq != null){
            
                Inequation isolation = clone.isolation_variable(variable_trig);   // 
              //  System.out.println("ISo is "+isolation.toString());
                Algebra algebre_in_insert = to_insert.variable_in(variable_trig);
                
                if(algebre_in_insert != null){
                    result = new Inequation();
                    result.setResult_algebra(to_insert.getResult_algebra());
                    result.operator = "=";
                    List<Algebra> replace = exchange_variable_to_algebras(algebre_in_insert, isolation);
                    
                    for(int k = 0; k < to_insert.algebras.size(); k++){
                        if(algebre_in_insert.equals(to_insert.algebras.get(k)) == false){
                            result.algebras.add(to_insert.algebras.get(k));
                        } 
                    }   
                    
                    for(int i = 0; i < replace.size(); i++){
                        result.algebras.add(replace.get(i));
                    }
             //       System.out.println("Before grouping: "+result.toString());
                    result.group_same_variable();
                }   else {
                    return to_insert;
                }
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on identification method. Error: "+e.getMessage());
        }
    }
    
    /**
     * 3x +5c +2y = 5
     * y = 3x + 2c
     * 1er etap => 2*(3x + 2c)
     * @param main_variable
     * @param origin_inequation
     * @return
     * @throws Exception 
     */
    public static List<Algebra> exchange_variable_to_algebras(Algebra main_variable, Inequation origin_inequation) throws Exception{
        try {
            List<Algebra> result = new ArrayList<>();
            for(int i = 0; i < origin_inequation.algebras.size(); i++){
                Fraction new_value = Fraction.mul(origin_inequation.algebras.get(i).coeff, main_variable.coeff);
                result.add(new Algebra(new_value, origin_inequation.algebras.get(i).variable));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on exchanging an algebras to an inequation");
        }
    }
    
    /**
     * Grouper les algebres qui ont les memes variables
     * @throws Exception 
     */
    public void group_same_variable() throws Exception{
        try {
            HashMap<String, Algebra> temp_values = new HashMap<>();
        
            for(int i = 0; i < algebras.size(); i++){
                String var = algebras.get(i).variable;
                if(var == null){
                    var = "temp";
                }
                if(temp_values.containsKey(var) == false){
                    temp_values.put(var, algebras.get(i));
                } else {
                    Algebra to_add = temp_values.get(var);
                    Algebra value_to_add = algebras.get(i);
                    Algebra new_value = Algebra.group_same_variables(to_add, value_to_add);
                    temp_values.replace(var, new_value);
                }
            }
            algebras.clear();
            for (Map.Entry<String, Algebra> entry : temp_values.entrySet()) {
                algebras.add(entry.getValue());
            }
            group_same_variable_result();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on grouping same variable. "+e.getMessage());
        }
    }
    
    /**
     * Treat the resultat_algebra in case of grouping all algebra
     * @throws Exception 
     */
    public void group_same_variable_result() throws Exception{
        String var = result_algebra.variable;
        if(var == null) result_algebra.variable = "temp";
        Integer index_same = null;
        for(int i = 0; i < algebras.size(); i++){
            Algebra algebra = algebras.get(i);
            if(algebra.variable == null) algebra.variable = "temp";
            
            if(algebra.variable.equals(result_algebra.variable) == true){
                index_same = i;
            }
        }
        
        if(index_same != null){
            Fraction temporary = Fraction.mul(new Fraction(-1), algebras.get(index_same).coeff);
            result_algebra.coeff = Fraction.sum(temporary, result_algebra.coeff);
            algebras.remove(algebras.get(index_same));
        }
        if(result_algebra.variable.equals("temp") == true) result_algebra.variable = null;
    }
    
    /**
     * Isolation d'une variable -> partie result
     * @param variable_to_isolate
     * @return
     * @throws Exception 
     */
    public Inequation isolation_variable(String variable_to_isolate) throws Exception{
        try {
            Inequation result = clone_inequation(this);                                 // CLoning the inequation
            Algebra variable_value = result.variable_in(variable_to_isolate);       // Check the variable in the inequation
            if(variable_value == null) throw new Exception("Variable "+ variable_to_isolate +" + not found");   
            
            boolean positive = variable_value.is_positive();                // Check the sign of the reearche variable
            Integer to_remove = null;                                       // will be the index of the algebra to remove
            for(int i = 0; i < result.algebras.size(); i++){
                Algebra algebra = result.algebras.get(i);
                
                if(i != result.algebras.indexOf(variable_value)){  // 
                    Fraction old = algebra.coeff;
                    Fraction minus = minus = Fraction.mul(new Fraction(-1), old);
                    algebra.coeff = Fraction.divide(minus, variable_value.coeff);
                } else{
                    to_remove = i;
                }
            }
            // AJout du resultat dans algebras
            // 2x = 3 -> x = 3/2  
            result.algebras.remove(result.algebras.get(to_remove));         // Remove the algebra trigger itselg in the algebras inequation
            Fraction for_result = Fraction.divide(result_algebra.coeff, variable_value.coeff);
            result.algebras.add(new Algebra(for_result, result_algebra.variable));      // Result manaraka signe anah variable_value
            result.result_algebra = new Algebra(new Fraction(1), variable_value.variable);
            
            return result;   
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on isolating the variable. "+e.getMessage());
        }
    }
    
    /**
     * CLoner une inequation pour eviter les pointeurs 
     * @param inequation
     * @return 
     */
    public static Inequation clone_inequation(Inequation inequation) throws Exception{
        List<Algebra> clone_al = new ArrayList<>();
        for(int i = 0; i < inequation.algebras.size(); i++){
            Fraction farction = new Fraction(inequation.algebras.get(i).coeff.num, inequation.algebras.get(i).coeff.den);
            String var = inequation.algebras.get(i).variable;
            Algebra algebra = new Algebra(farction, var);
            algebra.isArtificial = inequation.algebras.get(i).isArtificial;
            algebra.isEcart = inequation.algebras.get(i).isEcart;
            clone_al.add(algebra);
        }
        String op = inequation.operator;
        Fraction for_res = new Fraction(inequation.result_algebra.coeff.num, inequation.result_algebra.coeff.den);
        Algebra res = new Algebra(for_res, inequation.result_algebra.variable);
        
        Inequation result = new Inequation(clone_al, op, res);
        return result;
    }
    
    /**
     * Remise en 1 de la fonction pivot
     * divisions des autres coefficients par le coefficient pivot
     * et Rendre le coefficient du pivot -> 0 
     * @param variable_pivot
     * @throws Exception 
     */
    public void pivot_one(String variable_pivot) throws Exception{
        Algebra alg_dividor = variable_in(variable_pivot);
        
        try {
            if(alg_dividor != null){
                Fraction dividor = alg_dividor.coeff;
                for(Algebra al : algebras){
                    Fraction new_coeff = Fraction.divide(al.coeff, dividor);
                    al.coeff = new_coeff;
                }

                result_algebra.coeff  = Fraction.divide(result_algebra.coeff, dividor);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on pivot_one. Error: "+e.getMessage());
        }
    }
    
    
    /**
     * Prendre toutes les valeurs dans une liste d'inequations ou la variable est egale a variable_to_find(ex: x)
     * @param variable_to_find
     * @param inequations
     * @return 
     */
    public static Fraction[] variable_values(String variable_to_find, List<Inequation> inequations){
        Fraction[] result = new Fraction[inequations.size()];
        int index = 0;
        
        for(Inequation in : inequations){
            Algebra temp = in.variable_in(variable_to_find);
            
            if(temp == null) result[index] = new Fraction(0);
            else result[index] = temp.coeff;
            index++;
        }
        return result;
    }
    
    
    /**
     * Retourne la variable qui a le max de coefficient dans l'inequation et strictement positive
     * @return 
     */
    public Algebra max_variable(){
        int index_max = 0;
        for(int i = 0; i < algebras.size(); i++){
            if(algebras.get(i).is_positive() == true){
                 index_max = i;
                 break;
            }
        }
        
        for(int i = 0; i < algebras.size(); i++){
            if(algebras.get(index_max).coeff.value() <  algebras.get(i).coeff.value()){
                index_max = i;
            }
        }
        return algebras.get(index_max);
    }
    
    /**
     * Get the minimum algebra in an equation and get its strict negative algebre
     * @return 
     */
    public Algebra min_variable(){
        int index_max = 0;
        for(int i = 0; i < algebras.size(); i++){
            if(algebras.get(i).is_negative() == true){
                 index_max = i;
                 break;
            }
        }
        
        for(int i = 0; i < this.getAlgebras().size(); i++){
            if(this.getAlgebras().get(index_max).coeff.value() >  algebras.get(i).coeff.value()){
                index_max = i;
            }
        }
        return algebras.get(index_max);
    }
    
    /**
     * Pour voir la premiere base de l'equation: Priorisation de la variable artificielle, apres ecart
     * @return 
     */
    public Algebra first_base(){
        for(Algebra al : algebras){
            if(al.isArtificial == true) return al;
        }
        
        for(Algebra al : algebras){
            if(al.isEcart == true) return al;
        }
        return null;
    }
    
    
    /**
     * Verifie que la variable existe dans l'inequation -> Retourne sa valeur / null
     * @param variable_to_find
     * @return 
     */
    public Algebra variable_in(String variable_to_find) {
        for(Algebra al : algebras){
            if(al.variable.equals(variable_to_find) == true) return al;
        }
        return null;
    }
    
    @Override
    public String toString(){
        String res = "";
        for(Algebra al : algebras){
            res += "+"+al.toString();
        }
        res += " "+ operator + " "+ result_algebra;
        return res;
    }
    
    // Constructors
    public Inequation(){}
    
    public Inequation(List<Algebra> algebras, String operator, Algebra result_algebra) {
        this.algebras = algebras;
        this.operator = operator;
        this.result_algebra = result_algebra;
    }

    public List<Algebra> getAlgebras() {
        return algebras;
    }

    public void setAlgebras(List<Algebra> algebras) {
        this.algebras = algebras;
    }

    public Algebra getResult_algebra() {
        return result_algebra;
    }

    public void setResult_algebra(Algebra result_algebra) {
        this.result_algebra = result_algebra;
    }
    
    
}
