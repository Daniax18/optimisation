/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author rango
 */
public class Algebra {
    public Fraction coeff;
    public  String variable;
    public boolean isArtificial = false;
    public boolean isEcart = false;
    
    /**
     * Fusionner 2 algebres de meme variables en considerant le poids M
     * @param a
     * @param b
     * @return
     * @throws Exception 
     */
    public static Algebra group_same_variables(Algebra a, Algebra b) throws Exception{
        Algebra result = new Algebra();
        if(a.variable != null && b.variable != null){
            if(a.variable.equals(b.variable) == false){
                throw new Exception("Can  not group 2 algebras with different variables");
            }
            result.variable = a.variable;
        }
        
        result.coeff = Fraction.sum(a.coeff, b.coeff);

        return result;
    }
    /**
     * Check si l'algebre est positive ou non
     * @return 
     */
    public boolean is_positive(){
        return coeff.isPositive(); 
    }
    
        /**
     * Check si l'algebre est negative ou non
     * @return 
     */
    public boolean is_negative(){
        return coeff.isNegative(); 
    }
   
    
    @Override
    public String toString(){
        String co = "";
        if((coeff.toString().equals("1") == true || coeff.toString().equals("-1") == true) && variable == null){
            co = coeff.toString();
        } else if(coeff.toString().equals("1") == false){
            co = coeff.toString();
        } else if(coeff.toString().equals("-1") == true){
            co = "-";
        }
        if(variable == null){
           return "["+ co +"]"; 
        }else {
           return "["+co + variable + "]"; 
        }
    }
    
    // Constructors
    public Algebra(){}
    
    public Algebra(Fraction fraction, String variable){
        this.coeff = fraction;
        this.variable = variable;
    }
    
    public Algebra(Fraction fraction){
        this.coeff = fraction;
        this.variable = null;
    }
    
    public Algebra(String variable){
        this.coeff = new Fraction(1);
        this.variable = variable;
    }
}
