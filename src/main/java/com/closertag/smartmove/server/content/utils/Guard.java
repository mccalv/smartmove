package com.closertag.smartmove.server.content.utils;

/**
 * @author Clint Gilbert
 * 
 * Jun 11, 2009
 *
 * Intelligent Health Lab at the
 * Childrens Hospital Informatics Program at the
 * Harvard-MIT division of Health Sciences Technology
 * @link http://www.chip.org/ihl
 * 
 */
public abstract class Guard
{
    private Guard()
    {
        
    }

    public static final void notNull(final Object o)
    {
        if(o == null)
        {
            throw new IllegalArgumentException("Null argument pssed in");
        }
    }
}
