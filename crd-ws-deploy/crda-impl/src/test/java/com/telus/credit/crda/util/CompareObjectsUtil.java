package com.telus.credit.crda.util;

import static org.junit.Assert.assertFalse;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;


public class CompareObjectsUtil
{
    private static final Map<Class, Boolean> _customEquals = new ConcurrentHashMap<Class, Boolean>();
    private static final Map<Class, Boolean> _customHash = new ConcurrentHashMap<Class, Boolean>();
    private static final Map<Class, Collection<Field>> _reflectedFields = new ConcurrentHashMap<Class, Collection<Field>>();
    
    
    public static class CompRslt
    {
    	public boolean isCompareRslt() {
			return compareRslt;
		}
		public Object getFieldName() {
			return fieldName;
		}
		public Object getValue1() {
			return value1;
		}
		public Object getValue2() {
			return value2;
		}
		private final boolean compareRslt ;
    	private final Object fieldName;
    	private final Object value1;
    	private final Object value2;
        private CompRslt(Object afieldName, Object aValue1, Object aValue2,boolean aCompareRslt)
        {
        	fieldName = afieldName;
        	value1=aValue1;
        	value2 = aValue2;
        	
        	compareRslt=aCompareRslt;
        }
        
        
        
        public String toString(){
        	return "FieldName=" + getFieldName() + 
			": Value1=" + getValue1() + 
			" <> " + 
			": Value2=" + getValue2();
        }
    }
    private static class DualKey
    {
        private final Object _key1;
        private final Object _key2;

        
        private final Object _key1_FieldName;

        private DualKey(Object k1, Object k2,Object aFieldName)
        {
            _key1 = k1;
            _key2 = k2;
            _key1_FieldName=aFieldName;
  
        }
        
        public boolean equals(Object other)
        {
            if (other == null)
            {
                return false;
            }
            
            if (!(other instanceof DualKey))
            {
                return false;
            }
            
            DualKey that = (DualKey) other;
            return _key1 == that._key1 && _key2 == that._key2;
        }
        
