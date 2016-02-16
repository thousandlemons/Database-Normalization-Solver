/*
 * Copyright (c) 2015 SUN XIMENG (Nathaniel). All rights reserved.
 */

package io.bretty.solver.normalization;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The {@code FuncDep} represents a functional dependency (FD), in which all the attributes
 * on the left functionally determined all those on the right.
 * @author SUN XIMENG (Nathaniel)
 *
 */

public final class FuncDep {
	
	
	/**
	 * A builder class to help construct a {@code FuncDep} object
	 * @author SUN XIMENG (Nathaniel)
	 *
	 */
	public static class Builder{
		private Set<Attribute> left;
		private Set<Attribute> right;
		
		/**
		 * The default constructor
		 */
		public Builder(){
			this.left = new HashSet<Attribute>();
			this.right = new HashSet<Attribute>();
		}
		
		/**
		 * 
		 * @return a new {@code FuncDep} object
		 */
		public FuncDep build(){
			return new FuncDep(this.left, this.right);
		}
		
		/**
		 * Add {@code Attribute} objects to the left of the FD
		 * @param as one or more {@code Attribute} objects
		 * @return
		 */
		public Builder left(Attribute... as){
			this.left.addAll(Arrays.asList(as));
			return this;
		}
		
		/**
		 * Add {@code Attribute} objects to the left of the FD
		 * @param as a set of {@code Attribute} objects
		 * @return
		 */
		public Builder left(Set<Attribute> as){
			this.left.addAll(as);
			return this;
		}
		
		/**
		 * Add {@code Attribute} objects to the right of the FD
		 * @param as one or more {@code Attribute} objects
		 * @return
		 */
		public Builder right(Attribute... as){
			this.right.addAll(Arrays.asList(as));
			return this;
		}
		
		/**
		 * Add {@code Attribute} objects to the right of the FD
		 * @param as a set of {@code Attribute} objects
		 * @return
		 */
		public Builder right(Set<Attribute> as){
			this.right.addAll(as);
			return this;
		}
		
	}
	/**
	 * Get a set of {@code FuncDep} objects with only one string
	 * @param exprs a formatted string as the following example: "a, b --> c; d --> e,f" (white spaces are optional)
	 * @return a set of {@code FuncDep} objects
	 */
	public static Set<FuncDep> getSet(String exprs){
		if(exprs.equals("")){
			return new HashSet<FuncDep>();
		}
		exprs = exprs.replaceAll("\\s+","");
		return getSet(exprs.split(";"));
	}
	
	/**
	 * Get a set of {@code FuncDep} objects with only one string array
	 * @param exprs each element in this array is formatted as the following example: "a, b --> c, d"
	 * @return a set of {@code FuncDep} objects
	 */
	public static Set<FuncDep> getSet(String[] exprs){
		Set<FuncDep> fds = new HashSet<FuncDep>();
		for(String s : exprs){
			fds.add(FuncDep.of(s));
		}
		return fds;
	}
	
	/**
	 * Quickly construct a {@code FuncDep} object with a formatted string
	 * @param expr a formatted string as the following example: "a, b --> c, d"
	 * (white spaces are optional)
	 * @return a {@code FuncDep} object
	 */
	public static FuncDep of(String expr){
		String[] halves = expr.split("-->");
		return of(halves[0], halves[1]);
	}
	
	/**
	 * Quickly construct a {@code FuncDep} object with two formatted string, for the left and right parts respectively
	 * @param left a formatted string as the following example: "a, b"
	 * @param right a formatted string as the following example: "c, d"
	 * @return a {@code FuncDep} object
	 */
	public static FuncDep of(String left, String right){
		left = left.replaceAll("\\s+","");
		right = right.replaceAll("\\s+","");
		String[] lefts = left.split(",");
		String[] rights = right.split(",");
		Builder bd = new Builder();
		for(String s : lefts){
			bd.left(Attribute.of(s));
		}
		for(String s : rights){
			bd.right(Attribute.of(s));
		}
		return bd.build();
	}
	
	protected final Set<Attribute> left;
	
	protected final Set<Attribute> right;
	
	/**
	 * The default constructor
	 * @param left a set of {@code Attribute} objects to be placed on the left of the FD
	 * @param right a set of {@code Attribute} objects to be placed on the right of the FD
	 */
	public FuncDep(Set<Attribute> left, Set<Attribute> right){
		this.left = new HashSet<Attribute>(left);
		this.right = new HashSet<Attribute>(right);
	}
	
	/**
	 * A {@code FuncDep} object will equal to another if and only if they have exactly the left AND right attribute sets
	 */
	@Override
	public boolean equals(Object o){
		if(o == this){
			return true;
		}
		if(!(o instanceof FuncDep)){
			return false;
		}
		FuncDep fd = (FuncDep)o;
		return this.left.equals(fd.left) && this.right.equals(fd.right);
	}
	
	/**
	 * 
	 * @return the set of {@code Attribute} objects that appear in the left part of this FD
	 */
	public Set<Attribute> getLeft(){
		return new HashSet<Attribute>(this.left);
	}
	
	/**
	 * the set of {@code Attribute} objects that appear in the right part of this FD
	 * @return
	 */
	public Set<Attribute> getRight(){
		return new HashSet<Attribute>(this.right);
	}
	
	@Override
	public int hashCode(){
		int result = 17;
		for(Attribute at : this.left){
			result = 31 * result + at.hashCode();
		}
		for(Attribute at : this.right){
			result = 31 * result + at.hashCode();
		}
		return result;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder((this.left.size() + this.right.size()) * Attribute.AVERAGE_LENGTH);
		for(Attribute at : this.left){
			sb.append(at.toString());
			sb.append(", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append(" --> ");
		for(Attribute at : this.right){
			sb.append(at.toString());
			sb.append(", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		return sb.toString();
	}

}
