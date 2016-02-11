/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kurtov.jgrep.searcher;

import java.util.ArrayList;

/**
 *
 * @author mike
 */
public interface Searcher {
    public void search(char[] buffer);
    
    public ArrayList<String> terminate();
    
    public void reset();
}