        public int hashCode()
        {
            int h1 = _key1 != null ? _key1.hashCode() : 0;
            int h2 = _key2 != null ? _key2.hashCode() : 0;
            return h1 + h2;
        }
    }


    
    public static ArrayList<CompRslt> traversingCompare(Object a, Object b,ArrayList filterNameList) {

    	ArrayList<CompRslt> mismatchList =new ArrayList<CompRslt>();
        Set visited = new HashSet<DualKey>();
        LinkedList<DualKey> stack = new LinkedList<DualKey>();
        stack.addFirst(new DualKey(a, b,""));

        while (!stack.isEmpty())
        {            
            DualKey dualKey = stack.removeFirst();        
            visited.add(dualKey);


            localprint("FieldName=" + dualKey._key1_FieldName + " value1=" + dualKey._key1 + " , value2=" + dualKey._key2);
            
            
            if (dualKey._key1 == dualKey._key2)
            {   // Same instance is always equal to itself.
            	//localprint("Same instance is always equal to itself");
            	//localprint("FieldName=" + dualKey._key1_FieldName + " value1=" + dualKey._key1 + " , value2=" + dualKey._key2);
            	 
            	
            	 
                continue;
            }
            
            if (dualKey._key1 == null || dualKey._key2 == null)
            {   // If either one is null, not equal (both can't be null, due to above comparison).
            	//localprint(" either one is null, not equal (both can't be null)");
            	//localprint("FieldName=" + dualKey._key1_FieldName + " value1=" + dualKey._key1 + " , value2=" + dualKey._key2);

                //return false;
            	addToMistmachList(mismatchList, dualKey);  
                continue;
            }
                            
            if (!dualKey._key1.getClass().equals(dualKey._key2.getClass()))
            {   // Must be same class
            	//localprint("Are not of same class");
            	//localprint("FieldName=" + dualKey._key1_FieldName + " value1=" + dualKey._key1 + " , value2=" + dualKey._key2);
 
            	addToMistmachList(mismatchList, dualKey);            	 
            }
            
            // Handle all [] types.  In order to be equal, the arrays must be the same 
            // length, be of the same type, be in the same order, and all elements within
            // the array must be deeply equivalent.
            if (dualKey._key1.getClass().isArray())
            {
                if (!compareArrays(dualKey._key1, dualKey._key2, stack, visited))
                {
 
                	addToMistmachList(mismatchList, dualKey); 
                }
                continue;
            }
            
            // Special handle SortedSets because they are fast to compare because their
            // elements must be in the same order to be equivalent Sets.
            if (dualKey._key1 instanceof SortedSet)
            {
                if (!compareOrderedCollection((Collection) dualKey._key1, (Collection) dualKey._key2, stack, visited))
                {
                	addToMistmachList(mismatchList, dualKey); 
                }
                continue;
            }
            
            // Handled unordered Sets.  This is a slightly more expensive comparison because order cannot
            // be assumed, a temporary Map must be created, however the comparison still runs in O(N) time.
            if (dualKey._key1 instanceof Set)
            {
                if (!compareUnorderedCollection((Collection) dualKey._key1, (Collection) dualKey._key2, stack, visited))
                {
                	addToMistmachList(mismatchList, dualKey); 
                }
                continue;
            }
            
            // Check any Collection that is not a Set.  In these cases, element order
            // matters, therefore this comparison is faster than using unordered comparison.
            if (dualKey._key1 instanceof Collection)
            {
                if (!compareOrderedCollection((Collection) dualKey._key1, (Collection) dualKey._key2, stack, visited))
                {
                	addToMistmachList(mismatchList, dualKey); 
                }                                
                continue;
            }
            
            // Compare two SortedMaps.  This takes advantage of the fact that these
            // Maps can be compared in O(N) time due to their ordering.
            if (dualKey._key1 instanceof SortedMap)
            {
                if (!compareSortedMap((SortedMap) dualKey._key1, (SortedMap) dualKey._key2, stack, visited))
                {
                	addToMistmachList(mismatchList, dualKey); 
                }
                continue;
            }
            
            // Compare two Unordered Maps. This is a slightly more expensive comparison because
            // order cannot be assumed, therefore a temporary Map must be created, however the
            // comparison still runs in O(N) time.
            if (dualKey._key1 instanceof Map)
            {
                if (!compareUnorderedMap((Map) dualKey._key1, (Map) dualKey._key2, stack, visited))
                {
                	addToMistmachList(mismatchList, dualKey); 
                }
                continue;
            }
            
            if (hasCustomEquals(dualKey._key1.getClass()))
            {
                if (!dualKey._key1.equals(dualKey._key2))
                {
                	addToMistmachList(mismatchList, dualKey);
                }
                continue;
            }        
            
            Collection<Field> fields = getDeepDeclaredFields(dualKey._key1.getClass());               
            
            for (Field field : fields){
            	
                try
                {

                    DualKey dk = new DualKey(field.get(dualKey._key1), field.get(dualKey._key2), field.getName());
                    if(field.getName().equalsIgnoreCase("latestInvoluntaryCancelledDate")){
                    	System.out.println( "   field.get(dualKey._key1="  + field.get(dualKey._key1+ 
                    						" , field.get(dualKey._key2)=" + field.get(dualKey._key2) +
                    						" , field.getName()="          + field.getName()
                    						));
                    }
                    	
                    if (!visited.contains(dk))
                    {
                    	
                    	if (! filterNameList.contains(field.getName())){
                    		stack.addFirst(dk);
                    	}
                    }
                }
                catch (Exception ignored)
                { }
            }
        }

        //return true;
        return mismatchList; 
    }

	private static void addToMistmachList(ArrayList<CompRslt> CompRsltList,
			DualKey dualKey) {
		CompRsltList.add (new CompRslt(dualKey._key1_FieldName, dualKey._key1,dualKey._key2,false));
	}

