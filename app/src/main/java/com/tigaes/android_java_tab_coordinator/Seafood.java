package com.tigaes.android_java_tab_coordinator;

public class Seafood {
    String strMeal, strMealThumb, idMeal;

    public Seafood(String idMeal, String strMeal, String strMealThumb) {
        this.strMeal = strMeal;
        this.strMealThumb = strMealThumb;
        this.idMeal = idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public String getIdMeal() {
        return idMeal;
    }
}
