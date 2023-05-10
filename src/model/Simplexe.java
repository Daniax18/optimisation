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
    public List<String> unity;                  // 'x1', 'x2', ... ,'xn' 
    public List<String> base;                   // 's1', 's2', .. , 'sn' -> Changing variable 
    public List<Fraction> target;               // Fonction objectifs compose de plusieurs fractions / nombre
    public List<List<Fraction>> contraintes;    // List of all containtes
    public List<Fraction> vector;               // List of the right side of the inegality des containtes
    
   
    
    /**
     * Practice Gauss JOrdan
     * @throws Exception 
     */
    public void gaussJordan() throws Exception{
        try {
            // Initializing...
            this.standardize();
            
            int nature;
            int bland;
            
            // Loop until we have the Maximum
            while(canMaximize() == true){
                nature = pivot_nature();
                bland = pivot_bland();
//                System.out.println("Nature is "+nature);
//                System.out.println("Bland is "+bland);
                exchangeBase(nature, bland);
                
                // Turn pivot into 1 and calcul its line
                List<Fraction> pivot = contraintes.get(nature);
                Fraction dividor = pivot.get(bland);
                for(int p = 0; p < pivot.size(); p++){
                    pivot.set(p, Fraction.divide(pivot.get(p), dividor));
                }
                vector.set(nature, Fraction.divide(vector.get(nature), dividor));
                
                // MAke Jordan
                for(int i = 0; i < contraintes.size(); i++){
                    if(i != nature){
                        List<Fraction> ctes = contraintes.get(i);
                        Fraction mult = ctes.get(bland);            // Coeffincient that make the pivot and a specific line -> 0
                        for(int k = 0; k < ctes.size(); k++){
                            // L2 = L2 - 4*L3
                            Fraction temp = Fraction.minus(ctes.get(k), Fraction.mul(mult, contraintes.get(nature).get(k)));
                            ctes.set(k, temp);
                        }
                        vector.set(i, Fraction.minus(vector.get(i),Fraction.mul(mult, vector.get(nature))));
                    }
                }
           }  
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on Gauss Jordan "+ e.getMessage());
        }   
    }
    
    /**
     * TO know whether we can maximize SImplexe or not
     * @return 
     */
    public boolean canMaximize(){
        List<Fraction> last = contraintes.get(contraintes.size() - 1);      // Get the last liste of fractions
        for(int i = 0; i < last.size(); i++){
            if(last.get(i).isPositive() == true) return true;
        }
        return false;
    }
    
    /**
     * INsert the new UNity in the SImplexe BASE from a new pivot_index
     * @param pivot_index 
     */
    public void exchangeBase(int nature_pivot, int bland_pivot){
        String hors_Base = unity.get(bland_pivot);
        base.set(nature_pivot, hors_Base);
    }
    
      
    /**
     * Get the pivot nature index in the simplexe (COLUMN)
     * @return
     * @throws Exception 
     */
    public int pivot_nature() throws Exception{
        try {        
            int bland_pivot = pivot_bland();
            int count = 0;

            List<Fraction> ctes_colonnes = new ArrayList<>();
            for(int i = 0; i < contraintes.size() - 1; i++){
                ctes_colonnes.add(contraintes.get(i).get(bland_pivot));   // Get the colonne that matched with the bland_pivot from BLAND
              
            }
            
            // Store all calcul from DIVIDING Vector WITH the Column of max Cntaintes for NATURE Method
            double[] values = new double[ctes_colonnes.size()];
            for(int i = 0; i < ctes_colonnes.size(); i++){
                values[i] = Fraction.divide(vector.get(i) , ctes_colonnes.get(i)).value();
            }
            int min = 0;
            
            for(int i = 1; i < values.length; i++){
                if(values[i] > 0 &&  values[min] > values[i]){
                    min = i;
                }
            }
            if(values[min] <= 0) throw new Exception("We can not solve this linear");
            return min;           
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on searching the min Index for NATURE method");
        }    
    }
    
    /**
     * Get the index of BLAND method (LINE)
     * @return 
     */
    public  int pivot_bland(){
        List<Fraction> last = contraintes.get(contraintes.size() - 1);          // Get the last functions (Target)
        int max_index = Fraction.maxIndex(last);                        // Get the highest from BLAND Method
        
        return max_index;
    }
    
    /**
     * Get standardize form from canonical form
     */
    public void standardize(){
        // Get the number of the contraintes in order to get the nombre of Ecart value
        int size = this.contraintes.size();
        this.initialiseBase(size);
        
        for(int k = 0; k < base.size(); k++) unity.add(base.get(k));    // Adding the Base in UNity too
        
        // Add all Ecart Value in each function
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(j == i){
                    contraintes.get(i).add(new Fraction(1));        
                }else{
                    contraintes.get(i).add(new Fraction(0));
                }
            }
            // Fill the objectif function to 0
            target.add(new Fraction(0));
        }
        
        // Add the OBjectif in the COntraintes for calcul
        contraintes.add(target);
        
        // Add the FIRST MAX of the simplexe to 0
        vector.add(new Fraction(0));
    }
    
    /**
     * INitialize the BASE depending on the numbre of containtes and instantiate ECART VARIABLE -> base
     * @param count 
     */
    public void initialiseBase(int count){
        List<String> result = new ArrayList<>();
        
        for(int i = 1; i <= count; i++){
            String temp = "s";
            result.add(temp + i);
        }
        this.base = result;
    }
    
    /**
     * Printing of ctes and its Vector
     */
    public void showContraintes(){
        System.out.println("----- Details --------");
        System.out.print(" - | ");
        for(int j = 0; j < this.unity.size(); j++){      
            System.out.print(this.unity.get(j) + "  |  ");
        }
        System.out.println("");
        
        for(int i = 0; i < this.contraintes.size(); i++){
            if(i != this.contraintes.size() - 1){               // Target doent not have it
                 System.out.print(this.base.get(i) + " | ");
            } else {
                System.out.print(" - |");
            }
           
            for(int j = 0; j < this.contraintes.get(i).size(); j++){
                System.out.print( this.contraintes.get(i).get(j).toString() + " | " );
            }
            System.out.println(" ");
        }
        System.out.println("");
        System.out.println("------------ Vector -----------------");
        
        
        for(int k = 0; k < this.vector.size(); k++){
            System.out.print(this.vector.get(k).toString() + " | ");
        }
        System.out.println("");
        System.out.println("");
        System.out.println("-------------- end details ---------------");
    }
    
     /**
     * Printing the values
     * @return 
     */
    public HashMap<String, Fraction> result(){
        HashMap<String, Fraction> result = new HashMap<>();
        // Adding the result
        result.put("MAXIMUM", vector.get(vector.size() - 1));
        
        // Adding hors base -> MAke them into 0
        for(int i = 0; i < unity.size(); i++){
            if(base.contains(unity.get(i)) == false){
                result.put(unity.get(i), new Fraction(0));
            }
        }
        
        // Adding the base with thier specific value from vector
        for(int j = 0; j < base.size(); j++){
            result.put(base.get(j), vector.get(j));
        }
        return result;
     }
        
    /**
     * SHOWING FINAL RESULT
     * @return 
     */
    public String finalResult(){
        showContraintes();
        HashMap<String, Fraction> result = this.result();
        String test = "(";
        
        for (Map.Entry<String, Fraction> entry : result.entrySet()) {
            String key = entry.getKey();
            Fraction value = entry.getValue();
           // System.out.println(key + " = " + value.toString());
        }
        
        for(int k = 0; k < unity.size(); k++){
            if(result.containsKey(unity.get(k)) == true && k != unity.size() - 1){
                test += result.get(unity.get(k)) + ", ";
            } else {
                test += result.get(unity.get(k));
            }
        }
        
        test += "\n";
        test += "Maximum is " + result.get("MAXIMUM").toString();
        test += ")";
        
        System.out.println("");
        System.out.println("----------------  FINAL RESULT -----------------");
        System.out.println(test);
        return test;
    }
}