    private static void localprint(String string) {
    	System.out.println(string);
		
	}

    private static boolean compareArrays(Object array1, Object array2, LinkedList stack, Set visited)
    {
        // Same instance check already performed...

        int len = Array.getLength(array1);
        if (len != Array.getLength(array2))
        {
            return false;
        }

        for (int i = 0; i < len; i++)
        {
            DualKey dk = new DualKey(Array.get(array1, i), Array.get(array2, i),"");
            if (!visited.contains(dk))
            {   // push contents for further comparison
                stack.addFirst(dk);
            }
        }
        return true;
    }


    private static boolean compareOrderedCollection(Collection col1, Collection col2, LinkedList stack, Set visited)
    {
        // Same instance check already performed...

        if (col1.size() != col2.size())
        {
            return false;
        }
                        
        Iterator i1 = col1.iterator();
        Iterator i2 = col2.iterator();
        
        while (i1.hasNext())
        {
            DualKey dk = new DualKey(i1.next(), i2.next(),"");
            if (!visited.contains(dk))
            {   // push contents for further comparison
                stack.addFirst(dk);
            }
        }
        return true;
    }

    private static boolean compareUnorderedCollection(Collection col1, Collection col2, LinkedList stack, Set visited)
    {
        // Same instance check already performed...

        if (col1.size() != col2.size())
        {
            return false;
        }

        Map fastLookup = new HashMap();
        for (Object o : col2)
        {
            fastLookup.put(deepHashCode(o), o);
        }

        for (Object o : col1)
        {
            Object other = fastLookup.get(deepHashCode(o));
            if (other == null)
            {   // Item not even found in other Collection, no need to continue.
                return false;
            }

            DualKey dk = new DualKey(o, other,"");
            if (!visited.contains(dk))
            {   // Place items on 'stack' for further comparison.
                stack.addFirst(dk);
            }
        }
        return true;
    }



    private static boolean compareSortedMap(SortedMap map1, SortedMap map2, LinkedList stack, Set visited)
    {
        // Same instance check already performed...

        if (map1.size() != map2.size())
        {
            return false;
        }

        Iterator i1 = map1.entrySet().iterator();
        Iterator i2 = map2.entrySet().iterator();

        while (i1.hasNext())
        {
            Map.Entry entry1 = (Map.Entry)i1.next();
            Map.Entry entry2 = (Map.Entry)i2.next();

            // Must split the Key and Value so that Map.Entry's equals() method is not used.
            DualKey dk = new DualKey(entry1.getKey(), entry2.getKey(),"");
            if (!visited.contains(dk))
            {   // Push Keys for further comparison
                stack.addFirst(dk);
            }

            dk = new DualKey(entry1.getValue(), entry2.getValue(),"");
            if (!visited.contains(dk))
            {   // Push values for further comparison
                stack.addFirst(dk);
            }
        }
        return true;
    }


    private static boolean compareUnorderedMap(Map map1, Map map2, LinkedList stack, Set visited)
    {
        // Same instance check already performed...

        if (map1.size() != map2.size())
        {
            return false;
        }

        Map fastLookup = new HashMap();

        for (Map.Entry entry : (Set<Map.Entry>)map2.entrySet())
        {
            fastLookup.put(deepHashCode(entry.getKey()), entry);
        }

        for (Map.Entry entry : (Set<Map.Entry>)map1.entrySet())
        {
            Map.Entry other = (Map.Entry)fastLookup.get(deepHashCode(entry.getKey()));
            if (other == null)
            {
                return false;
            }

            DualKey dk = new DualKey(entry.getKey(), other.getKey(),"");
            if (!visited.contains(dk))
            {   // Push keys for further comparison
                stack.addFirst(dk);
            }

            dk = new DualKey(entry.getValue(), other.getValue(),"");
            if (!visited.contains(dk))
            {   // Push values for further comparison
                stack.addFirst(dk);
            }
        }

        return true;
    }


    public static boolean hasCustomEquals(Class c)
    {        
        Class origClass = c;
        if (_customEquals.containsKey(c))
        {
            return _customEquals.get(c);
        }

        while (!Object.class.equals(c))
        {
            try
            {
                c.getDeclaredMethod("equals", Object.class);
                _customEquals.put(origClass, true);
                return true;
            }
            catch (Exception ignored) { }
            c = c.getSuperclass();
        }
        _customEquals.put(origClass, false);
        return false;
    }


    public static int deepHashCode(Object obj)
    {
        Set visited = new HashSet();
        LinkedList<Object> stack = new LinkedList<Object>();
        stack.addFirst(obj);
        int hash = 0;

        while (!stack.isEmpty())
        {
            obj = stack.removeFirst();
            if (obj == null || visited.contains(obj))
            {
                continue;
            }
            
            visited.add(obj);
            
            if (obj.getClass().isArray())
            {
                int len = Array.getLength(obj);
                for (int i = 0; i < len; i++)
                {        
                    stack.addFirst(Array.get(obj, i));
                }
                continue;
            }
                                    
            if (obj instanceof Collection)
            {      
                stack.addAll(0, (Collection)obj);
                continue;
            }
            
            if (obj instanceof Map)
            {
                stack.addAll(0, ((Map)obj).keySet());
                stack.addAll(0, ((Map)obj).values());
                continue;                
            }
            
            if (hasCustomHashCode(obj.getClass()))
            {   // A real hashCode() method exists, call it.
                hash += obj.hashCode();
                continue;
            }
                        
            Collection<Field> fields = getDeepDeclaredFields(obj.getClass());
            for (Field field : fields)
            {
                try
                {           
                    stack.addFirst(field.get(obj));
                }
                catch (Exception ignored) { }
            }
        }
        return hash;        
    }
        

    public static boolean hasCustomHashCode(Class c)
    {   
        Class origClass = c;
        if (_customHash.containsKey(c))
        {
            return _customHash.get(c);
        }
        
        while (!Object.class.equals(c))
        {
            try
            {
                c.getDeclaredMethod("hashCode");
                _customHash.put(origClass, true);
                return true;
            }
            catch (Exception ignored) { }
            c = c.getSuperclass();
        }
        _customHash.put(origClass, false);
        return false;
    }
    

    public static Collection<Field> getDeepDeclaredFields(Class c)
    {
        if (_reflectedFields.containsKey(c))
        {
            return _reflectedFields.get(c);
        }
        Collection<Field> fields = new ArrayList<Field>();
        Class curr = c;
        
        while (curr != null)
        {
            try 
            {
                Field[] local = curr.getDeclaredFields();

                for (Field field : local)
                {
                    if (!field.isAccessible())
                    {
                        try 
                        {
                            field.setAccessible(true);
                        }
                        catch (Exception ignored) { }
                    }
                    
                    int modifiers = field.getModifiers();
                    if (!Modifier.isStatic(modifiers) && 
                        !field.getName().startsWith("this$") && 
                        !Modifier.isTransient(modifiers))
                    {   // speed up: do not count static fields, not go back up to enclosing object in nested case    
                        fields.add(field);
                    }                                      
                }               
            }
            catch (ThreadDeath t)
            {
                throw t;
            }
            catch (Throwable ignored)
            { }

            curr = curr.getSuperclass();
        }
        _reflectedFields.put(c, fields);
        return fields;
    }          
    
	public static void printCompareResult(
			ArrayList<com.telus.credit.crda.util.CompareObjectsUtil.CompRslt> CompRsltList) {
		System.out.println("\n\n\n************* compareRslt    ********");
		 	for (CompRslt compRslt : CompRsltList) {
				 System.out.println(compRslt.toString());
			}
			for (CompRslt compRslt : CompRsltList) {
				assertFalse(compRslt.toString(), !compRslt.isCompareRslt());
			}
	}
    
}
